package com.movielocations.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.movielocations.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    long insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movies")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM movies WHERE language = :language")
    List<Movie> getMoviesByLanguage(String language);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :searchQuery || '%'")
    List<Movie> searchMovies(String searchQuery);

    @Query("SELECT * FROM movies WHERE type = :type")
    List<Movie> getMoviesByType(String type);
}
