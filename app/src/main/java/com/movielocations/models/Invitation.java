package com.movielocations.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invitations")
public class Invitation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int friendId;
    private int locationId;
    private String message;
    private long timestamp;
    private String status; // "pending", "accepted", "declined"

    public Invitation(int friendId, int locationId, String message, long timestamp, String status) {
        this.friendId = friendId;
        this.locationId = locationId;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFriendId() { return friendId; }
    public void setFriendId(int friendId) { this.friendId = friendId; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
