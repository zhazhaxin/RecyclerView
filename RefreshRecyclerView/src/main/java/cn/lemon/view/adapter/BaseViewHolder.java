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
public class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final String TAG = "BaseViewHolder";
    private T mData;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        onInitializeView();
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
        itemView.setOnClickListener(this);
    }

    public T getData() {
        return mData;
    }

    /**
     * 需先调用 setData 方法才生效，并且保留 super.setData()
     * @param data
     */
    public void onItemViewClick(T data) {

    }

    @Override
    public void onClick(View v) {
        onItemViewClick(mData);
    }
}
