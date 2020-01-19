package com.newsmap.afar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import com.newsmap.afar.data.news;

import java.util.ArrayList;

import com.newsmap.afar.search.searchedNewsFragment;
import com.newsmap.afar.search.searchedNewsRecyclerViewAdapter;

//import jackmego.com.jieba_android.JiebaSegmenter;


public class SearchActivity extends AppCompatActivity implements searchedNewsFragment.OnListFragmentInteractionListener{
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件
    private RecyclerView recyclerView;//搜索结果列表
    private RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchInput;//搜索框
    private ArrayList<news> searchedNews=new ArrayList<>();//搜索结果
    InputMethodManager inputMethodManager;
//    JiebaSegmenter segmenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        JiebaSegmenter.init(getApplicationContext());
        Intent it=getIntent();
        final Bundle bundle=it.getExtras();
        if(bundle!=null)
            newsEvents = bundle.getParcelableArrayList("newsEvents");
        else{
            Log.e("TAG", "onCreate: 传入数据为空");
        }
        inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        searchInput=findViewById(R.id.searchBarInput);
        //设置软键盘监听事件
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEARCH)) {
                    //清除之前的搜索结果
                    inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(),0);
                    searchedNews.clear();
                    CharSequence s=textView.getText();

                    if(s == null || TextUtils.isEmpty(s.toString())){
                        return false;
                    }
                    String content = s.toString();
                    if(!TextUtils.isEmpty(content)){
                        for(news event:newsEvents){
                            String eventTitle=event.getTitle();
                            String eventContent=event.getContent();
                            if(eventTitle==null||eventContent==null)    return true;
                            if (eventTitle.contains(s)||eventContent.contains(s)){
                                searchedNews.add(event);
                                viewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                return false;
            }
        });
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
