package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.hotelbooking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
//https://mobiledevhub.com/2018/11/24/android-how-to-display-nearby-places/

//https://medium.com/@laurence.liuuu/android-google-maps-simple-usage-google-places-api-bb691133490b
//https://www.codeproject.com/articles/1121102/google-maps-search-nearby-displaying-nearby-places
//https://www.youtube.com/watch?v=wKrYU97Wwg4
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ProgressBar progressBar;
    private String nextPageToken = "";
    private int requestCount = 0;
    private final String API_KEY = "AIzaSyChKqzcXhIDF-8hGV5FfJQ4FK7U_YTRfag";
    private final int REQUEST_LIMIT = 3;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        progressBar = findViewById(R.id.progressBar);
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        smf.getMapAsync(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
//        createMap();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //TODO: Get current location
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_GPS_PERMISSION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcm = new LatLng(10.762622, 106.660172);
//        this.googleMap = googleMap;
//        googleMap.setMyLocationEnabled(true);
//        Location location = googleMap.getMyLocation();
//        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(hcm).title("Marker in HCM City"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hcm));

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }
                        LatLng currentLocation =
                                new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in current location"));


                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        CameraPosition cp = new CameraPosition.Builder().target(currentLocation).zoom(17).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_GPS_PERMISSION:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //TODO: Action when permission denied
                    }
                }
                break;
        }
    }
}
