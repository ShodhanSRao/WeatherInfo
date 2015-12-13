package com.lucifer.weatherinfo.RetrofitHelpers;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Shodhan on 12/12/2015.
 */
public interface ApiInterface {
    @GET("/data/2.5/weather")  //
    void getWeather(@Query("q") String city, Callback<ResponseData> callback);

    @GET("/data/2.5/weather")
    void getWheatherReport(@Query("q") String place, @Query("appid") String appId, Callback<ResponseData> cb);

}
