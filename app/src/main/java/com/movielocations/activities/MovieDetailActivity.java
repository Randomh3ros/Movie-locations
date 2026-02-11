package com.movielocations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movielocations.R;
import com.movielocations.adapters.LocationAdapter;
import com.movielocations.database.AppDatabase;
import com.movielocations.models.Location;
import com.movielocations.models.Movie;
import com.movielocations.utils.SocialMediaUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieDetailActivity extends AppCompatActivity implements LocationAdapter.OnLocationClickListener {
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView languageTextView;
    private TextView descriptionTextView;
    private RecyclerView locationsRecyclerView;
    private Button viewMapButton;
    private Button shareButton;

    private AppDatabase database;
    private ExecutorService executorService;
    private LocationAdapter locationAdapter;
    private Movie currentMovie;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
        loadMovieDetails();
    }

    private void initViews() {
        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        yearTextView = findViewById(R.id.yearTextView);
        languageTextView = findViewById(R.id.languageTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationsRecyclerView = findViewById(R.id.locationsRecyclerView);
        viewMapButton = findViewById(R.id.viewMapButton);
        shareButton = findViewById(R.id.shareButton);

        viewMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            if (currentMovie != null) {
                intent.putExtra("movie_id", currentMovie.getId());
            }
            startActivity(intent);
        });

        shareButton.setOnClickListener(v -> {
            if (currentMovie != null) {
                String shareText = String.format("Check out %s (%d) - %s",
                        currentMovie.getTitle(),
                        currentMovie.getYear(),
                        currentMovie.getDescription());
                SocialMediaUtils.shareGeneric(this, shareText);
            }
        });
    }

    private void setupRecyclerView() {
        locationAdapter = new LocationAdapter(this);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationsRecyclerView.setAdapter(locationAdapter);
    }

    private void loadMovieDetails() {
        int movieId = getIntent().getIntExtra("movie_id", -1);
        if (movieId == -1) {
            finish();
            return;
        }

        executorService.execute(() -> {
            currentMovie = database.movieDao().getMovieById(movieId);
            locations = database.locationDao().getLocationsByMovieId(movieId);

            runOnUiThread(() -> {
                if (currentMovie != null) {
                    titleTextView.setText(currentMovie.getTitle());
                    yearTextView.setText(String.valueOf(currentMovie.getYear()));
                    languageTextView.setText(currentMovie.getLanguage());
                    descriptionTextView.setText(currentMovie.getDescription());
                    
                    // Load poster image with Glide
                    if (currentMovie.getPosterUrl() != null && !currentMovie.getPosterUrl().isEmpty()) {
                        com.bumptech.glide.Glide.with(MovieDetailActivity.this)
                            .load(currentMovie.getPosterUrl())
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.ic_menu_gallery)
                            .into(posterImageView);
                    } else {
                        posterImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                }

                if (locations != null && !locations.isEmpty()) {
                    locationAdapter.setLocations(locations);
                }
            });
        });
    }

    @Override
    public void onLocationClick(Location location) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("location_id", location.getId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
