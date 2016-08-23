package cn.lemon.recyclerview.ui;

import android.view.ViewGroup;
import android.widget.TextView;

import cn.lemon.recyclerview.R;
import cn.lemon.view.adapter.BaseViewHolder;

class TextViewHolder extends BaseViewHolder<String> {

    private TextView mText;

    public TextViewHolder(ViewGroup parent) {
        super(parent, R.layout.holder_text);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        mText = findViewById(R.id.text_view);
    }

    @Override
    public void setData(String object) {
        super.setData(object);
            mText.setText(object);
    }
}

