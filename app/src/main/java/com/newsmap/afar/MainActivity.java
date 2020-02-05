package com.newsmap.afar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.text.Html;
import com.amap.api.maps.model.PolylineOptions.LineJoinType;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;//数组
import java.util.List;

import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.newsmap.afar.data.news;
import com.newsmap.afar.server.newsLinker;

//主界面
public class MainActivity extends AppCompatActivity {

    private AMap aMap;//高德地图对象
    private MapView mMapView;//地图视图
    private newsLinker connection = new newsLinker();//连接数据库
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件
    private View newsDetail;//底部详情页面
    private View searchBar;//顶部搜索栏
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView newsTitle;
    private TextView newsContent;
    private ConstraintLayout newsTitleCard;
    private ScrollView readPage;
    private CustomMapStyleOptions opt = new CustomMapStyleOptions();//自定义地图样式




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
        String dir = "/storage/emulated/0/Android/data/com.newsmap.afar/data/custom";
        copyCustomData(dir, "style.data");
        copyCustomData(dir, "style_extra.data");
        //文件路径
        File file = new File(dir, "style.data");
        if (file.exists()) {
            opt.setStyleDataPath(dir + "/style.data");
            opt.setStyleExtraPath(dir + "/style_extra.data");
            aMap.setCustomMapStyle((opt));
            opt.setEnable(true);

        } else {
            Log.e("TAG", "自定义样式文件不存在");
        }

        UiSettings uiSettings=aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);


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
//                    getRelatedNews(newsEvents);
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
        newsTitle=newsDetail.findViewById(R.id.newsTitle);
        newsContent=newsDetail.findViewById(R.id.newsContent);
        newsTitleCard=newsDetail.findViewById(R.id.newsTitleCard);
        readPage=newsDetail.findViewById(R.id.readPage);
        searchBar=findViewById(R.id.searchBar);
    }

    //设置事件监听
    private void setListener(){
        //设置Marker点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                newsDetail.setVisibility(View.VISIBLE);

                for(int i=0;i<newsEvents.size();i++){
                    if(newsEvents.get(i).getMarkerId().equals(marker.getId())){
                        newsTitle.setText(newsEvents.get(i).getTitle());
                        newsContent.setText(Html.fromHtml(newsEvents.get(i).getContent()));
                        //相关新闻飞线生成
                        for (int j=0;j<newsEvents.get(i).relatedNews.size();j++){
                            PolylineOptions option=new PolylineOptions();
                            option.add(newsEvents.get(i).getLocation());
                            option.add(newsEvents.get(i).relatedNews.get(j).getLocation());
                            option.geodesic(true);
                            option.lineJoinType(LineJoinType.LineJoinRound);
                            List<Integer>colors=new ArrayList<>();
                            colors.add(Color.argb(255,255,0,0));
                            colors.add(Color.argb(255,0,255,0));
                            option.useGradient(true);
                            option.colorValues(colors);
                            aMap.addPolyline(option);
                        }
                        break;
                    }
                }

                bottomSheetBehavior.setPeekHeight(newsTitleCard.getHeight());
                readPage.scrollTo(0,0);
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

        //设置详情页回调
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i==BottomSheetBehavior.STATE_COLLAPSED){
                    //重复打开同一新闻之前调整ScrollView
                    ScrollView readPage=newsDetail.findViewById(R.id.readPage);
                    readPage.scrollTo(0,0);
                    searchBar.setVisibility(View.VISIBLE);
                }
                else if(i==BottomSheetBehavior.STATE_EXPANDED){
                    Drawable background=getDrawable(R.drawable.squared_bottom_sheet);
                    newsDetail.setBackground(background);
                    background=getDrawable(R.drawable.bottom_rounded_card);
                    newsTitleCard.setBackground(background);
                }
                else if(i==BottomSheetBehavior.STATE_DRAGGING){
                    Drawable background=getDrawable(R.drawable.rounded_bottom_sheet);
                    newsDetail.setBackground(background);
                    background=getDrawable(R.drawable.rounded_card);
                    newsTitleCard.setBackground(background);
                    searchBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //设置搜索页回调
        final Intent intent=new Intent(this,SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("newsEvents",newsEvents);
        intent.putExtras(bundle);
        searchBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //切换到SearchActivity
                startActivity(intent);
            }
        });
    }

    private void copyCustomData(String fileDirPath, String fileName) {
        String filePath = fileDirPath + "/" + fileName;// 文件路径
        try {
            File dir = new File(fileDirPath);// 目录路径
            if (!dir.exists()) {// 如果不存在，则创建路径名
                if(!dir.mkdirs()) {
                    Log.e("TAG", "copyCustomData: 目录创建失败");
                    return;
                }
            }
            File file = new File(filePath);
            InputStream is = this.getAssets().open(fileName);
            FileOutputStream os = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            while ((is.read(buf)) != -1) {
                os.write(buf);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "复制样式文件失败");
        }
    }

//    private void getRelatedNews(ArrayList<news> newsEvents){
//        for (news event:newsEvents){
//            for (news event1:newsEvents){
//                boolean isAdded=false;
//                if (event!=event1){
//                    for (String keyWord:event.getKeyWords()){
//                        for (String keyWord1:event1.getKeyWords()){
//                            if (keyWord.equals(keyWord1)){
//                                event.relatedNews.add(event1);
//                                isAdded=true;
//                                break;
//                            }
//                        }
//                        if (isAdded)    break;
//                    }
//                }
//            }
//        }
//    }
}
