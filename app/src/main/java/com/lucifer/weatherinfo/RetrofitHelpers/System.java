package com.lucifer.weatherinfo.RetrofitHelpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shodhan on 12/12/2015.
 */
public class System {
    @SerializedName("message")
    String msg;
    @SerializedName("country")
    String country;
    @SerializedName("sunrise")
    String sunrise;
    @SerializedName("sunset")
    String sunset;

    public String getMsg() {
        return msg;
    }

    public String getCountry() {
        return country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
