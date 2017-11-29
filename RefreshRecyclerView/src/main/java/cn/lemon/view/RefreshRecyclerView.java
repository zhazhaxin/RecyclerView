package cn.lemon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * Created by linlongxin on 2016/1/24.
 */
public class RefreshRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = "RefreshRecyclerView";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<Action> mRefreshActions;
    private boolean loadMoreAble;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_refresh_recycler, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lemon_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.lemon_refresh_layout);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able, true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if (!refreshAble) {
            mSwipeRefreshLayout.setEnabled(false);
        }
        typedArray.recycle();
    }

    public void setAdapter(RecyclerAdapter adapter) {
        if (adapter == null) {
            return;
        }
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.loadMoreEnable = loadMoreAble;
    }

    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = mAdapter.getItemViewType(position);
                    if (type == RecyclerAdapter.HEADER_TYPE
                            || type == RecyclerAdapter.FOOTER_TYPE
                            || type == RecyclerAdapter.STATUS_TYPE) {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public void addRefreshAction(final Action action) {
        if (action == null) {
            return;
        }
        if (mRefreshActions == null) {
            mRefreshActions = new ArrayList<>();
        }
        mRefreshActions.add(action);
    }

    public void setLoadMoreAction(final Action action) {
        Log.d(TAG, "setLoadMoreAction");
        if (mAdapter.dismissLoadMore || !loadMoreAble) {
            return;
        }
        mAdapter.loadMoreEnable = true;
        mAdapter.setLoadMoreAction(action);
    }

    public void showNoMore() {
        mAdapter.showNoMore();
    }

    public void setItemSpace(int left, int top, int right, int bottom) {
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public TextView getNoMoreView() {
        return mAdapter.mNoMoreView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 8位16进制数 ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void showSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        for (Action a : mRefreshActions) {
            a.onAction();
        }
    }
}
