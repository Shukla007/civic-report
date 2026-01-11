import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
} from 'chart.js';
import { Line, Bar, Pie } from 'react-chartjs-2';
import { MapContainer, TileLayer, CircleMarker, Popup } from 'react-leaflet';
import Navigation from '../components/Navigation';
import { useAuth } from '../context/AuthContext';
import './Analytics.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

const API_URL = 'http://localhost:5000/api';

function Analytics() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [timeRange, setTimeRange] = useState('30'); // days
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    try {
      // Fetch reports (public endpoint for data)
      const response = await axios.get(`${API_URL}/reports`);
      setReports(response.data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching reports:', err);
      setLoading(false);
    }
  };

  // Calculate analytics data
  const getAnalytics = () => {
    const now = new Date();
    const rangeDate = new Date(now.getTime() - timeRange * 24 * 60 * 60 * 1000);
    const filteredReports = reports.filter(r => new Date(r.createdAt) >= rangeDate);

    // Category breakdown
    const categoryData = {};
    filteredReports.forEach(r => {
      categoryData[r.category] = (categoryData[r.category] || 0) + 1;
    });

    // Status breakdown
    const statusData = {};
    filteredReports.forEach(r => {
      statusData[r.status] = (statusData[r.status] || 0) + 1;
    });

    // Priority breakdown
    const priorityData = {};
    filteredReports.forEach(r => {
      priorityData[r.priority] = (priorityData[r.priority] || 0) + 1;
    });

    // Daily trends
    const dailyData = {};
    filteredReports.forEach(r => {
      const date = new Date(r.createdAt).toLocaleDateString();
      dailyData[date] = (dailyData[date] || 0) + 1;
    });

    // Average response time
    const resolvedReports = filteredReports.filter(r => r.status === 'resolved');
    const avgResponseTime = resolvedReports.length > 0
      ? resolvedReports.reduce((sum, r) => {
          const created = new Date(r.createdAt);
          const resolved = new Date(r.updatedAt);
          return sum + (resolved - created);
        }, 0) / resolvedReports.length / (1000 * 60 * 60) // Convert to hours
      : 0;

    // Department efficiency
    const departmentStats = {};
    filteredReports.forEach(r => {
      if (r.assignedTo) {
        if (!departmentStats[r.assignedTo]) {
          departmentStats[r.assignedTo] = { total: 0, resolved: 0 };
        }
        departmentStats[r.assignedTo].total++;
        if (r.status === 'resolved') {
          departmentStats[r.assignedTo].resolved++;
        }
      }
    });

    return {
      categoryData,
      statusData,
      priorityData,
      dailyData,
      avgResponseTime,
      departmentStats,
      totalReports: filteredReports.length,
      resolvedCount: resolvedReports.length
    };
  };

  const analytics = getAnalytics();

  // Chart configurations
  const categoryChartData = {
    labels: Object.keys(analytics.categoryData),
    datasets: [{
      label: 'Reports by Category',
      data: Object.values(analytics.categoryData),
      backgroundColor: [
        'rgba(59, 130, 246, 0.8)',
        'rgba(16, 185, 129, 0.8)',
        'rgba(245, 158, 11, 0.8)',
        'rgba(239, 68, 68, 0.8)',
        'rgba(139, 92, 246, 0.8)',
        'rgba(236, 72, 153, 0.8)',
        'rgba(14, 165, 233, 0.8)',
        'rgba(132, 204, 22, 0.8)'
      ]
    }]
  };

  const statusChartData = {
    labels: Object.keys(analytics.statusData).map(s => 
      s.charAt(0).toUpperCase() + s.slice(1)
    ),
    datasets: [{
      label: 'Reports by Status',
      data: Object.values(analytics.statusData),
      backgroundColor: [
        'rgba(245, 158, 11, 0.8)',
        'rgba(59, 130, 246, 0.8)',
        'rgba(139, 92, 246, 0.8)',
        'rgba(16, 185, 129, 0.8)'
      ]
    }]
  };

  const dailyTrendData = {
    labels: Object.keys(analytics.dailyData).slice(-14), // Last 14 days
    datasets: [{
      label: 'Daily Reports',
      data: Object.values(analytics.dailyData).slice(-14),
      borderColor: 'rgba(59, 130, 246, 1)',
      backgroundColor: 'rgba(59, 130, 246, 0.1)',
      tension: 0.4,
      fill: true
    }]
  };

  const departmentEfficiencyData = {
    labels: Object.keys(analytics.departmentStats),
    datasets: [
      {
        label: 'Total Assigned',
        data: Object.values(analytics.departmentStats).map(d => d.total),
        backgroundColor: 'rgba(59, 130, 246, 0.8)'
      },
      {
        label: 'Resolved',
        data: Object.values(analytics.departmentStats).map(d => d.resolved),
        backgroundColor: 'rgba(16, 185, 129, 0.8)'
      }
    ]
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };

  // Heatmap data for map
  const getHeatmapData = () => {
    const locationCounts = {};
    reports.forEach(r => {
      const key = `${r.latitude.toFixed(3)},${r.longitude.toFixed(3)}`;
      locationCounts[key] = (locationCounts[key] || 0) + 1;
    });
    return locationCounts;
  };

  const heatmapData = getHeatmapData();

  if (loading) {
    return (
      <div className="analytics-page">
        <Navigation />
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="analytics-page">
      <Navigation />
      
      <div className="analytics-container">
        <div className="analytics-header">
          <h1>Analytics & Insights</h1>
          <select
            className="time-range-select"
            value={timeRange}
            onChange={(e) => setTimeRange(e.target.value)}
          >
            <option value="7">Last 7 Days</option>
            <option value="30">Last 30 Days</option>
            <option value="90">Last 90 Days</option>
            <option value="365">Last Year</option>
          </select>
        </div>

        {/* Key Metrics */}
        <div className="metrics-grid">
          <div className="metric-card">
            <div className="metric-icon">📊</div>
            <div className="metric-content">
              <div className="metric-value">{analytics.totalReports}</div>
              <div className="metric-label">Total Reports</div>
            </div>
          </div>
          <div className="metric-card">
            <div className="metric-icon">✅</div>
            <div className="metric-content">
              <div className="metric-value">{analytics.resolvedCount}</div>
              <div className="metric-label">Resolved</div>
            </div>
          </div>
          <div className="metric-card">
            <div className="metric-icon">⏱️</div>
            <div className="metric-content">
              <div className="metric-value">{analytics.avgResponseTime.toFixed(1)}h</div>
              <div className="metric-label">Avg Response Time</div>
            </div>
          </div>
          <div className="metric-card">
            <div className="metric-icon">📈</div>
            <div className="metric-content">
              <div className="metric-value">
                {analytics.totalReports > 0 
                  ? ((analytics.resolvedCount / analytics.totalReports) * 100).toFixed(1)
                  : 0}%
              </div>
              <div className="metric-label">Resolution Rate</div>
            </div>
          </div>
        </div>

        {/* Charts Grid */}
        <div className="charts-grid">
          <div className="chart-card">
            <h3>Daily Trend</h3>
            <div className="chart-container">
              <Line data={dailyTrendData} options={chartOptions} />
            </div>
          </div>

          <div className="chart-card">
            <h3>Reports by Category</h3>
            <div className="chart-container">
              <Bar data={categoryChartData} options={chartOptions} />
            </div>
          </div>

          <div className="chart-card">
            <h3>Status Distribution</h3>
            <div className="chart-container">
              <Pie data={statusChartData} options={chartOptions} />
            </div>
          </div>

          <div className="chart-card">
            <h3>Department Efficiency</h3>
            <div className="chart-container">
              <Bar data={departmentEfficiencyData} options={chartOptions} />
            </div>
          </div>
        </div>

        {/* Heatmap */}
        <div className="heatmap-section card">
          <h3>Issue Heatmap</h3>
          <div className="heatmap-container">
            <MapContainer
              center={reports.length > 0 ? [reports[0].latitude, reports[0].longitude] : [28.6139, 77.2090]}
              zoom={11}
              style={{ height: '100%', width: '100%' }}
            >
              <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              {Object.entries(heatmapData).map(([coords, count]) => {
                const [lat, lng] = coords.split(',').map(Number);
                const radius = Math.min(5 + count * 3, 30);
                const opacity = Math.min(0.3 + count * 0.1, 0.8);
                
                return (
                  <CircleMarker
                    key={coords}
                    center={[lat, lng]}
                    radius={radius}
                    fillColor="#ef4444"
                    fillOpacity={opacity}
                    stroke={false}
                  >
                    <Popup>
                      <div>
                        <strong>{count} report{count > 1 ? 's' : ''}</strong>
                        <p>in this area</p>
                      </div>
                    </Popup>
                  </CircleMarker>
                );
              })}
            </MapContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
