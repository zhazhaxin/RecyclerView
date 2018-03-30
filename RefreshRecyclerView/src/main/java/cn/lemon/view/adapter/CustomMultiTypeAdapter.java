package cn.lemon.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对应 ViewTypeManager
 * <p>
 * Created by linlongxin on 2017/9/20.
 */

public class CustomMultiTypeAdapter extends RecyclerAdapter {

    private final String TAG = "CustomMultiTypeAdapter";
    private List<Object> mViewsData;
    private ViewTypeManager mViewHolderManager;


    public CustomMultiTypeAdapter(Context context) {
        super(context);
        mViewsData = new ArrayList<>();
        mViewHolderManager = new ViewTypeManager();
    }

    public void setViewHolderFactory(IViewHolderFactory factory) {
        if (factory == null) {
            return;
        }
        mViewHolderManager.setViewHolderFactory(factory);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STATUS_TYPE) {
            return new BaseViewHolder<>(mStatusView);
        } else {
            return mViewHolderManager.getViewHolder(parent, viewType);
        }
    }

    // 弃用
    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        // 显示加载更多
        if (!mIsNoMoring && mLoadMoreEnable && !mIsLoadMoring && isValidLoadMore(position)) {
            mIsLoadMoring = true;
            setViewVisible(mLoadMoreLayout, true);
            setViewVisible(mLoadMoreView, true);
            setViewVisible(mLoadMoreError, false);
            setViewVisible(mNoMoreView, false);
            if (mLoadMoreAction != null) {
                log("load more");
                mLoadMoreAction.onAction();
            }
        } else {
            if (position < mViewsData.size()) {
                holder.setData(mViewsData.get(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasEndStatusView() && position == mViewCount - 1) {
            return STATUS_TYPE;
        }
        return mViewHolderManager.getViewType(position);
    }

    public void add(int viewType) {
        add(new Object(), viewType);
    }

    public <T> void add(T data, int viewType) {
        if (mIsNoMoring || data == null) {
            return;
        }
        mIsLoadMoring = false;
        mViewsData.add(data);
        int positionStart;
        if (hasEndStatusView()) {
            positionStart = mViewCount - 1;
        } else {
            positionStart = mViewCount;
        }
        mViewHolderManager.putViewType(positionStart, viewType);
        mViewCount++;
        notifyItemRangeInserted(positionStart, 1);
    }

    public <T> void addAll(T[] data, int viewType) {
        addAll(Arrays.asList(data), viewType);
    }

    public <T> void addAll(List<T> data, int viewType) {

        if (mIsNoMoring || data == null || data.size() == 0) {
            return;
        }
        mIsLoadMoring = false;
        int size = data.size();
        mViewsData.addAll(data);
        int positionStart;
        if (hasEndStatusView()) {
            positionStart = mViewCount - 1;
        } else {
            positionStart = mViewCount;
        }
        for (int i = 0; i < size; i++) {
            mViewHolderManager.putViewType(positionStart + i, viewType);
        }
        mViewCount += size;
        notifyItemRangeInserted(positionStart, size);
    }

    @Override
    public void clear() {
        if (mViewsData == null) {
            return;
        }
        mViewsData.clear();
        mViewCount = hasEndStatusView() ? 1 : 0;
        mIsNoMoring = false;
        mIsLoadMoring = false;
        setViewVisible(mLoadMoreLayout, false);
        setViewVisible(mNoMoreView, false);
        notifyDataSetChanged();
    }
}
