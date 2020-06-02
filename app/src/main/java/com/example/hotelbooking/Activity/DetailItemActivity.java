package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailItemActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView hotelImmage;
    private TextView hotelName;
    private RatingBar hotelRating;
    private GoogleMap googleMap;
    private Button selectRoomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        hotelImmage = findViewById(R.id.image_item);
        hotelName = findViewById(R.id.name_item);
        hotelRating = findViewById(R.id.rating_item);
        selectRoomBtn = findViewById(R.id.btn_selectroom);

        Intent intent = getIntent();
        HomeItem item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        hotelImmage.setImageResource(item.getImageView());
        hotelName.setText(item.getName());
        hotelRating.setRating(item.getRating());

        selectRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HomeItem homeItem = (HomeItem) view.getItemAtPosition(i);

                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), ChooseRoomActivity.class);
//                intent.putExtra("InfoClickedItem", homeItem);
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
        createMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng hcm = new LatLng(10.762622, 106.660172);
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(hcm).title("Marker in HCM City"));
        CameraPosition cp = new CameraPosition.Builder().target(hcm).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
