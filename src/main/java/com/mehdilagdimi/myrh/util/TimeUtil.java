package com.mehdilagdimi.myrh.util;


import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;

public abstract class TimeUtil {
    public static Timestamp stringToTimestamp (String date) {
        if (StringUtils.countMatches(date, ":") == 1) {
            date += ":00";
        }
        Timestamp timestamp = Timestamp.valueOf(date.replace("T", " "));
        return timestamp;
    }
    public static String timestampToString (Timestamp timestamp) {
        String timestampStr ="";
//        if (StringUtils.countMatches(timestamp.toString(), "T") == 1) {
//            timestampStr = timestamp.toString().replace("T", " ");
//
//        }
        return timestampStr.split(".")[0];
    }
    public static String timestampToHtmlDateTimeLocal (String date) {
        if (StringUtils.countMatches(date, ":") == 1) {
            date += ":00";
        }
        return date.replace(" ", "T").split("\\.")[0];
    }

}
