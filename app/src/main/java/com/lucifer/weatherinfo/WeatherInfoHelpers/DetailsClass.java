package com.lucifer.weatherinfo.WeatherInfoHelpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.lucifer.weatherinfo.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Shodhan on 12/11/2015.
 */
public class DetailsClass
{

    private HashMap<String, Boolean> mUpdatedHashMap = new HashMap<String, Boolean>();
    private HashMap<String, String> mCityUrlMAp = new HashMap<String, String>();


    Context mContext ;
    public DetailsClass(Context inContext)
    {
        mContext = inContext;
        populateUpdatedHashMap();
        ClearPreferences();
    }


    private void populateUpdatedHashMap()
    {
        mUpdatedHashMap.clear();
        mUpdatedHashMap.put(Constants.kmsPrefCITY_DETAILS_BANGALORE, false);
        mUpdatedHashMap.put(Constants.kmsPrefCITY_DETAILS_DELHI, false);
        mUpdatedHashMap.put(Constants.kmsPrefCITY_DETAILS_CHENNAI, false);
        mUpdatedHashMap.put(Constants.kmsPrefCITY_DETAILS_KOLKATA, false);
        mUpdatedHashMap.put(Constants.kmsPrefCITY_DETAILS_MUMBAI, false);

        mCityUrlMAp.clear();
        mCityUrlMAp.put(Constants.kmsPrefCITY_DETAILS_BANGALORE, Constants.kmsPrefCITY_URL_BANGALORE);
        mCityUrlMAp.put(Constants.kmsPrefCITY_DETAILS_CHENNAI, Constants.kmsPrefCITY_URL_CHENNAI);
        mCityUrlMAp.put(Constants.kmsPrefCITY_DETAILS_KOLKATA, Constants.kmsPrefCITY_URL_KOLKATA);
        mCityUrlMAp.put(Constants.kmsPrefCITY_DETAILS_MUMBAI, Constants.kmsPrefCITY_URL_MUMBAI);
        mCityUrlMAp.put(Constants.kmsPrefCITY_DETAILS_DELHI, Constants.kmsPrefCITY_URL_DELHI);

    }

    public HashMap<String, Boolean> getmUpdatedHashMap()
    {
        return mUpdatedHashMap;
    }

    public HashMap<String, String > getCityUrlHashMap()
    {
        return mCityUrlMAp;
    }

    public String GetCityUrl (String inCityName)
    {
        if (mCityUrlMAp.containsKey(inCityName))
            return  mCityUrlMAp.get(inCityName);
        return Constants.kmsPrefCITY_URL_DEFAULT;
    }

    public void  ClearPreferences()
    {
        Iterator it = mUpdatedHashMap.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            SharedPreferences pref = mContext.getSharedPreferences(pair.getKey().toString(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.kmsPrefIsUpdated, false);
            editor.clear();
            editor.commit();

        }
    }
}
