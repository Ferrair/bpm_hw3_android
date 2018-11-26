package com.example.qihang.bpm_hw3.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qihang on 2018/11/24.
 */

public class TimeUtils {
    public static String timestamp2String(long timestamp) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(timestamp * 1000);
    }

    public static long string2Timestamp(String time) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d;
        try {
            d = sdf.parse(time);
            return d.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
