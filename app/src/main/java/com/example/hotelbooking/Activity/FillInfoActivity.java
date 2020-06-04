package com.example.hotelbooking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hotelbooking.R;

public class FillInfoActivity extends AppCompatActivity {
    private Button nextStepBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        nextStepBtn = findViewById(R.id.btn_next_step);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), BookingReviewActivity.class);
//                intent.putExtra("InfoClickedItem", homeItem);
                activity.startActivity(intent);

            }
        });
    }
}
