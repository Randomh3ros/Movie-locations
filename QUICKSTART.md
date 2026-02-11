# Quick Start Guide

Get the Movie Locations app running in 5 minutes!

## Prerequisites

- [ ] Android Studio installed
- [ ] Google Maps API key ready

## Steps

### 1. Clone Repository (30 seconds)

```bash
git clone https://github.com/Randomh3ros/Movie-locations.git
cd Movie-locations
```

### 2. Open in Android Studio (1 minute)

1. Launch Android Studio
2. File → Open → Select the `Movie-locations` folder
3. Wait for Gradle sync

### 3. Add Google Maps API Key (1 minute)

**Get API Key:**
1. Visit https://console.cloud.google.com/
2. Create project → Enable "Maps SDK for Android"
3. Create API Key

**Add to App:**
1. Open `app/src/main/AndroidManifest.xml`
2. Line 23: Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your key
3. Save file

### 4. Run the App (2 minutes)

**Option A: Physical Device**
1. Enable USB debugging on your Android phone
2. Connect via USB
3. Click ▶️ Run button

**Option B: Emulator**
1. Tools → AVD Manager
2. Create/start emulator
3. Click ▶️ Run button

### 5. Test Features

✅ **Browse Movies** - See sample movies on main screen
✅ **Search** - Type in search box
✅ **Filter** - Select language from dropdown
✅ **View Details** - Tap any movie
✅ **Map** - Tap "Map View" button
✅ **GPS** - Tap 🎯 button (grant permission)
✅ **Friends** - Tap Friends tab → Tap +
✅ **Invite** - On map, tap marker → "Invite Friend"

## Sample Data Included

The app comes with pre-loaded data:

🎬 **Movies:**
- The Lord of the Rings (2001) - English
- Game of Thrones (2011) - English  
- Amélie (2001) - French

📍 **Locations:**
- Hobbiton, New Zealand
- Mount Sunday, New Zealand
- Dark Hedges, UK
- Café des 2 Moulins, Paris

## Troubleshooting

**Map is blank?**
→ Check API key is correct and "Maps SDK for Android" is enabled

**"App won't run without Google Play services"?**
→ Use emulator with Google Play (not basic AOSP)

**Build errors?**
→ File → Invalidate Caches / Restart

**Location not working?**
→ Grant location permission in app settings

## What's Next?

**For Developers:**
- Read `FEATURES.md` - Understand all features
- Read `API_INTEGRATION.md` - Integrate real APIs
- Customize sample data in `MainActivity.java`

**For Users:**
- Add your own movies and locations
- Invite real friends
- Share on social media

## Need Help?

📖 Detailed guides:
- `SETUP.md` - Full setup instructions
- `FEATURES.md` - All features explained
- `README.md` - Project overview

🐛 Found a bug?
- Open an issue on GitHub

## Next Features to Add

Want to enhance the app? Start with:
1. TMDb API integration (see API_INTEGRATION.md)
2. User authentication with Firebase
3. Photo upload from locations
4. Reviews and ratings
5. Push notifications for invites

---

**Time to first run:** < 5 minutes  
**Sample data:** Included  
**Documentation:** Complete  

Happy coding! 🎬📍