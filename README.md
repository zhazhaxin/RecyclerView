# RefreshRecyclerView

- RecyclerAdapter : 支持下拉刷新，上拉加载，添加Header，Footer
- MultiTypeAdapter/CustomMultiTypeAdapter : 针对 复杂数据类型列表 展示Adapter 
        
## 使用方法

- gradle依赖

```
   compile 'cn.lemon:RefreshRecyclerView:1.4.0'
   compile 'com.android.support:recyclerview-v7:25.4.0'
```

- xml布局文件

```xml
    <cn.lemon.view.RefreshRecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:refresh_able="true"
        app:load_more_able="false"/>
```
         
- java代码

```java
   mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
   mRecyclerView.setSwipeRefreshColors(0xFF437845,0xFFE44F98,0xFF2FAC21);
   mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
   mRecyclerView.setAdapter(mAdapter);
   mRecyclerView.addRefreshAction(new Action() {
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
   mAdapter.setHeader(textView); //添加Header
   mAdapter.setFooter(footer); //添加Footer
```
                
### RecyclerAdapter

> 针对相同数据类型列表，可添加 Header，Footer

自定义 Adapter 应该继承 RecyclerAdapter<T>，如：

```java
class CardRecordAdapter extends RecyclerAdapter<Consumption> {

    public CardRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<Consumption> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new CardRecordHolder(parent);
    }
}
```

### MultiTypeAdapter

> 复杂数据类型列表的 Adapter，没有 Header,Footer 的概念，每个 Item 对应一个 ViewHolder
> 注意：通过反射实现，支持 ViewHolder 的带有一个参数（ViewGroup）和无参两种形式构造函数，性能方面微小的损耗。
> 构造函数为保证反射时能获取到，应该写成 public 静态内部类 或者 public 的单独类。

```
 private MultiTypeAdapter mAdapter = new MultiTypeAdapter(this);
 mAdapter.add(ImageViewHolder.class, getImageVirtualData());
 mAdapter.addAll(TextViewHolder.class, getTextVirtualData());
 mAdapter.addAll(TextImageViewHolder.class, getTextImageVirualData());
 mAdapter.addAll(CardRecordHolder.class, getRecordVirtualData());
```

### ViewHolder

> 自定义 ViewHolder 需继承 BaseViewHolder<T>，如：

 ```java
 class CardRecordHolder extends BaseViewHolder<Consumption> {

     //当使用MultiTypeAdapter时，务必加上此构造方法
     public CardRecordHolder(ViewGroup parent) {
         super(parent, R.layout.holder_consume);
     }

     @Override
     public void setData(Consumption object) {
         super.setData(object);
         name.setText("Demo");
         //UI绑定数据
     }

     @Override
     public void onInitializeView() {
         super.onInitializeView();
         name = findViewById(R.id.name);
         //初始化View
     }

     @Override
     public void onItemViewClick(Consumption object) {
         super.onItemViewClick(object);
         //点击事件
     }
 }
 ```

### CustomMultiTypeAdapter

> 功能和 MultiTypeAdapter 类似，但避免了反射带来的弊端，这里需要编写实现 IViewHolderFactory 接口，对 viewtype 和 ViewHolder 做映射。 

```java
    // 映射 viewtype 和 ViewHolder
    @Override
    public <V extends BaseViewHolder> V getViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT:
                return (V) new TextViewHolder(parent);
            case VIEW_TYPE_IAMGE:
                return (V) new ImageViewHolder(parent);
            case VIEW_TYPE_TEXT_IMAGE:
                return (V) new TextImageViewHolder(parent);
            case VIEW_TYPE_CARD:
                return (V) new CardRecordHolder(parent);
            default:
                return (V) new TextViewHolder(parent);
        }
    }
    
    // 绑定数据
    mAdapter.add(getImageVirtualData(), VIEW_TYPE_IAMGE);
    mAdapter.addAll(getTextVirtualData(), VIEW_TYPE_TEXT);
    mAdapter.addAll(getTextImageVirualData(), VIEW_TYPE_TEXT_IMAGE);
    mAdapter.addAll(getRecordVirtualData(), VIEW_TYPE_CARD);
```

[详细用法请看Demo](https://github.com/llxdaxia/RecyclerView/tree/master/demo)

<img src="screenshot/RecyclerAdapter.png" width="270" height="480"/>
<img src="screenshot/MultiTypeAdapter.png" width="270" height="480"/>
