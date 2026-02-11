package com.movielocations.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.movielocations.models.Location;
import com.movielocations.models.Movie;

public class SocialMediaUtils {
    
    public static void shareOnFacebook(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setPackage("com.facebook.katana");
        
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // Facebook app not installed, open in browser
            shareGeneric(context, text);
        }
    }

    public static void shareOnTwitter(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setPackage("com.twitter.android");
        
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // Twitter app not installed, open in browser
            shareGeneric(context, text);
        }
    }

    public static void shareOnInstagram(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setPackage("com.instagram.android");
        
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // Instagram app not installed
            shareGeneric(context, text);
        }
    }

    public static void shareGeneric(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static void shareLocation(Context context, Location location, Movie movie) {
        String text = String.format("Check out this filming location for %s!\n%s\nLocation: %s",
                movie.getTitle(),
                location.getLocationName(),
                location.getAddress());
        shareGeneric(context, text);
    }

    public static void inviteFriendToLocation(Context context, Location location, String friendEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + friendEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to visit filming location");
        intent.putExtra(Intent.EXTRA_TEXT, 
                String.format("I'd like to invite you to visit %s at %s!",
                        location.getLocationName(),
                        location.getAddress()));
        
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            android.util.Log.e("SocialMediaUtils", "Failed to send invitation email", e);
            android.widget.Toast.makeText(context, "Unable to send invitation. Please check email settings.", 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
