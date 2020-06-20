package com.example.hotelbooking.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.hotelbooking.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeMain extends Fragment {
    private Button searchBtn, checkinBtn, checkoutBtn;
    private int mYear, mMonth, mDay;

    String checkinStr, checkoutStr;

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
                Bundle bundle=new Bundle();
                bundle.putString("checkin", checkinStr);
                bundle.putString("checkout", checkoutStr);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentHomeListItem fragment = new FragmentHomeListItem();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, fragment)
                        .addToBackStack(null).commit();

            }
        });
        checkinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkinBtn.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        checkinStr = checkinBtn.getText().toString();
                    }
                }, mYear, mMonth, mDay);
                dd.show();
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkoutBtn.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        checkoutStr = checkoutBtn.getText().toString();
                    }
                }, mYear, mMonth, mDay);
                dd.show();
            }
        });
        return view;
    }
}
