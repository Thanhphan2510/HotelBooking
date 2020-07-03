package com.example.hotelbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeInformationActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    ImageView imageView;

    Button changeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        imageView = findViewById(R.id.image_change);
        changeImage = findViewById(R.id.btnChange_image);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.CATEGORY_APP_GALLERY);
//                startActivityForResult(Intent.createChooser(getApplicationContext(),GALLERY_REQUEST_CODE));
            }
        });
    }
}

