package com.uf.dancemarathon;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chris on 1/14/2016.
 */
public class TimeUtility {

    public static int MILLISECOND = 1;
    public static int SECOND = 1000;
    public static int MINUTE = 60 * SECOND;
    public static int HOUR = 60 * MINUTE;
    public static int DAY = 24 * HOUR;
    public static int YEAR = 365 * DAY;

    public static double getTimeDifference(Date d1, Date d2, int timeField)
    {
        double diff = d1.getTime() - d2.getTime();
        return diff / timeField;
    }
}
