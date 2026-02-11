package com.movielocations.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.movielocations.R;
import com.movielocations.adapters.FriendAdapter;
import com.movielocations.database.AppDatabase;
import com.movielocations.models.Friend;
import com.movielocations.models.Invitation;
import com.movielocations.models.Location;
import com.movielocations.utils.SocialMediaUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FriendsActivity extends AppCompatActivity implements FriendAdapter.OnFriendClickListener {
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private FloatingActionButton addFriendFab;
    private AppDatabase database;
    private ExecutorService executorService;
    private int locationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        locationId = getIntent().getIntExtra("location_id", -1);

        initViews();
        setupRecyclerView();
        loadFriends();
    }

    private void initViews() {
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        addFriendFab = findViewById(R.id.addFriendFab);

        addFriendFab.setOnClickListener(v -> showAddFriendDialog());
    }

    private void setupRecyclerView() {
        friendAdapter = new FriendAdapter(this);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(friendAdapter);
    }

    private void loadFriends() {
        executorService.execute(() -> {
            List<Friend> friends = database.friendDao().getAllFriends();
            runOnUiThread(() -> {
                if (friends != null) {
                    friendAdapter.setFriends(friends);
                }
            });
        });
    }

    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null);
        
        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
        EditText phoneEditText = dialogView.findViewById(R.id.phoneEditText);

        builder.setView(dialogView)
                .setTitle(R.string.add_friend)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String phone = phoneEditText.getText().toString().trim();

                    if (!name.isEmpty() && !email.isEmpty()) {
                        addFriend(name, email, phone);
                    } else {
                        Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addFriend(String name, String email, String phone) {
        executorService.execute(() -> {
            Friend friend = new Friend(name, email, phone, "", "", "");
            long friendId = database.friendDao().insert(friend);

            runOnUiThread(() -> {
                Toast.makeText(this, R.string.friend_added, Toast.LENGTH_SHORT).show();
                loadFriends();
            });
        });
    }

    @Override
    public void onFriendClick(Friend friend) {
        if (locationId != -1) {
            // Invite friend to location
            showInviteDialog(friend);
        } else {
            // Show friend options (social media connect, etc.)
            showFriendOptionsDialog(friend);
        }
    }

    private void showInviteDialog(Friend friend) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite " + friend.getName())
                .setMessage("Send invitation to visit this location?")
                .setPositiveButton("Send", (dialog, which) -> {
                    sendInvitation(friend);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendInvitation(Friend friend) {
        executorService.execute(() -> {
            Location location = database.locationDao().getLocationById(locationId);
            
            if (location != null) {
                // Create invitation record
                Invitation invitation = new Invitation(
                        friend.getId(),
                        locationId,
                        "Visit this filming location with me!",
                        System.currentTimeMillis(),
                        "pending"
                );
                database.invitationDao().insert(invitation);

                runOnUiThread(() -> {
                    // Send actual invitation via email
                    SocialMediaUtils.inviteFriendToLocation(this, location, friend.getEmail());
                    Toast.makeText(this, R.string.invitation_sent, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showFriendOptionsDialog(Friend friend) {
        String[] options = {"Share on Facebook", "Share on Twitter", "Share on Instagram", "Remove Friend"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(friend.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            SocialMediaUtils.shareOnFacebook(this, "Connect with " + friend.getName());
                            break;
                        case 1:
                            SocialMediaUtils.shareOnTwitter(this, "Connect with " + friend.getName());
                            break;
                        case 2:
                            SocialMediaUtils.shareOnInstagram(this, "Connect with " + friend.getName());
                            break;
                        case 3:
                            removeFriend(friend);
                            break;
                    }
                })
                .show();
    }

    private void removeFriend(Friend friend) {
        executorService.execute(() -> {
            database.friendDao().delete(friend);
            runOnUiThread(() -> {
                Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show();
                loadFriends();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
