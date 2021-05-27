package com.hills.mcs_02;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat getSimpleDateFormat(){
        return SIMPLE_DATE_FORMAT;
    }

    /** str must be yyyy-MM-dd HH:mm:ss */
    public static Date string2Date (String str) throws ParseException {
        return SIMPLE_DATE_FORMAT.parse(str);
    }

    /** str must be yyyy-MM-dd HH:mm:ss */
    public static long string2TimeStamp(String str) throws ParseException {
        return SIMPLE_DATE_FORMAT.parse(str).getTime();
    }
}
