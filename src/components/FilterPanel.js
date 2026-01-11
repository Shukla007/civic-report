import React from 'react';
import './FilterPanel.css';

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

const STATUSES = ['pending', 'acknowledged', 'in-progress', 'resolved', 'rejected'];
const PRIORITIES = ['low', 'medium', 'high'];

function FilterPanel({ filters, setFilters }) {
  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const clearFilters = () => {
    setFilters({
      category: '',
      status: '',
      priority: '',
      search: ''
    });
  };

  const hasActiveFilters = Object.values(filters).some(v => v !== '');

  return (
    <div className="filter-panel card">
      <div className="filter-header">
        <h3>Filters</h3>
        {hasActiveFilters && (
          <button className="btn-link" onClick={clearFilters}>
            Clear All
          </button>
        )}
      </div>

      <div className="filter-grid">
        {/* Search */}
        <div className="filter-group">
          <label>Search</label>
          <input
            type="text"
            className="input"
            placeholder="Search by title, ID..."
            value={filters.search}
            onChange={(e) => handleFilterChange('search', e.target.value)}
          />
        </div>

        {/* Category */}
        <div className="filter-group">
          <label>Category</label>
          <select
            className="input"
            value={filters.category}
            onChange={(e) => handleFilterChange('category', e.target.value)}
          >
            <option value="">All Categories</option>
            {CATEGORIES.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>

        {/* Status */}
        <div className="filter-group">
          <label>Status</label>
          <select
            className="input"
            value={filters.status}
            onChange={(e) => handleFilterChange('status', e.target.value)}
          >
            <option value="">All Statuses</option>
            {STATUSES.map(status => (
              <option key={status} value={status}>
                {status.charAt(0).toUpperCase() + status.slice(1)}
              </option>
            ))}
          </select>
        </div>

        {/* Priority */}
        <div className="filter-group">
          <label>Priority</label>
          <select
            className="input"
            value={filters.priority}
            onChange={(e) => handleFilterChange('priority', e.target.value)}
          >
            <option value="">All Priorities</option>
            {PRIORITIES.map(priority => (
              <option key={priority} value={priority}>
                {priority.charAt(0).toUpperCase() + priority.slice(1)}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}

export default FilterPanel;
