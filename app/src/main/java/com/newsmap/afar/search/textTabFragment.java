package com.newsmap.afar.search;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.newsmap.afar.R;
import com.newsmap.afar.data.news;
import java.util.ArrayList;

//文本搜索结果
public class textTabFragment extends Fragment implements searchedNewsFragment.OnListFragmentInteractionListener {
    private ArrayList<news>searchedNews=new ArrayList<>();
    private searchResultAdapter searchResultAdapter;
    private ViewPager viewPager;
    private RecyclerView recyclerView;//搜索结果列表
    private RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static textTabFragment newInstance(){
        textTabFragment fragment=new textTabFragment();
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_tab_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchResultAdapter = new searchResultAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(searchResultAdapter);
        recyclerView=view.findViewById(R.id.searchResultList);
        layoutManager=new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter=new searchedNewsRecyclerViewAdapter(searchedNews,this);
        recyclerView.setAdapter(viewAdapter);
    }

    //单个Item点击回调
    @Override
    public void onListFragmentInteraction(news item){
        Uri uri = Uri.parse(item.getUrl());
        Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
