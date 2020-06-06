package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        listView = findViewById(R.id.listview_room);
        reserveBtn = findViewById(R.id.reserve_btn);

        rooms = new ArrayList<>();

        Intent intent = getIntent();
        HomeItem item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        String itemID = item.getId();


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("hotels").document(itemID).collection("rooms");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
//
//                        Room item = new HomeItem(String.valueOf(doc.get("id")),String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
//                                new Long(String.valueOf(doc.get("star"))).floatValue(), new Integer(String.valueOf(doc.get("price"))),
//                                String.valueOf(doc.get("description1")),
//                                String.valueOf(doc.get("description2")), String.valueOf(doc.get("description3")).replaceAll("\\n","\r\n"));
                        List<String> facilites = (List<String>) doc.get("facilites");
                        Room item = new Room(String.valueOf(doc.get("name")),
                                new Integer(String.valueOf(doc.get("price"))),
                                String.valueOf(doc.get("description")),
                                facilites);
                        rooms.add(item);

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
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), FillInfoActivity.class);
//                intent.putExtra("InfoClickedItem", homeItem);
                activity.startActivity(intent);

            }
        });
    }
}
