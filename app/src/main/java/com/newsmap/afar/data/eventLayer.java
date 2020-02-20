package com.newsmap.afar.data;

import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Text;

import java.util.ArrayList;

//地图界面的新闻事件Marker图层
public class eventLayer {
    public ArrayList<news> internationalNews =new ArrayList<>();//国际要闻
    public ArrayList<news> domesticNews =new ArrayList<>();//国内新闻
    public ArrayList<Marker> internationalMarkers=new ArrayList<>();
    public ArrayList<Marker> domesticMarkers=new ArrayList<>();

    public void addMarker(AMap aMap){
        if (internationalNews.size()!=0){
            for (news event : internationalNews) {
                Marker marker=aMap.addMarker(event.getMarkerOptions());
                event.setMarkerId(marker.getId());
                Text text=aMap.addText(event.getTextOptions());
                event.setTextId(text.getId());
                internationalMarkers.add(marker);
            }
        }else {
            Log.e("TAG", "addMarker: 国际要闻为空");
            return;
        }
        if (domesticNews.size()!=0){
            for (news event : domesticNews) {
                Marker marker=aMap.addMarker(event.getMarkerOptions());
                event.setMarkerId(marker.getId());
                Text text=aMap.addText(event.getTextOptions());
                event.setTextId(text.getId());
                domesticMarkers.add(marker);
            }
        }else {
            Log.e("TAG", "addMarker: 国内新闻为空");
        }
    }

    public news findNewsByMarkerId(String id){
        for (news event: internationalNews){
            if (event.getMarkerId().equals(id)){
               return event;
            }
        }
        for (news event: domesticNews){
            if (event.getMarkerId().equals(id)){
                return event;
            }
        }
        Log.e("TAG", "findNewsByMarkerId: 无匹配项");
        return null;
    }

    public news findNewsById(int id){
        for (news event: internationalNews){
            if (event.getId()==id){
                return event;
            }
        }
        for (news event: domesticNews){
            if (event.getId()==id){
                return event;
            }
        }
        Log.e("TAG", "findNewsById: 无匹配项");
        return null;
    }

    public ArrayList<news> getAllNews(){
        ArrayList<news> events=new ArrayList<>();
        events.addAll(domesticNews);
        events.addAll(internationalNews);
        return events;
    }

    public void setInternationalVisible(boolean visible){
        for (Marker marker:internationalMarkers){
            marker.setVisible(visible);
        }
    }

    public void setDomesticVisible(boolean visible){
        for (Marker marker:domesticMarkers){
            marker.setVisible(visible);
        }
    }
}
