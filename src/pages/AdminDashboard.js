import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import Navigation from '../components/Navigation';
import ReportModal from '../components/ReportModal';
import FilterPanel from '../components/FilterPanel';
import { useAuth } from '../context/AuthContext';
import 'leaflet/dist/leaflet.css';
import './AdminDashboard.css';

// Fix for default marker icons in react-leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const API_URL = 'http://localhost:5000/api';

// Custom marker icons based on priority
const createCustomIcon = (priority, status) => {
  const colors = {
    high: '#ef4444',
    medium: '#f59e0b',
    low: '#3b82f6'
  };
  
  const opacity = status === 'resolved' ? 0.5 : 1;
  
  return L.divIcon({
    className: 'custom-marker',
    html: `<div style="background-color: ${colors[priority]}; opacity: ${opacity}; width: 24px; height: 24px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>`,
    iconSize: [24, 24],
    iconAnchor: [12, 12],
  });
};

function AdminDashboard() {
  const [reports, setReports] = useState([]);
  const [filteredReports, setFilteredReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    category: '',
    status: '',
    priority: '',
    search: ''
  });
  const [viewMode, setViewMode] = useState('map'); // 'map' or 'list'
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    fetchReports();
    // Poll for updates every 10 seconds
    const interval = setInterval(fetchReports, 10000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    applyFilters();
  }, [reports, filters]);

  const fetchReports = async () => {
    try {
      const response = await axios.get(`${API_URL}/reports`);
      setReports(response.data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching reports:', err);
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...reports];

    if (filters.category) {
      filtered = filtered.filter(r => r.category === filters.category);
    }
    if (filters.status) {
      filtered = filtered.filter(r => r.status === filters.status);
    }
    if (filters.priority) {
      filtered = filtered.filter(r => r.priority === filters.priority);
    }
    if (filters.search) {
      const searchLower = filters.search.toLowerCase();
      filtered = filtered.filter(r => 
        r.title.toLowerCase().includes(searchLower) ||
        r.description.toLowerCase().includes(searchLower) ||
        r.reportId.toLowerCase().includes(searchLower)
      );
    }

    setFilteredReports(filtered);
  };

  const handleUpdateReport = async (reportId, updates) => {
    try {
      await axios.patch(`${API_URL}/reports/${reportId}`, updates, {
        headers: getAuthHeader()
      });
      await fetchReports();
      setSelectedReport(null);
    } catch (err) {
      console.error('Error updating report:', err);
      if (err.response?.status === 401) {
        alert('Session expired. Please login again.');
      }
    }
  };

  const getStatusCounts = () => {
    return {
      total: reports.length,
      pending: reports.filter(r => r.status === 'pending').length,
      acknowledged: reports.filter(r => r.status === 'acknowledged').length,
      inProgress: reports.filter(r => r.status === 'in-progress').length,
      resolved: reports.filter(r => r.status === 'resolved').length
    };
  };

  const stats = getStatusCounts();
  const center = filteredReports.length > 0 
    ? [filteredReports[0].latitude, filteredReports[0].longitude]
    : [28.6139, 77.2090]; // Delhi, India

  if (loading) {
    return (
      <div className="admin-dashboard">
        <Navigation />
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      <Navigation />
      
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1>Administrative Dashboard</h1>
          <div className="view-toggle">
            <button
              className={`toggle-btn ${viewMode === 'map' ? 'active' : ''}`}
              onClick={() => setViewMode('map')}
            >
              🗺️ Map View
            </button>
            <button
              className={`toggle-btn ${viewMode === 'list' ? 'active' : ''}`}
              onClick={() => setViewMode('list')}
            >
              📋 List View
            </button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-value">{stats.total}</div>
            <div className="stat-label">Total Reports</div>
          </div>
          <div className="stat-card stat-pending">
            <div className="stat-value">{stats.pending}</div>
            <div className="stat-label">Pending</div>
          </div>
          <div className="stat-card stat-acknowledged">
            <div className="stat-value">{stats.acknowledged}</div>
            <div className="stat-label">Acknowledged</div>
          </div>
          <div className="stat-card stat-progress">
            <div className="stat-value">{stats.inProgress}</div>
            <div className="stat-label">In Progress</div>
          </div>
          <div className="stat-card stat-resolved">
            <div className="stat-value">{stats.resolved}</div>
            <div className="stat-label">Resolved</div>
          </div>
        </div>

        {/* Filter Panel */}
        <FilterPanel filters={filters} setFilters={setFilters} />

        {/* Main Content */}
        <div className="dashboard-content">
          {viewMode === 'map' ? (
            <div className="map-container">
              <MapContainer
                center={center}
                zoom={12}
                style={{ height: '100%', width: '100%' }}
              >
                <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {filteredReports.map(report => (
                  <Marker
                    key={report.reportId}
                    position={[report.latitude, report.longitude]}
                    icon={createCustomIcon(report.priority, report.status)}
                    eventHandlers={{
                      click: () => setSelectedReport(report)
                    }}
                  >
                    <Popup>
                      <div className="map-popup">
                        <h4>{report.title}</h4>
                        <p className="popup-category">{report.category}</p>
                        <span className={`badge badge-${report.status}`}>
                          {report.status}
                        </span>
                        <button
                          className="btn btn-primary btn-sm"
                          onClick={() => setSelectedReport(report)}
                          style={{ marginTop: '0.5rem', width: '100%' }}
                        >
                          View Details
                        </button>
                      </div>
                    </Popup>
                  </Marker>
                ))}
              </MapContainer>
            </div>
          ) : (
            <div className="list-container">
              {filteredReports.length === 0 ? (
                <div className="empty-state">
                  <p>No reports found matching your filters</p>
                </div>
              ) : (
                <div className="reports-list">
                  {filteredReports.map(report => (
                    <div
                      key={report.reportId}
                      className="report-card"
                      onClick={() => setSelectedReport(report)}
                    >
                      <div className="report-card-header">
                        <h3>{report.title}</h3>
                        <span className={`badge badge-${report.priority}`}>
                          {report.priority}
                        </span>
                      </div>
                      <div className="report-card-meta">
                        <span className="meta-item">📂 {report.category}</span>
                        <span className="meta-item">📍 {report.reportId}</span>
                        <span className="meta-item">
                          🕒 {new Date(report.createdAt).toLocaleDateString()}
                        </span>
                      </div>
                      <p className="report-card-description">{report.description}</p>
                      <div className="report-card-footer">
                        <span className={`badge badge-${report.status}`}>
                          {report.status}
                        </span>
                        {report.assignedTo && (
                          <span className="assigned-to">
                            Assigned to: {report.assignedTo}
                          </span>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Report Detail Modal */}
      {selectedReport && (
        <ReportModal
          report={selectedReport}
          onClose={() => setSelectedReport(null)}
          onUpdate={handleUpdateReport}
        />
      )}
    </div>
  );
}

export default AdminDashboard;
