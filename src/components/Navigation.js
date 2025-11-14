import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navigation.css';

function Navigation() {
  const location = useLocation();

  return (
    <nav className="navigation">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          <span className="logo-icon">🏛️</span>
          <span className="logo-text">Civic Report</span>
        </Link>

        <div className="nav-links">
          <Link 
            to="/report" 
            className={`nav-link ${location.pathname === '/report' ? 'active' : ''}`}
          >
            Report Issue
          </Link>
          <Link 
            to="/track" 
            className={`nav-link ${location.pathname === '/track' ? 'active' : ''}`}
          >
            Track Report
          </Link>
          <Link 
            to="/admin" 
            className={`nav-link ${location.pathname === '/admin' ? 'active' : ''}`}
          >
            Dashboard
          </Link>
          <Link 
            to="/analytics" 
            className={`nav-link ${location.pathname === '/analytics' ? 'active' : ''}`}
          >
            Analytics
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default Navigation;
