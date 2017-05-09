package cn.lemon.view.adapter;

import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linlongxin on 2016/8/22.
 */

public class ViewHolderManager {

    private final String TAG = "ViewHolderManager";
    private int mViewType = 10;
    private Map<Class<? extends BaseViewHolder>, Integer> mHolderType;
    private Map<Integer,Class<? extends BaseViewHolder>> mTypeHolder;

    public ViewHolderManager() {
        mHolderType = new HashMap<>();
        mTypeHolder = new HashMap<>();
    }

    public void addViewHolder(Class<? extends BaseViewHolder> viewHolder) {
        if (!mHolderType.containsKey(viewHolder)) {
            Class dataClass = (Class) ((ParameterizedType) viewHolder.getGenericSuperclass()).getActualTypeArguments()[0]; //获取ViewHolder的泛型数据class
            mViewType++;
            mHolderType.put(viewHolder, mViewType);
            mTypeHolder.put(mViewType,viewHolder);
            Log.e(TAG, "addViewHolder dataClassType : " + dataClass.getName());
        }
    }

    public int getViewType(Class<? extends BaseViewHolder> holder){
        if(!mHolderType.containsKey(holder)){
            throw new NullPointerException("please invoke addViewHolder method");
        }
        return mHolderType.get(holder);
    }

    public Class<? extends BaseViewHolder> getViewHolder(int viewType){
        if(!mTypeHolder.containsKey(viewType)){
            throw new NullPointerException("please invoke addViewHolder method");
        }
        return mTypeHolder.get(viewType);
    }
}
