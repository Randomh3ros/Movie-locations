package com.movielocations.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.movielocations.models.Invitation;

import java.util.List;

@Dao
public interface InvitationDao {
    @Insert
    long insert(Invitation invitation);

    @Update
    void update(Invitation invitation);

    @Delete
    void delete(Invitation invitation);

    @Query("SELECT * FROM invitations")
    List<Invitation> getAllInvitations();

    @Query("SELECT * FROM invitations WHERE id = :invitationId")
    Invitation getInvitationById(int invitationId);

    @Query("SELECT * FROM invitations WHERE friendId = :friendId")
    List<Invitation> getInvitationsByFriendId(int friendId);
}
