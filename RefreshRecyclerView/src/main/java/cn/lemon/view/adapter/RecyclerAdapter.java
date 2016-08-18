package cn.lemon.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.view.R;


/**
 * Created by llxal on 2015/12/19.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private static final String TAG = "RecyclerAdapter";

    private static final int HEADER_TYPE = 111;
    private static final int FOOTER_TYPE = 222;
    private static final int STATUS_TYPE = 333;
    private int mViewCount = 0;

    private boolean hasHeader = false;
    private boolean hasFooter = false;
    public boolean isRefreshing = false; //刷新
    public boolean isLoadingMore = false; //正在加载
    public boolean isShowNoMore = false;//停止加载
    public boolean isLoadEnd = false;

    private Action mLoadMoreAction;

    private List<T> mData = new ArrayList<>();

    private View headerView;
    private View footerView;
    private View mStatusView;
    private LinearLayout mLoadMoreView;
    private TextView mNoMoreView;

    private Context mContext;

    public RecyclerAdapter(Context context) {
        mContext = context;
        initStatusView(context);
    }

    public RecyclerAdapter(Context context, T[] data) {
        this(context, Arrays.asList(data));
    }

    public RecyclerAdapter(Context context, List<T> data) {
        mContext = context;
        initStatusView(context);
        this.mData = data;
        mViewCount += data.size();
        notifyDataSetChanged();
    }

    public void initStatusView(Context context) {
        mStatusView = LayoutInflater.from(context).inflate(R.layout.view_status_last, null);
        mStatusView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLoadMoreView = (LinearLayout) mStatusView.findViewById(R.id.load_more_view);
        mNoMoreView = (TextView) mStatusView.findViewById(R.id.no_more_view);
        mViewCount++;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder -- viewType : " + viewType);
        if (viewType == HEADER_TYPE) {
            return new BaseViewHolder(headerView);
        } else if (viewType == FOOTER_TYPE) {
            return new BaseViewHolder(footerView);
        } else if (viewType == STATUS_TYPE) {
            return new BaseViewHolder(mStatusView);
        } else
            return onCreateBaseViewHolder(parent, viewType);
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        Log.i(TAG, "onBindViewHolder -- position : " + position);
        if (!hasHeader && !hasFooter) { //没有Header和Footer
            holder.setData(mData.get(position));
        } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount - 1) { //有Header没有Footer
            holder.setData(mData.get(position - 1));
        } else if (!hasHeader && position < mViewCount - 2) { //没有Header，有Footer
            holder.setData(mData.get(position));
        } else { //都有
            if (position > 0 && position < mViewCount - 2) {
                holder.setData(mData.get(position - 1));
            }
        }
        //执行加载更多操作
        if (position == mViewCount - 2 && !isShowNoMore && position != 0) {
            mLoadMoreView.setVisibility(View.VISIBLE);
            isLoadEnd = true;
            if (mLoadMoreAction != null) {
                mLoadMoreAction.onAction();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType : " + mViewCount);
        if (position == mViewCount - 1) { //添加最后的状态view
            return STATUS_TYPE;
        }
        if (hasHeader && position == 0) {
            return HEADER_TYPE;
        }
        if (hasFooter && position == mViewCount - 2) {
            return FOOTER_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount : " + mViewCount);
        return mViewCount;
    }

    public void showNoMore() {
        mLoadMoreView.setVisibility(View.GONE);
        mNoMoreView.setVisibility(View.VISIBLE);
        isShowNoMore = true;
    }

    public void setLoadMoreAction(Action action) {
        mLoadMoreAction = action;
    }

    public void add(T object) {
        if (!isShowNoMore) {
            mData.add(object);
            mViewCount++;
        }
    }

    public void insert(T object, int position) {
        if (!isShowNoMore) {
            mData.add(position, object);
            mViewCount++;
            notifyItemChanged(position);
        }
    }

    public void addAll(List<T> list) {
        if (!isShowNoMore) {
            mData.addAll(list);
            mViewCount += list.size();
        }
    }

    public void addAll(T[] objects) {
        if (!isShowNoMore) {
            mData.addAll(Arrays.asList(objects));
            mViewCount += objects.length;
            notifyDataSetChanged();
        }
    }

    public void replace(T object, int position) {
        if (!isShowNoMore) {
            mData.set(position, object);
            mViewCount++;
            notifyItemChanged(position);
        }
    }

    public void remove(T object) {
        mData.remove(object);
        mViewCount--;
        notifyItemRemoved(mData.indexOf(object));
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mData.clear();
        mViewCount = 1;
        if (hasHeader) {
            mViewCount++;
        }
        if (hasFooter) {
            mViewCount++;
        }
        notifyDataSetChanged();

        isRefreshing = false;
        isShowNoMore = false;
        isLoadingMore = false;
        mLoadMoreView.setVisibility(View.GONE);
        mNoMoreView.setVisibility(View.GONE);
    }


    public void setHeader(View header) {
        hasHeader = true;
        headerView = header;
        mViewCount++;
    }

    public void setHeader(@LayoutRes int res) {
        setHeader(LayoutInflater.from(mContext).inflate(res, null));
    }

    public void setFooter(View footer) {
        hasFooter = true;
        footerView = footer;
        mViewCount++;
    }

    public void setFooter(@LayoutRes int res) {
        setFooter(LayoutInflater.from(mContext).inflate(res, null));
    }

    public void removeHeader() {
        if (hasHeader) {
            hasHeader = false;
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
        }
    }

}
