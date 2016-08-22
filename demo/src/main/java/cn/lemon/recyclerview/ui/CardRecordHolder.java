package cn.lemon.recyclerview.ui;

import android.view.ViewGroup;
import android.widget.TextView;

import cn.lemon.recyclerview.R;
import cn.lemon.recyclerview.model.bean.Consumption;
import cn.lemon.view.adapter.BaseViewHolder;

class CardRecordHolder extends BaseViewHolder<Consumption> {

        private TextView name;
        private TextView type;
        private TextView consumeNum;
        private TextView remainNum;
        private TextView consumeAddress;
        private TextView time;

        public CardRecordHolder(ViewGroup parent) {
            super(parent, R.layout.item_consume);
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

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            name = findViewById(R.id.name);
            type = findViewById(R.id.type);
            consumeNum = findViewById(R.id.consume_num);
            remainNum = findViewById(R.id.remain_num);
            consumeAddress = findViewById(R.id.consume_address);
            time = findViewById(R.id.time);
        }

        @Override
        public void onItemViewClick(Consumption object) {
            super.onItemViewClick(object);
            //点击事件
        }
    }