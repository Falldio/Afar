package com.newsmap.afar.server;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.TextOptions;

import java.util.Date;

//将数据库中获取的新闻数据存放到news类中
public class news {
    private String id;
    private LatLng location;//位置
    private int category;//分类
    private String content;//文章
    private String uri;//网址
    private String source;//来源
    private String title;//标题
    private Date date;//时间
    private BitmapDescriptor icon;
    private MarkerOptions markerOptions;
    private TextOptions textOptions;

    public news(){
        markerOptions=new MarkerOptions();
        textOptions=new TextOptions();
    }

    public MarkerOptions getMarkerOptions(){
        return markerOptions;
    }

    public TextOptions getTextOptions() {
        return textOptions;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setContent(String content){
        this.content=content;
    }
}
