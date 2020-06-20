package com.example.hotelbooking.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.hotelbooking.Activity.DetailItemActivity;
import com.example.hotelbooking.Activity.MapActivity;
import com.example.hotelbooking.Adapter.ItemHomeAdapter;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.example.hotelbooking.Item.MarkerItem;
//import com.google.maps.android.clustering.ClusterManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeListItem extends Fragment {
    private ListView listView;
    private Button mapBtn;

    ItemHomeAdapter itemHomeAdapter;
    ArrayList<HomeItem> items;
    HomeItem item;
    GeoDataClient geoDataClient;
    PlaceDetectionClient placeDetectionClient;
    String id;

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
        geoDataClient = Places.getGeoDataClient(getContext(), null);
        placeDetectionClient = Places.getPlaceDetectionClient(getContext(), null);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_list_item, container, false);
        listView = view.findViewById(R.id.items_home_listview);
        mapBtn = view.findViewById(R.id.title_map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });


        items = new ArrayList<>();

//        final String checkinStr = getArguments().getString("checkin");
//        final String checkoutStr = getArguments().getString("checkout");
//        Log.e("notice check in", checkinStr + "????" + checkoutStr);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("hotels");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        HomeItem item = new HomeItem(String.valueOf(doc.get("id")),
                                String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                new Long(String.valueOf(doc.get("star"))).floatValue(),
                                new Integer(String.valueOf(doc.get("price"))),
                                String.valueOf(doc.get("description1")),
                                String.valueOf(doc.get("description2")),
                                String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));

                        items.add(item);

                        itemHomeAdapter.notifyDataSetChanged();

//                        id = String.valueOf(doc.get("id"));
//                        Task<QuerySnapshot> reference = database.collection("hotels")
//                                .document(id).collection("rooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                                int leftQuanitity = new Integer(String.valueOf(doc.get("leftQuanitity")));
//
//                                                int state = new Integer(String.valueOf(doc.get("state")));
//                                                if (state == 1) {
////                                                    getRoom(checkinStr, checkoutStr);
//                                                    Log.e("lol","oke");
//
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                });

                    }
                }
            }
        });
//        collectionReference.document().collection("rooms").get().addOnCompleteListener
//                (new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                int leftQuanitity = new Integer(String.valueOf(doc.get("leftQuanitity")));
//
//                                int state = new Integer(String.valueOf(doc.get("state")));
//                                Log.e("LOL", state + " ");
//                                if (state == 1) {
//                                    getRoom(checkinStr, checkoutStr);
//                                }
//                            }
//                        }
//                    }
//
//                    private void getRoom(String checkinStr, String checkoutStr) {
//                        Task<QuerySnapshot> collectionReference1 = database.collection("hotels")
//                                .document().collection("rooms").document().collection("bookingInfo").get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                                Timestamp timeStamp = (Timestamp) doc.get("checkin");
//                                                Date date = timeStamp.toDate();
//                                                Log.i("Check-in", date.toString());
//                                            }
//                                        }
//                                    }
//                                });
//                    }
//
//                });

//       items.add(item);
//        items.add(new HomeItem(R.drawable.vonga, "Vonga Hotel",
//                (float) 1.0, 200000, "Price for 3 night, 2 adults",
//                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
//        items.add(new HomeItem(R.drawable.vonga, "Muong Thanh Hotel",
//                (float) 5.0, 500000, "Price for 3 night, 2 adults",
//                "Double Room", "Include taxes and charge\nNo prepayment needed"));
//        items.add(new HomeItem(R.drawable.vonga, "The Light Hotel",
//                (float) 3.0, 400000, "Price for 3 night, 2 adults",
//                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
//        items.add(new HomeItem(R.drawable.vonga, "Vonga Hotel",
//                (float) 1.0, 200000, "Price for 3 night, 2 adults",
//                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
//        items.add(new HomeItem(R.drawable.vonga, "Muong Thanh Hotel",
//                (float) 5.0, 500000, "Price for 3 night, 2 adults",
//                "Double Room", "Include taxes and charge\nNo prepayment needed"));
//        items.add(new HomeItem(R.drawable.vonga, "The Light Hotel",
//                (float) 3.0, 400000, "Price for 3 night, 2 adults",
//                "Single Room", "Include taxes and charge\nFREE cancellation\nNo prepayment needed"));
        itemHomeAdapter = new ItemHomeAdapter(listView.getContext(), items);
        itemHomeAdapter.notifyDataSetChanged();
        listView.setAdapter(itemHomeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemHomeAdapter.notifyDataSetChanged();
                HomeItem homeItem = (HomeItem) listView.getItemAtPosition(1);

//                Log.e("LOL", "onItemClick: "+homeItem.toString() );
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(getContext(), DetailItemActivity.class);
                intent.putExtra("InfoClickedItem", homeItem);
                startActivity(intent);

            }
        });

        return view;
    }
}
