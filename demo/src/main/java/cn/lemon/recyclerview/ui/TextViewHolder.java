package cn.lemon.recyclerview.ui;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.lemon.view.adapter.BaseViewHolder;

class TextViewHolder extends BaseViewHolder<String> {

    private TextView mText;

    public TextViewHolder(ViewGroup parent) {
        super(new TextView(parent.getContext()));
        mText = (TextView) itemView;
        mText.setTextSize(16f);
        mText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mText.setGravity(Gravity.CENTER);
        mText.setPadding(24,24,24,24);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
    }

    @Override
    public void setData(String object) {
        super.setData(object);
            mText.setText(object);
    }

    @Override
    public void onItemViewClick(String object) {
        Log.i("TextViewHolder","onItemViewClick");
    }
}

