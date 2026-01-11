import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navigation.css';

function Navigation() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    navigate('/report');
  };

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
          
          {isAuthenticated ? (
            <>
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
              <button 
                onClick={handleLogout}
                className="nav-link logout-btn"
              >
                Logout
              </button>
            </>
          ) : (
            <Link 
              to="/admin/login" 
              className={`nav-link admin-login-link ${location.pathname === '/admin/login' ? 'active' : ''}`}
            >
              Admin Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navigation;
