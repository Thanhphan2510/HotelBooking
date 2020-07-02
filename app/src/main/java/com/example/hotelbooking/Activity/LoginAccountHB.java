package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAccountHB extends AppCompatActivity {
    private EditText username, pass;
    private Button signInBtn ;
    private TextView forgotPassTv,signUpBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account_hb);
        username = findViewById(R.id.username_login_account_HB);
        pass = findViewById(R.id.password_login_account_HB);
        signInBtn = findViewById(R.id.signin_button_login_account_HB);
        signUpBtn = findViewById(R.id.signup_button_login_account_HB);
        forgotPassTv = findViewById(R.id.forgotpass_textview_login_account_HB);

        firebaseAuth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check value null ???
                String usernameString = "a";
                usernameString = username.getText().toString();
                String passString = "a";
                passString = pass.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(usernameString, passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginAccountHB.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(getApplicationContext(), "Fail Logged In", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = username.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAccountHB.this, CreateAccountHB.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });

    }
}