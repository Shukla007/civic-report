import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navigation from '../components/Navigation';
import PhotoUpload from '../components/PhotoUpload';
import VoiceRecorder from '../components/VoiceRecorder';
import './CitizenReport.css';

const API_URL = 'http://localhost:5000/api';

const CATEGORIES = [
  'Road Damage',
  'Street Lighting',
  'Garbage/Sanitation',
  'Water Supply',
  'Drainage',
  'Parks & Recreation',
  'Traffic Signal',
  'Other'
];

function CitizenReport() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: '',
    priority: 'medium'
  });
  const [photos, setPhotos] = useState([]);
  const [voiceNote, setVoiceNote] = useState(null);
  const [location, setLocation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [gettingLocation, setGettingLocation] = useState(false);

  // Get geolocation on mount
  useEffect(() => {
    getLocation();
  }, []);

  const getLocation = () => {
    setGettingLocation(true);
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude
          });
          setGettingLocation(false);
        },
        (error) => {
          console.error('Error getting location:', error);
          // Default location (Delhi, India)
          setLocation({ lat: 28.6139, lng: 77.2090 });
          setGettingLocation(false);
        }
      );
    } else {
      setLocation({ lat: 28.6139, lng: 77.2090 });
      setGettingLocation(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.title || !formData.category) {
      setError('Please fill in all required fields');
      return;
    }

    if (!location) {
      setError('Location is required. Please enable location services.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const submitData = new FormData();
      submitData.append('title', formData.title);
      submitData.append('description', formData.description);
      submitData.append('category', formData.category);
      submitData.append('priority', formData.priority);
      submitData.append('latitude', location.lat);
      submitData.append('longitude', location.lng);
      
      // Add photos
      photos.forEach((photo) => {
        submitData.append('photos', photo);
      });

      // Add voice note if exists
      if (voiceNote) {
        submitData.append('voiceNote', voiceNote);
      }

      const response = await axios.post(`${API_URL}/reports`, submitData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      // Save report ID to localStorage
      const reportId = response.data.reportId;
      const savedReports = JSON.parse(localStorage.getItem('myReports') || '[]');
      savedReports.unshift(reportId);
      localStorage.setItem('myReports', JSON.stringify(savedReports.slice(0, 10))); // Keep last 10

      setSuccess(reportId);
      // Reset form
      setFormData({
        title: '',
        description: '',
        category: '',
        priority: 'medium'
      });
      setPhotos([]);
      setVoiceNote(null);
      
      setTimeout(() => setSuccess(false), 8000);
    } catch (err) {
      console.error('Error submitting report:', err);
      setError('Failed to submit report. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="citizen-report-page">
      <Navigation />
      
      <div className="report-container">
        <div className="report-header">
          <h1>Report an Issue</h1>
          <p>Help improve your community by reporting civic issues</p>
        </div>

        <form onSubmit={handleSubmit} className="report-form">
          {/* Location Display */}
          <div className="location-section card">
            <div className="location-header">
              <span className="icon">📍</span>
              <h3>Location</h3>
            </div>
            {gettingLocation ? (
              <p className="location-status">Getting your location...</p>
            ) : location ? (
              <div className="location-info">
                <p>Lat: {location.lat.toFixed(6)}, Lng: {location.lng.toFixed(6)}</p>
                <button type="button" className="btn-link" onClick={getLocation}>
                  Update Location
                </button>
              </div>
            ) : (
              <button type="button" className="btn btn-outline" onClick={getLocation}>
                Get Location
              </button>
            )}
          </div>

          {/* Title */}
          <div className="form-group">
            <label htmlFor="title">Title *</label>
            <input
              type="text"
              id="title"
              name="title"
              className="input"
              placeholder="Brief description of the issue"
              value={formData.title}
              onChange={handleInputChange}
              required
            />
          </div>

          {/* Category */}
          <div className="form-group">
            <label htmlFor="category">Category *</label>
            <select
              id="category"
              name="category"
              className="input"
              value={formData.category}
              onChange={handleInputChange}
              required
            >
              <option value="">Select a category</option>
              {CATEGORIES.map(cat => (
                <option key={cat} value={cat}>{cat}</option>
              ))}
            </select>
          </div>

          {/* Priority */}
          <div className="form-group">
            <label htmlFor="priority">Priority Level</label>
            <div className="priority-buttons">
              {['low', 'medium', 'high'].map(level => (
                <button
                  key={level}
                  type="button"
                  className={`priority-btn ${formData.priority === level ? 'active' : ''}`}
                  onClick={() => setFormData(prev => ({ ...prev, priority: level }))}
                >
                  {level.charAt(0).toUpperCase() + level.slice(1)}
                </button>
              ))}
            </div>
          </div>

          {/* Description */}
          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              className="input"
              rows="4"
              placeholder="Provide more details about the issue..."
              value={formData.description}
              onChange={handleInputChange}
            />
          </div>

          {/* Photo Upload */}
          <PhotoUpload photos={photos} setPhotos={setPhotos} />

          {/* Voice Note */}
          <VoiceRecorder voiceNote={voiceNote} setVoiceNote={setVoiceNote} />

          {/* Submit Button */}
          <button 
            type="submit" 
            className="btn btn-primary submit-btn"
            disabled={loading}
          >
            {loading ? 'Submitting...' : 'Submit Report'}
          </button>

          {/* Error/Success Messages */}
          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}
          
          {success && (
            <div className="alert alert-success">
              <div className="success-content">
                <div className="success-icon">✓</div>
                <div>
                  <strong>Report submitted successfully!</strong>
                  <p>Your Report ID: <span className="report-id-display">{success}</span></p>
                  <div className="success-actions">
                    <button 
                      type="button" 
                      className="btn-link"
                      onClick={() => navigate('/track')}
                    >
                      Track Your Report →
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default CitizenReport;
