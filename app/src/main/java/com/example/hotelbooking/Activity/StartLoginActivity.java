package com.example.hotelbooking.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private TextView exit;
    private LoginButton loginFacebook;
    private SignInButton loginGoogle;
    private Button loginHotelBooking, createAccountHB;
    GoogleApiClient mGoogleApiClient;
//    LoginManager loginManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);
        exit = findViewById(R.id.exit_tv);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.hotelbooking",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(StartLoginActivity.this, ActivityAccount.class));
                }
            }
        };
        loginGoogle = findViewById(R.id.login_google_btn);
        loginFacebook = findViewById(R.id.login_facebook_btn);
        loginHotelBooking = findViewById(R.id.login_hotelbooking_btn);
        createAccountHB = findViewById(R.id.create_account_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this,
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
//        TextView textView = (TextView) loginGoogle.getChildAt(0);
//        textView.setText("Continute with Google");

//        LoginManager

        loginFacebook.setReadPermissions(Arrays.asList("email","public_profile"));

        callbackManager = CallbackManager.Factory.create();

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//<<<<<<< HEAD
//                exit.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
//                        "Auth Token: " + loginResult.getAccessToken().getToken());
//=======
//                boolean login = AccessToken.getCurrentAccessToken()== null ;
//                Log.d("API123", login+"?");
                handleFacebookAccessToken(loginResult.getAccessToken());

//                exit.setText("Login success");


//>>>>>>> 8b91b34b2326230e81dc33ef62977d4d013b195d
            }

            @Override
            public void onCancel() {
                exit.setText("Login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                exit.setText("Login failed.");
            }
        });
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartLoginActivity.this,"Click button login",Toast.LENGTH_SHORT).show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

//        printKeyHash(this);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginHotelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginAccountHB.class);
                startActivity(intent);

            }
        });

        createAccountHB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountHB.class);
                startActivity(intent);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,
//                        this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        // [END build_client]

//        loginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(intent, RC_SIGN_IN);
//                updateUI();
//            }
//        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(StartLoginActivity.this, "You are login", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(StartLoginActivity.this, ActivityAccount.class);
        startActivity(intent);
        finish();
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(StartLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("", "firebaseAuthWithGoogle:" + account.getId());
                handleGoogleAccessToken(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("", "Google sign in failed", e);
                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }
        }

    }
    private void handleGoogleAccessToken(String account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account, null);
                            mAuth.signInWithCredential(credential)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("StartLogin","signInWithGoogle:oncomplete"+ task.isSuccessful());
                                            if (!task.isSuccessful()){
                            Log.w("StartLogin","signInWithGoogle", task.getException());
                            Toast.makeText(StartLoginActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
//                            getProfileGoogle(null);
                        }
                        else {
                            Toast.makeText(StartLoginActivity.this,"Authentication successfull",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
//                            getProfileGoogle(user);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}