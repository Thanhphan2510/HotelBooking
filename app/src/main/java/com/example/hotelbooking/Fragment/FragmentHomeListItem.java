package com.example.hotelbooking.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hotelbooking.Activity.DetailItemActivity;
import com.example.hotelbooking.Adapter.ItemHomeAdapter;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeListItem extends Fragment {
    private ListView listView;
    ItemHomeAdapter itemHomeAdapter;
    ArrayList<HomeItem> items;

    public FragmentHomeListItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHomeListItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHomeListItem newInstance(String param1, String param2) {
        FragmentHomeListItem fragment = new FragmentHomeListItem();

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
        View view = inflater.inflate(R.layout.fragment_home_list_item, container, false);
        listView = view.findViewById(R.id.items_home_listview);

        items = new ArrayList<>();
        items.add(new HomeItem(R.drawable.vonga, "Vonga Hotel",
                (float) 1.0, 200000, "Price for 3 night, 2 adults",
                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
        items.add(new HomeItem(R.drawable.vonga, "Muong Thanh Hotel",
                (float) 5.0, 500000, "Price for 3 night, 2 adults",
                "Double Room", "Include taxes and charge\nNo prepayment needed"));
        items.add(new HomeItem(R.drawable.vonga, "The Light Hotel",
                (float) 3.0, 400000, "Price for 3 night, 2 adults",
                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
        items.add(new HomeItem(R.drawable.vonga, "Vonga Hotel",
                (float) 1.0, 200000, "Price for 3 night, 2 adults",
                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
        items.add(new HomeItem(R.drawable.vonga, "Muong Thanh Hotel",
                (float) 5.0, 500000, "Price for 3 night, 2 adults",
                "Double Room", "Include taxes and charge\nNo prepayment needed"));
        items.add(new HomeItem(R.drawable.vonga, "The Light Hotel",
                (float) 3.0, 400000, "Price for 3 night, 2 adults",
                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
        itemHomeAdapter = new ItemHomeAdapter(listView.getContext(), items);
        listView.setAdapter(itemHomeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeItem homeItem = (HomeItem) adapterView.getItemAtPosition(i);

                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), DetailItemActivity.class);
                intent.putExtra("InfoClickedItem", homeItem);
                activity.startActivity(intent);



            }
        });

        return view;

    }
}
