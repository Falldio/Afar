package com.newsmap.afar.server;

import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.TextOptions;

import java.util.Date;

import com.newsmap.afar.R;

//将数据库中获取的新闻数据存放到news类中
public class news {
    private String markerId;//对应marker的索引值
    private String textId;//对应text的索引值
    private LatLng location;//位置
    private int category;//分类
    private String content;//文章
    private String url;//网址
    private String source;//来源
    private String title;//标题
    private Date date;//时间
    private BitmapDescriptor icon;
    private MarkerOptions markerOptions;
    private TextOptions textOptions;

     news(){
        markerOptions=new MarkerOptions();
        textOptions=new TextOptions();
        markerOptions.infoWindowEnable(false);
        markerOptions.title(title);
        textOptions.backgroundColor(0);
        textOptions.fontSize(50);
        icon= BitmapDescriptorFactory.fromResource(R.drawable.location);
        markerOptions.icon(icon);
    }

    public MarkerOptions getMarkerOptions(){
         return markerOptions;
    }

    public TextOptions getTextOptions() {
        return textOptions;
    }

    public String getMarkerId(){
         return markerId;
    }

    public String getTextId(){
         return textId;
    }

    public String getTitle(){
         if(title!=null)
            return title;
         else{
             Log.e("TAG", "getTitle: 新闻标题为空");
             return null;
         }
    }

    public String getContent(){
        if(content!=null)
            return content;
        else{
            Log.e("TAG", "getContent: 新闻内容为空");
            return null;
        }
    }

    void setLocation(LatLng location) {
        this.location = location;
        markerOptions.position(location);
        textOptions.position(location);
    }

    void setTitle(String title){
        this.title=title;
        markerOptions.title(title);
        textOptions.text(title);
    }

    void setContent(String content){
        this.content=content;
    }


    void setUrl(String url){
        this.url=url;
    }

    public void setMarkerId(String id){
         this.markerId=id;
    }

    public void setTextId(String id){
         this.textId=id;
    }
}
