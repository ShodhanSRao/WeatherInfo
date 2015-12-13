package com.lucifer.weatherinfo.RetrofitHelpers;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Shodhan on 12/12/2015.
 */
public class Wind {
    @SerializedName("speed")
    String speed;
    @SerializedName("deg")
    String deg;

    public String getSpeed() {
        return speed;
    }

    public String getDeg() {
        return deg;
    }

}
