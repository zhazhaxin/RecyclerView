package cn.lemon.view.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseViewHolder 顶级父类
 * Created by linlongxin on 2015/12/19.
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    private final String TAG = "BaseViewHolder";
    private T mData;

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        onInitializeView();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(mData);
            }
        });
    }

    public void onInitializeView() {

    }

    public <T extends View> T findViewById(@IdRes int resId) {
        if (itemView != null) {
            return (T) itemView.findViewById(resId);
        } else {
            return null;
        }
    }

    public void setData(final T data) {
        if (data == null) {
            return;
        }
        mData = data;
    }

    public T getData() {
        return mData;
    }

    public void onItemViewClick(T data) {

    }
}
