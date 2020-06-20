package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hotelbooking.Item.HomeItem;
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
import com.squareup.picasso.Picasso;

public class DetailItemActivity extends AppCompatActivity implements OnMapReadyCallback  {
//    private static final String[] INITIAL_PERMS={
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.READ_CONTACTS
//    };
////    private static final String[] LOCATION_PERMS={
////            Manifest.permission.ACCESS_FINE_LOCATION
////    };
////    private static final int INITIAL_REQUEST=1337;
////    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;

    private ImageView hotelImmage;
    private TextView hotelName;
    private RatingBar hotelRating;
    private GoogleMap mMap;
    private Button selectRoomBtn;
    HomeItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        hotelImmage = findViewById(R.id.image_item);
        hotelName = findViewById(R.id.name_item);
        hotelRating = findViewById(R.id.rating_item);
        selectRoomBtn = findViewById(R.id.btn_selectroom);

        Intent intent = getIntent();
        item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        Picasso.with(this.getApplicationContext()).load(item.getImageView()).into(hotelImmage);
        hotelName.setText(item.getName());
        hotelRating.setRating(item.getRating());

        selectRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), ChooseRoomActivity.class);
                intent.putExtra("InfoClickedItem", item);
                activity.startActivity(intent);

            }
        });

        createMap();
    }

    private void createMap() {
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map_detail_item);
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
                        CameraPosition cp = new CameraPosition.Builder().target(currentLocation).zoom(13).build();
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
