package com.lucifer.weatherinfo.RetrofitHelpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shodhan on 12/12/2015.
 */
public class Clouds {
    @SerializedName("all")
    String all;

    public String getAll() {
        return all;
    }
}
