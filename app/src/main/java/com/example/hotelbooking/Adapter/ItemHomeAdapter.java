package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hotelbooking.Activity.DetailItemActivity;
import com.example.hotelbooking.Item.HomeItem;
import com.example.hotelbooking.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ItemHomeAdapter extends ArrayAdapter<HomeItem> {
    private Context context;
    private List<HomeItem> homeItems;

    public ItemHomeAdapter( Context context, List<HomeItem> homeItems) {
        super(context, 0,homeItems);
        this.context = context;
        this.homeItems = homeItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        listView = LayoutInflater.from(context).inflate(R.layout.item_homeitem,parent,false);

        HomeItem homeItem = homeItems.get(position);

        ImageView imageView = (ImageView)  listView.findViewById(R.id.image_homeitem);
        TextView name = listView.findViewById(R.id.name_homeitem);
        RatingBar ratingBar = listView.findViewById(R.id.ratingbar_homeitem);
        TextView des1 = listView.findViewById(R.id.des1_homeitem);
        TextView des2 = listView.findViewById(R.id.des2_homeitem);
        TextView price = listView.findViewById(R.id.price_homeitem);
        TextView des3 = listView.findViewById(R.id.des3_homeitem);

        imageView.setImageResource(homeItem.getImageView());
        name.setText(homeItem.getName());
        ratingBar.setRating(homeItem.getRating());
        des1.setText(homeItem.getDescription1());
        des2.setText(homeItem.getDescription2());
        des3.setText(homeItem.getDescription3());
        price.setText("VND "+ String.valueOf(homeItem.getPrice()));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageView.setFocusable(false);
        return listView;
    }
}
