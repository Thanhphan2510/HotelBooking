package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoomAdapter extends ArrayAdapter<Room> {
    private Context context;
    private List<Room> rooms;

    public RoomAdapter(Context context, List<Room> rooms) {
        super(context, 0, rooms);
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        Room room = rooms.get(position);

        TextView name = view.findViewById(R.id.tv_room);
        TextView facilites_1 = view.findViewById(R.id.facilites_1);
        TextView facilites_2 = view.findViewById(R.id.facilites_2);
        TextView facilites_3 = view.findViewById(R.id.facilites_3);
        TextView price = view.findViewById(R.id.price_tv_room);
        TextView des = view.findViewById(R.id.des_room);
        Button selectBtn = view.findViewById(R.id.select_btn);

        name.setText(room.getName());
        facilites_1.setText(room.getFacilites().get(0));
        facilites_2.setText(room.getFacilites().get(1));
        facilites_3.setText(room.getFacilites().get(2));
        price.setText("VND " + room.getPrice());
        des.setText(room.getDescription());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Selected", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}
