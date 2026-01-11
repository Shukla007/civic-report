const Report = require('./models/Report');
const Counter = require('./models/Counter');

// Initialize database (now a no-op since MongoDB handles this)
async function initDB() {
  // MongoDB connection is handled in config/db.js
  console.log('📊 Database module initialized');
}

// Get all reports
async function getAllReports() {
  try {
    const reports = await Report.find().sort({ createdAt: -1 }).lean();
    // Transform _id to match existing API format
    return reports.map(report => ({
      ...report,
      createdAt: report.createdAt.toISOString(),
      updatedAt: report.updatedAt.toISOString()
    }));
  } catch (error) {
    console.error('Error getting all reports:', error);
    return [];
  }
}

// Get report by ID
async function getReportById(reportId) {
  try {
    const report = await Report.findOne({ reportId }).lean();
    if (!report) return null;
    return {
      ...report,
      createdAt: report.createdAt.toISOString(),
      updatedAt: report.updatedAt.toISOString()
    };
  } catch (error) {
    console.error('Error getting report by ID:', error);
    return null;
  }
}

// Add new report
async function addReport(reportData) {
  try {
    const report = new Report(reportData);
    await report.save();
    const saved = report.toObject();
    return {
      ...saved,
      createdAt: saved.createdAt.toISOString(),
      updatedAt: saved.updatedAt.toISOString()
    };
  } catch (error) {
    console.error('Error adding report:', error);
    throw error;
  }
}

// Update report
async function updateReport(reportId, updates) {
  try {
    const report = await Report.findOneAndUpdate(
      { reportId },
      { $set: updates },
      { new: true }
    ).lean();
    
    if (!report) return null;
    return {
      ...report,
      createdAt: report.createdAt.toISOString(),
      updatedAt: report.updatedAt.toISOString()
    };
  } catch (error) {
    console.error('Error updating report:', error);
    return null;
  }
}

// Delete report
async function deleteReport(reportId) {
  try {
    const report = await Report.findOneAndDelete({ reportId }).lean();
    if (!report) return null;
    return {
      ...report,
      createdAt: report.createdAt.toISOString(),
      updatedAt: report.updatedAt.toISOString()
    };
  } catch (error) {
    console.error('Error deleting report:', error);
    return null;
  }
}

// Get next report ID (using atomic counter)
async function getNextReportId() {
  try {
    const sequence = await Counter.getNextSequence('reportId');
    return sequence;
  } catch (error) {
    console.error('Error getting next report ID:', error);
    // Fallback: count existing reports
    const count = await Report.countDocuments();
    return count + 1;
  }
}

// Get report count
async function getReportCount() {
  try {
    return await Report.countDocuments();
  } catch (error) {
    console.error('Error getting report count:', error);
    return 0;
  }
}

module.exports = {
  initDB,
  getAllReports,
  getReportById,
  addReport,
  updateReport,
  deleteReport,
  getNextReportId,
  getReportCount
};
