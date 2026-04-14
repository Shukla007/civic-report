// Central API URL selection.
//
// Priority:
// 1) Explicit `REACT_APP_API_URL`
// 2) Local development default (frontend on 3000, backend on 5000)
// 3) Production default (Railway)

function normalizeBaseUrl(url) {
  return url.replace(/\/$/, '');
}

export function getApiBaseUrl() {
  const envUrl = process.env.REACT_APP_API_URL;
  if (envUrl) return normalizeBaseUrl(envUrl);

  // CRA sets NODE_ENV to 'development' for `npm start`.
  // Also treat localhost as dev if someone built locally.
  const isBrowser = typeof window !== 'undefined';
  const isLocalhost = isBrowser && (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1');

  if (process.env.NODE_ENV === 'development' || isLocalhost) {
    return 'http://localhost:5000/api';
  }

  return 'https://civic-report-production.up.railway.app/api';
}

export const API_URL = getApiBaseUrl();
export const ASSET_BASE_URL = API_URL.replace(/\/api$/, '');
