package com.newsmap.afar.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.ListFragment;


//将标签子页面存入ViewPager
public class searchResultAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;

    public searchResultAdapter(FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return textTabFragment.newInstance();
            case 1:
                return mapTabFragment.newInstance();
            default:
                Log.e("TAG", "getItem: Fragment生成参数错误");
                return textTabFragment.newInstance();
        }
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }
}


//  class ArrayListFragment extends ListFragment {
//    int mNum;
//
//    /**
//     * Create a new instance of CountingFragment, providing "num"
//     * as an argument.
//     */
//    static ArrayListFragment newInstance(int num) {
//        ArrayListFragment f = new ArrayListFragment();
//
//        // Supply num input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("num", num);
//        f.setArguments(args);
//
//        return f;
//    }
//}

