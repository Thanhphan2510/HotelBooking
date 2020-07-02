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
import com.example.hotelbooking.Item.BookHistory;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Utils.MyUntil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookingHistoryAdapter extends ArrayAdapter<BookHistory> {
    private Context context;
    private List<BookHistory> bookingHistories;

    public BookingHistoryAdapter(Context context, List<BookHistory> bookingHistories) {
        super(context, 0, bookingHistories);
        this.context = context;
        this.bookingHistories = bookingHistories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_history, parent, false);

        BookHistory bookHistory = bookingHistories.get(position);
        final ImageView imageView = view.findViewById(R.id.image_booking_history);

        final TextView hotelName = view.findViewById(R.id.hotel_booking_history);
        TextView roomName = view.findViewById(R.id.room_booking_history);
        TextView checkin = view.findViewById(R.id.checkin_history);
        TextView checkout = view.findViewById(R.id.checkout_history);

        String hotelID = bookHistory.getHotelID();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("hotels").document(hotelID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    hotelName.setText(String.valueOf(doc.get("name")));
                    Picasso.with(context).load(String.valueOf(doc.get("picture"))).fit().centerCrop().into(imageView);
                }

            }
        });
        roomName.setText(MyUntil.UpString(bookHistory.getRoomID()+" room"));

        try {
            checkin.setText("Check in: "+MyUntil.covertDateToString(bookHistory.getCheckin()));
            checkout.setText("Check out: "+MyUntil.covertDateToString(bookHistory.getCheckout()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
