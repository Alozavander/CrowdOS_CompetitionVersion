package com.hills.mcs_02.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SQLiteTimeUtil {
    public static String[] getStartandEndTime(){
        //get the specific pattern current time.
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        //补足0
        String endTime = "";
        String startTime = year + "-";
        month++;
        if (month <10) startTime += "0"+month +"-";
        else startTime+= month +"-";
        if (day <10) startTime += "0"+day + " ";
        else startTime += day + " ";

        endTime = startTime;
        hour--;
        if (hour<10) startTime += "0"+hour+":";
        else startTime += hour+":";
        hour++;
        if (hour<10) endTime += "0"+hour+":";
        else endTime += hour+":";
        if (minute<10) {
            startTime+="0"+minute+":";
            endTime+="0"+minute+":";
        }
        else {
            startTime+=minute+":";
            endTime+=minute+":";
        }
        if (second<10) {
            endTime+="0"+second;
            startTime+="0"+second;
        }
        else {
            startTime+=second;
            endTime+=second;
        }
        return new String[]{startTime,endTime};
    }

    public static String getCurrentTimeNoSpaceAndMaoHao(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat lFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        return lFormat.format(date);
    }

}
