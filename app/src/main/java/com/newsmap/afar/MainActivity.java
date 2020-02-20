package com.newsmap.afar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.text.Html;

import com.amap.api.maps.model.Polyline;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.newsmap.afar.data.news;
import com.newsmap.afar.server.newsLinker;
import com.newsmap.afar.data.eventLayer;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

//主界面
public class MainActivity extends AppCompatActivity {

    private AMap aMap;//高德地图对象
    private MapView mMapView;//地图视图
    private newsLinker connection = new newsLinker();//连接数据库
    private eventLayer newsEvents=new eventLayer();//新闻事件图层
    private View newsDetail;//底部详情页面
    private View searchBar;//顶部搜索栏
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView newsTitle;
    private TextView newsContent;
    private ConstraintLayout newsTitleCard;
    private NestedScrollView readPage;
    private CustomMapStyleOptions opt = new CustomMapStyleOptions();//自定义地图样式
    private ArrayList<Polyline>relativeLines=new ArrayList<>();//相关新闻飞线




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
        uiSettings.setLogoBottomMargin(-200);


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
                if(!connection.initNews(newsEvents)){
                    Log.e("TAG","initData: 数据库为空");
                }
                newsEvents.addMarker(aMap);
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
        bottomSheetBehavior.setHideable(false);
    }

    //设置事件监听
    private void setListener(){
        //设置Marker点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                newsDetail.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setPeekHeight(newsTitleCard.getHeight());

                news event=newsEvents.findNewsByMarkerId(marker.getId());
                newsTitle.setText(event.getTitle());
                newsContent.setText(Html.fromHtml(event.getContent(),FROM_HTML_MODE_COMPACT));

                //相关新闻飞线生成
                for (Polyline line:relativeLines){
                    line.remove();
                }
                relativeLines.clear();

                for (int i:event.relatedNews) {
                    PolylineOptions option=new PolylineOptions();
                    option.add(event.getLocation());
                    option.add(newsEvents.findNewsById(i).getLocation());
                    List<Integer>colors=new ArrayList<>();
                    colors.add(Color.argb(255,255,0,0));
                    colors.add(Color.argb(255,0,255,0));
                    option.useGradient(true);
                    option.colorValues(colors);
                    option.geodesic(true);
                    option.lineJoinType(LineJoinType.LineJoinRound);
                    relativeLines.add(aMap.addPolyline(option));
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
                    readPage=newsDetail.findViewById(R.id.readPage);
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

        //设置标题栏点击返回顶端
        newsTitleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    readPage=newsDetail.findViewById(R.id.readPage);
                    readPage.scrollTo(0,0);
                }
            }
        });

        //设置搜索页回调
        final Intent intent=new Intent(this,SearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("newsEvents",newsEvents.getAllNews());
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
}
