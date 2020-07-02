package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.Activity.BookingHistory;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookingHistoryAdapter extends ArrayAdapter<BookingHistory> {
    private Context context;
    private List<BookingHistory> bookingHistories;

    public BookingHistoryAdapter(Context context, List<BookingHistory> bookingHistories) {
        super(context, 0, bookingHistories);
        this.context = context;
        this.bookingHistories = bookingHistories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_history, parent, false);

        ImageView imageView = view.findViewById(R.id.image_booking_history);

        TextView hotelName = view.findViewById(R.id.hotel_booking_history);
        TextView roomName = view.findViewById(R.id.room_booking_history);
        TextView checkin = view.findViewById(R.id.checkin_history);
        TextView checkout = view.findViewById(R.id.checkout_history);


        return view;
    }
}
