package cn.lemon.recyclerview.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.lemon.recyclerview.R;
import cn.lemon.recyclerview.model.bean.Consumption;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.MultiTypeAdapter;

public class MultiTypeActivity extends AppCompatActivity {

    private RefreshRecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private int page = 1;
    private Toolbar toolbar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);

        mAdapter = new MultiTypeAdapter(this);
        setTitle("一卡通消费记录");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
            }
        });
        getData(false);
    }

    public void getData(boolean isRefresh){
        if(isRefresh){
            mAdapter.clear();
        }
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(CardRecordHolder.class,getRecordVirtualData());
                mAdapter.addAll(TextViewHolder.class, getVirtualData());
            }
        }, 1000);
    }


    class TextViewHolder extends BaseViewHolder<String> {

        private TextView mText;

        public TextViewHolder(ViewGroup parent) {
            super(parent, R.layout.holder_text);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mText = findViewById(R.id.text);
        }

        @Override
        public void setData(String object) {
            super.setData(object);
            mText.setText(object);
        }
    }

    public String[] getVirtualData() {
        return new String[]{
                "啦啦啦",
                "啦啦啦",
                "啦啦啦",
        };
    }
    public Consumption[] getRecordVirtualData() {
        return new Consumption[]{
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼")
        };
    }
}
