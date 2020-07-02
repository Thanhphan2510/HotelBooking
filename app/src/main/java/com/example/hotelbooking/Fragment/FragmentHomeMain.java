package com.example.hotelbooking.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.example.hotelbooking.Utils.MyUntil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeMain extends Fragment {
    private Button searchBtn, checkinBtn, checkoutBtn;
    private int mYear, mMonth, mDay;
    private AutoCompleteTextView autoCompleteTextView;
    private ListView image_show;
    String checkinStr ="", checkoutStr="";

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

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final List<String> hotelNames = new ArrayList<>();
        autoCompleteTextView = view.findViewById(R.id.search_autocomplete);
        final ArrayAdapter searchAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,hotelNames);
        autoCompleteTextView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
        autoCompleteTextView.setThreshold(1);
        CollectionReference reference = database.collection("hotels");
                reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String name = String.valueOf(documentSnapshot.get("name"));
                        hotelNames.add(name);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();



                if(autoCompleteTextView.getText().toString().equals("")){
                    if(checkoutStr.equals("")||checkinStr.equals("")){
                        Toast.makeText(getContext(),"Enter checkin, checkout", Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        try {
                            if(MyUntil.covertStringtoDate(checkinStr).before(MyUntil.covertStringtoDate(checkoutStr))){
                                String pass ="2;"+checkinStr+";"+checkoutStr;
                                bundle.putString("pass",pass);
                                bundle.putString("checkin", checkinStr);
                                bundle.putString("checkout", checkoutStr);
                            }else{
                                Toast.makeText(getContext(),"Enter checkin < checkout", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }else{
                    int check = 0;
                    for(String name: hotelNames){
                        if(!autoCompleteTextView.getText().toString().equals(name)){
                          check --;
                        }else{
                            check = hotelNames.size();
                        }

                    }
                    if(check >=0){
                        bundle.putString("pass", "1;"+autoCompleteTextView.getText().toString()+";null");
                    }else{
                        Toast.makeText(getContext(),"Enter hotel name again", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentHomeListItem fragment = new FragmentHomeListItem();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_home, fragment).hide(FragmentHomeMain.this)
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
