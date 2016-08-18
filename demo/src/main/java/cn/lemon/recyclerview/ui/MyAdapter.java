package cn.lemon.recyclerview.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.lemon.recyclerview.R;
import cn.lemon.recyclerview.model.bean.Consumption;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


class MyAdapter extends RecyclerAdapter<Consumption> {

    public MyAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<Consumption> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<Consumption> {

        private TextView name;
        private TextView type;
        private TextView consumeNum;
        private TextView remainNum;
        private TextView consumeAddress;
        private TextView time;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_consume);
            itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            consumeNum = (TextView) itemView.findViewById(R.id.consume_num);
            remainNum = (TextView) itemView.findViewById(R.id.remain_num);
            consumeAddress = (TextView) itemView.findViewById(R.id.consume_address);
            time = (TextView) itemView.findViewById(R.id.time);

        }

        @Override
        public void setData(Consumption object) {
            super.setData(object);
            name.setText("Demo");
            type.setText(object.getLx());
            consumeNum.setText("消费金额：" + object.getJe());
            remainNum.setText("卡里余额：" + object.getYe());
            consumeAddress.setText(object.getSh());
            time.setText(object.getSj());
        }
    }
}