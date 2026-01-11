const crypto = require('crypto');

// Hardcoded admin credentials
const ADMIN_CREDENTIALS = {
  username: 'admin',
  password: 'admin123'
};

// Simple token storage (in production, use Redis or database)
const activeTokens = new Set();

// Generate a secure token
function generateToken() {
  return crypto.randomBytes(32).toString('hex');
}

// Verify admin credentials
function verifyCredentials(username, password) {
  return username === ADMIN_CREDENTIALS.username && 
         password === ADMIN_CREDENTIALS.password;
}

// Create a new session token
function createSession() {
  const token = generateToken();
  activeTokens.add(token);
  return token;
}

// Validate a session token
function validateToken(token) {
  return activeTokens.has(token);
}

// Invalidate a session token (logout)
function invalidateToken(token) {
  activeTokens.delete(token);
}

// Middleware to protect admin routes
function adminAuth(req, res, next) {
  const authHeader = req.headers.authorization;
  
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Unauthorized: No token provided' });
  }
  
  const token = authHeader.split(' ')[1];
  
  if (!validateToken(token)) {
    return res.status(401).json({ error: 'Unauthorized: Invalid or expired token' });
  }
  
  next();
}

module.exports = {
  verifyCredentials,
  createSession,
  validateToken,
  invalidateToken,
  adminAuth
};
