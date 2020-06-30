package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotelbooking.Adapter.RoomBookingReviewAdapter;
import com.example.hotelbooking.Item.BookingInfo;
import com.example.hotelbooking.Item.BookingReviewRoom;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Utils.MyUntil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BookingReviewActivity extends AppCompatActivity {
    private ListView roomListView;
    private Button bookingBtn, paymentBtn;
    RoomBookingReviewAdapter adapter;
    List<BookingReviewRoom> rooms;
    ArrayList<Room> selectedRooms;
    BookingInfo bookingInfo;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_review);
        roomListView = findViewById(R.id.listview_room_bookingreview);
        bookingBtn = findViewById(R.id.btn_booking);
        paymentBtn = findViewById(R.id.btn_payment);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        bookingInfo = (BookingInfo) intent.getSerializableExtra("BookingInfo");
        selectedRooms = new ArrayList<>();
        selectedRooms.addAll(bookingInfo.getRooms());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        rooms = new ArrayList<>();
        for (Room room : selectedRooms) {
            rooms.add(new BookingReviewRoom(room.getName(), room.getPrice()));


        }
//        FirebaseDatabase databasetest = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = databasetest.getReference("users/uid_of_first_user");
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                User value = dataSnapshot.getValue(User.class);
//                Log.d("", "Value is: " + value);
//                Toast.makeText(BookingReviewActivity.this,"get value success",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("", "Failed to read value.", error.toException());
//                Toast.makeText(BookingReviewActivity.this,"get value fail",Toast.LENGTH_LONG).show();
//            }
//        });

//
//
//
//        rooms.add(new BookingReviewRoom("Single Room",200000));
//        rooms.add(new BookingReviewRoom("Double Room",300000));
        adapter = new RoomBookingReviewAdapter(getApplicationContext(), rooms);
        roomListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (final Room room : selectedRooms) {
                    rooms.add(new BookingReviewRoom(room.getName(), room.getPrice()));
                    adapter.notifyDataSetChanged();
                    final Map<String, Object> docData = new HashMap<>();
                    try {
                        docData.put("checkin", new Timestamp(MyUntil.covertStringtoDate(bookingInfo.getCheckin())));
                        docData.put("checkout", new Timestamp(MyUntil.covertStringtoDate(bookingInfo.getCheckout())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final String clientID = user.getProviderId();
                    docData.put("hotelID", bookingInfo.getHotelID());
                    docData.put("roomID", room.getRoomID());
                    docData.put("clientID", clientID);
                    docData.put("lastName", bookingInfo.getLastName());
                    docData.put("firstName", bookingInfo.getFirstName());
                    docData.put("email", bookingInfo.getEmail());
                    docData.put("phone", bookingInfo.getPhone());
                    docData.put("country", bookingInfo.getCountry());
                    final String bookID = new Random().nextInt() + "";

                    Task<Void> documentReference = database.collection("book").document(bookID).set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("thanhphan", "DocumentSnapshot successfully written!");
                                    Map<String, Object> book = new HashMap<>();
                                    book.put("bookID", bookID);
                                    book.put("clientID", clientID);
                                    Task<Void> documentReference = database.collection("clients").document(clientID)
                                            .collection("books").document(bookID)
                                            .set(book)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e("thanhphan", "book in client successfully written!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("thanhphan", "DocumentSnapshot fail written!");
                                                }
                                            });
                                    Log.e("thanhphan", "onSuccess: " + room.toString());
                                    DocumentReference reference = database.collection("hotels").document(bookingInfo.getHotelID())
                                            .collection("rooms").document(room.getRoomID());
                                    reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                int left = new Integer((String.valueOf(documentSnapshot.get("leftroom"))));
                                                if (left > 0) {
                                                    Task<Void> documentReference1 = database.collection("hotels").document(bookingInfo.getHotelID())
                                                            .collection("rooms").document(room.getRoomID())
                                                            .update("leftroom", new Integer((String.valueOf(documentSnapshot.get("leftroom")))) - 1);
                                                }

                                            }
                                        }
                                    });

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("thanhphan", "DocumentSnapshot fail written!");
                                }
                            });
                }
            }
        });

    }
}
