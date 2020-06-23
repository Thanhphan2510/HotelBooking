package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

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
    private Button selectRoomBtn, checkinBtn, checkoutBtn;
    HomeItem item;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        hotelImmage = findViewById(R.id.image_item);
        hotelName = findViewById(R.id.name_item);
        hotelRating = findViewById(R.id.rating_item);
        selectRoomBtn = findViewById(R.id.btn_selectroom);
        checkinBtn = findViewById(R.id.btn_checkin_detailitem);
        checkoutBtn = findViewById(R.id.btn_checkout_detailitem);

        Intent intent = getIntent();
        item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        Log.e("thanhphan", "item:"+item.toString() );
        Picasso.with(this.getApplicationContext()).load(item.getImageView()).into(hotelImmage);
        hotelName.setText(item.getName());
        hotelRating.setRating(item.getRating());
        String checkin_str = intent.getStringExtra("checkinInfor");
        String checkout_str = intent.getStringExtra("checkoutInfor");

        checkinBtn.setText(checkin_str);
        checkinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(DetailItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkinBtn.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                dd.show();
            }
        });

        checkoutBtn.setText(checkout_str);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(DetailItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkoutBtn.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                dd.show();
            }
        });


        selectRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), ChooseRoomActivity.class);
                intent.putExtra("InfoClickedItem", item);
                intent.putExtra("checkinInfor", checkinBtn.getText().toString());
                intent.putExtra("checkoutInfor", checkoutBtn.getText().toString());
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //TODO: Get hotel location
            getHotelLocation();
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

    private void getHotelLocation() {
        String id = item.getId();
        Log.e("ID Item", "getHotelLocation: "+id );
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        DocumentReference reference= database.collection("hotels").document(id);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    String str = String.valueOf(doc.get("latlng"));
                    String name_str = String.valueOf(doc.get("name"));
                    String[] arr = str.split(",");
                    LatLng hotel_latlng = new LatLng(new Double(arr[0]), new Double(arr[1]));

                    mMap.addMarker(new MarkerOptions().position(hotel_latlng).title(name_str)).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel_latlng));
                    CameraPosition cp = new CameraPosition.Builder().target(hotel_latlng).zoom(14).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

                }
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
