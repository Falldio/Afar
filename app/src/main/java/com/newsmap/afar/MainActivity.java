package com.newsmap.afar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;


import java.util.List;//数组
import com.newsmap.afar.server.news;
import com.newsmap.afar.server.newsLinker;

//主界面
public class MainActivity extends AppCompatActivity {

    private AMap aMap;//高德地图对象
    private MapView mMapView;//地图视图
    private newsLinker connection = null;//连接数据库
    private List<news> newsEvents;//新闻事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afar);
        //初始化地图与数据
        initMapView(savedInstanceState);
        initData();
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
        if(!connection.connectToServer()) {
            Log.e("TAG", "initData: 数据库连接失败" );
        }
        if(!connection.getNewsEvents(newsEvents)){
            Log.e("TAG","initData: 数据库为空");
        }
    }
}
