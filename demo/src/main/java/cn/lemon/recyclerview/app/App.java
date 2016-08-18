package cn.lemon.recyclerview.app;

import android.app.Application;

/**
 * Created by linlongxin on 2016/1/24.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        alien95.cn.util.Utils.initialize(this);

    }
}
