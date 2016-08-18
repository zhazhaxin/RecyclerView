package cn.lemon.recyclerview.model;


import cn.lemon.recyclerview.config.API;
import cn.lemon.recyclerview.model.bean.Consumption;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by linlongxin on 2016/1/26.
 */
public class ConsumeAPIModel {

    private static ConsumeApI consumeApI;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API.ExpensesRecord)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  //在使用RxJava的使用要加上
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    public static void getConsumeRecord(String unifiedVerificationCode, int page, Observer<Consumption[]> observer) {
        if (consumeApI == null) {
            consumeApI = retrofit.create(ConsumeApI.class);
        }
        consumeApI.getConsumeRecord(unifiedVerificationCode, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
