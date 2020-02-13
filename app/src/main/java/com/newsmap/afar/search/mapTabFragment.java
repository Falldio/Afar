package com.newsmap.afar.search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Text;
import com.newsmap.afar.R;
import com.newsmap.afar.data.news;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class mapTabFragment extends Fragment {
    private AMap aMap;//高德地图对象
    private MapView mMapView;//地图视图
    private CustomMapStyleOptions opt = new CustomMapStyleOptions();//自定义地图样式
    private ArrayList<news> searchedNews=new ArrayList<>();


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_tab_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMapView=view.findViewById(R.id.mapView);
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

        mMapView.onCreate(savedInstanceState);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i=0;i<searchedNews.size();i++) {
                    if (searchedNews.get(i).getMarkerId().equals(marker.getId())) {
                        Uri uri = Uri.parse(searchedNews.get(i).getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
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
            if(this.getActivity()!=null) {
                InputStream is = this.getActivity().getAssets().open(fileName);
                FileOutputStream os = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                while ((is.read(buf)) != -1) {
                    os.write(buf);
                }
                is.close();
                os.close();
            }
            else{
                Log.e("TAG", "copyCustomData: mapTabFragment未Attach");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "复制样式文件失败");
        }
    }

    void changeSearchResult(ArrayList<news> events){
        if (events.size()>0){
            searchedNews.clear();
            searchedNews.addAll(events);
            aMap.clear();
            for (news event : searchedNews) {
                event.setLocation(event.getLocation());
                Marker marker=aMap.addMarker(event.getMarkerOptions());
                event.setMarkerId(marker.getId());
                Text text=aMap.addText(event.getTextOptions());
                event.setTextId(text.getId());
            }
        }
    }
}
