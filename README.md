# Movie Locations Android App

An Android application that helps you discover filming locations of movies and TV shows, with social features to connect with friends and GPS/Google Maps integration.

## Features

### 🎬 Movie & TV Show Locations
- Browse extensive database of movies and TV shows
- View filming locations for each movie/show
- Detailed information about each location including address and scene descriptions

### 🗺️ GPS & Google Maps Integration
- Interactive map showing all filming locations
- GPS-based location tracking
- Navigate to nearby filming locations
- View your current location on the map

### 🌍 Language Filter
- Filter movies and TV shows by language
- Supports multiple languages:
  - English
  - Spanish
  - French
  - German
  - Italian
  - Japanese
  - Korean
  - Chinese

### 👥 Friend Management
- Add friends to your network
- View your friends list
- Connect with friends through social media

### 📧 Location Invitations
- Invite friends to visit filming locations with you
- Share location details via email
- Track invitation status

### 📱 Social Media Integration
- Share locations on Facebook
- Share locations on Twitter
- Share locations on Instagram
- General sharing options for all social platforms

## Technical Stack

- **Language**: Java
- **Architecture**: Activity-based with RecyclerView adapters
- **Database**: Room (SQLite)
- **Maps**: Google Maps Android API
- **Location Services**: Google Play Services Location
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Glide
- **UI Components**: Material Design Components

## Project Structure

```
app/
├── src/main/
│   ├── java/com/movielocations/
│   │   ├── activities/           # UI Activities
│   │   │   ├── MainActivity.java
│   │   │   ├── MapActivity.java
│   │   │   ├── MovieDetailActivity.java
│   │   │   └── FriendsActivity.java
│   │   ├── adapters/             # RecyclerView Adapters
│   │   │   ├── MovieAdapter.java
│   │   │   ├── LocationAdapter.java
│   │   │   └── FriendAdapter.java
│   │   ├── models/               # Data Models
│   │   │   ├── Movie.java
│   │   │   ├── Location.java
│   │   │   ├── Friend.java
│   │   │   └── Invitation.java
│   │   ├── database/             # Room Database
│   │   │   ├── AppDatabase.java
│   │   │   ├── MovieDao.java
│   │   │   ├── LocationDao.java
│   │   │   ├── FriendDao.java
│   │   │   └── InvitationDao.java
│   │   └── utils/                # Utility Classes
│   │       ├── PermissionUtils.java
│   │       └── SocialMediaUtils.java
│   ├── res/                      # Resources
│   │   ├── layout/               # XML Layouts
│   │   ├── values/               # Strings, Colors, Themes
│   │   └── menu/                 # Navigation Menus
│   └── AndroidManifest.xml       # App Configuration
└── build.gradle                  # App Dependencies
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Google Maps API Key

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Randomh3ros/Movie-locations.git
cd Movie-locations
```

2. Open the project in Android Studio

3. Get a Google Maps API Key:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable "Maps SDK for Android" API
   - Create credentials (API Key)
   - Restrict the key to Android apps with your package name

4. Add your Google Maps API Key:
   - Open `app/src/main/AndroidManifest.xml`
   - Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual API key

5. Build and run the app:
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio

## Permissions Required

The app requires the following permissions:
- `INTERNET` - For fetching movie data and maps
- `ACCESS_FINE_LOCATION` - For GPS location tracking
- `ACCESS_COARSE_LOCATION` - For approximate location
- `ACCESS_NETWORK_STATE` - For checking network connectivity

## Usage

### Browsing Movies
1. Launch the app
2. Use the search bar to find specific movies/TV shows
3. Use the language filter to filter by language
4. Tap on a movie to view details and filming locations

### Viewing Locations on Map
1. From movie details, tap "Map View"
2. View all filming locations as markers on the map
3. Tap markers to see location details
4. Use the floating action button to center map on your location

### Managing Friends
1. Tap the "Friends" tab in bottom navigation
2. Tap the "+" button to add a new friend
3. Enter friend details (name, email, phone)
4. Tap on a friend to see social media sharing options

### Inviting Friends to Locations
1. View a location on the map
2. Tap "Invite Friend" button
3. Select a friend from your list
4. Invitation will be sent via email

## Sample Data

The app includes sample data for demonstration:
- The Lord of the Rings (2001) - English
  - Hobbiton, New Zealand
  - Mount Sunday, New Zealand
- Game of Thrones (2011) - English
  - Dark Hedges, UK
- Amélie (2001) - French
  - Café des 2 Moulins, Paris

## Future Enhancements

- Integration with TMDb API for real movie data
- User authentication and cloud sync
- Real-time friend location sharing
- Reviews and ratings for locations
- Photo sharing from locations
- Augmented Reality features
- Offline mode with cached data

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or contributions, please open an issue on GitHub.
