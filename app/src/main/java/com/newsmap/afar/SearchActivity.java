package com.newsmap.afar;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.newsmap.afar.data.news;

import java.util.ArrayList;
import java.util.Comparator;
import com.newsmap.afar.search.tabViewPager;
import com.newsmap.afar.search.searchResultAdapter;
import jackmego.com.jieba_android.JiebaSegmenter;



public class SearchActivity extends FragmentActivity{
    private ArrayList<news> newsEvents=new ArrayList<>();//新闻事件
    private EditText searchInput;//搜索框
    private ArrayList<news> searchedNews=new ArrayList<>();//搜索结果
    private TabLayout tabLayout;//标签切换
    private tabViewPager viewPager;//视图切换
    private searchResultAdapter viewPagerAdapter;
    InputMethodManager inputMethodManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        initView();
        initData();
        setListener();
    }



    private void setListener(){
        //设置软键盘监听事件
        inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEARCH)) {
                    //清除之前的搜索结果
                    searchedNews.clear();
                    inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(),0);
                    //处理搜索请求:删除特殊符号
                    String processedText=textView.getText().toString().replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+","");
                    ArrayList<String> query=JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(processedText);
                    if(query.size()<=0){
                        return false;
                    }
                    for(String keyword:query){
                        for(news event:newsEvents){
                            event.restore();
                            String eventTitle=event.getTitle();
                            String eventContent=event.getContent();
                            if(eventTitle==null||eventContent==null)    break;
                            if(eventTitle.contains(keyword)||eventContent.contains(keyword)){
                                event.relativity++;
                            }
                        }
                    }
                    ArrayList<news> result=new ArrayList<>();
                    for(news event:newsEvents){
                        if (event.relativity>0){
                            result.add(event);
                        }
                    }
                    //根据相关度降序排列
                    result.sort(new Comparator<news>() {
                        @Override
                        public int compare(news news, news t1) {
                            if(news.relativity < t1.relativity){
                                return 1;
                            }else if(news.relativity > t1.relativity){
                                return -1;
                            }
                            return 0;
                        }
                    });
                    searchedNews.addAll(result);
                    viewPagerAdapter.changeSearchResult(searchedNews);
                }
                return false;
            }
        });

    }

    private void initView(){
        //标签切换页相关变量初始化
        tabLayout=findViewById(R.id.tabLayout);
        viewPagerAdapter=new searchResultAdapter(getSupportFragmentManager());
        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //搜索结果界面
        searchInput=findViewById(R.id.searchBarInput);
    }

    private void initData(){
        Intent it=getIntent();
        final Bundle bundle=it.getExtras();
        if(bundle!=null)
            newsEvents = bundle.getParcelableArrayList("newsEvents");
        else{
            Log.e("TAG", "onCreate: 传入数据为空");
        }
        if(newsEvents!=null&&newsEvents.size()==0)
            Toast.makeText(this,"蜉蝣天地，沧海一粟。",Toast.LENGTH_LONG).show();
    }
}
