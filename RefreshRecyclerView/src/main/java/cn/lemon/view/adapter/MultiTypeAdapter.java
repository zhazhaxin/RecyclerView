package cn.lemon.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复杂的数据类型列表Adapter，这里没有Header,Footer的概念，所有的item都对应一个ViewHolder
 * Created by linlongxin on 2016/8/22.
 */

public class MultiTypeAdapter extends RecyclerAdapter {

    private final String TAG = "MultiTypeAdapter";
    private List<Object> mViewsData;
    private Map<Integer, Integer> mPositionViewType;  //position --> ViewType
    private ViewHolderManager mViewHolderManager;

    public MultiTypeAdapter(Context context) {
        super(context);
        mViewsData = new ArrayList<>();
        mPositionViewType = new HashMap<>();
        mViewHolderManager = new ViewHolderManager();
    }

    public <T> void add(Class<? extends BaseViewHolder<T>> viewHolder, T data) {
        if (isShowNoMore) {
            return;
        }
        mViewsData.add(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);
        mPositionViewType.put(mViewCount - 1, viewType);//mViewCount从1开始
        int positionStart = mViewCount - 1;
        mViewCount++;
        notifyItemRangeInserted(positionStart, 1);
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, T[] data) {
        addAll(viewHolder, Arrays.asList(data));
    }

    public <T> void addAll(Class<? extends BaseViewHolder<T>> viewHolder, List<T> data) {
        int size = data.size();
        if (isShowNoMore || size == 0) {
            return;
        }
        mViewsData.addAll(data);
        mViewHolderManager.addViewHolder(viewHolder);
        int viewType = mViewHolderManager.getViewType(viewHolder);
        int positionStart = mViewCount - 1;
        for (int i = 0; i < size; i++) {
            mPositionViewType.put(mViewCount - 1, viewType); //mViewCount从1开始
            mViewCount++;
        }
        notifyItemRangeInserted(positionStart, size);
    }

    public void clear() {
        if (mViewsData == null || mViewsData.size() == 0) {
            return;
        }
        mViewsData.clear();
        mViewCount = 1;
        notifyDataSetChanged();

        isShowNoMore = false;
        mLoadMoreView.setVisibility(View.GONE);
        mNoMoreView.setVisibility(View.GONE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mViewCount - 1) {
            return STATUS_TYPE;
        }
        return mPositionViewType.get(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        log("onCreateViewHolder -- viewType : " + viewType);
        if (viewType == STATUS_TYPE) {
            return new BaseViewHolder(mStatusView);
        }
        Class clazzViewHolder = mViewHolderManager.getViewHolder(viewType);
        try {
            //这里只适配了ViewHolder构造函数只有ViewGroup.class参数或者无参情况的构造函数
            BaseViewHolder holder;
            Constructor constructor = clazzViewHolder.getDeclaredConstructor(new Class[]{ViewGroup.class});
            constructor.setAccessible(true);
            holder = (BaseViewHolder) constructor.newInstance(new Object[]{parent});
            if (holder == null) {
                constructor = clazzViewHolder.getDeclaredConstructor();
                holder = (BaseViewHolder) constructor.newInstance();
            }
            return holder;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateBaseViewHolder : " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateBaseViewHolder : " + e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateBaseViewHolder : " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreateBaseViewHolder : " + e.getMessage());
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

        } else if (position == mViewCount - 1) {
            // 显示加载更多
            if (loadMoreAble && mLoadMoreAction != null && !isShowNoMore) {
                mLoadMoreView.setVisibility(View.VISIBLE);
                mLoadMoreAction.onAction();
            }
        } else {
            holder.setData(mViewsData.get(position));
        }
    }

}
