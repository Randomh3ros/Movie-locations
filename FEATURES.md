# Movie Locations App - Features Documentation

This document provides detailed information about all the features implemented in the Movie Locations Android app.

## 🎬 Core Features

### 1. Movie & TV Show Database

**Description**: Browse a comprehensive database of movies and TV shows with their filming locations.

**Implementation**:
- Room database for local storage
- Movie entity with fields: title, type (movie/tvshow), language, description, poster URL, year
- Sample data includes:
  - The Lord of the Rings (2001) - English
  - Game of Thrones (2011) - English  
  - Amélie (2001) - French

**User Actions**:
- Browse all movies in a scrollable list
- View movie poster thumbnails
- See movie title, year, and language at a glance
- Tap to view detailed information

**Code Location**: 
- `models/Movie.java`
- `database/MovieDao.java`
- `activities/MainActivity.java`

---

### 2. Filming Locations

**Description**: Each movie/show has multiple filming locations with GPS coordinates and detailed information.

**Implementation**:
- Location entity with fields: movie ID, location name, address, latitude, longitude, description, scene name
- One-to-many relationship between movies and locations
- Sample locations include real-world places like Hobbiton (NZ), Dark Hedges (UK), Café des 2 Moulins (Paris)

**User Actions**:
- View all locations for a specific movie
- See location name, address, and scene description
- Tap location to view on map

**Code Location**:
- `models/Location.java`
- `database/LocationDao.java`
- `activities/MovieDetailActivity.java`

---

### 3. Language Filter

**Description**: Filter movies and TV shows by their original language.

**Implementation**:
- Spinner (dropdown) with language options
- Supported languages: All, English, Spanish, French, German, Italian, Japanese, Korean, Chinese
- Real-time filtering without database queries

**User Actions**:
- Select language from dropdown in main screen
- Movies automatically filter based on selection
- Combine with search for refined results

**Code Location**:
- `activities/MainActivity.java` (setupLanguageSpinner, filterMovies methods)
- `res/values/strings.xml` (language strings)

---

### 4. Search Functionality

**Description**: Search movies and TV shows by title.

**Implementation**:
- EditText with TextWatcher for real-time search
- Case-insensitive search
- Works in combination with language filter

**User Actions**:
- Type in search box
- Results filter automatically as you type
- Clear search to see all movies

**Code Location**:
- `activities/MainActivity.java` (setupSearch, filterMovies methods)

---

### 5. Google Maps Integration

**Description**: Interactive map showing all filming locations with markers and details.

**Implementation**:
- Google Maps SDK for Android
- Custom markers for each location
- Info window with location details
- Camera animations for smooth transitions

**User Actions**:
- View all locations on a world map
- Tap markers to see location information
- Zoom and pan the map
- See location details in a bottom card

**Code Location**:
- `activities/MapActivity.java`
- `res/layout/activity_map.xml`
- `AndroidManifest.xml` (Google Maps API key configuration)

**Setup Required**:
- Google Maps API key (see SETUP.md)
- Maps SDK for Android enabled in Google Cloud Console

---

### 6. GPS Location Tracking

**Description**: Track user's current location and show on map.

**Implementation**:
- FusedLocationProviderClient for location services
- Runtime permission requests (Android 6.0+)
- Location permission handling with user-friendly prompts

**User Actions**:
- Grant location permission when prompted
- Tap "My Location" FAB to center map on current position
- See your location with blue dot on map

**Code Location**:
- `utils/PermissionUtils.java`
- `activities/MapActivity.java` (showMyLocation method)

**Permissions Required**:
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`

---

### 7. Friend Management System

**Description**: Add and manage friends within the app.

**Implementation**:
- Friend entity with fields: name, email, phone, profile picture, social media ID/type
- FriendDao for database operations
- RecyclerView adapter for displaying friends

**User Actions**:
- View list of all friends
- Add new friend with dialog (name, email, phone)
- Tap friend to see options
- Remove friends from list

**Code Location**:
- `models/Friend.java`
- `database/FriendDao.java`
- `activities/FriendsActivity.java`
- `adapters/FriendAdapter.java`
- `res/layout/dialog_add_friend.xml`

---

### 8. Friend Invitations to Locations

**Description**: Invite friends to visit filming locations with you.

**Implementation**:
- Invitation entity tracking invitation status
- Email intent for sending invitations
- Location details included in invitation

**User Actions**:
- View location on map
- Tap "Invite Friend" button
- Select friend from list
- Confirmation dialog before sending
- Email app opens with pre-filled invitation text

**Code Location**:
- `models/Invitation.java`
- `database/InvitationDao.java`
- `activities/FriendsActivity.java` (sendInvitation method)
- `utils/SocialMediaUtils.java` (inviteFriendToLocation method)

---

### 9. Social Media Integration

**Description**: Share locations and connect with friends on social media platforms.

**Implementation**:
- Intent-based sharing for Facebook, Twitter, Instagram
- Fallback to generic share if app not installed
- Custom share text for each location/movie

**Supported Platforms**:
- Facebook
- Twitter
- Instagram
- Generic sharing (SMS, WhatsApp, etc.)

**User Actions**:
- Share movie details from detail screen
- Share specific locations
- Connect with friends on social media
- Choose sharing platform from dialog

**Code Location**:
- `utils/SocialMediaUtils.java`
- `activities/MovieDetailActivity.java` (share button)
- `activities/FriendsActivity.java` (showFriendOptionsDialog method)

**Methods**:
- `shareOnFacebook()` - Share to Facebook
- `shareOnTwitter()` - Share to Twitter
- `shareOnInstagram()` - Share to Instagram
- `shareGeneric()` - Generic share picker
- `shareLocation()` - Share specific location

---

## 🎨 User Interface Features

### 10. Material Design

**Implementation**:
- Material Components library
- CardViews for list items
- Floating Action Buttons
- Material color themes
- Consistent design language

**Components Used**:
- MaterialCardView
- BottomNavigationView
- FloatingActionButton
- TextInputLayout
- Toolbar with AppBar

**Code Location**:
- `res/values/themes.xml`
- `res/values/colors.xml`
- All layout XML files

---

### 11. Bottom Navigation

**Description**: Easy navigation between main app sections.

**Implementation**:
- Three main sections: Movies, Map, Friends
- Active section highlighting
- Icon + label for each section

**Code Location**:
- `res/layout/activity_main.xml`
- `res/menu/bottom_nav_menu.xml`
- `activities/MainActivity.java` (setupBottomNavigation)

---

### 12. RecyclerView Lists

**Description**: Efficient scrolling lists for movies, locations, and friends.

**Implementation**:
- Custom adapters for each data type
- ViewHolder pattern for performance
- Click listeners for item selection

**Lists**:
- Movies list (main screen)
- Locations list (movie detail screen)
- Friends list (friends screen)

**Code Location**:
- `adapters/MovieAdapter.java`
- `adapters/LocationAdapter.java`
- `adapters/FriendAdapter.java`

---

## 💾 Data Management Features

### 13. Room Database

**Description**: Local SQLite database using Room persistence library.

**Implementation**:
- Four main entities: Movie, Location, Friend, Invitation
- DAO interfaces for each entity
- Singleton database instance
- Background thread execution

**Capabilities**:
- CRUD operations for all entities
- Filtered queries (by language, movie ID, etc.)
- Search queries
- Relationship management

**Code Location**:
- `database/AppDatabase.java`
- All DAO files in `database/` package

---

### 14. Sample Data

**Description**: Pre-populated sample data for testing and demonstration.

**Implementation**:
- Sample movies and locations inserted on first launch
- Real-world filming locations with accurate GPS coordinates
- Diverse language representation

**Sample Content**:
1. **The Lord of the Rings** (English, 2001)
   - Hobbiton, New Zealand (-37.8722, 175.6830)
   - Mount Sunday, New Zealand (-43.3990, 171.0790)

2. **Game of Thrones** (English, 2011)
   - Dark Hedges, UK (55.1419, -6.3819)

3. **Amélie** (French, 2001)
   - Café des 2 Moulins, Paris (48.8844, 2.3338)

**Code Location**:
- `activities/MainActivity.java` (loadSampleData method)

---

## 🔒 Security & Permissions Features

### 15. Runtime Permissions

**Description**: Proper handling of Android runtime permissions.

**Implementation**:
- Permission check before accessing location
- User-friendly permission request dialogs
- Graceful handling of denied permissions
- Settings redirection for permanent denials

**Permissions Managed**:
- Location (fine and coarse)
- Internet (manifest-level)
- Network state (manifest-level)

**Code Location**:
- `utils/PermissionUtils.java`
- `activities/MapActivity.java` (onRequestPermissionsResult)

---

## 📱 Additional Features

### 16. Multi-Activity Architecture

**Activities**:
1. **MainActivity** - Movie browsing with search and filter
2. **MovieDetailActivity** - Movie details and locations
3. **MapActivity** - Interactive map with locations
4. **FriendsActivity** - Friend management

**Navigation Flow**:
```
MainActivity
  ├─> MovieDetailActivity
  │     └─> MapActivity (specific location)
  ├─> MapActivity (all locations)
  └─> FriendsActivity
        └─> Email intent (invitations)
```

---

### 17. Responsive Layouts

**Implementation**:
- ConstraintLayout for flexible layouts
- ScrollView for content longer than screen
- RecyclerView for efficient lists
- Proper margin and padding
- Material elevation and shadows

**Layouts**:
- Portrait orientation optimized
- Tablet compatibility through responsive constraints
- Accessibility-friendly sizing

---

### 18. Image Loading Support

**Implementation**:
- Glide library integrated
- Placeholder support
- Error handling
- Image caching

**Usage** (ready for implementation):
```java
Glide.with(context)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .into(imageView);
```

**Code Location**:
- Dependencies in `app/build.gradle`
- TODO comments in adapter classes

---

### 19. Network Capability

**Implementation**:
- Retrofit library integrated
- OkHttp for HTTP client
- Gson converter for JSON parsing
- Logging interceptor for debugging

**Ready for**:
- API integration (TMDb, OMDb, etc.)
- Cloud synchronization
- Real-time updates

**Code Location**:
- Dependencies in `app/build.gradle`

---

## 🚀 Future Enhancement Possibilities

The app architecture supports easy addition of:

1. **User Authentication**
   - Firebase Auth integration point ready
   - User model can be added

2. **Cloud Sync**
   - Firebase Firestore integration ready
   - Local-first architecture in place

3. **Real Movie Data**
   - Retrofit setup ready for TMDb/OMDb API
   - Data models designed for API responses

4. **Push Notifications**
   - Firebase Cloud Messaging can be added
   - Invitation system ready for notifications

5. **Photo Upload**
   - Camera and gallery intents can be added
   - Image upload infrastructure ready

6. **Reviews & Ratings**
   - Database schema can be extended
   - UI components can be added

7. **Offline Mode**
   - Room database provides offline-first capability
   - Sync logic can be added

8. **Augmented Reality**
   - ARCore can be integrated
   - Location-based AR experiences possible

---

## 📊 Technical Specifications

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Language**: Java
- **Architecture**: Activity-based with Room database
- **Build System**: Gradle 8.0
- **UI Framework**: Material Design Components

---

## 🛠️ Extensibility

The app is designed for easy extension:

1. **Adding New Entities**: Create model → DAO → integrate
2. **Adding New Screens**: Create Activity → Layout → Add navigation
3. **Adding API Integration**: Retrofit interface → Service → Repository
4. **Adding Features**: Modular util classes → Update relevant activities

All features are well-documented and follow Android best practices.