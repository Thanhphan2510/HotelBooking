package com.example.hotelbooking.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MyUntil {
    public static long covertDatetoTimeStamp(String str_date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = (Date)formatter.parse(str_date);
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        return timestamp;
    }
    public static Date covertStringtoDate(String str_date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(str_date);
        return date;
    }



}
