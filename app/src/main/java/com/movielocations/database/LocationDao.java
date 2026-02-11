package com.movielocations.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.movielocations.models.Location;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    long insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

    @Query("SELECT * FROM locations")
    List<Location> getAllLocations();

    @Query("SELECT * FROM locations WHERE id = :locationId")
    Location getLocationById(int locationId);

    @Query("SELECT * FROM locations WHERE movieId = :movieId")
    List<Location> getLocationsByMovieId(int movieId);
}
