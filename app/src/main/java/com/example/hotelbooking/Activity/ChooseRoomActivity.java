package com.example.hotelbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button reserveBtn, btnBack;
    ArrayList<Room> rooms;
    RoomAdapter roomAdapter;

    ArrayList<Room> selectedRooms;
    Room room;
    String itemID; //HotelID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        listView = findViewById(R.id.listview_room);
        reserveBtn = findViewById(R.id.reserve_btn);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rooms = new ArrayList<>();
        selectedRooms = new ArrayList<>();
//        ArrayList<Room> rooms_back = new ArrayList<>();
//        Intent i = getIntent();
//
//        rooms_back = (ArrayList<Room>) i.getSerializableExtra("InfoClickedRooms");

        room = new Room();

        Intent intent = getIntent();
        HomeItem item = (HomeItem) intent.getSerializableExtra("InfoClickedItem");
        itemID = item.getId();

        final String checkin_str = intent.getStringExtra("checkinInfor");
        final String checkout_str = intent.getStringExtra("checkoutInfor");


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("hotels").document(itemID).collection("rooms");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        List<String> facilites = (List<String>) doc.get("facilites");
                        room = new Room(String.valueOf(doc.get("roomID")), String.valueOf(doc.get("name")),
                                new Integer(String.valueOf(doc.get("price"))),
                                String.valueOf(doc.get("description")),
                                facilites);
                        rooms.add(room);
                        roomAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        roomAdapter = new RoomAdapter(getApplicationContext(), rooms);
        roomAdapter.notifyDataSetChanged();
        listView.setAdapter(roomAdapter);

        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), FillInfoActivity.class);
                selectedRooms.addAll(roomAdapter.getSelectedRooms());
                roomAdapter.clearSelectedRooms();
                roomAdapter.notifyDataSetChanged();
                intent.putExtra("InfoClickedRooms", selectedRooms);
                Log.e("selectedRooms", "onClick: " + selectedRooms.toString());
                intent.putExtra("HoteID", itemID);
                intent.putExtra("checkinInfor", checkin_str);
                intent.putExtra("checkoutInfor", checkout_str);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                selectedRooms.clear();
            }
        });

    }

}
