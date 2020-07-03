package com.example.hotelbooking.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hotelbooking.Activity.DetailItemActivity;
import com.example.hotelbooking.Activity.HomeActivity;
import com.example.hotelbooking.Activity.MapActivity;
import com.example.hotelbooking.Adapter.ItemHomeAdapter;
import com.example.hotelbooking.Item.HomeItem;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

//import java.util.Date;

//import com.example.hotelbooking.Item.MarkerItem;
//import com.google.maps.android.clustering.ClusterManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeListItem extends Fragment {
    private ListView listView;
    private Button mapBtn, sortBtn, btnBack;


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

    HashMap<String, Integer> hotelID_roomID; //khách sạn đã có phòng booked_số lượng phòng đc book của roomID
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
        sortBtn = view.findViewById(R.id.title_sort);
        btnBack = view.findViewById(R.id.btnBack);
        final Dialog dialog = new Dialog(getContext());

        RadioButton radio_price, radio_star_05, radio_star_50;
        dialog.setContentView(R.layout.dialog_sort);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 2;   //x position
        wmlp.y = 3;   //y position

        radio_price = dialog.findViewById(R.id.radio_price);
        radio_star_05 = dialog.findViewById(R.id.radio_star_05);
        radio_star_50 = dialog.findViewById(R.id.radio_star_50);
        radio_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(items, new Comparator<HomeItem>() {
                    @Override
                    public int compare(HomeItem homeItem, HomeItem t1) {
                        if (homeItem.getPrice() > t1.getPrice()) {
                            return 1;
                        }
                        return -1;
                    }
                });
                itemHomeAdapter.notifyDataSetChanged();
                dialog.hide();
            }
        });
        radio_star_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(items, new Comparator<HomeItem>() {
                    @Override
                    public int compare(HomeItem homeItem, HomeItem t1) {
                        if (homeItem.getStar() < t1.getStar()) {
                            return 1;
                        }
                        return -1;
                    }
                });
                itemHomeAdapter.notifyDataSetChanged();
                dialog.hide();
            }
        });
        radio_star_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(items, new Comparator<HomeItem>() {
                    @Override
                    public int compare(HomeItem homeItem, HomeItem t1) {
                        if (homeItem.getStar() > t1.getStar()) {
                            return 1;
                        }
                        return -1;
                    }
                });
                itemHomeAdapter.notifyDataSetChanged();
                dialog.hide();
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HomeActivity.class));
            }
        });
        items = new ArrayList<>();
        hotelIDs = new ArrayList();
        bookedHotelIDs = new ArrayList();
        hotelID_roomID = new HashMap<>();
        roomIDs = new ArrayList<>();

        String pass = getArguments().getString("pass");
        String[] passArr = pass.split(";");
        final String check = passArr[0];
        final String checkinStr = passArr[1];
        final String checkoutStr = passArr[2];
        final String search_hotel_name = passArr[1];


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
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("InfoClickedItem", homeItem);
                if (check.equals("2")) {
                    intent.putExtra("checkinInfor", checkinStr);
                } else {
                    intent.putExtra("checkinInfor", "null");
                }

                intent.putExtra("checkoutInfor", checkoutStr);
                startActivity(intent);

            }
        });

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Log.e("CHECK", "onCreateView: " + check);
        if (check.equals("2")) {

            //Fuction add hotel by checkin, checkout
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
                            Date checkoutDate = null;
                            Date checkinDate = null;
                            try {
                               checkoutDate = MyUntil.covertStringtoDate(checkoutStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                checkinDate = MyUntil.covertStringtoDate(checkinStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


//                            check date không thỏa mãn
                            if (checkinDate.before(checkinDate_Data) && checkoutDate.after(checkoutDate_Data)) {
                                if (!hotelID_roomID.containsKey(hotel_id)) {
                                    hotelID_roomID.put(hotel_id, 1);
                                    room_list.add(room_id);
                                    Log.e("check roomID", "!hotelID_roomID.containsKey(hotel_id): " + room_id + " " + hotel_id);
                                    bookedHotelIDs.add(hotel_id);
                                } else {
                                    hotelID_roomID.replace(hotel_id, hotelID_roomID.get(hotel_id).intValue() + 1);
                                    room_list.add(room_id);
                                    Log.e("check roomID", "hotelID_roomID.containsKey(hotel_id): " + room_id + " " + hotel_id);
                                }
                                //xử lý các trường hợp date không thỏa mãn
                                Log.e("check roomID", "các trường hợp date không thỏa mãn: " + hotel_id + " " + room_list.toString());
                                roomIDs.addAll(room_list);
                                for (int i = 0; i < bookedHotelIDs.size(); i++) {
//                                    Log.e("check roomID", "hotelID_roomID.get(bookedHotelIDs.get(i)): " + );
                                    if (hotelID_roomID.containsKey((bookedHotelIDs.get(i)))) {
                                        int value = 0;
                                        value = hotelID_roomID.get(bookedHotelIDs.get(i)).intValue();
                                        Log.e("check roomID", "onComplete: " + value);
                                        for (int j = 0; j < value; j++) {
                                            Log.e("check roomID", "onComplete: " + roomIDs.get(j));
                                            DocumentReference documentReference = database.collection("hotels")
                                                    .document(bookedHotelIDs.get(i)).collection("rooms")
                                                    .document(roomIDs.get(j));
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        int leftRoom = new Integer(String.valueOf(documentSnapshot.get("leftroom")));
                                                        String hotelID = String.valueOf(documentSnapshot.get("hotelID"));

                                                        //nếu date không thỏa mãn nhưng phòng trống >0 -> OK
                                                        if (leftRoom > 0) {
                                                            DocumentReference documentReference = database.collection("hotels").document(hotelID);
                                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot doc = task.getResult();
                                                                        HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                                                                String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                                                                new Float(String.valueOf(doc.get("star"))),
                                                                                new Integer(String.valueOf(doc.get("price"))),
                                                                                String.valueOf(doc.get("description1")),
                                                                                String.valueOf(doc.get("description2")),
                                                                                String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                                                        Log.e("items", "onComplete: xử lý các trường hợp date không thỏa mãn " + items.toString());
                                                                        //check exist hotel in list
                                                                        boolean exist = false;
                                                                        for (HomeItem item1 : items) {
                                                                            if (item1.getId().equals(item.getId())) {
                                                                                {
                                                                                    exist = true;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (exist == false) {
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
                            } else if (checkinDate_Data.before(checkinDate) && checkinDate.before(checkinDate_Data) || checkinDate_Data.before(checkoutDate) && checkoutDate_Data.after(checkoutDate)) {
                                if (!hotelID_roomID.containsKey(hotel_id)) {
                                    hotelID_roomID.put(hotel_id, 1);
                                    room_list.add(room_id);
                                    Log.e("check roomID", "!hotelID_roomID.containsKey(hotel_id): " + room_id + " " + hotel_id);
                                    bookedHotelIDs.add(hotel_id);
                                } else {
                                    hotelID_roomID.replace(hotel_id, hotelID_roomID.get(hotel_id).intValue() + 1);
                                    room_list.add(room_id);
                                    Log.e("check roomID", "hotelID_roomID.containsKey(hotel_id): " + room_id + " " + hotel_id);
                                }
                                //xử lý các trường hợp date không thỏa mãn
                                Log.e("check roomID", "các trường hợp date không thỏa mãn: " + hotel_id + " " + room_list.toString());
                                roomIDs.addAll(room_list);
                                for (int i = 0; i < bookedHotelIDs.size(); i++) {
//                                    Log.e("check roomID", "hotelID_roomID.get(bookedHotelIDs.get(i)): " + );
                                    if (hotelID_roomID.containsKey((bookedHotelIDs.get(i)))) {
                                        int value = 0;
                                        value = hotelID_roomID.get(bookedHotelIDs.get(i)).intValue();
                                        Log.e("check roomID", "onComplete: " + value);
                                        for (int j = 0; j < value; j++) {
                                            Log.e("check roomID", "onComplete: " + roomIDs.get(j));
                                            DocumentReference documentReference = database.collection("hotels")
                                                    .document(bookedHotelIDs.get(i)).collection("rooms")
                                                    .document(roomIDs.get(j));
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        int leftRoom = new Integer(String.valueOf(documentSnapshot.get("leftroom")));
                                                        String hotelID = String.valueOf(documentSnapshot.get("hotelID"));

                                                        //nếu date không thỏa mãn nhưng phòng trống >0 -> OK
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
                                                                        Log.e("items", "onComplete: xử lý các trường hợp date không thỏa mãn " + items.toString());
                                                                        //check exist hotel in list
                                                                        boolean exist = false;
                                                                        for (HomeItem item1 : items) {
                                                                            if (item1.getId().equals(item.getId())) {
                                                                                {
                                                                                    exist = true;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (exist == false) {
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
                            } else {
//                                //date thỏa mãn ->OK
                                DocumentReference documentReference = database.collection("hotels").document(hotel_id);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                                    String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                                    new Long(String.valueOf(doc.get("star"))).floatValue(),
                                                    new Integer(String.valueOf(doc.get("price"))).intValue(),
                                                    String.valueOf(doc.get("description1")),
                                                    String.valueOf(doc.get("description2")),
                                                    String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                            Log.e("imagefirebase fail", "onComplete: xử lý các trường hợp date  thỏa mãn " + items.toString());

                                            //check exist hotel in list
                                            boolean exist = false;
                                            for (HomeItem item1 : items) {
                                                if (item1.getId().equals(item.getId())) {
                                                    {
                                                        exist = true;
                                                    }
                                                }
                                            }
                                            if (exist == false) {
                                                item.setImageView(item.getImageView());
                                                items.add(item);

                                            }

                                            itemHomeAdapter.notifyDataSetChanged();

                                        }
                                    }
                                });
                                }
//                            }
                        }
                    }


                }
            });
            //các trường hợp room chưa có ai book ( All room are empty)
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
                                            if (total == leftroom) {
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
                                                            Log.e("items", "onComplete: các trường hợp room chưa có ai book " + items.toString());

                                                            //check exist hotel in list
                                                            boolean exist = false;
                                                            for (HomeItem item1 : items) {
                                                                if (item1.getId().equals(item.getId())) {
                                                                    {
                                                                        exist = true;
                                                                    }
                                                                }
                                                            }
                                                            if (exist == false) {
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

        } else { //check == 1
            Log.e("TAG", "onCreateView: " + search_hotel_name);
            CollectionReference documentReference = database.collection("hotels");
            documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = String.valueOf(doc.get("name"));
                            if (name.equals(search_hotel_name)) {
                                HomeItem item = new HomeItem(String.valueOf(doc.get("hotelID")),
                                        String.valueOf(doc.get("image")), String.valueOf(doc.get("name")),
                                        new Long(String.valueOf(doc.get("star"))).floatValue(),
                                        new Integer(String.valueOf(doc.get("price"))),
                                        String.valueOf(doc.get("description1")),
                                        String.valueOf(doc.get("description2")),
                                        String.valueOf(doc.get("description3")).replaceAll("\\n", "\r\n"));
                                items.add(item);

                                itemHomeAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }
            });
        }

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_listitem, FragmentHomeMain.newInstance())
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
        return view;
    }


}
