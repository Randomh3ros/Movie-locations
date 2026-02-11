package com.movielocations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.movielocations.R;
import com.movielocations.adapters.MovieAdapter;
import com.movielocations.database.AppDatabase;
import com.movielocations.models.Location;
import com.movielocations.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private RecyclerView moviesRecyclerView;
    private MovieAdapter movieAdapter;
    private EditText searchEditText;
    private Spinner languageSpinner;
    private AppDatabase database;
    private ExecutorService executorService;
    private List<Movie> allMovies = new ArrayList<>();
    private String selectedLanguage = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
        setupLanguageSpinner();
        setupSearch();
        setupBottomNavigation();
        loadSampleData();
        loadMovies();
    }

    private void initViews() {
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        languageSpinner = findViewById(R.id.languageSpinner);
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(this);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesRecyclerView.setAdapter(movieAdapter);
    }

    private void setupLanguageSpinner() {
        String[] languages = {"All", "English", "Spanish", "French", "German", "Italian", "Japanese", "Korean", "Chinese"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = languages[position];
                filterMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_movies);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_map) {
                startActivity(new Intent(this, MapActivity.class));
                return true;
            } else if (id == R.id.nav_friends) {
                startActivity(new Intent(this, FriendsActivity.class));
                return true;
            }
            return id == R.id.nav_movies;
        });
    }

    private void loadSampleData() {
        executorService.execute(() -> {
            // Check if database is empty
            List<Movie> existingMovies = database.movieDao().getAllMovies();
            if (existingMovies.isEmpty()) {
                // Add sample movies
                Movie movie1 = new Movie("The Lord of the Rings", "movie", "English", 
                        "Epic fantasy adventure film", "", 2001);
                long movieId1 = database.movieDao().insert(movie1);

                Movie movie2 = new Movie("Game of Thrones", "tvshow", "English",
                        "Fantasy drama television series", "", 2011);
                long movieId2 = database.movieDao().insert(movie2);

                Movie movie3 = new Movie("Amélie", "movie", "French",
                        "Romantic comedy film", "", 2001);
                long movieId3 = database.movieDao().insert(movie3);

                // Add sample locations
                Location loc1 = new Location((int)movieId1, "Hobbiton", "501 Buckland Rd, Hinuera, Matamata 3472, New Zealand",
                        -37.8722, 175.6830, "Hobbiton Movie Set", "Shire scenes");
                database.locationDao().insert(loc1);

                Location loc2 = new Location((int)movieId1, "Mount Sunday", "Canterbury, New Zealand",
                        -43.3990, 171.0790, "Edoras location", "Rohan capital scenes");
                database.locationDao().insert(loc2);

                Location loc3 = new Location((int)movieId2, "Dark Hedges", "Bregagh Rd, Ballymoney BT53 8TP, UK",
                        55.1419, -6.3819, "Kingsroad location", "Kingsroad scenes");
                database.locationDao().insert(loc3);

                Location loc4 = new Location((int)movieId3, "Café des 2 Moulins", "15 Rue Lepic, 75018 Paris, France",
                        48.8844, 2.3338, "Amélie's workplace", "Café scenes");
                database.locationDao().insert(loc4);
            }
        });
    }

    private void loadMovies() {
        executorService.execute(() -> {
            allMovies = database.movieDao().getAllMovies();
            runOnUiThread(() -> {
                movieAdapter.setMovies(allMovies);
            });
        });
    }

    private void filterMovies() {
        String query = searchEditText.getText().toString().toLowerCase();
        List<Movie> filtered = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean matchesLanguage = selectedLanguage.equals("All") || movie.getLanguage().equals(selectedLanguage);
            boolean matchesSearch = query.isEmpty() || movie.getTitle().toLowerCase().contains(query);

            if (matchesLanguage && matchesSearch) {
                filtered.add(movie);
            }
        }

        movieAdapter.setMovies(filtered);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
