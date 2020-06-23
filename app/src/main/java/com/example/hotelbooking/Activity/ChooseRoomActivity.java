package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.hotelbooking.Adapter.RoomAdapter;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseRoomActivity extends AppCompatActivity {
    private ListView listView;
    private Button reserveBtn;
    RoomAdapter roomAdapter;
    ArrayList<Room> rooms;
    Room room;
    String itemID; //HotelID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        listView = findViewById(R.id.listview_room);
        reserveBtn = findViewById(R.id.reserve_btn);

        rooms = new ArrayList<>();
        room = new Room();

        Intent intent = getIntent();
        HomeItem item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        itemID = item.getId();


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("hotels").document(itemID).collection("rooms");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        List<String> facilites = (List<String>) doc.get("facilites");
                        room = new Room(String.valueOf(doc.get("name")),
                                new Integer(String.valueOf(doc.get("price"))),
                                String.valueOf(doc.get("description")),
                                facilites);
                        rooms.add(room);

                        roomAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

//        ArrayList<String> facilites = new ArrayList<>();
//        facilites.add("Free Wifi");
//        facilites.add("Free Wifi");
//        facilites.add("Free Wifi");
//        rooms.add(new Room("Single Room", 20000, "Only 1 room left on HotelBooking. \nThis is description", facilites));
//        rooms.add(new Room("Double Room", 40000, "Only 1 room left on HotelBooking. \nThis is description", facilites));
//        rooms.add(new Room("Twice", 20000, "Only 1 room left on HotelBooking. \nThis is description", facilites));
//        rooms.add(new Room("Sea Room", 20000, "Only 1 room left on HotelBooking. \nThis is description", facilites));
        roomAdapter = new RoomAdapter(getApplicationContext(), rooms);
        listView.setAdapter(roomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                roomAdapter.notifyDataSetChanged();
                 room = (Room) listView.getItemAtPosition(i);


            }
        });
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), FillInfoActivity.class);
                intent.putExtra("InfoClickedRoom", room);
                intent.putExtra("HoteID", itemID);
                activity.startActivity(intent);

            }
        });
    }
}
