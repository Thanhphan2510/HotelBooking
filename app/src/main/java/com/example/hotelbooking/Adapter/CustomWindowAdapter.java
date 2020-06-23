package com.example.hotelbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hotelbooking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View view;
    private Context context;

    public CustomWindowAdapter(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_infor_window_map,null);
        this.context = context;
    }

    private void RendowWindowText(Marker marker, View veiew){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title_map);
        if(!title.equals("")){
            tvTitle.setText(title);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        RendowWindowText(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        RendowWindowText(marker, view);
        return view;
    }
}
