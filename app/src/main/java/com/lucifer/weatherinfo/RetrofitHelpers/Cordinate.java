package com.lucifer.weatherinfo.RetrofitHelpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shodhan on 12/12/2015.
 */
public class Cordinate {
    @SerializedName("lat")
    String lat;
    @SerializedName("lon")
    String lon;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
