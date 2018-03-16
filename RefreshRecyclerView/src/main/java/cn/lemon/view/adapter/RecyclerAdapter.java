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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.view.R;


/**
 * Created by linlongxin on 2015/12/19.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private static final String TAG = "RecyclerAdapter";
    private boolean allowLog = true;  //改成false关闭日志

    public static final int HEADER_TYPE = 111;
    public static final int FOOTER_TYPE = 222;
    public static final int STATUS_TYPE = 333;
    int mViewCount = 0;

    private boolean hasHeader = false;
    private boolean hasFooter = false;

    // 是否可加载更多
    protected boolean mLoadMoreEnable = false;
    // 是否可显示 no more
    protected boolean mShowNoMoreEnable = false;
    // 是否正在显示 no more
    protected boolean mIsShowNoMoring = false;

    protected Action mLoadMoreAction;

    private List<T> mData = new ArrayList<>();

    private View headerView;
    private View footerView;
    protected View mStatusView;
    protected LinearLayout mLoadMoreView;
    public TextView mNoMoreView;

    protected Context mContext;

    public void colseLog() {
        allowLog = false;
    }

    public RecyclerAdapter(Context context) {
        mContext = context;
    }

    public RecyclerAdapter(Context context, T[] data) {
        this(context, Arrays.asList(data));
    }

    public RecyclerAdapter(Context context, List<T> data) {
        mContext = context;
        this.mData = data;
        mViewCount += data.size();
        notifyDataSetChanged();
    }

    private void initEndStatusView() {
        if (isHasEndStatusView() && mStatusView == null) {
            mStatusView = LayoutInflater.from(getContext()).inflate(R.layout.view_status_last, null);
            mStatusView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreView = (LinearLayout) mStatusView.findViewById(R.id.load_more_view);
            mNoMoreView = (TextView) mStatusView.findViewById(R.id.no_more_view);
            mViewCount++;
        }
    }

    public void setLoadMoreEnable(boolean b) {
        mLoadMoreEnable = b;
        initEndStatusView();
    }

    public void setShowNoMoreEnable(boolean b) {
        mShowNoMoreEnable = b;
        initEndStatusView();
    }

    public boolean isShowNoMoring() {
        return mIsShowNoMoring;
    }

    public void setIsShowNoMoring(boolean b) {
        mIsShowNoMoring = b;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new BaseViewHolder<>(headerView);
        } else if (viewType == FOOTER_TYPE) {
            return new BaseViewHolder<>(footerView);
        } else if (viewType == STATUS_TYPE) {
            return new BaseViewHolder<>(mStatusView);
        } else {
            return onCreateBaseViewHolder(parent, viewType);
        }
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    /* ViewHolder 绑定数据，这里的 position 和 getItemViewType() 方法的 position 不一样
        这里的 position 指当前可见的 item 的 position 的位置。
        注意 ：每个 ViewHolder 绑定数据时值调用此方法一次
     */
    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        log("onBindViewHolder()  viewCount : " + mViewCount + " position : " + position);
        if (holder == null || position < 0) {
            return;
        }
        int dataSize = mData.size();
        if (position == 0) {
            // 最先加载 mStatusView 时不需要绑定数据，header 不负责数据加载
            if (mViewCount == 1 && (isHasEndStatusView() || hasHeader)) {
                return;
            } else if (dataSize > 0) {
                holder.setData(mData.get(0));
            }
        } else {
            if (isHasEndStatusView()) {
                if (!hasHeader && !hasFooter && position < dataSize) {
                    //没有Header和Footer
                    holder.setData(mData.get(position));
                } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount - 1 && position - 1 < dataSize) {
                    //有Header没有Footer
                    holder.setData(mData.get(position - 1));
                } else if (!hasHeader && position < mViewCount - 2 && position < dataSize) {
                    //没有Header，有Footer
                    holder.setData(mData.get(position));
                } else if (position > 0 && position < mViewCount - 2 && position - 1 < dataSize) {
                    //Header, Footer 都有
                    holder.setData(mData.get(position - 1));
                }
            } else {
                if (!hasHeader && !hasFooter && position < dataSize) {
                    //没有Header和Footer
                    holder.setData(mData.get(position));
                } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount && position - 1 < dataSize) {
                    //有Header没有Footer
                    holder.setData(mData.get(position - 1));
                } else if (!hasHeader && position < mViewCount - 1 && position < dataSize) {
                    //没有Header，有Footer
                    holder.setData(mData.get(position));
                } else if (position > 0 && position < mViewCount - 1 && position - 1 < dataSize) {
                    //Header, Footer 都有
                    holder.setData(mData.get(position - 1));
                }
            }
        }

        // 最后一个可见的 item 时 加载更多。解决 remove 时 bug
        if (mLoadMoreEnable && !mIsShowNoMoring && position == mViewCount - 2) {
            setViewVisible(mLoadMoreView, true);
            if (mLoadMoreAction != null) {
                mLoadMoreAction.onAction();
            }
        }
    }

    /**
     * ViewHolder 更新 Item 的位置选择 ViewType , 和 UI 是同步的
     */
    @Override
    public int getItemViewType(int position) {
        // header
        if (hasHeader && position == 0) {
            return HEADER_TYPE;
        }
        // footer
        if (hasFooter && isHasEndStatusView() && position == mViewCount - 2) {
            return FOOTER_TYPE;
        } else if (hasFooter && !isHasEndStatusView() && position == mViewCount - 1) {
            return FOOTER_TYPE;
        }
        // status
        if (isHasEndStatusView() && position == mViewCount - 1) {
            return STATUS_TYPE;
        }

        return super.getItemViewType(position);
    }

    /**
     * 包含了 Header , Footer , 状态显示 Item
     */
    @Override
    public int getItemCount() {
        return mViewCount;
    }

    public void showNoMore() {
        if (!mShowNoMoreEnable) {
            return;
        }
        mIsShowNoMoring = true;
        if (mLoadMoreView != null) {
            mLoadMoreView.post(new Runnable() {
                @Override
                public void run() {
                    setViewVisible(mLoadMoreView, false);
                    setViewVisible(mNoMoreView, true);
                }
            });
        }
    }

    public void openLoadMore() {
        mIsShowNoMoring = false;
        if (mLoadMoreView != null) {
            mLoadMoreView.post(new Runnable() {
                @Override
                public void run() {
                    setViewVisible(mLoadMoreView, true);
                    setViewVisible(mNoMoreView, false);
                }
            });
        }
    }

    public void setLoadMoreAction(Action action) {
        mLoadMoreAction = action;
    }

    public void add(T object) {
        if (!mIsShowNoMoring && object != null) {
            mData.add(object);
            int position;
            if (hasFooter && isHasEndStatusView()) {
                position = mViewCount - 2;
            } else if (hasFooter && !isHasEndStatusView()) {
                position = mViewCount - 1;
            } else if (!hasFooter && isHasEndStatusView()) {
                position = mViewCount - 1;
            } else {
                position = mViewCount;
            }
            mViewCount++;
            notifyItemInserted(position);
        }
    }

    public void insert(T object, int itemPosition) {
        int maxPosition = isHasEndStatusView() ? mViewCount - 2 : mViewCount - 1;
        if (mData != null && itemPosition < maxPosition && object != null) {
            int dataPosition;
            if (hasHeader) {
                dataPosition = itemPosition - 1;
            } else {
                dataPosition = itemPosition;
            }
            mData.add(dataPosition, object);
            mViewCount++;
            notifyItemInserted(itemPosition);
        }
    }

    public void addAll(List<T> data) {
        if (data == null) {
            return;
        }
        int size = data.size();
        if (!mIsShowNoMoring && size > 0) {
            mData.addAll(data);
            int positionStart;
            if (hasFooter && isHasEndStatusView()) {
                positionStart = mViewCount - 2;
            } else if (hasFooter && !isHasEndStatusView()) {
                positionStart = mViewCount - 1;
            } else if (!hasFooter && isHasEndStatusView()) {
                positionStart = mViewCount - 1;
            } else {
                positionStart = mViewCount;
            }
            mViewCount += size;
            notifyItemRangeInserted(positionStart, size);
            log("addAll()  startPosition : " + positionStart + "  itemCount : " + size);
        }
    }

    public void addAll(T[] objects) {
        addAll(Arrays.asList(objects));
    }

    public void replace(T object, int itemPosition) {
        if (mData != null && object != null) {
            int dataPosition;
            if (hasHeader) {
                dataPosition = itemPosition - 1;
            } else {
                dataPosition = itemPosition;
            }
            if (dataPosition < mData.size()) {
                mData.set(dataPosition, object);
                mViewCount++;
                notifyItemChanged(itemPosition);
            }
        }
    }

    //position start with 0
    public void remove(T object) {
        if (object != null && !mData.contains(object)) {
            Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
            log("remove()  without the object : " + object.getClass().getName());
            return;
        }
        int dataPosition = mData.indexOf(object);
        int itemPosition;
        if (hasHeader) {
            itemPosition = dataPosition + 1;
        } else {
            itemPosition = dataPosition;
        }
        remove(itemPosition);
    }

    /**
     * positionItem start with 0
     */
    public void remove(int itemPosition) {
        int dataPosition;
        int dataSize = mData.size();
        if (hasHeader) {
            dataPosition = itemPosition - 1;
            if (dataPosition >= 0 && dataPosition < dataSize) {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            } else if (dataPosition >= dataSize) {
                Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
            } else {
                throw new IndexOutOfBoundsException("RecyclerView has header,position is should more than 0 ." +
                        "if you want remove header , pleasure user removeHeader()");
            }
        } else {
            dataPosition = itemPosition;
            if (dataPosition >= dataSize) {
                Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
            } else {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            }
        }
    }

    public void clear() {
        if (mData == null) {
            log("clear() mData is null");
            return;
        }
        mData.clear();
        mViewCount = isHasEndStatusView() ? 1 : 0;
        if (hasHeader) {
            mViewCount++;
        }
        if (hasFooter) {
            mViewCount++;
        }
        mIsShowNoMoring = false;
        setViewVisible(mLoadMoreView, false);
        setViewVisible(mNoMoreView, false);
        notifyDataSetChanged();
    }

    /**
     * header 不负责数据加载。。。。也就是不会掉用 onBindViewHolder
     *
     * @param header
     */
    public void setHeader(View header) {
        hasHeader = true;
        headerView = header;
        mViewCount++;
    }

    public void setHeader(@LayoutRes int res) {
        setHeader(LayoutInflater.from(mContext).inflate(res, null));
    }

    public View getHeader() {
        return headerView;
    }

    public View getFooter() {
        return footerView;
    }

    public void setFooter(View footer) {
        hasFooter = true;
        footerView = footer;
        mViewCount++;
    }

    /**
     * Footer 这里不负责数据的加载
     *
     * @param res
     */
    public void setFooter(@LayoutRes int res) {
        setFooter(LayoutInflater.from(mContext).inflate(res, null));
    }

    public void removeHeader() {
        if (hasHeader) {
            hasHeader = false;
            notifyItemRemoved(0);
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
            if (isHasEndStatusView() && mViewCount > 1) {
                notifyItemRemoved(mViewCount - 2);
            } else if (!isHasEndStatusView() && mViewCount > 0) {
                notifyItemRemoved(mViewCount - 1);
            }
        }
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    protected void setViewVisible(View view, boolean visibile) {
        if (view != null) {
            view.setVisibility(visibile ? View.VISIBLE : View.GONE);
        }
    }

    protected boolean isHasEndStatusView() {
        return mLoadMoreEnable || mShowNoMoreEnable;
    }

    public void log(String content) {
        if (allowLog) {
            Log.d(TAG, content);
        }
    }
}
