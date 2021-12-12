package com.example.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String nowDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        String d = sdf.format(date);
        return d;
    }

}
