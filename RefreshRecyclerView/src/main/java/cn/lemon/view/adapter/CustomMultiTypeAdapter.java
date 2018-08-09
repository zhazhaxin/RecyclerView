package cn.lemon.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 * 对应 ViewTypeManager
 * <p>
 * Created by linlongxin on 2017/9/20.
 */

public class CustomMultiTypeAdapter extends RecyclerAdapter {

    private final String TAG = "CustomMultiTypeAdapter";
    private ViewTypeManager mViewHolderManager;


    public CustomMultiTypeAdapter(Context context) {
        super(context);
        mViewHolderManager = new ViewTypeManager();
    }

    public void setViewHolderFactory(IViewHolderFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not null");
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
        if (position < mData.size()) {
            holder.setData(mData.get(position));
        }
        // 显示加载更多
        if (!mIsNoMoring && mLoadMoreEnable && !mIsLoadMoring && isValidLoadMore(position)) {
            performLoadMore();
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
        mData.add(data);
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
        mData.addAll(data);
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
}
