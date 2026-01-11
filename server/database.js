const fs = require('fs');
const path = require('path');

const DB_PATH = path.join(__dirname, 'db.json');

// Initialize database file if it doesn't exist
function initDB() {
  if (!fs.existsSync(DB_PATH)) {
    fs.writeFileSync(DB_PATH, JSON.stringify({ reports: [] }, null, 2));
  }
}

// Read all data from database
function readDB() {
  initDB();
  try {
    const data = fs.readFileSync(DB_PATH, 'utf8');
    return JSON.parse(data);
  } catch (error) {
    console.error('Error reading database:', error);
    return { reports: [] };
  }
}

// Write data to database
function writeDB(data) {
  try {
    fs.writeFileSync(DB_PATH, JSON.stringify(data, null, 2));
    return true;
  } catch (error) {
    console.error('Error writing to database:', error);
    return false;
  }
}

// Get all reports
function getAllReports() {
  const db = readDB();
  return db.reports || [];
}

// Get report by ID
function getReportById(reportId) {
  const db = readDB();
  return db.reports.find(r => r.reportId === reportId);
}

// Add new report
function addReport(report) {
  const db = readDB();
  db.reports.push(report);
  writeDB(db);
  return report;
}

// Update report
function updateReport(reportId, updates) {
  const db = readDB();
  const index = db.reports.findIndex(r => r.reportId === reportId);
  
  if (index === -1) {
    return null;
  }
  
  db.reports[index] = { ...db.reports[index], ...updates };
  writeDB(db);
  return db.reports[index];
}

// Delete report
function deleteReport(reportId) {
  const db = readDB();
  const index = db.reports.findIndex(r => r.reportId === reportId);
  
  if (index === -1) {
    return null;
  }
  
  const deleted = db.reports.splice(index, 1)[0];
  writeDB(db);
  return deleted;
}

// Get next report ID
function getNextReportId() {
  const db = readDB();
  return db.reports.length + 1;
}

module.exports = {
  initDB,
  getAllReports,
  getReportById,
  addReport,
  updateReport,
  deleteReport,
  getNextReportId
};
