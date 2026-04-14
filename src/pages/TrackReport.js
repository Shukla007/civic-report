import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navigation from '../components/Navigation';
import './TrackReport.css';
import { API_URL, ASSET_BASE_URL } from '../config/api';

function TrackReport() {
  const [reportId, setReportId] = useState('');
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [recentReports, setRecentReports] = useState([]);

  useEffect(() => {
    // Load recent reports from localStorage
    const saved = localStorage.getItem('myReports');
    if (saved) {
      setRecentReports(JSON.parse(saved));
    }
  }, []);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!reportId.trim()) {
      setError('Please enter a Report ID');
      return;
    }

    setLoading(true);
    setError('');
    setReport(null);

    try {
      const response = await axios.get(`${API_URL}/reports/${reportId.trim()}`);
      setReport(response.data);
    } catch (err) {
      setError('Report not found. Please check the Report ID and try again.');
    } finally {
      setLoading(false);
    }
  };

  const loadReport = async (id) => {
    setReportId(id);
    setLoading(true);
    setError('');
    setReport(null);

    try {
      const response = await axios.get(`${API_URL}/reports/${id}`);
      setReport(response.data);
    } catch (err) {
      setError('Report not found.');
    } finally {
      setLoading(false);
    }
  };

  const getStatusIcon = (status) => {
    const icons = {
      pending: '⏳',
      acknowledged: '👀',
      'in-progress': '🔧',
      resolved: '✅'
    };
    return icons[status] || '📋';
  };

  const getStatusColor = (status) => {
    const colors = {
      pending: '#f59e0b',
      acknowledged: '#3b82f6',
      'in-progress': '#8b5cf6',
      resolved: '#10b981'
    };
    return colors[status] || '#6b7280';
  };

  return (
    <div className="track-report-page">
      <Navigation />
      
      <div className="track-container">
        <div className="track-header">
          <h1>Track Your Report</h1>
          <p>Enter your Report ID to check the status of your submission</p>
        </div>

        <form onSubmit={handleSearch} className="search-form card">
          <div className="search-input-group">
            <input
              type="text"
              className="input search-input"
              placeholder="Enter Report ID (e.g., IND-00001)"
              value={reportId}
              onChange={(e) => setReportId(e.target.value.toUpperCase())}
            />
            <button type="submit" className="btn btn-primary search-btn" disabled={loading}>
              {loading ? 'Searching...' : 'Track Report'}
            </button>
          </div>
          {error && <div className="alert alert-error">{error}</div>}
        </form>

        {/* Recent Reports */}
        {recentReports.length > 0 && !report && (
          <div className="recent-reports card">
            <h3>Your Recent Reports</h3>
            <div className="recent-reports-grid">
              {recentReports.map((id) => (
                <button
                  key={id}
                  className="recent-report-btn"
                  onClick={() => loadReport(id)}
                >
                  <span className="report-id-badge">{id}</span>
                  <span>View Status</span>
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Report Details */}
        {report && (
          <div className="report-details">
            {/* Status Timeline */}
            <div className="status-timeline card">
              <h3>Report Status</h3>
              <div className="timeline">
                <div className={`timeline-item ${report.status === 'pending' || report.status === 'acknowledged' || report.status === 'in-progress' || report.status === 'resolved' ? 'completed' : ''}`}>
                  <div className="timeline-marker">⏳</div>
                  <div className="timeline-content">
                    <h4>Submitted</h4>
                    <p>Your report has been received</p>
                  </div>
                </div>
                <div className={`timeline-item ${report.status === 'acknowledged' || report.status === 'in-progress' || report.status === 'resolved' ? 'completed' : ''} ${report.status === 'acknowledged' ? 'active' : ''}`}>
                  <div className="timeline-marker">👀</div>
                  <div className="timeline-content">
                    <h4>Acknowledged</h4>
                    <p>Report reviewed by authorities</p>
                  </div>
                </div>
                <div className={`timeline-item ${report.status === 'in-progress' || report.status === 'resolved' ? 'completed' : ''} ${report.status === 'in-progress' ? 'active' : ''}`}>
                  <div className="timeline-marker">🔧</div>
                  <div className="timeline-content">
                    <h4>In Progress</h4>
                    <p>Work is being done to resolve the issue</p>
                  </div>
                </div>
                <div className={`timeline-item ${report.status === 'resolved' ? 'completed active' : ''}`}>
                  <div className="timeline-marker">✅</div>
                  <div className="timeline-content">
                    <h4>Resolved</h4>
                    <p>Issue has been resolved</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Report Info Cards */}
            <div className="info-cards-grid">
              <div className="info-card card">
                <div className="info-card-header">
                  <span className="info-icon">📋</span>
                  <h4>Report Details</h4>
                </div>
                <div className="info-list">
                  <div className="info-row">
                    <span className="label">Report ID:</span>
                    <span className="value">{report.reportId}</span>
                  </div>
                  <div className="info-row">
                    <span className="label">Category:</span>
                    <span className="value">{report.category}</span>
                  </div>
                  <div className="info-row">
                    <span className="label">Priority:</span>
                    <span className={`badge badge-${report.priority}`}>{report.priority}</span>
                  </div>
                  <div className="info-row">
                    <span className="label">Submitted:</span>
                    <span className="value">{new Date(report.createdAt).toLocaleString()}</span>
                  </div>
                  {report.assignedTo && (
                    <div className="info-row">
                      <span className="label">Assigned To:</span>
                      <span className="value">{report.assignedTo}</span>
                    </div>
                  )}
                </div>
              </div>

              <div className="info-card card">
                <div className="info-card-header">
                  <span className="info-icon">📍</span>
                  <h4>Issue Description</h4>
                </div>
                <div className="description-content">
                  <h5>{report.title}</h5>
                  <p>{report.description}</p>
                </div>
              </div>
            </div>

            {/* Photos */}
            {report.photos && report.photos.length > 0 && (
              <div className="report-photos card">
                <h3>Attached Photos</h3>
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
              <div className="report-voice card">
                <h3>Voice Note</h3>
                <audio controls className="audio-player">
                  <source src={`${ASSET_BASE_URL}${report.voiceNote}`} type="audio/webm" />
                  Your browser does not support the audio element.
                </audio>
              </div>
            )}

            {/* History */}
            {report.history && report.history.length > 0 && (
              <div className="report-history card">
                <h3>Update History</h3>
                <div className="history-list">
                  {report.history.map((entry, index) => (
                    <div key={index} className="history-entry">
                      <div className="history-time">
                        {new Date(entry.timestamp).toLocaleString()}
                      </div>
                      <div className="history-action">
                        <strong>{entry.action}</strong>
                        {entry.notes && <p>{entry.notes}</p>}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default TrackReport;
