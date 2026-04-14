# Civic Report - Modern Civic Issue Reporting Platform

A comprehensive, mobile-first web application for reporting and managing civic issues, built with React and Node.js.

## Features

### Citizen Interface
- 📸 **Photo Upload**: Capture and upload up to 5 photos per report
- 📍 **Auto Geolocation**: Automatic location capture for accurate issue tracking
- 🎤 **Voice Notes**: Record voice descriptions for detailed reporting
- 📱 **Mobile-First Design**: Optimized for smartphones and tablets
- ⚡ **Real-Time Updates**: Instant status notifications
- 🔍 **Report Tracking**: Track your submitted reports with unique Report ID
- 💾 **Recent Reports**: Quick access to your last 10 submitted reports
- 📊 **Visual Status Timeline**: See report progress from submission to resolution

### Administrative Dashboard
- 🗺️ **Interactive Map**: Live visualization of all reported issues
- 🔍 **Advanced Filtering**: Filter by category, status, priority, and search
- 📋 **List/Map View**: Toggle between map and list views
- 🏢 **Department Assignment**: Automated routing to appropriate departments
- 📊 **Status Tracking**: Manage reports through confirmation → acknowledgment → resolution
- 👥 **Staff Management**: Assign issues to specific teams or departments

### Analytics & Insights
- 📈 **Trend Analysis**: Daily report trends over time
- 🎯 **Category Breakdown**: Visual distribution of issue types
- 🗺️ **Heatmaps**: Identify high-volume problem areas
- ⏱️ **Response Metrics**: Average response times and resolution rates
- 🏆 **Department Efficiency**: Track departmental performance
- 📊 **Interactive Charts**: Multiple visualization types using Chart.js

## Tech Stack

### Frontend
- **React 18** - Modern UI framework
- **React Router** - Navigation and routing
- **Leaflet & React-Leaflet** - Interactive maps
- **Chart.js & React-Chartjs-2** - Data visualization
- **Axios** - HTTP client
- **Socket.io-client** - Real-time updates

### Backend
- **Node.js & Express** - REST API server
- **Multer** - File upload handling
- **Socket.io** - WebSocket for real-time communication
- **UUID** - Unique ID generation

## Installation

### Prerequisites
- Node.js 14+ and npm
- Modern web browser with geolocation support
- MongoDB (either local MongoDB running on your machine, or a MongoDB Atlas cluster)

### Setup

1. **Clone or navigate to the project directory**
   ```bash
   cd /run/media/spider/D8EA0265EA02406C/web\ dev/civic\ report
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure MongoDB**

    This backend uses MongoDB via Mongoose.

    - **Option A (Local MongoDB)**: install MongoDB and ensure it is running on `mongodb://localhost:27017`.
       - If you do nothing, the server will default to `mongodb://localhost:27017/civic-report`.

    - **Option B (MongoDB Atlas)**: create a `.env` file in the repo root and set `MONGODB_URI`.
       - Start from the template in `.env.example`.
       - If you use a `mongodb+srv://...` URI and the server fails with `querySrv ECONNREFUSED`, it’s a DNS/SRV resolver issue in Node.
          Set `MONGODB_DNS_SERVERS` (see `.env.example`) or switch to a non-SRV Atlas connection string (`mongodb://<host1>,<host2>,<host3>/...`).

4. **Start the development environment**
   
   **Option 1: Run both frontend and backend together**
   ```bash
   npm run dev
   ```
   
   **Option 2: Run separately**
   ```bash
   # Terminal 1 - Frontend (React)
   npm start
   
   # Terminal 2 - Backend (Node.js)
   npm run server
   ```

4. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:5000

## User Interface

The application features a modern, gradient-based design with:
- **Purple gradient navigation** with smooth transitions
- **Card-based layouts** with hover effects and shadows
- **Interactive status timeline** with animated progress indicators
- **Responsive grid layouts** that adapt to all screen sizes
- **Smooth animations** and micro-interactions throughout

## Usage

### For Citizens

1. **Navigate to Report Issue** (`/report`)
2. **Allow location access** when prompted (defaults to Delhi if denied)
3. **Fill in the form**:
   - Title (required)
   - Category (required)
   - Priority level
   - Description
4. **Add media** (optional):
   - Upload up to 5 photos
   - Record a voice note
5. **Submit** and receive your unique Report ID
6. **Save your Report ID** to track status later

### Track Your Report

1. **Navigate to Track Report** (`/track`)
2. **Enter your Report ID** (e.g., IND-00001)
3. **View detailed status** including:
   - Visual timeline showing progress
   - Current status and department assignment
   - Complete history of updates
   - All photos and voice notes
4. **Quick access** to recently submitted reports

### For Municipal Staff

1. **Navigate to Dashboard** (`/admin`)
2. **View reports** on map or in list view across Indian cities
3. **Filter** by category, status, priority
4. **Click on a report** to:
   - View full details and location
   - Update status
   - Assign to department (PWD, Electricity Board, Municipal Corporation, etc.)
   - Add notes
5. **Track progress** through status updates

### For Administrators

1. **Navigate to Analytics** (`/analytics`)
2. **Select time range** (7/30/90/365 days)
3. **View metrics**:
   - Total reports and resolution rate
   - Daily trends
   - Category distribution
   - Department efficiency
4. **Analyze heatmap** for problem areas

## API Endpoints

### Reports
- `GET /api/reports` - Get all reports
- `GET /api/reports/:id` - Get single report
- `POST /api/reports` - Create new report (multipart/form-data)
- `PATCH /api/reports/:id` - Update report
- `DELETE /api/reports/:id` - Delete report

### Analytics
- `GET /api/analytics` - Get analytics data

### Health
- `GET /health` - Server health check

## Project Structure

```
civic-report/
├── public/              # Static files
├── server/              # Backend API
│   ├── index.js        # Express server
│   └── uploads/        # Uploaded files
├── src/
│   ├── components/     # Reusable components
│   │   ├── Navigation.js
│   │   ├── PhotoUpload.js
│   │   ├── VoiceRecorder.js
│   │   ├── FilterPanel.js
│   │   └── ReportModal.js
│   ├── pages/          # Page components
│   │   ├── CitizenReport.js
│   │   ├── TrackReport.js (NEW)
│   │   ├── AdminDashboard.js
│   │   └── Analytics.js
│   ├── App.js          # Main app component
│   ├── App.css
│   ├── index.js        # Entry point
│   └── index.css       # Global styles
├── package.json
└── README.md
```

## Features in Detail

### Real-Time Updates
The application uses Socket.io for real-time bidirectional communication:
- New report notifications
- Status update notifications
- Live dashboard updates

### Report Tracking
Users can track their reports using unique Report IDs:
- Report IDs stored in browser localStorage
- Quick access to last 10 reports
- Detailed status timeline visualization
- Complete update history

### Geolocation
Automatic geolocation capture using the browser's Geolocation API:
- Captures latitude and longitude
- Fallback to default location if denied
- Can be manually updated

### File Uploads
Secure file handling with validation:
- Image formats: JPEG, PNG, GIF, WebP
- Audio formats: WebM, MP3, WAV, OGG
- 10MB file size limit
- Stored locally in `/server/uploads/`

### Responsive Design
Mobile-first approach with breakpoints:
- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px

## Configuration

### Environment Variables (Optional)
Create a `.env` file in the root directory:

```env
PORT=5000
REACT_APP_API_URL=http://localhost:5000/api
```

### Map Configuration
Default center coordinates are set to Delhi, India. The application includes mock data from major Indian cities:
- Delhi
- Mumbai
- Bangalore
- Chennai
- Hyderabad

To change the default center, edit the coordinates in:
- `src/pages/CitizenReport.js`
- `src/pages/AdminDashboard.js`
- `src/pages/Analytics.js`

## Performance Optimization

- Code splitting with React.lazy (can be implemented)
- Image optimization recommendations
- Lazy loading for map markers
- Debounced search filters
- Pagination for large datasets (can be implemented)

## Browser Support

- Chrome/Edge (latest 2 versions)
- Firefox (latest 2 versions)
- Safari (latest 2 versions)
- Mobile browsers (iOS Safari, Chrome Mobile)

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Email/SMS notifications
- [ ] Database integration (MongoDB, PostgreSQL)
- [ ] Cloud storage for uploads (AWS S3, Cloudinary)
- [ ] Progressive Web App (PWA) capabilities
- [ ] Offline support
- [ ] Multi-language support
- [ ] Export reports to PDF/CSV
- [ ] Public API documentation
- [ ] Admin user management
- [ ] Report comments/discussions
- [ ] Image optimization and thumbnails
- [ ] Advanced analytics (AI/ML insights)

## Troubleshooting

### Location not working
- Ensure HTTPS (or localhost) for geolocation API
- Check browser permissions
- Try a different browser

### File upload fails
- Check file size (< 10MB)
- Verify file format
- Ensure `/server/uploads/` directory exists

### Maps not loading
- Check internet connection
- Verify Leaflet CSS is loaded
- Check browser console for errors

## Contributing

This is a demonstration project. For production use:
1. Implement proper authentication
2. Add database integration
3. Set up proper error logging
4. Implement rate limiting
5. Add comprehensive tests
6. Set up CI/CD pipeline

## License

MIT License - Feel free to use this project for learning and development.

## Support

For issues and questions:
- Check the browser console for errors
- Review the server logs
- Ensure all dependencies are installed
- Verify Node.js version compatibility

---

Built with ❤️ for better civic engagement
