import React, { useState } from 'react';
import './ReportModal.css';
import { ASSET_BASE_URL } from '../config/api';

const DEPARTMENTS = [
  'Public Works Department',
  'Electricity Board',
  'Municipal Corporation',
  'Traffic Police',
  'Water Board',
  'Sanitation Department',
  'Other'
];

function ReportModal({ report, onClose, onUpdate }) {
  const [status, setStatus] = useState(report.status);
  const [assignedTo, setAssignedTo] = useState(report.assignedTo || '');
  const [notes, setNotes] = useState('');
  const [updating, setUpdating] = useState(false);

  const handleUpdate = async () => {
    setUpdating(true);
    const updates = {
      status,
      assignedTo: assignedTo || undefined,
      notes: notes || undefined
    };
    await onUpdate(report.reportId, updates);
    setUpdating(false);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content report-modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Report Details</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        <div className="modal-body">
          {/* Report Info */}
          <div className="report-section">
            <div className="section-header">
              <h3>Report Information</h3>
              <span className={`badge badge-${report.priority}`}>
                {report.priority} priority
              </span>
            </div>
            
            <div className="info-grid">
              <div className="info-item">
                <span className="info-label">Report ID:</span>
                <span className="info-value">{report.reportId}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Category:</span>
                <span className="info-value">{report.category}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Created:</span>
                <span className="info-value">
                  {new Date(report.createdAt).toLocaleString()}
                </span>
              </div>
              <div className="info-item">
                <span className="info-label">Location:</span>
                <span className="info-value">
                  {report.latitude.toFixed(6)}, {report.longitude.toFixed(6)}
                </span>
              </div>
            </div>

            <div className="description-section">
              <h4>{report.title}</h4>
              <p>{report.description}</p>
            </div>
          </div>

          {/* Photos */}
          {report.photos && report.photos.length > 0 && (
            <div className="report-section">
              <h3>Photos</h3>
              <div className="photos-grid">
                {report.photos.map((photo, index) => (
                  <img
                    key={index}
                    src={`${ASSET_BASE_URL}${photo}`}
                    alt={`Report ${index + 1}`}
                    className="report-photo"
                  />
                ))}
              </div>
            </div>
          )}

          {/* Voice Note */}
          {report.voiceNote && (
            <div className="report-section">
              <h3>Voice Note</h3>
              <audio controls className="audio-player">
                <source src={`${ASSET_BASE_URL}${report.voiceNote}`} type="audio/webm" />
                Your browser does not support the audio element.
              </audio>
            </div>
          )}

          {/* Update Section */}
          <div className="report-section update-section">
            <h3>Update Report</h3>
            
            <div className="form-group">
              <label>Status</label>
              <select
                className="input"
                value={status}
                onChange={(e) => setStatus(e.target.value)}
              >
                <option value="pending">Pending</option>
                <option value="acknowledged">Acknowledged</option>
                <option value="in-progress">In Progress</option>
                <option value="resolved">Resolved</option>
                <option value="rejected">Rejected</option>
              </select>
            </div>

            <div className="form-group">
              <label>Assign to Department</label>
              <select
                className="input"
                value={assignedTo}
                onChange={(e) => setAssignedTo(e.target.value)}
              >
                <option value="">Not Assigned</option>
                {DEPARTMENTS.map(dept => (
                  <option key={dept} value={dept}>{dept}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Notes</label>
              <textarea
                className="input"
                rows="3"
                placeholder="Add notes or comments..."
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
              />
            </div>

            <button
              className="btn btn-primary"
              onClick={handleUpdate}
              disabled={updating}
              style={{ width: '100%' }}
            >
              {updating ? 'Updating...' : 'Update Report'}
            </button>
          </div>

          {/* History */}
          {report.history && report.history.length > 0 && (
            <div className="report-section">
              <h3>History</h3>
              <div className="history-timeline">
                {report.history.map((entry, index) => (
                  <div key={index} className="history-item">
                    <div className="history-date">
                      {new Date(entry.timestamp).toLocaleString()}
                    </div>
                    <div className="history-content">
                      <strong>{entry.action}</strong>
                      {entry.notes && <p>{entry.notes}</p>}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ReportModal;
