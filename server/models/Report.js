const mongoose = require('mongoose');

const historySchema = new mongoose.Schema({
  timestamp: {
    type: Date,
    default: Date.now
  },
  action: {
    type: String,
    required: true
  },
  notes: String
}, { _id: false });

const reportSchema = new mongoose.Schema({
  reportId: {
    type: String,
    required: true,
    unique: true,
    index: true
  },
  title: {
    type: String,
    required: true,
    trim: true
  },
  description: {
    type: String,
    required: true
  },
  category: {
    type: String,
    required: true,
    enum: ['Road Damage', 'Street Lighting', 'Garbage/Sanitation', 'Water Supply', 'Drainage', 'Parks & Recreation', 'Traffic Signal', 'Other']
  },
  priority: {
    type: String,
    enum: ['low', 'medium', 'high'],
    default: 'medium'
  },
  status: {
    type: String,
    enum: ['pending', 'acknowledged', 'in-progress', 'resolved', 'rejected'],
    default: 'pending'
  },
  latitude: {
    type: Number,
    required: true
  },
  longitude: {
    type: Number,
    required: true
  },
  photos: [{
    type: String
  }],
  voiceNote: {
    type: String,
    default: null
  },
  assignedTo: {
    type: String,
    default: null
  },
  history: [historySchema]
}, {
  timestamps: true
});

// Create indexes for common queries
reportSchema.index({ status: 1 });
reportSchema.index({ category: 1 });
reportSchema.index({ priority: 1 });
reportSchema.index({ createdAt: -1 });

// Virtual to get formatted dates
reportSchema.virtual('createdAtFormatted').get(function() {
  return this.createdAt.toISOString();
});

reportSchema.virtual('updatedAtFormatted').get(function() {
  return this.updatedAt.toISOString();
});

// Ensure virtuals are included in JSON
reportSchema.set('toJSON', { virtuals: true });
reportSchema.set('toObject', { virtuals: true });

const Report = mongoose.model('Report', reportSchema);

module.exports = Report;
