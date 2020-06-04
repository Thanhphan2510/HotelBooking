package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hotelbooking.Item.BookingReviewRoom;
import com.example.hotelbooking.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoomBookingReviewAdapter extends ArrayAdapter<BookingReviewRoom> {
    private Context context;
    private List<BookingReviewRoom> brooms;

    public RoomBookingReviewAdapter(Context context, List<BookingReviewRoom> brooms) {
        super(context, 0, brooms);
        this.context = context;
        this.brooms = brooms;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_bookingreview, parent, false);
        BookingReviewRoom room = brooms.get(position);

        TextView name = view.findViewById(R.id.tv_name_room_booking);
        TextView price = view.findViewById(R.id.tv_price_room_booking);

        name.setText(room.getName());
        price.setText("VND "+room.getPrice());

        return view;
    }
}
