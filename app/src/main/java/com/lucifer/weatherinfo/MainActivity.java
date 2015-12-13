package com.lucifer.weatherinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.lucifer.weatherinfo.SwipeToDismiss.SwipeDismissRecyclerViewTouchListener;
import com.lucifer.weatherinfo.WeatherInfoHelpers.DetailsClass;
import com.lucifer.weatherinfo.WeatherInfoHelpers.WeatherInfoManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerview = null;//To hold the expanded listview
    private ExpandableListAdapter mExpandableListAdapter = null ;
    private List<ExpandableListAdapter.Item> mData = null;
    private WeatherInfoManager mWeatherInfoManager = null;
    private ScreenHandler mScreenHandler = null;
    private RelativeLayout mRootLayout = null;
    private  Snackbar  mSnackbar = null;
    DetailsClass mDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(); // to initilize variables
        RefereshWeatherInfo(); //Fetch weather data and populate views
    }

    private void init()
    {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        mData = new ArrayList<>();
        SwipeDismissRecyclerViewTouchListener mTouchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerview,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position)
                            {
                                if (mData != null)
                                {
                                    ExpandableListAdapter.Item  item = mData.get(position);
                                    if (item.type  == ExpandableListAdapter.CHILD )
                                        return  false;
                                    else if ((item.type  == ExpandableListAdapter.HEADER)  && (item.resourceID == R.drawable.circle_minus))
                                        return  false;
                                }
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mData.remove(position);

                                }
                                mExpandableListAdapter.notifyDataSetChanged();
                            }
                        });
        mRecyclerview.setOnTouchListener(mTouchListener);
        mDetails = new DetailsClass(this);
        mDetails.getmUpdatedHashMap();
        mScreenHandler = new ScreenHandler(MainActivity.this, mRecyclerview, mData , mExpandableListAdapter,mDetails);
        mWeatherInfoManager = new WeatherInfoManager(this.getApplicationContext(), mScreenHandler,mDetails);
    }

    private void ShowNoNetworkDialog ()
    {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle(getString(R.string.str_no_connection));
            alertDialog.setMessage(getString(R.string.str_no_connection_descrip));
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog.show();
        }
        catch(Exception e)
        {
        }

    }


    private void CreateSnapBar ()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        mSnackbar =    Snackbar.make(mRootLayout, getString(R.string.str_last_updated) + ":" + formattedDate, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction(getString(R.string.str_dismiss), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
            }
        });
        mSnackbar.show();
    }

    public void RefereshWeatherInfo ()
    {
        if (mWeatherInfoManager.IsNetworkAvilable())
        {

            mWeatherInfoManager.CheckForWeatherUpdates();;
        }
        else
            ShowNoNetworkDialog ();
    }
    public void PopulateRecyclerView()
    {
        HashMap<String ,Boolean> updateHashmap = mDetails .getmUpdatedHashMap();
        if (mData != null)
        {
            mData.clear();
        }
        if (updateHashmap != null)
        {
            Iterator it = updateHashmap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String city_name = pair.getKey().toString();
                SharedPreferences pref = this.getSharedPreferences(city_name, Context.MODE_PRIVATE);
                Picasso city_image = Picasso.with(this);
                city_image.setLoggingEnabled(true);
                ExpandableListAdapter.Item city = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, city_name + ": "+pref.getString(Constants.kmsPrefTEMPERATURE, "0"), city_image, mDetails.GetCityUrl(city_name));
                city.invisibleChildren = new ArrayList<>();
                city.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "MIN Temperature : " +pref.getString(Constants.kmsPrefMIN_TEMPERATURE, "0"), null, null));
                city.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "MAX Temperature : " +pref.getString(Constants.kmsPrefMAX_TEMPERATURE, "0"), null, null));
                city.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Sun Rise : " +pref.getString(Constants.kmsPrefSUN_RISE, "0"), null, null));
                city.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Sun Set : " +pref.getString(Constants.kmsPrefSUN_SET, "0"), null, null));
                city.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Pressure : " +pref.getString(Constants.kmsPref_PRESSURE, "0"), null, null));
                mData.add(city);
            }
            mExpandableListAdapter = new ExpandableListAdapter(mData);
            mRecyclerview.setAdapter(mExpandableListAdapter);
        }
        mScreenHandler.DismissDialog();
        CreateSnapBar ();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Swiipe to dissmiss Listerner

}
