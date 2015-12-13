package com.lucifer.weatherinfo.WeatherInfoHelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.lucifer.weatherinfo.Constants;
import com.lucifer.weatherinfo.RetrofitHelpers.ApiInterface;
import com.lucifer.weatherinfo.RetrofitHelpers.ResponseData;
import com.lucifer.weatherinfo.ScreenHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shodhan on 12/11/2015.
 */
public class WeatherInfoManager
{

    private Context mContext;
    HashMap<String, Boolean> mUpdatedHashMap = null;
    private boolean mIsUpdating = false;
    private RestAdapter mRestAdapter = null;
    private ApiInterface mApiInterface= null;
    private Handler mScreenHandler = null;
    public WeatherInfoManager(Context inContext, ScreenHandler inScreenHandler, DetailsClass inDetailsClass)
    {
        mContext = inContext;
        mIsUpdating = false;
        mRestAdapter = new RestAdapter.Builder().setEndpoint(Constants.OPENAPI_URL).build();
        mRestAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        mApiInterface = mRestAdapter.create(ApiInterface.class);
        mScreenHandler = inScreenHandler;
        mUpdatedHashMap = inDetailsClass.getmUpdatedHashMap();

    }

    public  HashMap<String, Boolean> GetHasMap()
    {
        return  mUpdatedHashMap;
    }

    private boolean  IsInfosUpdated()
    {
        boolean isUpdated = false;
        Iterator it = mUpdatedHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            //Get each city data whether it is updated or not
            SharedPreferences pref = mContext.getSharedPreferences(pair.getKey().toString(), Context.MODE_PRIVATE);
            isUpdated = pref.getBoolean(Constants.kmsPrefIsUpdated, false);


        }
        return isUpdated;
    }
    public void CheckForWeatherUpdates ()
    {
        mScreenHandler.sendEmptyMessage(Constants.kmiHANDLER_SHOW_PROGRESS_DIALOG);
        Iterator it = mUpdatedHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            //Get each city data whether it is updated or not
            SharedPreferences pref = mContext.getSharedPreferences(pair.getKey().toString(), Context.MODE_PRIVATE);
            if (!pref.getBoolean(Constants.kmsPrefIsUpdated, false))
            {
                FetchWeatherInformation(pair.getKey().toString());
            }

        }

    }

    //Fetch weather information
    private void FetchWeatherInformation(String inCityName)
    {
        mApiInterface.getWheatherReport(inCityName, "2de143494c0b295cca9337e1e96b00e0", mCallback);

    }

    //Update Preferences
    public void UpdatePreference (String inCityName,ResponseData inResponsedata)
    {
        SharedPreferences pref = mContext.getSharedPreferences(inCityName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.kmsPrefTEMPERATURE, WeatherInfoHelper.ConvertKelvinToCelsius(inResponsedata.getMain().getTemp() ));
        editor.putString(Constants.kmsPrefMAX_TEMPERATURE, WeatherInfoHelper.ConvertKelvinToCelsius(inResponsedata.getMain().getTemp_max() ));
        editor.putString(Constants.kmsPrefMIN_TEMPERATURE, WeatherInfoHelper.ConvertKelvinToCelsius(inResponsedata.getMain().getTemp_min() ));
        editor.putString(Constants.kmsPrefSUN_RISE, new Date(Long.parseLong(inResponsedata.getSys().getSunrise())*1000).toString());
        editor.putString(Constants.kmsPrefSUN_SET, new Date(Long.parseLong(inResponsedata.getSys().getSunset())*1000).toString());
        editor.putString(Constants.kmsPref_PRESSURE, inResponsedata.getMain().getPressure());
        editor.putBoolean(Constants.kmsPrefIsUpdated, true);
        editor.commit();

    }
    retrofit.Callback mCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            ResponseData responseData= (ResponseData) o;
            UpdatePreference(responseData.cityName.toString().toLowerCase(), responseData);
            if (IsInfosUpdated())
            {
                if (mScreenHandler != null)
                {
                    mScreenHandler.sendEmptyMessage(Constants.kmiHANDLER_DATA_READY);
                }
            }
            else
            {
                mScreenHandler.sendEmptyMessage(Constants.kmiHANDLER_UPDATE_PROGRESS);
            }
        }

        @Override
        public void failure(RetrofitError error)
        {
            mScreenHandler.sendEmptyMessage(Constants.kmiHANDLER_ERROR_FETCHING);

        }
    };


    public boolean IsNetworkAvilable()
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

}
