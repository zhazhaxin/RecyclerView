package cn.lemon.view.util;

import android.util.Log;

/**
 * Created by linlongxin on 2018/8/9
 */
public class LogUtils {

    private static boolean DEBUG = false;

    public static void setLogEnale(boolean b){
        DEBUG = b;
    }

    public static void log(String tag, String content) {
        if (DEBUG) {
            Log.i(tag, content);
        }
    }
}
