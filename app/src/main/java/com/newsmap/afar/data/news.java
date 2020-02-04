package com.newsmap.afar.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.TextOptions;

import com.newsmap.afar.R;

import java.util.ArrayList;

//将数据库中获取的新闻数据存放到news类中
public class news implements Parcelable {
    private String markerId;//对应marker的索引值
    private String textId;//对应text的索引值
    private LatLng location;//位置
    private String category;//分类
    private String content;//文章
    private String url;//网址
    private String source;//来源
    private String title;//标题
    private String date;//日期
    private BitmapDescriptor icon;
    private MarkerOptions markerOptions;
    private TextOptions textOptions;
    public int relativity=0;//搜索相关度
    private ArrayList<String> keyWords = new ArrayList<>();
    public ArrayList<news> relatedNews= new ArrayList<>();

     public news(){
        markerOptions=new MarkerOptions();
        textOptions=new TextOptions();
        markerOptions.infoWindowEnable(false);
        markerOptions.title(title);
        textOptions.backgroundColor(0);
        textOptions.fontSize(50);
        icon= BitmapDescriptorFactory.fromResource(R.drawable.location);
        markerOptions.icon(icon);
    }
    public void addKeyWord(String keyWord){
         if(keyWord!=null&&!keyWord.equals("")) {
             this.keyWords.add(keyWord);
             Log.i("TAG", "addKeyWord: "+keyWord);
         }
         else{
             Log.e("TAG", "addKeyWord: keyWord为空");
         }
    }
    public String getCategory(){
         if(category!=null)
             return category;
         else{
             Log.e("TAG", "getCategory: 分类为空");
             return null;
         }
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

    public LatLng getLocation(){
         if (location!=null)
             return location;
         else {
             Log.e("TAG","getLocation: 坐标为空");
             return null;
         }
    }

    public void setCategory(String category){
         this.category=category;
    }

    public void setLocation(LatLng location) {
        this.location = location;
        markerOptions.position(location);
        textOptions.position(location);
    }

    public void setTitle(String title){
        this.title=title;
        markerOptions.title(title);
        textOptions.text(title);
    }

    public void setContent(String content){
        this.content=content;
    }


    public void setUrl(String url){
        this.url=url;
    }

    public String getUrl(){
        if(url!=null)
            return url;
        else{
            Log.e("TAG", "getUrl: Url为空");
            return null;
        }
    }

    public void setMarkerId(String id){
         this.markerId=id;
    }

    public void setTextId(String id){
         this.textId=id;
    }

    public void setDate(String date){
         this.date=date;
    }

    public String getDate(){
         if(date!=null)
             return date;
         else{
             Log.e("TAG", "getDate: 日期为空");
             return null;
         }
    }

    public void setSource(String source){
         this.source=source;
    }

    public String getSource(){
         if(source!=null)
             return source;
         else{
             Log.e("TAG", "getSource: 来源为空");
             return null;
         }
    }

    public static final Parcelable.Creator<news>CREATOR
            =new Parcelable.Creator<news>(){
         public news createFromParcel(Parcel in){
             return new news(in);
         }

         public news[] newArray(int size){
             return new news[size];
         }
    };

    @Override
    public int describeContents(){
         return 0;
    }

    @Override
    public void writeToParcel(Parcel out,int Flags){
         out.writeString(markerId);
         out.writeString(textId);
         out.writeString(category);
         out.writeString(content);
         out.writeString(url);
         out.writeString(source);
         out.writeString(title);
         out.writeString(date);
         //其余数据无需传入搜索页
    }

    private news(Parcel in){
         markerId=in.readString();
         textId=in.readString();
         category=in.readString();
         content=in.readString();
         url=in.readString();
         source=in.readString();
         title=in.readString();
         date=in.readString();
    }

    public ArrayList<String> getKeyWords() {
        if (keyWords.size()>0)
            return keyWords;
        else {
            Log.e("TAG", "getKeyWords: 关键词为空");
            return null;
        }
    }

    //重设部分参数
    public void restore(){
        relativity=0;
    }
}
