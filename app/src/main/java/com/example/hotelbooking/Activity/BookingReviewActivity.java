package com.example.hotelbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbooking.Adapter.RoomBookingReviewAdapter;
import com.example.hotelbooking.Item.BookingInfo;
import com.example.hotelbooking.Item.BookingReviewRoom;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BookingReviewActivity extends AppCompatActivity {
    private ListView roomListView;
    private Button bookingBtn, btnBack;
    private TextView checkin_tv, checkout_tv, night_tv,price_tv;
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
        checkin_tv = findViewById(R.id.checkin_tv);
        checkout_tv = findViewById(R.id.checkout_tv);
        price_tv = findViewById(R.id.price_tv);
        night_tv = findViewById(R.id.night_tv);
        btnBack = findViewById(R.id.btnBack);


        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        bookingInfo = (BookingInfo) intent.getSerializableExtra("BookingInfo");
        selectedRooms = new ArrayList<>();
        selectedRooms.addAll(bookingInfo.getRooms());

        checkin_tv.setText(bookingInfo.getCheckin());
        checkout_tv.setText(bookingInfo.getCheckout());



        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        rooms = new ArrayList<>();
        int price = 0;
        for (Room room : selectedRooms) {
            rooms.add(new BookingReviewRoom(room.getName(), room.getPrice()));
            price += room.getPrice();
        }

        long diff = 0;
        try {
            diff = MyUntil.covertStringtoDate(bookingInfo.getCheckout()).getTime() - MyUntil.covertStringtoDate(bookingInfo.getCheckin()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        night_tv.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" nights");
        price_tv.setText("VND "+price*TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));




        adapter = new RoomBookingReviewAdapter(getApplicationContext(), rooms);
        roomListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                rooms.remove(i);
                selectedRooms.remove(i);
                int finalPrice = 0;
                for (Room room : selectedRooms) {
                     finalPrice += room.getPrice();
                }
                long diff = 0;
                try {
                    diff = MyUntil.covertStringtoDate(bookingInfo.getCheckout()).getTime() - MyUntil.covertStringtoDate(bookingInfo.getCheckin()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                price_tv.setText("VND "+finalPrice*TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                adapter.notifyDataSetChanged();
            }
        });

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedRooms.size()<1){
                    Toast.makeText(BookingReviewActivity.this, "choose room!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(checkin_tv.getText().toString().equals("null")||checkout_tv.getText().toString().equals("null")){
                    Toast.makeText(BookingReviewActivity.this, "choose checkin, checkout!", Toast.LENGTH_LONG).show();
                    return;
                }
                for (final Room room : selectedRooms) {
//                    rooms.add(new BookingReviewRoom(room.getName(), room.getPrice()));
//                    adapter.notifyDataSetChanged();
                    final Map<String, Object> docData = new HashMap<>();
                    try {
                        docData.put("checkin", new Timestamp(MyUntil.covertStringtoDate(bookingInfo.getCheckin())));
                        docData.put("checkout", new Timestamp(MyUntil.covertStringtoDate(bookingInfo.getCheckout())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final String clientID = user.getDisplayName();
                    docData.put("hotelID", bookingInfo.getHotelID());
                    docData.put("roomID", room.getRoomID());
                    docData.put("clientID", clientID);
                    docData.put("lastName", bookingInfo.getLastName());
                    docData.put("firstName", bookingInfo.getFirstName());
                    docData.put("email", bookingInfo.getEmail());
                    docData.put("phone", bookingInfo.getPhone());
                    docData.put("country", bookingInfo.getCountry());



                    //add booking room to database
                    final String bookID = Math.abs(new Random().nextInt()) + "";

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
                                                    Toast.makeText(getApplicationContext(),"Booked Success",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(BookingReviewActivity.this, HomeActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(),"Booked Fail",Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getApplicationContext(),"Booked Fail",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

    }

}
