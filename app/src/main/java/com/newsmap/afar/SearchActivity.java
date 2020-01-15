package com.newsmap.afar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.newsmap.afar.server.news;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent it=getIntent();
        final Bundle bundle=it.getExtras();
        if(bundle!=null)
            newsEvents = bundle.getParcelableArrayList("newsEvents");
        else{
            Log.e("TAG", "onCreate: 传入数据为空");
        }
    }
}
