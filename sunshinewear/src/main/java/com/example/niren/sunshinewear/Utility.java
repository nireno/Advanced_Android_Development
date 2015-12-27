/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.niren.sunshinewear;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static final String DATE_FORMAT = "yyyyMMdd";

    static String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return DateFormat.getDateInstance().format(date);
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param dateInMillis The date in milliseconds
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(long dateInMillis) {
        // Use the form "Mon Jun 3"
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
        return shortenedDateFormat.format(dateInMillis);
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return a user-friendly representation of the date.
     */
    public static String getFullFriendlyDayString(Context context, long dateInMillis) {

        String day = getDayName(context, dateInMillis);
        int formatId = R.string.format_full_friendly_date;
        return String.format(context.getString(
                formatId,
                day,
                getFormattedMonthDay(context, dateInMillis)));
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        Time time = new Time();
        time.setToNow();
        // Otherwise, the format is just the day of the week (e.g "Wednesday".
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        return dayFormat.format(dateInMillis);
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }

//    /**
//     * Helper method to provide the art urls according to the weather condition id returned
//     * by the OpenWeatherMap call.
//     *
//     * @param context Context to use for retrieving the URL format
//     * @param weatherId from OpenWeatherMap API response
//     * @return url for the corresponding weather artwork. null if no relation is found.
//     */
//    public static String getArtUrlForWeatherCondition(Context context, int weatherId) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String formatArtUrl = prefs.getString(context.getString(R.string.pref_art_pack_key),
//                context.getString(R.string.pref_art_pack_sunshine));
//
//        // Based on weather code data found at:
//        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
//        if (weatherId >= 200 && weatherId <= 232) {
//            return String.format(Locale.US, formatArtUrl, "storm");
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return String.format(Locale.US, formatArtUrl, "light_rain");
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return String.format(Locale.US, formatArtUrl, "rain");
//        } else if (weatherId == 511) {
//            return String.format(Locale.US, formatArtUrl, "snow");
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return String.format(Locale.US, formatArtUrl, "rain");
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return String.format(Locale.US, formatArtUrl, "snow");
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return String.format(Locale.US, formatArtUrl, "fog");
//        } else if (weatherId == 761 || weatherId == 781) {
//            return String.format(Locale.US, formatArtUrl, "storm");
//        } else if (weatherId == 800) {
//            return String.format(Locale.US, formatArtUrl, "clear");
//        } else if (weatherId == 801) {
//            return String.format(Locale.US, formatArtUrl, "light_clouds");
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return String.format(Locale.US, formatArtUrl, "clouds");
//        }
//        return null;
//    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String formatTemperature(Context context, double temperature) {
        return String.format(context.getString(R.string.format_temperature), temperature);
    }
   }