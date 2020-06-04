package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.hotelbooking.Adapter.RoomBookingReviewAdapter;
import com.example.hotelbooking.Item.BookingReviewRoom;
import com.example.hotelbooking.R;

import java.util.ArrayList;
import java.util.List;

public class BookingReviewActivity extends AppCompatActivity {
    private ListView roomListView;
    RoomBookingReviewAdapter adapter;
    List<BookingReviewRoom> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_review);
        roomListView = findViewById(R.id.listview_room_bookingreview);

        rooms = new ArrayList<>();
        rooms.add(new BookingReviewRoom("Single Room",200000));
        rooms.add(new BookingReviewRoom("Double Room",300000));
        adapter = new RoomBookingReviewAdapter(getApplicationContext(), rooms);
        roomListView.setAdapter(adapter);


    }
}
