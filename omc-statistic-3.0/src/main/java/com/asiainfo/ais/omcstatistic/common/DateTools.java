package com.asiainfo.ais.omcstatistic.common;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * 根据已知时间返回特定格式
     * 
     */
    public String getFormatDay(String date, String returFormat) {
        DateTime datetime = dtf.parseDateTime(date);
        DateTimeFormatter returnDtf = DateTimeFormat.forPattern(returFormat);
        return datetime.toString(returnDtf);
    }

}
