package com.movielocations.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.movielocations.models.Friend;

import java.util.List;

@Dao
public interface FriendDao {
    @Insert
    long insert(Friend friend);

    @Update
    void update(Friend friend);

    @Delete
    void delete(Friend friend);

    @Query("SELECT * FROM friends")
    List<Friend> getAllFriends();

    @Query("SELECT * FROM friends WHERE id = :friendId")
    Friend getFriendById(int friendId);

    @Query("SELECT * FROM friends WHERE email = :email")
    Friend getFriendByEmail(String email);
}
