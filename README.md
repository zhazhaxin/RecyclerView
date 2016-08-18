# RefreshRecyclerView --- 可下拉刷新，上拉加载，添加Header，Footer的RecyclerView
        
## 使用方法

gradle依赖

```
   compile 'cn.lemon:RefreshRecyclerView:0.1.0'
```

xml布局文件

```xml
    <cn.lemon.view.RefreshRecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
         
java代码

```
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
                page++;
            }
        });
```
                
RefreshRecyclerView需要设置一个Adapter。

Adapter应该继承 RecyclerAdapter<T>，如：

```java
class MyAdapter extends RecyclerAdapter<Consumption>{
   public MyAdapter(Context context) {
        super(context);
   }

   @Override
   public BaseViewHolder<Consumption> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent.getContext(), R.layout.item_consume);
   }
}
```
        
RecyclerView使用了ViewHolder，自定义ViewHolder需继承BaseViewHolder<T>,如：

```java
   class MyViewHolder extends BaseViewHolder<Consumption>
```
     
自定义的ViewHolder重写两个方法就好了。

```java
    class MyViewHolder extends BaseViewHolder<Consumption> {

        private TextView name;
        private TextView type;
        private TextView consumeNum;
        private TextView remainNum;
        private TextView consumeAddress;
        private TextView time;

        public MyViewHolder(ViewGroup parent) {
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
```

### RefreshRecyclerView添加Header或Footer
     
```
   adapter = new MyAdapter(this);
   //添加Header
   TextView textView = new TextView(this);
   textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(48)));
   textView.setTextSize(16);
   textView.setGravity(Gravity.CENTER);
   textView.setText("重庆邮电大学");
   adapter.setHeader(textView);
```

### 注意事项

 - 依赖了其他库

```
    compile 'com.android.support:recyclerview-v7:23.4.0'
    compile 'com.android.support:support-annotations:23.4.0'
```
<!DOCTYPE html>
<html>
<body>
<video width="320" height="570" controls="controls" autoplay="autoplay">
  <source src="/i/movie.ogg" type="video/ogg" />
  <source src="/i/movie.mp4" type="video/mp4" />
  <source src="/i/movie.webm" type="video/webm" />
  <object data="/i/movie.mp4" width="320" height="570">
    <embed width="320" height="570" src="demo.mp4" />
  </object>
</video>

</body>
</html>