package com.example.hotelbooking.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hotelbooking.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeMain extends Fragment {
    private Button searchBtn, checkinBtn, checkoutBtn;

    public FragmentHomeMain() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentHomeMain newInstance() {
        FragmentHomeMain fragment = new FragmentHomeMain();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        searchBtn = view.findViewById(R.id.search_btn);
        checkinBtn = view.findViewById(R.id.checkin_button);
        checkoutBtn = view.findViewById(R.id.checkout_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentHomeListItem fragment = new FragmentHomeListItem();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, fragment)
                        .addToBackStack(null).commit();

            }
        });
        return view;
    }
}
