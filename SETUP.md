# Setup Guide for Movie Locations Android App

This guide will help you set up and run the Movie Locations Android app on your development machine.

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Android Studio** (version Arctic Fox or later)
   - Download from: https://developer.android.com/studio

2. **Java Development Kit (JDK)** 8 or higher
   - Android Studio typically includes this

3. **Android SDK**
   - Minimum API Level: 24 (Android 7.0)
   - Target API Level: 34 (Android 14)

4. **Google Maps API Key**
   - Required for map functionality

## Step 1: Clone the Repository

```bash
git clone https://github.com/Randomh3ros/Movie-locations.git
cd Movie-locations
```

## Step 2: Open Project in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository folder
4. Click "OK"
5. Wait for Gradle sync to complete

## Step 3: Configure Google Maps API Key

### A. Get Google Maps API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Navigate to "APIs & Services" > "Library"
4. Search for and enable the following APIs:
   - **Maps SDK for Android**
   - **Places API** (optional, for enhanced location features)
5. Go to "APIs & Services" > "Credentials"
6. Click "Create Credentials" > "API Key"
7. Copy the generated API key

### B. Restrict API Key (Recommended)

1. Click on your newly created API key
2. Under "Application restrictions":
   - Select "Android apps"
   - Click "Add an item"
   - Enter package name: `com.movielocations`
   - Get your SHA-1 fingerprint (see below)
3. Under "API restrictions":
   - Select "Restrict key"
   - Choose "Maps SDK for Android"
4. Click "Save"

### C. Get SHA-1 Fingerprint

**For Debug Certificate:**
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

**For Release Certificate:**
```bash
keytool -list -v -keystore /path/to/your/keystore.jks -alias your-alias-name
```

### D. Add API Key to Project

1. Open `app/src/main/AndroidManifest.xml`
2. Find the line:
   ```xml
   android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE"
   ```
3. Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual API key
4. Save the file

## Step 4: Sync Project with Gradle

1. Click "File" > "Sync Project with Gradle Files"
2. Wait for the sync to complete
3. Resolve any dependency issues if they appear

## Step 5: Set Up an Android Device or Emulator

### Option A: Physical Device

1. Enable Developer Options on your Android device:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
2. Enable USB Debugging:
   - Go to Settings > Developer Options
   - Enable "USB Debugging"
3. Connect device via USB
4. Accept the debugging authorization prompt on your device

### Option B: Android Emulator

1. Click "Tools" > "AVD Manager" in Android Studio
2. Click "Create Virtual Device"
3. Select a device (e.g., Pixel 5)
4. Download and select a system image (API 24 or higher with Google Play)
5. Click "Finish"
6. Start the emulator

## Step 6: Run the App

1. Select your device/emulator from the device dropdown
2. Click the "Run" button (green play icon) or press Shift+F10
3. Wait for the app to build and install
4. The app should launch automatically

## Troubleshooting

### Build Errors

**Error: "SDK location not found"**
- Solution: Create `local.properties` file in project root:
  ```
  sdk.dir=/path/to/your/Android/Sdk
  ```

**Error: "Unable to resolve dependency"**
- Solution: Check your internet connection and try:
  - File > Invalidate Caches / Restart
  - Clean Project (Build > Clean Project)
  - Rebuild Project (Build > Rebuild Project)

### Google Maps Issues

**Map not showing or showing blank**
- Verify API key is correct
- Ensure "Maps SDK for Android" is enabled in Google Cloud Console
- Check package name matches in API key restrictions
- Verify SHA-1 fingerprint is added to API key restrictions

**"This app won't run without Google Play services"**
- Ensure your emulator has Google Play services
- Use a system image with "Google APIs" or "Google Play"

### Permission Issues

**Location not working**
- Grant location permissions when prompted
- On Android 6.0+, manually grant permissions in:
  - Settings > Apps > Movie Locations > Permissions > Location

### Database Issues

**App crashes on startup**
- Clear app data:
  - Settings > Apps > Movie Locations > Storage > Clear Data
- Reinstall the app

## Testing Features

### 1. Browse Movies
- Open the app
- You should see sample movies (Lord of the Rings, Game of Thrones, Amélie)
- Use the search bar to filter
- Use the language spinner to filter by language

### 2. View Movie Details
- Tap on any movie
- View filming locations list
- Tap "Map View" button

### 3. Map Functionality
- From movie details or bottom navigation, go to Map View
- You should see location markers
- Tap on markers to see location details
- Tap the floating action button to center on your location
- Grant location permission if prompted

### 4. Friends Feature
- Tap "Friends" in bottom navigation
- Tap the "+" button to add a friend
- Fill in name and email
- Friend should appear in the list

### 5. Invite Friends to Location
- Go to Map View
- Tap on a location marker
- Tap "Invite Friend" button
- Select a friend
- Email client should open with invitation

### 6. Social Sharing
- From movie details, tap "Share" button
- Choose a sharing method
- Share information should be populated

## Development Tips

### Enable Debug Logging

Add to your app's `build.gradle`:
```gradle
android {
    buildTypes {
        debug {
            buildConfigField "boolean", "ENABLE_LOGGING", "true"
        }
    }
}
```

### View Database

Use Android Studio's Database Inspector:
1. Run the app
2. View > Tool Windows > App Inspection
3. Select "Database Inspector" tab
4. Explore tables and data

### Monitor Network Requests

1. View > Tool Windows > Logcat
2. Filter by package name: `com.movielocations`

## Building for Release

1. Generate signed APK:
   - Build > Generate Signed Bundle / APK
   - Select "APK"
   - Create or select keystore
   - Fill in keystore details
   - Select "release" build variant
   - Click "Finish"

2. The APK will be generated in:
   ```
   app/release/app-release.apk
   ```

## Additional Configuration

### Add Custom Movie Data

Edit `MainActivity.java`, locate `loadSampleData()` method and add:

```java
Movie customMovie = new Movie("Movie Title", "movie", "English", 
    "Description", "poster_url", 2024);
long movieId = database.movieDao().insert(customMovie);

Location customLocation = new Location((int)movieId, "Location Name", 
    "Address", latitude, longitude, "Description", "Scene");
database.locationDao().insert(customLocation);
```

### Connect to Real API

1. Add your API service interface in a new package `api`
2. Configure Retrofit in a `NetworkModule` class
3. Replace sample data loading with API calls

## Support

If you encounter any issues:

1. Check the [GitHub Issues](https://github.com/Randomh3ros/Movie-locations/issues)
2. Ensure all steps in this guide were followed
3. Verify your Android Studio and SDK are up to date
4. Create a new issue with:
   - Android Studio version
   - Device/Emulator details
   - Complete error message
   - Steps to reproduce

## Next Steps

After successful setup:

1. Explore the codebase
2. Customize the app with your own branding
3. Add more movies and locations
4. Integrate with real movie databases (TMDb, OMDb)
5. Implement user authentication
6. Add cloud synchronization

Happy coding! 🎬📍