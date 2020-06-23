package com.example.hotelbooking.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.hotelbooking.Activity.DetailItemActivity;
import com.example.hotelbooking.Activity.MapActivity;
import com.example.hotelbooking.Adapter.ItemHomeAdapter;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.Item.Room;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Utils.MyUntil;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.opencensus.internal.Utils;

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


    ArrayList<String> hotelIDs;
    ArrayList<String> bookedHotelIDs;
    ArrayAdapter<String> adapter;

    int total, bookedTotal;
    int checkBookedRoom = -1;

    HashMap<String, Integer> hotelID_roomID;
    ArrayList<String> roomIDs;

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
        hotelIDs = new ArrayList();
        bookedHotelIDs = new ArrayList();
        hotelID_roomID = new HashMap<>();
        roomIDs = new ArrayList<>();

        final String checkinStr = getArguments().getString("checkin");
        final String checkoutStr = getArguments().getString("checkout");


        itemHomeAdapter = new ItemHomeAdapter(listView.getContext(), items);
        itemHomeAdapter.notifyDataSetChanged();
        listView.setAdapter(itemHomeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemHomeAdapter.notifyDataSetChanged();
                HomeItem homeItem = (HomeItem) listView.getItemAtPosition(i);

//                Log.e("LOL", "onItemClick: "+homeItem.toString() );
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(getContext(), DetailItemActivity.class);
                intent.putExtra("InfoClickedItem", homeItem);
                intent.putExtra("checkinInfor",checkinStr);
                intent.putExtra("checkoutInfor",checkoutStr);
                startActivity(intent);

            }
        });
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference reference1 = database.collection("book");
        reference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String hotel_id = String.valueOf(doc.get("hotelID"));
                        String room_id = String.valueOf(doc.get("roomID"));

                        Timestamp checkinTS = (Timestamp) doc.get("checkin");
                        Date checkinDate_Data = checkinTS.toDate();

                        Timestamp checkoutTS = (Timestamp) doc.get("checkout");
                        Date checkoutDate_Data = checkoutTS.toDate();
                        ArrayList<String> room_list = new ArrayList<>();
                        try {
                            Date checkinDate = MyUntil.covertStringtoDate(checkinStr);
                            Date checkoutDate = MyUntil.covertStringtoDate(checkoutStr);

                            //check date không thỏa mãn
                            if ((checkinDate_Data.before(checkinDate) && checkinDate.before(checkinDate_Data)) ||
                                    (checkinDate.before(checkinDate_Data) && checkoutDate.after(checkoutDate_Data)) ||
                                    (checkinDate_Data.before(checkoutDate) && checkoutDate_Data.after(checkoutDate))) {
                                if (!hotelID_roomID.containsKey(hotel_id)) {
                                    hotelID_roomID.put(hotel_id, 1);
                                    room_list.add(room_id);
                                    bookedHotelIDs.add(hotel_id);
                                } else {
                                    hotelID_roomID.replace(hotel_id, hotelID_roomID.get(hotel_id).intValue() + 1);
                                    room_list.add(room_id);
                                }

                            }else{
                                //date thỏa mãn
                                DocumentReference documentReference = database.collection("hotels").document(hotel_id);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                                    String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                                    new Long(String.valueOf(doc.get("star"))).floatValue(),
                                                    new Integer(String.valueOf(doc.get("price"))),
                                                    String.valueOf(doc.get("description1")),
                                                    String.valueOf(doc.get("description2")),
                                                    String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                            Log.e("items", "onComplete: xử lý các trường hợp date  thỏa mãn "+items.toString() );

                                            //check exist hotel in list
                                            boolean exist = false;
                                            for(HomeItem item1 : items){
                                                if(item1.getId().equals(item.getId())){{
                                                    exist = true;
                                                }}
                                            }
                                            if(exist==false){
                                                items.add(item);
                                            }

                                            itemHomeAdapter.notifyDataSetChanged();

                                        }
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //xử lý các trường hợp date không thỏa mãn
                for (int i = 0; i < bookedHotelIDs.size(); i++) {
                    if (hotelID_roomID.containsKey(hotelID_roomID.get(bookedHotelIDs.get(i)))) {
                        int value = hotelID_roomID.get(bookedHotelIDs.get(i)).intValue();
                        for (int j = 0; j < value; j++) {
                            DocumentReference documentReference = database.collection("hotels")
                                    .document(hotelIDs.get(i)).collection("rooms")
                                    .document(roomIDs.get(j));
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        int leftRoom = new Integer(String.valueOf(documentSnapshot.get("leftroom")));
                                        String hotelID = String.valueOf(documentSnapshot.get("hotelID"));
                                        if (leftRoom > 0) {
                                            DocumentReference documentReference = database.collection("hotels").document(hotelID);
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot doc = task.getResult();
                                                        HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                                                String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                                                new Long(String.valueOf(doc.get("star"))).floatValue(),
                                                                new Integer(String.valueOf(doc.get("price"))),
                                                                String.valueOf(doc.get("description1")),
                                                                String.valueOf(doc.get("description2")),
                                                                String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                                        Log.e("items", "onComplete: xử lý các trường hợp date không thỏa mãn "+items.toString() );
                                                        //check exist hotel in list
                                                        boolean exist = false;
                                                        for(HomeItem item1 : items){
                                                            if(item1.getId().equals(item.getId())){{
                                                                exist = true;
                                                            }}
                                                        }
                                                        if(exist==false){
                                                            items.add(item);
                                                        }

                                                        itemHomeAdapter.notifyDataSetChanged();

                                                    }
                                                }
                                            });
                                        }
                                    }

                                }
                            });
                        }
                    }
                }
            }
        });
//các trường hợp room chưa có ai book
        final CollectionReference reference = database.collection("hotels");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String hotelID = String.valueOf(doc.get("hotelID"));
                        reference.document(hotelID).collection("rooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        int total = new Integer(String.valueOf(doc.get("total")));
                                        int leftroom = new Integer(String.valueOf(doc.get("leftroom")));
                                        if(total==leftroom){
                                            String hotelID = String.valueOf(doc.get("hotelID"));
                                            DocumentReference documentReference = database.collection("hotels").document(hotelID);
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot doc = task.getResult();
                                                        HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                                                String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                                                new Long(String.valueOf(doc.get("star"))).floatValue(),
                                                                new Integer(String.valueOf(doc.get("price"))),
                                                                String.valueOf(doc.get("description1")),
                                                                String.valueOf(doc.get("description2")),
                                                                String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                                        Log.e("items", "onComplete: các trường hợp room chưa có ai book "+items.toString() );

                                                        //check exist hotel in list
                                                        boolean exist = false;
                                                        for(HomeItem item1 : items){
                                                            if(item1.getId().equals(item.getId())){{
                                                                exist = true;
                                                            }}
                                                        }
                                                        if(exist==false){
                                                            items.add(item);
                                                        }

                                                        itemHomeAdapter.notifyDataSetChanged();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }

            }
        });


        return view;
    }
}
