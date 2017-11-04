package cn.lemon.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 复杂的数据类型列表 Adapter , 没有 Header , Footer 的概念，所有的 item 都对应一个 ViewHolder
 * 通过反射自动处理 onCreateViewHolder 过程，如需避免反射调用请使用 CustomMultiTypeAdapter
 *
 * Created by linlongxin on 2016/8/22.
 */

public class MultiTypeAdapter extends RecyclerAdapter {

    private final String TAG = "MultiTypeAdapter";
    private List<Object> mViewsData;
    private ViewHolderManager mViewHolderManager;

    public MultiTypeAdapter(Context context) {
        super(context);
        mViewsData = new ArrayList<>();
        mViewHolderManager = new ViewHolderManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mViewCount - 1) {
            return STATUS_TYPE;
        }
        return mViewHolderManager.getViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        log("onCreateViewHolder -- viewType : " + viewType);
        if (mViewHolderManager == null) {
            throw new ExceptionInInitializerError("mViewHolderManager is null , it need init");
        }
        Class clazzViewHolder = mViewHolderManager.getViewHolderClass(viewType);
        try {
            //这里只适配了 ViewHolder 构造函数只有 ViewGroup.class 参数 或者 无参 情况的构造函数，具体请看 Demo
            BaseViewHolder holder;
            Constructor constructor = clazzViewHolder.getDeclaredConstructor(new Class[]{ViewGroup.class});
            constructor.setAccessible(true);
            holder = (BaseViewHolder) constructor.newInstance(new Object[]{parent});
            if (holder == null) {
                constructor = clazzViewHolder.getDeclaredConstructor();
                holder = (BaseViewHolder) constructor.newInstance();
            }
            return holder;
        } catch (Exception e) {
            Log.e(TAG, "onCreateBaseViewHolder : " + e.getMessage());
        }
        return null;
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        log("onBindViewHolder -- position : " + position);
        if (position == 0 && mViewCount == 1) {
            return;
        }
        if (position == mViewCount - 1 && mLoadMoreView != null) {
            // 显示加载更多
            if (loadMoreEnable && mLoadMoreAction != null && !dismissLoadMore) {
                setViewVisible(mLoadMoreView, true);
                mLoadMoreAction.onAction();
            }
        } else if (mViewsData != null && holder != null){
            holder.setData(mViewsData.get(position));
        }
    }

    public <T> void add(Class<? extends BaseViewHolder<T>> viewHolder, T data) {
        if (dismissLoadMore || data == null || viewHolder == null) {
            return;
        }
        mViewsData.add(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);
        mViewHolderManager.putViewType(mViewCount - 1, viewType); //mViewCount从1开始
        if (mViewCount > 0) {
            int positionStart = mViewCount - 1;
            mViewCount++;
            notifyItemRangeInserted(positionStart, 1);
        }
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, T[] data) {
        addAll(viewHolder, Arrays.asList(data));
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, List<T> data) {
        if (dismissLoadMore || data == null || data.size() == 0) {
            return;
        }
        int size = data.size();
        mViewsData.addAll(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);
        if (mViewCount > 0) {
            int positionStart = mViewCount - 1;
            for (int i = 0; i < size; i++) {
                mViewHolderManager.putViewType(mViewCount - 1, viewType); //mViewCount从1开始
                mViewCount++;
            }
            notifyItemRangeInserted(positionStart, size);
        }
    }

    @Override
    public void clear() {
        if (mViewsData == null) {
            log("clear() mData is null");
            return;
        }
        mViewsData.clear();
        mViewCount = 1;
        dismissLoadMore = false;
        setViewVisible(mLoadMoreView, false);
        setViewVisible(mNoMoreView, false);
        notifyDataSetChanged();
    }
}
