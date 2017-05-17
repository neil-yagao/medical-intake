package com.neil.medical.util;

import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by nhu on 5/16/2017.
 */
public class TimeUtil {

    public static JSONObject parseTimespan(String timespan){
        String[] times = timespan.split(":");
        JSONObject timespanRange = new JSONObject().fluentPut("$gte", Long.parseLong(times[0]));
        if (times.length > 1) {
            timespanRange.put("$lte", Long.parseLong(times[1]));
        }
        return timespanRange;
    }

    public static String getCurrentDate(){
       return getCurrentDateFormat("yyyy-MM-dd");
    }

    public static String getCurrentDateFormat(String format){
        LocalDateTime datetime = LocalDateTime.now();
        return datetime.format(DateTimeFormatter.ofPattern(format));
    }
}
