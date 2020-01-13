package com.newsmap.afar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;


import java.util.ArrayList;//数组

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Text;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.newsmap.afar.server.news;
import com.newsmap.afar.server.newsLinker;

//主界面
public class MainActivity extends AppCompatActivity {

    private AMap aMap;//高德地图对象
    private MapView mMapView;//地图视图
    private newsLinker connection = new newsLinker();//连接数据库
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件
    private View newsDetail;
    private BottomSheetBehavior<View> bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化地图与数据
        initMapView(savedInstanceState);
        initData();
        //初始化视图
        initView();
        //设置事件监听
        setListener();
    }


    //初始化地图View
    private void initMapView(Bundle savedInstanceState){
        mMapView=findViewById(R.id.map);
        aMap=mMapView.getMap();

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

    }

    //初始化新闻数据
    private void initData(){
        new Thread(){
            public void run(){
                if(!connection.connectToServer()) {
                    Log.e("TAG", "initData: 数据库连接失败" );
                    return;
                }
                if(!connection.getNewsEvents(newsEvents)){
                    Log.e("TAG","initData: 数据库为空");
                }
                if(newsEvents.size()!=0) {
                    for (news event : newsEvents) {
                        Marker marker=aMap.addMarker(event.getMarkerOptions());
                        event.setMarkerId(marker.getId());
                        Text text=aMap.addText(event.getTextOptions());
                        event.setTextId(text.getId());
                    }
                }
                else{
                    Log.e("TAG", "onCreate: newsEvents为空");
                }
            }
        }.start();
    }

    //初始化视图
    private void initView(){
        //底部弹出BottomSheet,显示新闻内容
        newsDetail=findViewById(R.id.newsDetail);
        bottomSheetBehavior=BottomSheetBehavior.from(newsDetail);
        newsDetail.setVisibility(View.GONE);
    }

    //设置事件监听
    private void setListener(){
        //设置Marker点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                newsDetail.setVisibility(View.VISIBLE);

                TextView newsTitle=newsDetail.findViewById(R.id.newsTitle);
                TextView newsContent=newsDetail.findViewById(R.id.newsContent);

                for(int i=0;i<newsEvents.size();i++){
                    if(newsEvents.get(i).getMarkerId()==marker.getId()){
                        newsTitle.setText(newsEvents.get(i).getTitle());
                        newsContent.setText(newsEvents.get(i).getContent());
                    }
                }
                return true;
            }
        });

        //设置地图触摸事件
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener(){
            @Override
            public void onTouch(android.view.MotionEvent event){
                if(newsDetail.isShown()){
                    newsDetail.setVisibility(View.GONE);
                }
            }
        });
    }
}
