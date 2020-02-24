package com.newsmap.afar.data;

import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;

import java.util.ArrayList;

//地图界面的新闻事件Marker图层
public class eventLayer {
    public ArrayList<news> internationalNews =new ArrayList<>();//国际要闻
    public ArrayList<news> domesticNews =new ArrayList<>();//国内新闻
    public ArrayList<abstractNews> countries =new ArrayList<>();//抽象新闻符号
    private ArrayList<Marker> internationalMarkers=new ArrayList<>();
    private ArrayList<Marker> domesticMarkers=new ArrayList<>();
    private ArrayList<Marker> countryMarkers=new ArrayList<>();

    public void addMarker(AMap aMap){
        if (internationalNews.size()!=0){
            for (news event : internationalNews) {
                Marker marker=aMap.addMarker(event.getMarkerOptions());
                event.setMarkerId(marker.getId());
//                Text text=aMap.addText(event.getTextOptions());
//                event.setTextId(text.getId());
                marker.setVisible(false);
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
//                Text text=aMap.addText(event.getTextOptions());
//                event.setTextId(text.getId());
                domesticMarkers.add(marker);
            }
        }else {
            Log.e("TAG", "addMarker: 国内新闻为空");
        }
        if (countries.size()!=0){
            for(abstractNews event:countries){
                Marker marker=aMap.addMarker(new MarkerOptions()
                        .position(event.getCoordinate()));
                marker.setVisible(false);
                countryMarkers.add(marker);
            }
        }else {
            Log.e("TAG", "addMarker: 抽象新闻为空");
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

    private void setInternationalVisible(boolean visible){
        if (internationalMarkers.get(0).isVisible()==visible)   return;
        for (Marker marker:internationalMarkers){
            marker.setVisible(visible);
        }
    }

    private void setDomesticVisible(boolean visible){
        if (domesticMarkers.get(0).isVisible()==visible)    return;
        for (Marker marker:domesticMarkers){
            marker.setVisible(visible);
        }
    }

    private void setCountriesVisible(boolean visible){
        if(countryMarkers.get(0).isVisible()==visible)  return;
        for (Marker marker:countryMarkers){
            marker.setVisible(visible);
        }
    }

    public void onZoomChanged(float zoom){
        //zoom越大，比例尺越大，3-17
        if (zoom>=3&&zoom<=8){
            setCountriesVisible(true);
            setDomesticVisible(false);
            setInternationalVisible(false);
        }else if(zoom>=9&&zoom<=13){
            setCountriesVisible(false);
            setDomesticVisible(false);
            setInternationalVisible(true);
        }else if(zoom>=14&&zoom<=17){
            setCountriesVisible(false);
            setDomesticVisible(true);
            setInternationalVisible(false);
        }
    }
}
