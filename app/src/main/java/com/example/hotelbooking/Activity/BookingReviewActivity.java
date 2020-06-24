package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.hotelbooking.Adapter.RoomBookingReviewAdapter;
import com.example.hotelbooking.Item.BookingInfo;
import com.example.hotelbooking.Item.BookingReviewRoom;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BookingReviewActivity extends AppCompatActivity {
    private ListView roomListView;
    private Button bookingBtn, paymentBtn;
    RoomBookingReviewAdapter adapter;
    List<BookingReviewRoom> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_review);
        roomListView = findViewById(R.id.listview_room_bookingreview);
        bookingBtn = findViewById(R.id.btn_booking);
        paymentBtn = findViewById(R.id.btn_payment);

        Intent intent = getIntent();
        BookingInfo bookingInfo = (BookingInfo) intent.getSerializableExtra("BookingInfo");
        ArrayList<Room> selectedRooms = new ArrayList<>();
        selectedRooms.addAll(bookingInfo.getRooms());

        rooms = new ArrayList<>();
        for(Room room:selectedRooms){
            rooms.add(new BookingReviewRoom(room.getName(), room.getPrice()));
        }

//
//
//
//        rooms.add(new BookingReviewRoom("Single Room",200000));
//        rooms.add(new BookingReviewRoom("Double Room",300000));
        adapter = new RoomBookingReviewAdapter(getApplicationContext(), rooms);
        roomListView.setAdapter(adapter);


    }
}
