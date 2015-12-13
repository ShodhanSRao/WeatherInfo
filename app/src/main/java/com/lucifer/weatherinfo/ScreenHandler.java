package com.lucifer.weatherinfo;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lucifer.weatherinfo.WeatherInfoHelpers.DetailsClass;

import java.util.List;

/**
 * Created by Shodhan on 12/11/2015.
 */
public class ScreenHandler extends Handler
{

    private MainActivity mContext;
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private List<ExpandableListAdapter.Item> mData = null;
    private ExpandableListAdapter mExpandableListAdapter ;
    private ProgressDialog mLodingDialog;
    private DetailsClass mDetailsClass;


    public  ScreenHandler(MainActivity inContext, RecyclerView inRecyclerView,
                           List<ExpandableListAdapter.Item> inData,ExpandableListAdapter inExpandableListAdapter, DetailsClass inDetailsClass)
    {
        mContext = inContext;
        mRecyclerView = inRecyclerView;
        mData = inData;
        mExpandableListAdapter = inExpandableListAdapter;
        mDetailsClass = inDetailsClass;
    }

    @Override
    public void handleMessage(Message msg)
    {

        switch (msg.what)
        {
            case Constants.kmiHANDLER_DATA_READY :    ((MainActivity)mContext).PopulateRecyclerView();

                                                   break;
            case Constants.kmiHANDLER_UI_READY :

                                                   break;

            case Constants.kmiHANDLER_SHOW_PROGRESS_DIALOG: ShowPrgogressDialog ();
                                                            break;
            case Constants.kmiHANDLER_UPDATE_PROGRESS :
                                                         if (mLodingDialog.isShowing())
                                                         {
                                                             mLodingDialog.setProgress(mLodingDialog.getProgress()+20);
                                                         }
                                                         break;

            case Constants.kmiHANDLER_ERROR_FETCHING:
                                                       Toast.makeText(mContext, "Error Fetching Results .Please try later", Toast.LENGTH_LONG).show();
                                                       DismissDialog ();
                                                       break;


            default: break;
        }


    }



     public void ShowPrgogressDialog ()
     {
         mLodingDialog =new ProgressDialog(mContext);
         mLodingDialog.setMessage("Fetching Data...");
         mLodingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         mLodingDialog.setIndeterminate(false);
         mLodingDialog.setMax(100);
         mLodingDialog.setProgress(0);
         mLodingDialog.setCanceledOnTouchOutside(false);
         mLodingDialog.show();
     }

     public void DismissDialog ()
     {
         if (mLodingDialog != null)
         {
             if (mLodingDialog.isShowing())
                   mLodingDialog.dismiss();

         }
     }

    }
