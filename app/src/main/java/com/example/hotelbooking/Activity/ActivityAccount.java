package com.example.hotelbooking.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityAccount extends AppCompatActivity {
    ImageView avatar;
    TextView txtName, txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_new);


        txtName = findViewById(R.id.txtUsername);
        avatar = findViewById(R.id.imageView);
        txtEmail = findViewById(R.id.txtEmail);
        boolean logout = AccessToken.getCurrentAccessToken() == null;
        if(!logout){
            Picasso.with(this).load(Profile.getCurrentProfile().
                    getProfilePictureUri(200,200)).into(avatar);
            Log.d("TAG", "Username:" + Profile.getCurrentProfile().getName());
            getUserProfile(AccessToken.getCurrentAccessToken());
        }
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
                            txtName.setText("Name: " + last_name+" "+ first_name);
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