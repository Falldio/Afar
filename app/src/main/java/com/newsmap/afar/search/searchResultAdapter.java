package com.newsmap.afar.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.newsmap.afar.data.news;

import java.util.ArrayList;
import java.util.List;


//将标签子页面存入ViewPager
public class searchResultAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private ArrayList<news> searchedNews=new ArrayList<>();
    private List<String>titles;
    private List<Fragment> fragments;
    private textTabFragment textTab;
    private mapTabFragment mapTab;

    public searchResultAdapter(FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        titles=new ArrayList<>();
        titles.add("文本");
        titles.add("地图");
        fragments=new ArrayList<>();
        textTab=new textTabFragment();
        mapTab=new mapTabFragment();
        fragments.add(textTab);
        fragments.add(mapTab);
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        if (position>=0&&position<=1){
            return fragments.get(position);
        }
        else {
            Log.e("TAG", "getItem: Position超限");
            return fragments.get(0);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    public void changeSearchResult(ArrayList<news> events){
        if (events.size()>0){
            searchedNews.clear();
            searchedNews.addAll(events);
            textTab.changeSearchResult(searchedNews);
            mapTab.changeSearchResult(searchedNews);
        }
    }
}

