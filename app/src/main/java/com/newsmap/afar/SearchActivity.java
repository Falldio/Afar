package com.newsmap.afar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.newsmap.afar.data.news;

import java.util.ArrayList;
import java.util.List;

import com.newsmap.afar.search.searchedNewsFragment;
import com.newsmap.afar.search.searchedNewsRecyclerViewAdapter;

public class SearchActivity extends AppCompatActivity implements searchedNewsFragment.OnListFragmentInteractionListener {
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件
    private RecyclerView recyclerView;
    private RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextInputEditText searchInput;
    private ArrayList<news> searchedNews=new ArrayList<>();//搜索结果


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
        searchInput=findViewById(R.id.searchInput);
        recyclerView=findViewById(R.id.searchResultList);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        viewAdapter=new searchedNewsRecyclerViewAdapter(searchedNews,this);
        recyclerView.setAdapter(viewAdapter);
    }

    //单个Item点击回调
    @Override
    public void onListFragmentInteraction(news item){

    }


}
