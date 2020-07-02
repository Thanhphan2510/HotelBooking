package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.hotelbooking.Adapter.BookingHistoryAdapter;
import com.example.hotelbooking.Adapter.ItemHomeAdapter;
import com.example.hotelbooking.Item.BookHistory;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingHistory extends AppCompatActivity {
    ListView book_listview;
    BookingHistoryAdapter adapter;
    FirebaseAuth mAuth;

    FirebaseUser user;
    List<BookHistory> bookingHistoryList;
    BookHistory bookingHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        book_listview = findViewById(R.id.list_booking);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        bookingHistoryList = new ArrayList<>();
        adapter = new BookingHistoryAdapter(book_listview.getContext(), bookingHistoryList);
        adapter.notifyDataSetChanged();
        book_listview.setAdapter(adapter);
        String clientID = user.getDisplayName();
        Log.e("thanhphan", "onComplete: " + clientID);
        CollectionReference reference = database.collection("clients").document(clientID).collection("books");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot doc : task.getResult()) {
                        String bookID = String.valueOf(doc.get("bookID"));
                        Log.e("thanhphan", "onComplete: " + bookID);
                        DocumentReference reference1 = database.collection("book")
                                .document(bookID);
                        reference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot doc1 = task.getResult();
                                Timestamp checkinTS = (Timestamp) doc1.get("checkin");
                                Date checkinDate_Data = checkinTS.toDate();

                                Timestamp checkoutTS = (Timestamp) doc1.get("checkout");
                                Date checkoutDate_Data = checkoutTS.toDate();

                                bookingHistory = new BookHistory(checkinDate_Data.toString(),
                                       checkoutDate_Data.toString(), String.valueOf(doc1.get("clientID")),
                                        String.valueOf(doc1.get("country")), String.valueOf(doc1.get("E-mail")),
                                        String.valueOf(doc1.get("firstName")), String.valueOf(doc1.get("lastName")),
                                        String.valueOf(doc1.get("phone")), String.valueOf(doc1.get("hotelID")),
                                        String.valueOf(doc1.get("roomID")));
                                Log.e("thanhphan", "onComplete: "+bookingHistory.toString());
                                bookingHistoryList.add(bookingHistory);
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }

            }
        });


    }
}
