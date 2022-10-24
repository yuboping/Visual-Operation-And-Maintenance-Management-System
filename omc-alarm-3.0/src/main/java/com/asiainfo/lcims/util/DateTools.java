package com.asiainfo.lcims.util;

import java.sql.Timestamp;
import java.util.Calendar;

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
    public static final String suffixes = "MM_dd";
    private DateTimeFormatter dtf;
    public static final DateTools DEFAULT = new DateTools(DEFAULT_FORMAT);

    public DateTools(String format) {
        this.dtf = DateTimeFormat.forPattern(format);
    }

    /**
     * 获取传入的指定日期的毫秒数
     * 
     * @param date
     * @return
     */
    public long getFormatTime(String date) {
        DateTime dateTime = DateTime.parse(date, dtf);
        return dateTime.getMillis();
    }

    /**
     * 获取知道月的最后一个工作日是多少号
     * 
     * @param month
     * @param flag
     *            是否跨年了
     * @return
     */
    public int getLastWeekdays(int month, int year) {
        int week = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// 获取该月最大一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        week = cal.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1;// 获得最后一天是星期几
        if (week == 7) {
            lastDay = lastDay - 2;
        } else if (week == 6) {
            lastDay = lastDay - 1;
        }

        return lastDay;
    }

    /**
     * 获取月的倒数第三天
     * 
     * @param month
     * @param year
     * @return
     */
    public int getLastButTwoDay(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int lastButTwoDay = lastDay - 2;
        return lastButTwoDay;
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

    public static String getCurrentDate(String format) {
        DateTime date = new DateTime();
        return date.toString(format);
    }

    public String getSuffixes(String date) {
        DateTime datetime = dtf.parseDateTime(date);
        return datetime.toString(suffixes);
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
     * 返回当前分钟数
     * 
     * @return
     */
    public String getCurrentMin() {
        DateTimeFormatter d = DateTimeFormat.forPattern("mm");
        DateTime date = new DateTime();
        return date.toString(d);
    }

    /**
     * 返回当前小时
     * 
     * @return
     */
    public String getCurrentHour() {
        DateTimeFormatter d = DateTimeFormat.forPattern("HH");
        DateTime date = new DateTime();
        return date.toString(d);
    }

    // 返回两个时间天数
    /**
     * 返回格式：1days, 09:28:00
     * 
     * @param firstTime
     * @param format1
     * @param currentTime
     * @param format2
     * @return
     */
    public String differentDays(String firstTime, String format1, String currentTime,
            String format2) {
        DateTimeFormatter df_1 = DateTimeFormat.forPattern(format1);
        ;
        DateTimeFormatter df_2 = DateTimeFormat.forPattern(format2);
        ;
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
