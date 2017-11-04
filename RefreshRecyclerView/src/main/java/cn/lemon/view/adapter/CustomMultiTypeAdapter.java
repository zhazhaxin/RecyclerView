package cn.lemon.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对应 ViewTypeManager
 *
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
        } else
            return mViewHolderManager.getViewHolder(parent, viewType);
    }

    // 弃用 如果加入 header 和 footer 会对 position 造成影响
    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position == 0 && mViewCount == 1) {
            return;
        }
        // 显示加载更多
        if (position == mViewCount - 1) {
            if (loadMoreEnable && mLoadMoreAction != null && !dismissLoadMore) {
                setViewVisible(mLoadMoreView, true);
                mLoadMoreAction.onAction();
            }
        } else {
            holder.setData(mViewsData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mViewCount - 1) {
            return STATUS_TYPE;
        }
        return mViewHolderManager.getViewType(position);
    }

    public <T> void add(T data, int viewType) {
        if (dismissLoadMore && data == null) {
            return;
        }
        mViewsData.add(data);
        mViewHolderManager.putViewType(mViewCount - 1, viewType); //mViewCount从1开始
        int positionStart = mViewCount - 1;
        mViewCount ++;
        notifyItemRangeInserted(positionStart, 1);
    }

    public <T> void addAll(T[] data, int viewType) {
        addAll(Arrays.asList(data), viewType);
    }

    public <T> void addAll(List<T> data, int viewType) {

        if (dismissLoadMore || data == null || data.size() == 0) {
            return;
        }
        int size = data.size();
        mViewsData.addAll(data);
        int positionStart = mViewCount - 1;
        for (int i = 0; i < size; i++) {
            mViewHolderManager.putViewType(mViewCount - 1, viewType); //mViewCount从1开始
            mViewCount++;
        }
        notifyItemRangeInserted(positionStart, size);
    }

    @Override
    public void clear() {
        if (mViewsData == null) {
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
