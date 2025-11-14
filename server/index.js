const express = require('express');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "http://localhost:3000",
    methods: ["GET", "POST"]
  }
});

const PORT = 5000;

// Middleware
app.use(cors());
app.use(express.json());
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Ensure uploads directory exists
const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)) {
  fs.mkdirSync(uploadsDir, { recursive: true });
}

// Configure multer for file uploads
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, uploadsDir);
  },
  filename: (req, file, cb) => {
    const uniqueName = `${Date.now()}-${uuidv4()}${path.extname(file.originalname)}`;
    cb(null, uniqueName);
  }
});

const upload = multer({
  storage,
  limits: { fileSize: 10 * 1024 * 1024 }, // 10MB limit
  fileFilter: (req, file, cb) => {
    const allowedTypes = /jpeg|jpg|png|gif|webp|webm|mp3|wav|ogg/;
    const extname = allowedTypes.test(path.extname(file.originalname).toLowerCase());
    const mimetype = allowedTypes.test(file.mimetype);
    
    if (extname && mimetype) {
      return cb(null, true);
    }
    cb(new Error('Invalid file type'));
  }
});

// In-memory database (replace with real database in production)
let reports = [];

// Socket.io connection
io.on('connection', (socket) => {
  console.log('Client connected');
  
  socket.on('disconnect', () => {
    console.log('Client disconnected');
  });
});

// Helper function to emit updates
function emitUpdate(type, data) {
  io.emit(type, data);
}

// Routes

// Get all reports
app.get('/api/reports', (req, res) => {
  res.json(reports);
});

// Get single report
app.get('/api/reports/:id', (req, res) => {
  const report = reports.find(r => r.reportId === req.params.id);
  if (!report) {
    return res.status(404).json({ error: 'Report not found' });
  }
  res.json(report);
});

// Create new report
app.post('/api/reports', upload.fields([
  { name: 'photos', maxCount: 5 },
  { name: 'voiceNote', maxCount: 1 }
]), (req, res) => {
  try {
    const { title, description, category, priority, latitude, longitude } = req.body;
    
    const reportId = `IND-${String(reports.length + 1).padStart(5, '0')}`;
    
    const photos = req.files['photos'] 
      ? req.files['photos'].map(file => `/uploads/${file.filename}`)
      : [];
    
    const voiceNote = req.files['voiceNote']
      ? `/uploads/${req.files['voiceNote'][0].filename}`
      : null;
    
    const newReport = {
      reportId,
      title,
      description,
      category,
      priority: priority || 'medium',
      status: 'pending',
      latitude: parseFloat(latitude),
      longitude: parseFloat(longitude),
      photos,
      voiceNote,
      assignedTo: null,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      history: [
        {
          timestamp: new Date().toISOString(),
          action: 'Report created',
          notes: 'Initial submission by citizen'
        }
      ]
    };
    
    reports.push(newReport);
    
    // Emit real-time update
    emitUpdate('newReport', newReport);
    
    res.status(201).json(newReport);
  } catch (error) {
    console.error('Error creating report:', error);
    res.status(500).json({ error: 'Failed to create report' });
  }
});

// Update report
app.patch('/api/reports/:id', (req, res) => {
  try {
    const reportIndex = reports.findIndex(r => r.reportId === req.params.id);
    
    if (reportIndex === -1) {
      return res.status(404).json({ error: 'Report not found' });
    }
    
    const { status, assignedTo, notes } = req.body;
    
    if (status) {
      reports[reportIndex].status = status;
    }
    
    if (assignedTo !== undefined) {
      reports[reportIndex].assignedTo = assignedTo;
    }
    
    reports[reportIndex].updatedAt = new Date().toISOString();
    
    // Add to history
    const historyEntry = {
      timestamp: new Date().toISOString(),
      action: status ? `Status changed to ${status}` : 'Report updated',
      notes: notes || undefined
    };
    
    if (assignedTo) {
      historyEntry.action += ` - Assigned to ${assignedTo}`;
    }
    
    reports[reportIndex].history.push(historyEntry);
    
    // Emit real-time update
    emitUpdate('reportUpdated', reports[reportIndex]);
    
    res.json(reports[reportIndex]);
  } catch (error) {
    console.error('Error updating report:', error);
    res.status(500).json({ error: 'Failed to update report' });
  }
});

// Delete report (admin only)
app.delete('/api/reports/:id', (req, res) => {
  const reportIndex = reports.findIndex(r => r.reportId === req.params.id);
  
  if (reportIndex === -1) {
    return res.status(404).json({ error: 'Report not found' });
  }
  
  const deletedReport = reports.splice(reportIndex, 1)[0];
  
  // Delete associated files
  if (deletedReport.photos) {
    deletedReport.photos.forEach(photo => {
      const filePath = path.join(__dirname, photo);
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
      }
    });
  }
  
  if (deletedReport.voiceNote) {
    const filePath = path.join(__dirname, deletedReport.voiceNote);
    if (fs.existsSync(filePath)) {
      fs.unlinkSync(filePath);
    }
  }
  
  emitUpdate('reportDeleted', { reportId: req.params.id });
  
  res.json({ message: 'Report deleted successfully' });
});

// Get analytics data
app.get('/api/analytics', (req, res) => {
  const analytics = {
    totalReports: reports.length,
    byCategory: {},
    byStatus: {},
    byPriority: {},
    byDepartment: {}
  };
  
  reports.forEach(report => {
    // Category
    analytics.byCategory[report.category] = (analytics.byCategory[report.category] || 0) + 1;
    
    // Status
    analytics.byStatus[report.status] = (analytics.byStatus[report.status] || 0) + 1;
    
    // Priority
    analytics.byPriority[report.priority] = (analytics.byPriority[report.priority] || 0) + 1;
    
    // Department
    if (report.assignedTo) {
      if (!analytics.byDepartment[report.assignedTo]) {
        analytics.byDepartment[report.assignedTo] = { total: 0, resolved: 0 };
      }
      analytics.byDepartment[report.assignedTo].total++;
      if (report.status === 'resolved') {
        analytics.byDepartment[report.assignedTo].resolved++;
      }
    }
  });
  
  res.json(analytics);
});

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', reports: reports.length });
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ error: err.message || 'Something went wrong!' });
});

// Start server
server.listen(PORT, () => {
  console.log(`🚀 Server running on http://localhost:${PORT}`);
  console.log(`📊 Reports in database: ${reports.length}`);
});
