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

import java.util.ArrayList;
import java.util.List;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.RecyclerAdapter;
import cn.lemon.view.util.LogUtils;


/**
 * Created by linlongxin on 2016/1/24.
 */
public class RefreshRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = "RefreshRecyclerView";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<Action> mRefreshActions = new ArrayList<>();
    private boolean mLoadMoreEnable;
    private boolean mShowNoMoreEnable;

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
        mLoadMoreEnable = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_enable, true);
        mShowNoMoreEnable = typedArray.getBoolean(R.styleable.RefreshRecyclerView_show_no_more_enable, true);
        boolean refreshEnable = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_enable, true);
        if (!refreshEnable) {
            mSwipeRefreshLayout.setEnabled(false);
        } else {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        typedArray.recycle();
    }

    public void setAdapter(RecyclerAdapter adapter) {
        if (adapter == null) {
            return;
        }
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.setLoadMoreEnable(mLoadMoreEnable);
        mAdapter.setShowNoMoreEnable(mShowNoMoreEnable);
    }

    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        if (!(layoutManager instanceof GridLayoutManager)) {
            return;
        }
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

    public void addRefreshAction(final Action action) {
        if (action == null) {
            return;
        }
        mRefreshActions.add(action);
    }

    public void addLoadMoreAction(final Action action) {
        if (mAdapter == null) {
            throw new NullPointerException("must call setAdapter before");
        }
        if (mAdapter.isShowNoMoring() || !mLoadMoreEnable) {
            return;
        }
        mAdapter.addLoadMoreAction(action);
    }

    public void addLoadMoreErrorAction(final Action action) {
        if (mAdapter == null) {
            throw new NullPointerException("must call setAdapter before");
        }
        if (mAdapter.isShowNoMoring() || !mLoadMoreEnable) {
            return;
        }
        mAdapter.addLoadMoreErrorAction(action);
    }

    public void showNoMore() {
        log("showNoMore");
        if (mAdapter == null) {
            throw new NullPointerException("must call setAdapter before");
        }
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

    public void setDebug(boolean b) {
        LogUtils.setLogEnale(b);
    }

    protected void log(String content) {
        LogUtils.log(TAG, content);
    }
}
