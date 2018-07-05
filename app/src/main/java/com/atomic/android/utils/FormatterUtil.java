
package com.atomic.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import com.atomic.android.R;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

public class FormatterUtil {

    public static String firebaseDBDate = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String firebaseDBDay = "yyyy-MM-dd";
    public static final long NOW_TIME_RANGE = DateUtils.MINUTE_IN_MILLIS * 5; // 5 minutes

    public static ArrayList<String> countries;
    public static ArrayList<String> opportunity;
    public static String dateTime = "yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat getFirebaseDateFormat() {
        SimpleDateFormat cbDateFormat = new SimpleDateFormat(firebaseDBDate);
        cbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cbDateFormat;
    }

    public static CharSequence getRelativeTimeSpanString(Context context, long time) {
        long now = System.currentTimeMillis();
        long range = Math.abs(now - time);

        if (range < NOW_TIME_RANGE) {
            return context.getString(R.string.now_time_range);
        }

        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
    }

    public static CharSequence getRelativeTimeSpanStringShort(Context context, long time) {
        long now = System.currentTimeMillis();
        long range = Math.abs(now - time);
        return formatDuration(context, range, time);
    }

    private static CharSequence formatDuration(Context context, long range, long time) {
        final Resources res = context.getResources();
        if (range >= DateUtils.WEEK_IN_MILLIS + DateUtils.DAY_IN_MILLIS) {
            return shortFormatEventDay(context, time);
        } else if (range >= DateUtils.WEEK_IN_MILLIS) {
            final int days = (int) ((range + (DateUtils.WEEK_IN_MILLIS / 2)) / DateUtils.WEEK_IN_MILLIS);
            return String.format(res.getString(R.string.duration_week_shortest), days);
        } else if (range >= DateUtils.DAY_IN_MILLIS) {
            final int days = (int) ((range + (DateUtils.DAY_IN_MILLIS / 2)) / DateUtils.DAY_IN_MILLIS);
            return String.format(res.getString(R.string.duration_days_shortest), days);
        } else if (range >= DateUtils.HOUR_IN_MILLIS) {
            final int hours = (int) ((range + (DateUtils.HOUR_IN_MILLIS / 2)) / DateUtils.HOUR_IN_MILLIS);
            return String.format(res.getString(R.string.duration_hours_shortest), hours);
        } else if (range >= NOW_TIME_RANGE) {
            final int minutes = (int) ((range + (DateUtils.MINUTE_IN_MILLIS / 2)) / DateUtils.MINUTE_IN_MILLIS);
            return String.format(res.getString(R.string.duration_minutes_shortest), minutes);
        } else {
            return res.getString(R.string.now_time_range);
        }
    }

    private static String shortFormatEventDay(Context context, long time) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;
        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        return DateUtils.formatDateRange(context, f, time, time, flags).toString();
    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("đ", "d");
        s = s.replaceAll("Đ", "D");
        return s;
    }

    public static String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static String getDateWithDateFormat(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static ArrayList<String> getListCountry(Context context)
    {

        countries = new ArrayList<String>();
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("en", countryCode);
            String countryName = locale.getDisplayCountry( new Locale("en", "US") );
            countries.add(countryName);
        }
        Collections.sort(countries);
        countries.add( 0,context.getResources().getString(R.string.General_ChooseYourCountry) );
        return countries;
    }

}
