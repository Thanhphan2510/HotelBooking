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

    public static String covertDateToString(String date) throws ParseException {

        SimpleDateFormat spf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
        Date newDate=spf.parse(date);
        spf= new SimpleDateFormat("dd/MM/yyyy");
        date = spf.format(newDate);
        return date;
    }

    public static String UpString(String stringfromclient){
//        String stringfromclient = "what happen in your class?";
        //cắt string thành mảng qua các dấu Space
        String[] arr = stringfromclient.split(" ");
        //dùng vòng lặp duyệt các từ và thay thế từ đầu tiên!
        String stringfromclient1 = "";
        for (String x : arr) {
            stringfromclient1 = stringfromclient1 + (x.substring(0, 1).toUpperCase() + x.substring(1));
            stringfromclient1 = stringfromclient1 + " ";
        }
        return stringfromclient1;
    }



}
