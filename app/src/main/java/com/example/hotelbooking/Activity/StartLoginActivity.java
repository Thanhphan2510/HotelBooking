package com.example.hotelbooking.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.Fragment.FragmentAccount;
import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jgabrielfreitas.core.BlurImageView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class StartLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button exit;
    private LoginButton loginFacebook;
    private SignInButton loginGoogle;
    private Button loginHotelBooking, createAccountHB;
    GoogleApiClient mGoogleApiClient;
    private Fragment fragAccount;
    //    LoginManager loginManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;
    BlurImageView blurImageView;
    boolean clicked;

    //    private final int


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        AppEventsLogger.activateApp(this);

        exit = (Button) findViewById(R.id.exit_btn);
        blurImageView = (BlurImageView) findViewById(R.id.BlurImageView);
        blurImageView.setBlur(2);
        clicked = false;
        fragAccount = new FragmentAccount();
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

        loginGoogle = findViewById(R.id.login_google_btn);
        loginFacebook = findViewById(R.id.login_facebook_btn);
        loginHotelBooking = findViewById(R.id.login_hotelbooking_btn);
//        loginFacebook.setFragment(this);
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
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("thanhphan", "onSuccess: " + "Login fb: " + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());
                updateUI();
//
            }

            @Override
            public void onCancel() {
//                exit.setText("Login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
//                exit.setText("Login failed.");
            }
        });
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartLoginActivity.this, "Click button login", Toast.LENGTH_SHORT).show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
//        printKeyHash(this);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                Toast.makeText(StartLoginActivity.this, "Click exit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StartLoginActivity.this, HomeActivity.class);
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

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUI() {
        Intent intent = new Intent(StartLoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e("thanhphan", "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(StartLoginActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("thanhphan", "onSuccess: " + "Login fb : đẩy lên fairebase");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("thanhphan", "onSuccess: " + "Login fb :  not đẩy lên fairebase");
                        }
                        Log.e("thanhphan", "LOL");
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

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

    private void handleGoogleAccessToken(String account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("StartLogin", "signInWithGoogle:oncomplete" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("StartLogin", "signInWithGoogle", task.getException());
                            Toast.makeText(StartLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                            getProfileGoogle(null);

                        } else {
                            Toast.makeText(StartLoginActivity.this, "Authentication successfull", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
//                            getProfileGoogle(user);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}