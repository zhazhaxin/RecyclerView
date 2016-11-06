package cn.lemon.recyclerview.app;

import android.app.Application;

import cn.alien95.util.Utils;


/**
 * Created by linlongxin on 2016/1/24.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.initialize(this);
    }
}
