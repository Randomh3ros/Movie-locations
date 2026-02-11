package com.movielocations.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.movielocations.R;
import com.movielocations.database.AppDatabase;
import com.movielocations.utils.PermissionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase database;
    private ExecutorService executorService;
    private MaterialCardView locationInfoCard;
    private TextView locationNameTextView;
    private TextView locationAddressTextView;
    private TextView locationDescriptionTextView;
    private Button inviteFriendButton;
    private FloatingActionButton myLocationFab;
    private com.movielocations.models.Location selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initViews();
        setupMap();
        checkLocationPermission();
    }

    private void initViews() {
        locationInfoCard = findViewById(R.id.locationInfoCard);
        locationNameTextView = findViewById(R.id.locationNameTextView);
        locationAddressTextView = findViewById(R.id.locationAddressTextView);
        locationDescriptionTextView = findViewById(R.id.locationDescriptionTextView);
        inviteFriendButton = findViewById(R.id.inviteFriendButton);
        myLocationFab = findViewById(R.id.myLocationFab);

        myLocationFab.setOnClickListener(v -> showMyLocation());

        inviteFriendButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("location_id", selectedLocation.getId());
                startActivity(intent);
            }
        });
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void checkLocationPermission() {
        if (!PermissionUtils.hasLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;

        if (PermissionUtils.hasLocationPermission(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }

        loadLocations();
    }

    private void loadLocations() {
        int movieId = getIntent().getIntExtra("movie_id", -1);
        int locationId = getIntent().getIntExtra("location_id", -1);

        executorService.execute(() -> {
            List<com.movielocations.models.Location> locations;
            
            if (movieId != -1) {
                locations = database.locationDao().getLocationsByMovieId(movieId);
            } else {
                locations = database.locationDao().getAllLocations();
            }

            runOnUiThread(() -> {
                if (locations != null && !locations.isEmpty()) {
                    for (com.movielocations.models.Location location : locations) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(location.getLocationName())
                                .snippet(location.getAddress());
                        googleMap.addMarker(markerOptions);

                        if (locationId == location.getId()) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            showLocationInfo(location);
                        }
                    }

                    if (locationId == -1 && !locations.isEmpty()) {
                        com.movielocations.models.Location firstLocation = locations.get(0);
                        LatLng latLng = new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                }
            });
        });

        googleMap.setOnMarkerClickListener(marker -> {
            executorService.execute(() -> {
                List<com.movielocations.models.Location> locations = database.locationDao().getAllLocations();
                for (com.movielocations.models.Location location : locations) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (marker.getPosition().latitude == latLng.latitude 
                            && marker.getPosition().longitude == latLng.longitude) {
                        runOnUiThread(() -> showLocationInfo(location));
                        break;
                    }
                }
            });
            return false;
        });
    }

    private void showLocationInfo(com.movielocations.models.Location location) {
        selectedLocation = location;
        locationNameTextView.setText(location.getLocationName());
        locationAddressTextView.setText(location.getAddress());
        locationDescriptionTextView.setText(location.getDescription());
        locationInfoCard.setVisibility(View.VISIBLE);
    }

    private void showMyLocation() {
        if (!PermissionUtils.hasLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null && googleMap != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleMap != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                            == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                }
            } else {
                Toast.makeText(this, R.string.location_permission_required, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
