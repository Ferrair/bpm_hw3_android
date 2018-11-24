package com.example.qihang.bpm_hw3.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by qihang on 2018/11/24.
 */

public class TimeUtils {
    public static String timestamp2String(long timestamp) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(timestamp);
    }
}
