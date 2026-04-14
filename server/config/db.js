const mongoose = require('mongoose');
const dns = require('dns');

// NOTE:
// - `index.js` already calls `dotenv.config()`.
// - We intentionally do not force a specific .env path here, so running from the
//   repo root (`node server/index.js`) or via Docker environment vars behaves predictably.

const DEFAULT_MONGODB_URI = 'mongodb://localhost:27017/civic-report';
const MONGODB_URI = process.env.MONGODB_URI || DEFAULT_MONGODB_URI;

function sanitizeMongoUri(mongoUri) {
  try {
    const url = new URL(mongoUri);
    // Avoid leaking credentials in logs.
    return `${url.protocol}//${url.host}${url.pathname}`;
  } catch {
    // If parsing fails, return a minimal hint.
    return mongoUri.startsWith('mongodb') ? mongoUri.split('@').pop() : '<invalid-uri>';
  }
}

async function ensureSrvDnsWorks(mongoUri) {
  // MongoDB Atlas SRV URIs require SRV DNS lookups.
  if (!mongoUri.startsWith('mongodb+srv://')) return;

  let host;
  try {
    host = new URL(mongoUri).host;
  } catch {
    throw new Error('Invalid MONGODB_URI: unable to parse mongodb+srv URI');
  }

  const srvName = `_mongodb._tcp.${host}`;

  // Optional override: comma-separated DNS servers.
  // Example: MONGODB_DNS_SERVERS=10.198.193.235 or 1.1.1.1,8.8.8.8
  const override = (process.env.MONGODB_DNS_SERVERS || '')
    .split(',')
    .map(s => s.trim())
    .filter(Boolean);

  const originalServers = dns.getServers();
  const candidateServers = override.length > 0 ? override : originalServers;

  // Quick path: try current resolver first.
  try {
    await dns.promises.resolveSrv(srvName);
    return;
  } catch (err) {
    // Continue to fallback attempts below.
  }

  // Fallback: try each DNS server individually until SRV works.
  for (const server of candidateServers) {
    try {
      dns.setServers([server]);
      await dns.promises.resolveSrv(srvName);
      console.log(`🧭 Using DNS server for Mongo SRV: ${server}`);
      return;
    } catch {
      // try next
    }
  }

  // Restore original servers before failing.
  try {
    dns.setServers(originalServers);
  } catch {
    // ignore
  }

  const serverList = originalServers.length > 0 ? originalServers.join(', ') : '<none>';
  const loopbackOnly = originalServers.length > 0 && originalServers.every(s => s === '127.0.0.1' || s === '::1');

  const hint = override.length > 0
    ? 'The configured MONGODB_DNS_SERVERS did not resolve the SRV record.'
    : loopbackOnly
      ? `Node is configured to use only loopback DNS (${serverList}), which commonly breaks SRV lookups.`
      : `Your current DNS resolver is refusing SRV queries from Node (Node DNS servers: ${serverList}).`;

  throw new Error(
    [
      `MongoDB Atlas SRV DNS lookup failed for ${srvName}.`,
      hint,
      'Fix options:',
      '- Set MONGODB_DNS_SERVERS to a working DNS server for this network (e.g. the one `nslookup` uses).',
      '- Or use a standard (non-SRV) MongoDB connection string: mongodb://<host1>,<host2>,<host3>/?replicaSet=...&tls=true',
    ].join(' ') 
  );
}

const connectDB = async () => {
  try {
    await ensureSrvDnsWorks(MONGODB_URI);

    const conn = await mongoose.connect(MONGODB_URI, {
      serverSelectionTimeoutMS: 10_000,
    });

    console.log(`📦 MongoDB Connected: ${conn.connection.host}`);
    return conn;
  } catch (error) {
    console.error(`❌ MongoDB connection error (${sanitizeMongoUri(MONGODB_URI)}): ${error.message}`);
    process.exit(1);
  }
};

module.exports = connectDB;
