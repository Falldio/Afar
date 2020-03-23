package com.newsmap.afar;
import android.app.Application;
import android.content.Context;

import jackmego.com.jieba_android.JiebaSegmenter;

public class MainApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        JiebaSegmenter.init(getApplicationContext());
    }

    public static Context getContext(){
        return context;
    }
}
