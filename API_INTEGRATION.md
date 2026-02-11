# API Integration Guide

This guide explains how to integrate real movie databases and APIs into the Movie Locations app.

## Overview

The app currently uses sample data but is designed to easily integrate with external APIs like:
- TMDb (The Movie Database)
- OMDb (Open Movie Database)
- Custom backend API

## Recommended: TMDb API Integration

### Why TMDb?

- Free API with generous limits
- Comprehensive movie and TV show data
- High-quality images
- Multiple language support
- Active community

### Step 1: Get TMDb API Key

1. Go to https://www.themoviedb.org/
2. Create a free account
3. Navigate to Settings → API
4. Request an API key (choose "Developer" option)
5. Fill out the form with your app details
6. Copy your API key

### Step 2: Create API Service Interface

Create `app/src/main/java/com/movielocations/api/TMDbApiService.java`:

```java
package com.movielocations.api;

import com.movielocations.api.models.MovieResponse;
import com.movielocations.api.models.MovieDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApiService {
    
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
        @Query("api_key") String apiKey,
        @Query("query") String query,
        @Query("language") String language,
        @Query("page") int page
    );
    
    @GET("movie/{movie_id}")
    Call<MovieDetailResponse> getMovieDetails(
        @Path("movie_id") int movieId,
        @Query("api_key") String apiKey,
        @Query("language") String language
    );
    
    @GET("discover/movie")
    Call<MovieResponse> discoverMovies(
        @Query("api_key") String apiKey,
        @Query("language") String language,
        @Query("sort_by") String sortBy,
        @Query("page") int page
    );
    
    @GET("tv/{tv_id}")
    Call<MovieDetailResponse> getTVShowDetails(
        @Path("tv_id") int tvId,
        @Query("api_key") String apiKey,
        @Query("language") String language
    );
}
```

### Step 3: Create API Response Models

Create `app/src/main/java/com/movielocations/api/models/MovieResponse.java`:

```java
package com.movielocations.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {
    @SerializedName("page")
    private int page;
    
    @SerializedName("results")
    private List<MovieResult> results;
    
    @SerializedName("total_pages")
    private int totalPages;
    
    @SerializedName("total_results")
    private int totalResults;
    
    // Getters and setters
    public int getPage() { return page; }
    public List<MovieResult> getResults() { return results; }
    public int getTotalPages() { return totalPages; }
    public int getTotalResults() { return totalResults; }
}

class MovieResult {
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("original_language")
    private String originalLanguage;
    
    @SerializedName("overview")
    private String overview;
    
    @SerializedName("poster_path")
    private String posterPath;
    
    @SerializedName("release_date")
    private String releaseDate;
    
    @SerializedName("vote_average")
    private double voteAverage;
    
    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOriginalLanguage() { return originalLanguage; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return posterPath; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    
    // Helper method to get full poster URL
    public String getFullPosterUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
```

### Step 4: Create Retrofit Client

Create `app/src/main/java/com/movielocations/api/RetrofitClient.java`:

```java
package com.movielocations.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
            
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    public static TMDbApiService getTMDbService() {
        return getClient().create(TMDbApiService.class);
    }
}
```

### Step 5: Create Repository Layer

Create `app/src/main/java/com/movielocations/repository/MovieRepository.java`:

```java
package com.movielocations.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.movielocations.api.RetrofitClient;
import com.movielocations.api.TMDbApiService;
import com.movielocations.api.models.MovieResponse;
import com.movielocations.database.AppDatabase;
import com.movielocations.models.Movie;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TMDB_API_KEY = "YOUR_TMDB_API_KEY_HERE";
    private TMDbApiService apiService;
    private AppDatabase database;
    private ExecutorService executorService;
    
    public MovieRepository(Context context) {
        apiService = RetrofitClient.getTMDbService();
        database = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public LiveData<List<Movie>> searchMovies(String query, String language) {
        MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
        
        apiService.searchMovies(TMDB_API_KEY, query, language, 1)
            .enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Convert API results to local Movie objects
                        // Store in database
                        // Update LiveData
                    }
                }
                
                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    // Load from local database as fallback
                    executorService.execute(() -> {
                        List<Movie> localMovies = database.movieDao().searchMovies(query);
                        moviesLiveData.postValue(localMovies);
                    });
                }
            });
        
        return moviesLiveData;
    }
    
    public LiveData<List<Movie>> getMoviesByLanguage(String language) {
        MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
        
        executorService.execute(() -> {
            List<Movie> movies;
            if (language.equals("All")) {
                movies = database.movieDao().getAllMovies();
            } else {
                movies = database.movieDao().getMoviesByLanguage(language);
            }
            moviesLiveData.postValue(movies);
        });
        
        return moviesLiveData;
    }
}
```

### Step 6: Store API Key Securely

Add to `local.properties` (not committed to git):
```properties
tmdb.api.key=your_actual_api_key_here
```

Add to `app/build.gradle`:
```gradle
android {
    defaultConfig {
        // ...
        def localProperties = new Properties()
        localProperties.load(new FileInputStream(rootProject.file("local.properties")))
        buildConfigField "String", "TMDB_API_KEY", "\"${localProperties['tmdb.api.key']}\""
    }
}
```

Use in code:
```java
private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
```

### Step 7: Update MainActivity

```java
public class MainActivity extends AppCompatActivity {
    private MovieRepository movieRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        movieRepository = new MovieRepository(this);
        
        // Load movies from API
        movieRepository.searchMovies("", "en").observe(this, movies -> {
            if (movies != null) {
                movieAdapter.setMovies(movies);
            }
        });
    }
}
```

## Alternative: Custom Backend API

### Benefits
- Full control over data
- Custom filming location data
- User authentication
- Cloud synchronization

### Technology Stack Options

**Option 1: Firebase**
- Firestore for database
- Firebase Auth for authentication
- Firebase Storage for images
- Cloud Functions for backend logic

**Option 2: Node.js + Express**
- MongoDB for database
- JWT for authentication
- AWS S3 for images
- RESTful API

**Option 3: Django**
- PostgreSQL for database
- Django REST Framework
- Token authentication
- AWS hosting

### Backend API Endpoints

```
# Movies
GET    /api/movies                  # List all movies
GET    /api/movies?language=en      # Filter by language
GET    /api/movies/:id              # Get movie details
POST   /api/movies                  # Create movie (admin)

# Locations
GET    /api/locations               # List all locations
GET    /api/movies/:id/locations    # Get locations for movie
GET    /api/locations/:id           # Get location details
POST   /api/locations               # Create location (admin)

# Friends
GET    /api/friends                 # List user's friends
POST   /api/friends                 # Add friend
DELETE /api/friends/:id             # Remove friend

# Invitations
GET    /api/invitations             # List invitations
POST   /api/invitations             # Send invitation
PUT    /api/invitations/:id         # Update invitation status

# Authentication
POST   /api/auth/register           # Register user
POST   /api/auth/login              # Login
POST   /api/auth/logout             # Logout
```

### Sample Backend Integration

Create `app/src/main/java/com/movielocations/api/CustomApiService.java`:

```java
public interface CustomApiService {
    
    @GET("movies")
    Call<List<Movie>> getAllMovies(@Header("Authorization") String token);
    
    @GET("movies/{id}")
    Call<Movie> getMovie(@Path("id") int id);
    
    @GET("movies/{id}/locations")
    Call<List<Location>> getMovieLocations(@Path("id") int movieId);
    
    @POST("friends")
    Call<Friend> addFriend(@Body Friend friend, @Header("Authorization") String token);
    
    @POST("invitations")
    Call<Invitation> sendInvitation(@Body Invitation invitation, @Header("Authorization") String token);
}
```

## Filming Location Data Sources

### Where to Find Filming Location Data

1. **IMDb Filming Locations**
   - URL: https://www.imdb.com/
   - Navigate to movie → "Filming & Production" → "Filming Locations"
   - Manual data entry required

2. **Movie-Locations.com**
   - Comprehensive database
   - GPS coordinates available
   - API might be available

3. **Google Places API**
   - Search for "Filming location [Movie Name]"
   - Get address and coordinates
   - Requires Google Places API key

4. **Wikipedia**
   - Production sections often list filming locations
   - Manual extraction needed

5. **User-Generated Content**
   - Allow users to submit locations
   - Verification system
   - Community-driven

### Automating Location Data Collection

```python
# Sample Python script to scrape and format location data
import requests
from bs4 import BeautifulSoup

def scrape_imdb_locations(imdb_id):
    url = f"https://www.imdb.com/title/{imdb_id}/locations"
    response = requests.get(url)
    soup = BeautifulSoup(response.content, 'html.parser')
    
    locations = []
    for location in soup.find_all('dt', class_='ipl-zebra-list__label'):
        location_name = location.text.strip()
        # Extract coordinates using Geocoding API
        coordinates = geocode_location(location_name)
        locations.append({
            'name': location_name,
            'latitude': coordinates['lat'],
            'longitude': coordinates['lng']
        })
    
    return locations

def geocode_location(address):
    # Use Google Geocoding API
    api_key = "YOUR_GEOCODING_API_KEY"
    url = f"https://maps.googleapis.com/maps/api/geocode/json?address={address}&key={api_key}"
    response = requests.get(url)
    data = response.json()
    
    if data['results']:
        location = data['results'][0]['geometry']['location']
        return {'lat': location['lat'], 'lng': location['lng']}
    return None
```

## Testing API Integration

1. **Unit Tests**: Test API service calls
2. **Integration Tests**: Test repository layer
3. **Mock Server**: Use MockWebServer for testing
4. **Error Handling**: Test network failures, timeouts, invalid responses

## Best Practices

1. **Caching**: Cache API responses in Room database
2. **Offline-First**: Always load from database first
3. **Error Handling**: Graceful fallbacks for API failures
4. **Rate Limiting**: Respect API rate limits
5. **API Keys**: Never commit API keys to version control
6. **Loading States**: Show loading indicators
7. **Pagination**: Implement for large datasets
8. **Image Loading**: Use Glide with caching

## Performance Tips

- Use WorkManager for background sync
- Implement paging for large lists
- Compress images before upload
- Use ProGuard for release builds
- Enable R8 optimization

## Monitoring & Analytics

Consider integrating:
- Firebase Analytics for usage tracking
- Crashlytics for crash reporting
- Performance monitoring
- API call analytics

## Resources

- TMDb API Docs: https://developers.themoviedb.org/3
- Retrofit Docs: https://square.github.io/retrofit/
- Room Docs: https://developer.android.com/training/data-storage/room
- Google Maps API: https://developers.google.com/maps/documentation/android-sdk