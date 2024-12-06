package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class location extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private Location targetLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set target location (latitude, longitude)
        targetLocation = new Location("target");
        targetLocation.setLatitude(37.7749); // Replace with actual latitude
        targetLocation.setLongitude(-122.4194); // Replace with actual longitude
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, createNotification());

        startLocationTracking();

        return START_STICKY;
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "location_channel")
                .setContentTitle("Tracking Location")
                .setContentText("Your location is being tracked.")
                .setSmallIcon(R.drawable.pin)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    private void startLocationTracking() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.requestLocationUpdates(
                    LocationRequest.create()
                            .setInterval(5000) // 5 seconds
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY),
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult != null) {
                                for (Location location : locationResult.getLocations()) {
                                    verifyLocation(location);
                                }
                            }
                        }
                    },
                    Looper.myLooper()
            );
        }
    }

    private void verifyLocation(Location currentLocation) {
        float distance = currentLocation.distanceTo(targetLocation); // Distance in meters

        if (distance > 24.38) {
            // User is outside the radius
            sendNotification("Outside Radius", "You are outside the allowed area.");
        } else {
            // User is inside the radius
            sendNotification("Inside Radius", "You are within the allowed area.");
        }
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "location_channel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.pin)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
