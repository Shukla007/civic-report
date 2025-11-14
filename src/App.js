import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import CitizenReport from './pages/CitizenReport';
import AdminDashboard from './pages/AdminDashboard';
import Analytics from './pages/Analytics';
import TrackReport from './pages/TrackReport';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<Navigate to="/report" replace />} />
          <Route path="/report" element={<CitizenReport />} />
          <Route path="/track" element={<TrackReport />} />
          <Route path="/admin" element={<AdminDashboard />} />
          <Route path="/analytics" element={<Analytics />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
