package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hotelbooking.Item.BookingInfo;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Utils.MyUntil;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.util.ArrayList;

public class FillInfoActivity extends AppCompatActivity {
    private Button nextStepBtn;
    private EditText fisrtName, lastName, email, country, phonenumber;
    BookingInfo bookingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        fisrtName = findViewById(R.id.edt_first_name);
        lastName = findViewById(R.id.edt_last_name);
        email = findViewById(R.id.edt_email_address);
        country = findViewById(R.id.edt_country);
        phonenumber = findViewById(R.id.edt_mobile_phone);


        nextStepBtn = findViewById(R.id.btn_next_step);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent1 = new Intent(activity.getApplicationContext(), BookingReviewActivity.class);
                Intent intent = getIntent();
                ArrayList<Room> rooms = (ArrayList<Room>) intent.getSerializableExtra("InfoClickedRooms");
                String hotelID = (String) intent.getStringExtra("HoteID");
                String checkin_str = intent.getStringExtra("checkinInfor");
                String checkout_str = intent.getStringExtra("checkoutInfor");
                bookingInfo = new BookingInfo(fisrtName.getText().toString(),

                        lastName.getText().toString(),
                        email.getText().toString(),
                        country.getText().toString(),
                        phonenumber.getText().toString(),
                        hotelID, rooms, checkin_str,
                        checkout_str);

                intent1.putExtra("BookingInfo", bookingInfo);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent1);

            }
        });
            }
//  @Override
//  public void onBackPressed() {
//      startActivity(new Intent(this, ChooseRoomActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
//  }
}
