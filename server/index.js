const express = require('express');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const http = require('http');
const socketIo = require('socket.io');
require('dotenv').config();

// Import MongoDB connection
const connectDB = require('./config/db');

// Import auth and database modules
const { verifyCredentials, createSession, invalidateToken, adminAuth } = require('./auth');
const db = require('./database-mongo');

const app = express();
const server = http.createServer(app);

// CORS origins - comma-separated in production
const corsOrigins = process.env.CORS_ORIGIN 
  ? process.env.CORS_ORIGIN.split(',').map(s => s.trim())
  : ["http://localhost:3000"];

const io = socketIo(server, {
  cors: {
    origin: corsOrigins,
    methods: ["GET", "POST", "PATCH", "DELETE"]
  }
});

const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors({ origin: corsOrigins, credentials: true }));
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

// ==================== AUTH ROUTES ====================

// Admin login
app.post('/api/admin/login', (req, res) => {
  const { username, password } = req.body;
  
  if (!username || !password) {
    return res.status(400).json({ error: 'Username and password are required' });
  }
  
  if (verifyCredentials(username, password)) {
    const token = createSession();
    res.json({ 
      success: true, 
      token,
      message: 'Login successful'
    });
  } else {
    res.status(401).json({ error: 'Invalid credentials' });
  }
});

// Admin logout
app.post('/api/admin/logout', adminAuth, (req, res) => {
  const token = req.headers.authorization.split(' ')[1];
  invalidateToken(token);
  res.json({ success: true, message: 'Logged out successfully' });
});

// Verify admin token
app.get('/api/admin/verify', adminAuth, (req, res) => {
  res.json({ valid: true, message: 'Token is valid' });
});

// ==================== PUBLIC ROUTES (Citizens) ====================

// Get all reports (public - for citizen tracking)
app.get('/api/reports', async (req, res) => {
  try {
    const reports = await db.getAllReports();
    res.json(reports);
  } catch (error) {
    console.error('Error fetching reports:', error);
    res.status(500).json({ error: 'Failed to fetch reports' });
  }
});

// Get single report (public - for citizen tracking)
app.get('/api/reports/:id', async (req, res) => {
  try {
    const report = await db.getReportById(req.params.id);
    if (!report) {
      return res.status(404).json({ error: 'Report not found' });
    }
    res.json(report);
  } catch (error) {
    console.error('Error fetching report:', error);
    res.status(500).json({ error: 'Failed to fetch report' });
  }
});

// Create new report (public - citizens can submit)
app.post('/api/reports', upload.fields([
  { name: 'photos', maxCount: 5 },
  { name: 'voiceNote', maxCount: 1 }
]), async (req, res) => {
  try {
    const { title, description, category, priority, latitude, longitude } = req.body;
    
    const reportNumber = await db.getNextReportId();
    const reportId = `IND-${String(reportNumber).padStart(5, '0')}`;
    
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
      history: [
        {
          timestamp: new Date().toISOString(),
          action: 'Report created',
          notes: 'Initial submission by citizen'
        }
      ]
    };
    
    const savedReport = await db.addReport(newReport);
    
    // Emit real-time update
    emitUpdate('newReport', savedReport);
    
    res.status(201).json(savedReport);
  } catch (error) {
    console.error('Error creating report:', error);
    res.status(500).json({ error: 'Failed to create report' });
  }
});

// ==================== ADMIN PROTECTED ROUTES ====================

// Update report (admin only)
app.patch('/api/reports/:id', adminAuth, async (req, res) => {
  try {
    const report = await db.getReportById(req.params.id);
    
    if (!report) {
      return res.status(404).json({ error: 'Report not found' });
    }
    
    const { status, assignedTo, notes } = req.body;
    
    const updates = {};
    
    if (status) {
      updates.status = status;
    }
    
    if (assignedTo !== undefined) {
      updates.assignedTo = assignedTo;
    }
    
    // Add to history
    const historyEntry = {
      timestamp: new Date().toISOString(),
      action: status ? `Status changed to ${status}` : 'Report updated',
      notes: notes || undefined
    };
    
    if (assignedTo) {
      historyEntry.action += ` - Assigned to ${assignedTo}`;
    }
    
    updates.history = [...(report.history || []), historyEntry];
    
    const updatedReport = await db.updateReport(req.params.id, updates);
    
    // Emit real-time update
    emitUpdate('reportUpdated', updatedReport);
    
    res.json(updatedReport);
  } catch (error) {
    console.error('Error updating report:', error);
    res.status(500).json({ error: 'Failed to update report' });
  }
});

// Delete report (admin only)
app.delete('/api/reports/:id', adminAuth, async (req, res) => {
  try {
    const report = await db.getReportById(req.params.id);
    
    if (!report) {
      return res.status(404).json({ error: 'Report not found' });
    }
    
    // Delete associated files
    if (report.photos) {
      report.photos.forEach(photo => {
        const filePath = path.join(__dirname, photo);
        if (fs.existsSync(filePath)) {
          fs.unlinkSync(filePath);
        }
      });
    }
    
    if (report.voiceNote) {
      const filePath = path.join(__dirname, report.voiceNote);
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
      }
    }
    
    await db.deleteReport(req.params.id);
    
    emitUpdate('reportDeleted', { reportId: req.params.id });
    
    res.json({ message: 'Report deleted successfully' });
  } catch (error) {
    console.error('Error deleting report:', error);
    res.status(500).json({ error: 'Failed to delete report' });
  }
});

// Get analytics data (admin only)
app.get('/api/analytics', adminAuth, async (req, res) => {
  try {
    const reports = await db.getAllReports();
    
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
  } catch (error) {
    console.error('Error fetching analytics:', error);
    res.status(500).json({ error: 'Failed to fetch analytics' });
  }
});

// Health check
app.get('/health', async (req, res) => {
  try {
    const count = await db.getReportCount();
    res.json({ status: 'ok', reports: count, database: 'mongodb' });
  } catch (error) {
    res.status(500).json({ status: 'error', error: error.message });
  }
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ error: err.message || 'Something went wrong!' });
});

// Connect to MongoDB and start server
async function startServer() {
  try {
    // Connect to MongoDB
    await connectDB();
    
    // Initialize database module
    await db.initDB();
    
    // Start listening
    server.listen(PORT, async () => {
      const count = await db.getReportCount();
      console.log(`🚀 Server running on http://localhost:${PORT}`);
      console.log(`📊 Reports in database: ${count}`);
    });
  } catch (error) {
    console.error('Failed to start server:', error);
    process.exit(1);
  }
}

// Run the server
startServer();
