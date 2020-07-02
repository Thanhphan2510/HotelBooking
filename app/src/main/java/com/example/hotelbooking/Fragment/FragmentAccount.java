package com.example.hotelbooking.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Activity.HomeActivity;
import com.example.hotelbooking.Activity.StartLoginActivity;
import com.example.hotelbooking.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CircleImageView avatar;
    private TextView txtName, txtEmail;
    private Button btnBack, btnChangeInfor, btnBookingHistory, btnFavouriteList;
    private Button btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInAccount account;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account_new, container, false);


        txtName = view.findViewById(R.id.txtUsername);
        avatar = view.findViewById(R.id.imageView);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnBack = view.findViewById(R.id.btnBack);
        btnChangeInfor = view.findViewById(R.id.btnChangeInfor);
        btnBookingHistory = view.findViewById(R.id.btnBookingHistory);
        btnFavouriteList = view.findViewById(R.id.btnFaList);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);


        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
//                    getProfileGoogle(mAuth.getCurrentUser());
                } else {
                }
            }
        };

//
//        acct = GoogleSignIn.getLastSignedInAccount(this);
        //login with facebook
        final boolean logout = AccessToken.getCurrentAccessToken() == null;
        if (!logout) {
            Picasso.with(getContext()).load(Profile.getCurrentProfile().
                    getProfilePictureUri(200, 200)).into(avatar);
            Log.d("TAG", "Username:" + Profile.getCurrentProfile().getName());

        }

        if (mAuth.getCurrentUser() != null) {
//            switch (mAuth.getCurrentUser().getProviderId()) {
//                case "google.com":
//                    getProfileGoogle(mAuth.getCurrentUser());
//                    Log.e("thanhphan", "GoogleAuthProviderID: ");
//                    break;
//                case "facebook.com":
////                    getProfileGoogle(mAuth.getCurrentUser());
//                    Log.e("thanhphan", "FacebookAuthProviderID: ");
//                    break;
//                case "password":
//                    Log.e("thanhphan", "EmailAuthProviderID: ");
//                    break;
//                default:
//                    Log.e("thanhphan", "default: "+mAuth.getCurrentUser().getProviderData().get(1).getProviderId());
//                    break;
//            }

            for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (user.getProviderId().equals("password")) {
                    Log.e("thanhphan", "User is signed in with email/password ");
                }
                if (user.getProviderId().equals("facebook.com")) {
                    Log.e("thanhphan", "User is signed in with fb ");
                    getUserProfileFacebook(AccessToken.getCurrentAccessToken());
                }
                if (user.getProviderId().equals("google.com")) {
                    Log.e("thanhphan", "User is signed in with gg");
                    getProfileGoogle(mAuth.getCurrentUser());
                }
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                LoginManager.getInstance().logOut();

                updateUI();
            }
        });


        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_menu, FragmentMenuMain.newInstance()).commit();
        return view;
    }

    private void updateUI() {
        Toast.makeText(getContext(), "You are logout", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), StartLoginActivity.class));
    }


    private void getProfileGoogle(FirebaseUser firebaseUser) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String user_name = acct.getDisplayName();
            String user_email = acct.getEmail();
            Uri user_photo = acct.getPhotoUrl();
            txtName.setText(user_name);
            txtEmail.setText(user_email);
            Glide.with(this).load(String.valueOf(user_photo)).into(avatar);
        }

    }

    private void getUserProfileFacebook(AccessToken currentAccessToken) {

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
                            Picasso.with(getContext()).load(img_url).into(avatar);

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