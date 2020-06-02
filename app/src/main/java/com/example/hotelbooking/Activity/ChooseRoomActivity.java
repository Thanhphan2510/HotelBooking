package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.hotelbooking.Adapter.RoomAdapter;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;

import java.util.ArrayList;

public class ChooseRoomActivity extends AppCompatActivity {
    private ListView listView;
    private Button reserveBtn;
    RoomAdapter roomAdapter;
    ArrayList<Room> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        listView = findViewById(R.id.listview_room);
        reserveBtn = findViewById(R.id.reserve_btn);

        rooms = new ArrayList<>();
        ArrayList<String> facilites = new ArrayList<>();
        facilites.add("Free Wifi");
        facilites.add("Free Wifi");
        facilites.add("Free Wifi");
        rooms.add(new Room("Single Room", 20000,"Only 1 room left on HotelBooking. \nThis is description", facilites ));
        rooms.add(new Room("Double Room", 40000,"Only 1 room left on HotelBooking. \nThis is description", facilites ));
        rooms.add(new Room("Twice", 20000,"Only 1 room left on HotelBooking. \nThis is description", facilites ));
        rooms.add(new Room("Sea Room", 20000,"Only 1 room left on HotelBooking. \nThis is description", facilites ));
        roomAdapter = new RoomAdapter(getApplicationContext(), rooms);
        listView.setAdapter(roomAdapter);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), FillInfoActivity.class);
//                intent.putExtra("InfoClickedItem", homeItem);
                activity.startActivity(intent);

            }
        });
    }
}
