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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hotelbooking.Fragment.FragmentAccount;
import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.jgabrielfreitas.core.BlurImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class StartLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button exit;
    private LoginButton loginFacebook;
    private SignInButton loginGoogle;
    private Button loginHotelBooking, createAccountHB;
    GoogleApiClient mGoogleApiClient;
    private Fragment fragAccount;
    //    LoginManager loginManager;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;
    BlurImageView blurImageView;
    boolean clicked;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;

    //    private final int


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);

        exit = (Button) findViewById(R.id.exit_btn);
        blurImageView = (BlurImageView) findViewById(R.id.BlurImageView);
        blurImageView.setBlur(2);
        clicked = false;
        fragAccount = new FragmentAccount();
        //show key hash
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
        //----------

        mAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI();
                } else {

                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    mAuth.signOut();
                }
            }
        };


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

        user = FirebaseAuth.getInstance().getCurrentUser();

        loginFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("thanhphan", "onSuccess: " + "Login fb: " + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());
                updateUI();

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
                Intent intent = new Intent(getApplicationContext(), LoginAccountHB.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        createAccountHB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountHB.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        // Merge prevUser and currentUser accounts and data
                        // ...
                        Log.e("thanhphan", "onComplete: fb accout" );
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
//                            FirebaseUser user = mAuth.getCurrentUser();
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