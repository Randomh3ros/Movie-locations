package com.movielocations.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "friends")
public class Friend {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private String socialMediaId;
    private String socialMediaType; // "facebook", "twitter", "instagram"

    public Friend(String name, String email, String phoneNumber, String profilePictureUrl, String socialMediaId, String socialMediaType) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
        this.socialMediaId = socialMediaId;
        this.socialMediaType = socialMediaType;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getSocialMediaId() { return socialMediaId; }
    public void setSocialMediaId(String socialMediaId) { this.socialMediaId = socialMediaId; }

    public String getSocialMediaType() { return socialMediaType; }
    public void setSocialMediaType(String socialMediaType) { this.socialMediaType = socialMediaType; }
}
