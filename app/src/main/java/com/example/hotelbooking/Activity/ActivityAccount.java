package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivityAccount extends AppCompatActivity {
    private ImageView avatar;
    private TextView txtName, txtEmail;
    private Button btnBack, btnChangeInfor, btnBookingHistory, btnFavouriteList;
    private Button btnLogout;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
//    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_new);

        txtName = findViewById(R.id.txtUsername);
        avatar = findViewById(R.id.imageView);
        txtEmail = findViewById(R.id.txtEmail);
        btnBack = findViewById(R.id.btnBack);
        btnChangeInfor = findViewById(R.id.btnChangeInfor);
        btnBookingHistory = findViewById(R.id.btnBookingHistory);
        btnFavouriteList = findViewById(R.id.btnFaList);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        acct = GoogleSignIn.getLastSignedInAccount(this);

        final boolean logout = AccessToken.getCurrentAccessToken() == null;
        if(!logout){
            Picasso.with(this).load(Profile.getCurrentProfile().
                    getProfilePictureUri(200,200)).into(avatar);
            Log.d("TAG", "Username:" + Profile.getCurrentProfile().getName());
            getUserProfile(AccessToken.getCurrentAccessToken());
        }
//        else if(acct!= null){
//            //get information of account google
//            String personName = acct.getDisplayName();
//            String personEmail = acct.getEmail();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            txtName.setText(personName);
//            txtEmail.setText(personEmail);
//            Glide.with(this).load(String.valueOf(personPhoto)).into(avatar);
//
//        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAccount.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                updateUI();
            }
        });
    }
    private void updateUI() {
        Toast.makeText(ActivityAccount.this, "You are logout", Toast.LENGTH_LONG).show();
        Intent refresh = new Intent(this, ActivityAccount.class);
        startActivity(refresh);
//        finish();
    }
    private void getUserProfile(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
            currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("TAG", object.toString());
                    try {
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");
                        String img_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                        txtName.setText("Name: " + last_name + " " + first_name);
                        txtEmail.setText(email);
                        Picasso.with(ActivityAccount.this).load(img_url).into(avatar);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );

        Bundle p = new Bundle();
        p.putString("fields", "first_name, last_name,email,id");
        request.setParameters(p);
        request.executeAsync();

    }
}