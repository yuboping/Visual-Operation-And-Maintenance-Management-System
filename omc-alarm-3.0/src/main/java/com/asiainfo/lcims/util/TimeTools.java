package com.asiainfo.lcims.util;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeTools {
    public static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmmss");
    public static Timestamp string2Timestamp(String time) {
        DateTime date = FORMAT.parseDateTime(time);
        return new Timestamp(date.getMillis());
    }

    public static String getTime(String format) {
        DateTime date = new DateTime();
        return date.toString(format);
    }
    
    public static String getTime() {
        DateTime date = new DateTime();
        return date.toString(FORMAT);
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String timestamp2String(Timestamp time) {
        DateTime date = new DateTime(time.getTime());
        return date.toString(FORMAT);
    }

    public static String timestamp2String(Timestamp time, String format) {
        DateTime date = new DateTime(time.getTime());
        return date.toString(format);
    }

    /**
     * 计算时间范围（间隔：分钟）
     * 
     * @param cycle
     * @param format
     * @return
     */
    public static String[] countScanDir(int cycle, String format) {
        String[] dirs = null;
        DateTime now = new DateTime();
        DateTime before = now.minusMinutes(cycle);
        String start = before.toString(format);
        String end = now.toString(format);
        if (start.equals(end)) {
            dirs = new String[] { start };
        } else {
            dirs = new String[] { start, end };
        }
        return dirs;
    }

    /**
     * 计算cycle天前的日期
     * 
     * @param cycle
     * @param format
     * @return
     */
    public static String minusDays(int cycle, String format) {
        DateTime now = new DateTime();
        DateTime before = now.minusDays(cycle);
        return before.toString(format);
    }

    /**
     * 校验时间字符串格式是否符合format
     * 
     * @param time
     * @param format
     * @return
     */
    public static boolean checkTimestr(String time, String format) {
        DateTimeFormatter df = DateTimeFormat.forPattern(format);
        try {
            df.parseDateTime(time);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static String getYesterdayTime(String format) {
        DateTime date = new DateTime();
        date = date.plusDays(-1);
        return date.toString(format);
    }
}
