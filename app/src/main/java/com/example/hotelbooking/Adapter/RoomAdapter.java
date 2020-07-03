package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends ArrayAdapter<Room> {
    private Context context;
    private List<Room> rooms;
    private List<Room> selectedRooms;

    public RoomAdapter(Context context, List<Room> rooms) {
        super(context, 0, rooms);
        this.context = context;
        this.rooms = rooms;
        selectedRooms = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        final Room room = rooms.get(position);

        TextView name = view.findViewById(R.id.tv_room);
        TextView facilities_1 = view.findViewById(R.id.facilites_1);
        TextView facilities_2 = view.findViewById(R.id.facilites_2);
        TextView facilities_3 = view.findViewById(R.id.facilites_3);
        TextView price = view.findViewById(R.id.price_tv_room);
        TextView des = view.findViewById(R.id.des_room);
        Button selectBtn = view.findViewById(R.id.select_btn);

        name.setText(room.getName());
//        facilities_1.setText(room.getFacilities().get(0));
//        facilities_2.setText(room.getFacilities().get(1));
//        facilities_3.setText(room.getFacilities().get(2));
        price.setText("VND " + room.getPrice());
        des.setText(room.getDescription());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               selectedRooms.add(room);
               Toast.makeText(getContext(),"Selected "+selectedRooms.size()+" rooms",Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    public List<Room> getSelectedRooms() {
        return selectedRooms;
    }
    public void clearSelectedRooms(){
        selectedRooms.clear();
        selectedRooms = new ArrayList<>();
    }
}
