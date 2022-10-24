package com.asiainfo.lcims.omc.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 日期计算工具（默认格式yyyyMM）
 * 
 * @author qinwoli
 * 
 */
public class DateTools {
    /** 默认的时间格式，格式为：{@value} */
    public static final String DEFAULT_FORMAT = "yyyyMM";

    private DateTimeFormatter dtf;
    public static final DateTools DEFAULT = new DateTools(DEFAULT_FORMAT);

    public DateTools(String format) {
        this.dtf = DateTimeFormat.forPattern(format);
    }
    
    /**
     * 获取DEFAULT_FORMAT格式的当前时间
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getCurrentDate() {
        DateTime date = new DateTime();
        return date.toString(dtf);
    }
    
    public String getCurrentDateHHMM(){
    	DateTimeFormatter df = DateTimeFormat.forPattern("HH:mm");
    	DateTime date = new DateTime();
    	return date.toString(df);
    }
    
    /**
     * 获取
     * @return
     */
    public static String getCurrentFormatDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取上个月时间
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getLastMonth() {
        DateTime lastdate = new DateTime().plusMonths(-1);
        return lastdate.toString(dtf);
    }

    /**
     * 获取昨天
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getLastDay() {
        DateTime lastdate = new DateTime().plusDays(-1);
        return lastdate.toString(dtf);
    }

    /**
     * 按月相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetMonth(int offset) {
        DateTime date = new DateTime().plusMonths(offset);
        return date.toString(dtf);
    }

    /**
     * 根据已知时间按月相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetMonth(int offset, String date) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTime offsettime = datetime.plusMonths(offset);
        return offsettime.toString(dtf);
    }

    /**
     * 按日相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetDay(int offset) {
        DateTime date = new DateTime().plusDays(offset);
        return date.toString(dtf);
    }

    /**
     * 根据已知时间按日相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetDay(int offset, String date) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTime offsettime = datetime.plusDays(offset);
        return offsettime.toString(dtf);
    }
    
    /**
     * 根据已知时间按小时相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetHour(int offset, String date) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTime offsettime = datetime.plusHours(offset);
        return offsettime.toString(dtf);
    }
    
    /**
     * 返回当前分钟数
     * @return
     */
    public String getCurrentMin(){
        DateTimeFormatter d =  DateTimeFormat.forPattern("mm");
        DateTime date = new DateTime();
        return date.toString(d);
    }
    
    /**
     * 返回当前小时数
     * 
     * @return
     */
    public String getCurrentHour() {
        DateTimeFormatter d = DateTimeFormat.forPattern("HH");
        DateTime date = new DateTime();
        return date.toString(d);
    }

    /**
     * 返回当前天数
     * 
     * @return
     */
    public String getCurrentDay() {
        DateTimeFormatter d = DateTimeFormat.forPattern("dd");
        DateTime date = new DateTime();
        return date.toString(d);
    }

    /**
     * 根据已知时间按小时相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetHour(int offset) {
        DateTime offsettime = new DateTime().plusHours(offset);
        return offsettime.toString(dtf);
    }

    /**
     * 按分钟相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetMin(int offset) {
        DateTime date = new DateTime().plusMinutes(offset);
        return date.toString(dtf);
    }
    
    /**
     * 按分钟相加减
     * 
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public String getOffsetMin(int offset, String date) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTime offsettime = datetime.plusMinutes(offset);
        return offsettime.toString(dtf);
    }

    public String time2String(Timestamp time) {
        DateTime date = new DateTime(time.getTime());
        return date.toString(dtf);
    }

    /**
     * 获取一个月当中的天数
     * 
     * @param date
     * @return
     */
    public int getDayOfMonth(String date) {
        DateTime dateTime = DateTime.parse(date, dtf);
        int day = dateTime.getDayOfMonth();
        return day;
    }

    /**
     * 根据已知时间返回特定格式
     * 
     */
    public String getFormatDay(String date, String returFormat) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTimeFormatter returnDtf = DateTimeFormat.forPattern(returFormat);
        return datetime.toString(returnDtf);
    }

    // 返回两个时间天数
    public String differentDays(String firstTime, String format1, String currentTime,
            String format2) {
        DateTimeFormatter df_1 = DateTimeFormat.forPattern(format1);
        DateTimeFormatter df_2 = DateTimeFormat.forPattern(format2);
        DateTime dateTime_1 = DateTime.parse(firstTime, df_1);
        DateTime dateTime_2 = DateTime.parse(currentTime, df_2);
        long diffMills = dateTime_2.getMillis() - dateTime_1.getMillis();
        long days = diffMills / (1000 * 3600 * 24);
        if (days == 0l) {
            days = 1l;
        }
        String time = currentTime.substring(11, currentTime.length());
        if (time.length() < 8) {
            time = time + ":00";
        }
        return days + "days, " + time;
    }

}
