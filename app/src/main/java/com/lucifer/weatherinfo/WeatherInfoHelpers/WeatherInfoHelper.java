package com.lucifer.weatherinfo.WeatherInfoHelpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shodhan on 12/12/2015.
 */
public class WeatherInfoHelper
{


    public static String ConvertKelvinToCelsius (String inTemperature)
    {
       float  temp = Float.parseFloat(inTemperature);
        temp = temp - 273.15f;
        return String.valueOf(Math.round(temp))+ (char) 0x00B0 +"C";

    }

    public static String GetTimeAndDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
