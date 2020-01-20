package com.newsmap.afar;
import android.app.Application;
import jackmego.com.jieba_android.JiebaSegmenter;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JiebaSegmenter.init(getApplicationContext());
    }
}
