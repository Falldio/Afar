package com.newsmap.afar.search;

import androidx.viewpager.widget.ViewPager;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;

public class tabViewPager extends ViewPager {

    public tabViewPager(Context context) {
        super(context);
    }

    public tabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (getCurrentItem()==0){
            performClick();
            return super.onTouchEvent(arg0);
        }else {
            return false;
        }

    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(getCurrentItem()==0){
            return super.onInterceptTouchEvent(arg0);
        }
        else {
            return false;
        }

    }
}
