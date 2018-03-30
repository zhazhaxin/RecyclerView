package cn.lemon.view.adapter;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

/**
 * Created by linlongxin on 2018/3/29
 */
public class WeakHandler extends Handler {

    private WeakReference<IHandler> mHandler;

    public WeakHandler(IHandler handler) {
        this.mHandler = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        IHandler handler = mHandler.get();
        if (handler != null) {
            handler.handMsg(msg);
        }
    }
}
