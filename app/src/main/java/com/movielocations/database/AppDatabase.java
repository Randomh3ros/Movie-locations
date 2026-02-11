package com.movielocations.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.movielocations.models.Friend;
import com.movielocations.models.Invitation;
import com.movielocations.models.Location;
import com.movielocations.models.Movie;

@Database(entities = {Movie.class, Location.class, Friend.class, Invitation.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract MovieDao movieDao();
    public abstract LocationDao locationDao();
    public abstract FriendDao friendDao();
    public abstract InvitationDao invitationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "movie_locations_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
