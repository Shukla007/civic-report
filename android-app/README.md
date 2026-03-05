# Civic Report - Android App

A native Android application built with Kotlin and Jetpack Compose for reporting and tracking civic issues.

## Features

### Citizen Features
- **Report Issues**: Submit civic problems with photos, description, location, and priority
- **Auto Location**: Automatically captures GPS coordinates
- **Photo Capture**: Take photos or select from gallery (up to 5 photos)
- **Track Reports**: Search and view report status with timeline history

### Admin Features
- **Dashboard**: View all reports with statistics
- **Search & Filter**: Search by ID/title and filter by status
- **Analytics**: View comprehensive statistics and charts
- **Update Status**: Change report status with notes

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose
- **State Management**: Kotlin StateFlow
- **Location**: Google Play Services Location
- **Permissions**: Accompanist Permissions

## Project Structure

```
android-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/civicreport/
│   │   │   ├── CivicReportApp.kt          # Application class
│   │   │   ├── MainActivity.kt            # Main activity
│   │   │   ├── data/
│   │   │   │   ├── api/
│   │   │   │   │   ├── ApiConstants.kt    # API configuration
│   │   │   │   │   └── CivicReportApi.kt  # Retrofit API interface
│   │   │   │   ├── model/
│   │   │   │   │   └── Models.kt          # Data classes
│   │   │   │   └── repository/
│   │   │   │       ├── AuthRepository.kt  # Auth token management
│   │   │   │       └── ReportRepository.kt # API operations
│   │   │   ├── di/
│   │   │   │   └── AppModule.kt           # Hilt dependency injection
│   │   │   ├── navigation/
│   │   │   │   ├── Navigation.kt          # Navigation setup
│   │   │   │   └── Screen.kt              # Route definitions
│   │   │   ├── ui/
│   │   │   │   ├── components/
│   │   │   │   │   └── CommonComponents.kt # Reusable UI components
│   │   │   │   ├── screens/
│   │   │   │   │   ├── ReportScreen.kt     # Submit report
│   │   │   │   │   ├── TrackScreen.kt      # Track report
│   │   │   │   │   ├── AdminLoginScreen.kt # Admin login
│   │   │   │   │   ├── AdminDashboardScreen.kt # Admin dashboard
│   │   │   │   │   ├── AnalyticsScreen.kt  # Analytics view
│   │   │   │   │   └── ReportDetailScreen.kt # Report details
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt           # Color definitions
│   │   │   │       ├── Shape.kt           # Shape definitions
│   │   │   │       ├── Theme.kt           # App theme
│   │   │   │       └── Type.kt            # Typography
│   │   │   └── viewmodel/
│   │   │       ├── AuthViewModel.kt
│   │   │       ├── ReportViewModel.kt
│   │   │       ├── TrackViewModel.kt
│   │   │       ├── LoginViewModel.kt
│   │   │       ├── DashboardViewModel.kt
│   │   │       ├── AnalyticsViewModel.kt
│   │   │       └── ReportDetailViewModel.kt
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       └── file_paths.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Running backend server

### Configuration

1. **Update API URL** (for physical device):
   
   Edit `app/src/main/java/com/civicreport/data/api/ApiConstants.kt`:
   ```kotlin
   // For emulator (default)
   const val BASE_URL = "http://10.0.2.2:5000/"
   
   // For physical device, use your computer's IP
   const val BASE_URL = "http://YOUR_IP:5000/"
   ```

2. **Start Backend Server**:
   ```bash
   cd ../server
   npm install
   npm start
   ```

### Building the App

1. Open the `android-app` folder in Android Studio
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator
4. Click Run (▶️) or press Shift+F10

### Building APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing config)
./gradlew assembleRelease
```

## Permissions Required

- **INTERNET**: API communication
- **ACCESS_FINE_LOCATION**: GPS coordinates for reports
- **CAMERA**: Take photos of issues
- **READ_MEDIA_IMAGES**: Select photos from gallery

## Demo Credentials

- **Username**: admin
- **Password**: admin123

## API Endpoints

The app communicates with the Express.js backend:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/reports | Get all reports |
| GET | /api/reports/:id | Get report by ID |
| POST | /api/reports | Create new report |
| POST | /api/admin/login | Admin login |
| PATCH | /api/reports/:id | Update report (admin) |

## Screenshots

The app includes:
- Material 3 design with dynamic theming
- Bottom navigation for main sections
- Card-based report listings
- Timeline view for report history
- Charts for analytics

## Troubleshooting

### Cannot connect to server
- Ensure the backend is running on port 5000
- For emulator: Use `10.0.2.2` as the host
- For physical device: Use your computer's IP and ensure both are on same network
- Check that `android:usesCleartextTraffic="true"` is in AndroidManifest.xml

### Location not working
- Grant location permission in app settings
- Enable GPS on device
- Try outdoors for better GPS signal

### Build errors
- Run `./gradlew clean` and rebuild
- Invalidate caches in Android Studio (File > Invalidate Caches)
- Ensure JDK 17 is configured
