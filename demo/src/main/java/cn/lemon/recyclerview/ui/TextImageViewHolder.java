package cn.lemon.recyclerview.ui;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.recyclerview.R;
import cn.lemon.recyclerview.ui.bean.TextImage;
import cn.lemon.view.adapter.BaseViewHolder;

/**
 * Created by linlongxin on 2016/8/23.
 */

public class TextImageViewHolder extends BaseViewHolder<TextImage> {

    private TextView mText;
    private ImageView mImage;

    public TextImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.holder_text_image);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        mText = findViewById(R.id.text);
        mImage = findViewById(R.id.image);
    }

    @Override
    public void setData(TextImage object) {
        super.setData(object);
        mText.setText(object.text);
        Glide.with(itemView.getContext())
                .load(object.image)
                .into(mImage);
    }
}
