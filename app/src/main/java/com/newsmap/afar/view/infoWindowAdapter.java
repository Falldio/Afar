package com.newsmap.afar.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.newsmap.afar.MainApplication;
import com.newsmap.afar.R;

public class infoWindowAdapter implements AMap.InfoWindowAdapter {
    public View getInfoWindow(Marker marker){
        View view= LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.view_news_title,null);
        TextView title=view.findViewById(R.id.textView);
        title.setText(marker.getTitle());
        Log.i("infowindow", "getInfoWindow: "+title.getText());
        return view;
    }

    public View getInfoContents(Marker marker){
        View view= LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.view_news_title,null);
        TextView title=view.findViewById(R.id.textView);
        title.setText(marker.getTitle());
        Log.i("infowindow", "getInfoContents: "+view);
        return view;
    }
}
