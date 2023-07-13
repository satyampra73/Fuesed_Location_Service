package com.satyam.locationservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xml.sax.Locator;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request the location permissions
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            },
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    startLocationService();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the location permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    },
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ActionStartLocationService);
            startService(intent);
            Toast.makeText(MainActivity.this, "LocationService Started", Toast.LENGTH_SHORT).show();

        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ActionStopLocationService);
            startService(intent);
            Toast.makeText(MainActivity.this, "Location Service Stopped", Toast.LENGTH_SHORT).show();

        }
    }

}
