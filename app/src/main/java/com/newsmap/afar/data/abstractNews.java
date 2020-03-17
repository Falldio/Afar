package com.newsmap.afar.data;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

//国际要闻中比例尺较小时显式抽象的新闻事件
public class abstractNews {
    private String location;
    private int count=0;
    private LatLng coordinate;

    public void setLocation(String location){
        this.location=location;
    }

    public String getLocation(){
        if (location==null){
            Log.e("abstractNews", "getLocation: 位置为空");
            return null;
        }else {
            return location;
        }
    }

    public void setCoordinate(LatLng coordinate){
        this.coordinate=coordinate;
    }

    LatLng getCoordinate(){
        if (coordinate==null){
            Log.e("abstractNews", "getCoordinate: 坐标为空");
            return null;
        }else {
            return coordinate;
        }
    }

    public void setCount(int count){
        this.count=count;
    }
}
