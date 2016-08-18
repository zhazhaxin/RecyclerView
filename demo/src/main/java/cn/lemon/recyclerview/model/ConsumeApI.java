package cn.lemon.recyclerview.model;

import cn.lemon.recyclerview.model.bean.Consumption;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by linlongxin on 2016/2/26.
 */
public interface ConsumeApI {

    @GET("oracle_ykt0529.php")
    rx.Observable<Consumption[]> getConsumeRecord(@Query("UsrID") String userID, @Query("page") int page);
}

