package com.asiainfo.lcims.omc.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.TimeCell;
import com.asiainfo.lcims.omc.param.common.CommonInit;

/**
 * 时间，表名后缀等统一管理
 * 
 * @author luohuawuyin
 *
 */
public class TimeControl {
    private static final String FORMAT = "yyyy-MM-dd HH:mm";
    private static final DateTools DATATOOL = new DateTools(FORMAT);

    private static final String FORMAT_DAY = "yyyy-MM-dd";
    private static final DateTools DATATOOL_DAY = new DateTools(FORMAT_DAY);
    private static final String FORMAT_MONTH = "yyyy-MM";
    private static final DateTools DATATOOL_MONTH = new DateTools(FORMAT_MONTH);

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String LASTTIME = lasttime();// 最近一次监控时间
    private static final String TBSUFFIXES = getSuffixes(LASTTIME);// 最近一次监控数据所在表后缀名
    private static final TimeCell twelvehour = allin12hourSuffixes();
    private static final TimeCell fivemin = pre5minSuffixes();
    private static final TimeCell threeday = avgindaysSuffixes();
    private static final TimeCell yesterday = yesterdaySuffixes();

    public static String lasttime() {
        String lasttime = null;
        String time = DATATOOL.getCurrentDate();
        if (Integer.parseInt(time.substring(time.length() - 1)) >= 5) {
            lasttime = time.substring(0, time.length() - 1) + "5";
        } else {
            lasttime = time.substring(0, time.length() - 1) + "0";
        }
        return lasttime;
    }

    /**
     * 昨天同一时间
     * 
     * @return
     */
    private static TimeCell yesterdaySuffixes() {
        TimeCell cell = new TimeCell();
        cell.setEndtime(LASTTIME);
        cell.setStarttime(DATATOOL.getOffsetDay(-1, LASTTIME));
        String startday = getSuffixes(cell.getStarttime());
        List<String> days = new ArrayList<String>();
        days.add(startday);
        days.add(TBSUFFIXES);
        cell.setDays(days);
        return cell;
    }

    /**
     * 获取监控记录表表名日期后缀
     * 
     * @param time
     * @return
     */
    private static String getSuffixes(String time) {
        if (time != null && time.length() == FORMAT.length()) {
            String suffixes = time.substring(5, 7) + "_" + time.substring(8, 10);
            return suffixes;
        }
        return time;
    }

    /**
     * 三天内
     * 
     * @return
     */
    private static TimeCell avgindaysSuffixes() {
        TimeCell cell = new TimeCell();
        cell.setEndtime(LASTTIME);
        List<String> days = new ArrayList<String>();
        for (int i = 3; i > 0; i--) {
            String time = DATATOOL.getOffsetDay(-i, LASTTIME);
            if (i == 3) {
                cell.setStarttime(time);
            }
            days.add(getSuffixes(time));
        }
        days.add(TBSUFFIXES);
        cell.setDays(days);
        return cell;
    }

    /**
     * 12小时以内
     * 
     * @return
     */
    private static TimeCell allin12hourSuffixes() {
        TimeCell cell = new TimeCell();
        cell.setEndtime(LASTTIME);
        cell.setStarttime(DATATOOL.getOffsetHour(-12, LASTTIME));
        String startday = getSuffixes(cell.getStarttime());
        String endday = TBSUFFIXES;
        List<String> days = new ArrayList<String>();
        days.add(startday);
        if (!endday.equals(startday)) {// 开始和结束时间不在同一天
            days.add(endday);
        }
        cell.setDays(days);
        return cell;
    }

    /**
     * 5分钟前
     * 
     * @return
     */
    private static TimeCell pre5minSuffixes() {
        TimeCell cell = new TimeCell();
        cell.setEndtime(LASTTIME);
        cell.setStarttime(DATATOOL.getOffsetMin(-5, LASTTIME));
        String startday = getSuffixes(cell.getStarttime());
        List<String> days = new ArrayList<String>();
        days.add(startday);
        cell.setDays(days);
        return cell;
    }

    public static String getFiveMinTime(int addNum) {
        String lasttime = null;
        String time = DATATOOL.getOffsetMin(addNum);
        if (Integer.parseInt(time.substring(time.length() - 1)) >= 5) {
            lasttime = time.substring(0, time.length() - 1) + "5";
        } else {
            lasttime = time.substring(0, time.length() - 1) + "0";
        }
        return lasttime;
    }

    public static String getOneHourTime(int addNum) {
        String time = DATATOOL.getOffsetHour(addNum);
        return time.substring(0, time.length() - 2) + "00";
    }

    public static String getOneDayTime(int addNum) {
        String time = DATATOOL.getOffsetDay(addNum);
        return time.substring(0, time.length() - 5) + "23:55";
    }

    public static String getLasttime() {
        return LASTTIME;
    }

    public static String getTbsuffixes() {
        return TBSUFFIXES;
    }

    public static TimeCell getTwelvehour() {
        return twelvehour;
    }

    public static TimeCell getFivemin() {
        return fivemin;
    }

    public static TimeCell getThreeday() {
        return threeday;
    }

    public static TimeCell getYesterday() {
        return yesterday;
    }

    /**
     * 获取周期时间
     * 
     * @param cycle周期类型
     * @param cycleNum
     *            周期数 0：当前周期 ，-1：上个周期，1：下个周期
     * @return
     */
    public static String getCycleTime(String cycle, int cycleNum, Integer cycleId,
            String queryDate) {
        int ct = 5;
        String cron = null;
        String lasttime = "";
        switch (cycle) {
        case "1min":
            ct = 1;
            break;
        case "5min":
            ct = 5;
            break;
        case "10min":
            ct = 10;
            break;
        case "15min":
            ct = 15;
            break;
        case "20min":
            ct = 20;
            break;
        case "30min":
            ct = 30;
            break;
        case "1hour":
            ct = 60;
            cron = CommonInit.getCollCycleCronById(cycleId);
            // 单独写
            lasttime = getCycleTime1Hour(cron, cycleNum, queryDate);
            break;
        case "1day":
            cron = CommonInit.getCollCycleCronById(cycleId);
            lasttime = getCycleTime1Day(cron, cycleNum);
            break;
        case "1month":
            cron = CommonInit.getCollCycleCronById(cycleId);
            lasttime = getCycleTime1month(cron, cycleNum);
            break;
        default:
            ct = 5;
            break;
        }
        if (ToolsUtils.StringIsNull(lasttime)) {
            String time = DATATOOL.getOffsetMin(ct * cycleNum);
            // 1分钟的不需要处理时间
            if (ct == 1) {
                if (!ToolsUtils.StringIsNull(queryDate)
                        && !DATATOOL.getCurrentDate().contains(queryDate)) {
                    time = DATATOOL.getOffsetMin(ct * cycleNum, queryDate + " 23:59");
                }
                return time;
            }
            if (!ToolsUtils.StringIsNull(queryDate)
                    && !DATATOOL.getCurrentDate().contains(queryDate)) {
                time = DATATOOL.getOffsetMin(ct * cycleNum, queryDate + " 23:55");
            }
            String[] timeArr = time.split(":");
            int min = Integer.parseInt(timeArr[1]);
            int result = min / ct;
            lasttime = timeArr[0] + ":";
            if (result == 0) {
                lasttime = lasttime + "00";
            } else {
                lasttime = result == 1 && ct == 5 ? lasttime + "0" + ct : lasttime + result * ct;
            }
        }
        return lasttime;
    }

    private static String getCycleTime1Day(String cron, int cycleNum) {
        if (ToolsUtils.StringIsNull(cron))
            return "";
        String[] crons = cron.split(" ");
        String lasttime = DATATOOL_DAY.getOffsetMin(0) + " " + crons[2] + ":" + crons[1];
        lasttime = DATATOOL.getOffsetDay(cycleNum, lasttime);
        return lasttime;
    }

    private static String getCycleTime1month(String cron, int cycleNum) {
        if (ToolsUtils.StringIsNull(cron))
            return "";
        String[] cronM = cron.split(" ");
        String lasttime = DATATOOL_MONTH.getOffsetMin(0) + "-" + cronM[3] + " " + cronM[2] + ":"
                + cronM[1];
        lasttime = DATATOOL.getOffsetMonth(cycleNum, lasttime);
        return lasttime;
    }

    private static String getCycleTime1Hour(String cron, int cycleNum, String queryDate) {
        if (ToolsUtils.StringIsNull(cron))
            return "";
        String[] crons = cron.split(" ");
        String lasttime = DATATOOL.getOffsetMin(0);
        String currentMin = DATATOOL.getCurrentMin();
        if (Integer.parseInt(crons[1]) > Integer.parseInt(currentMin)) {
            // 取上个小时数
            lasttime = DATATOOL.getOffsetMin(-60);
        }
        if (!ToolsUtils.StringIsNull(queryDate) && !lasttime.startsWith(queryDate)) {
            lasttime = queryDate + " 23:" + fillParam(crons[1]);
        } else {
            String[] timeArr = lasttime.split(":");
            lasttime = timeArr[0] + ":" + fillParam(crons[1]);
        }
        lasttime = DATATOOL.getOffsetHour(cycleNum, lasttime);
        return lasttime;
    }

    private static String fillParam(String param) {
        if (param.length() == 1) {
            return "0" + param;
        }
        return param;
    }

    /**
     * 获取一天时间内周期时间断 HH:mm
     * 
     * @param cycle周期类型
     * @return
     */
    public static List<String> getCycleTimeOneDayHHmm(String cycle, String format, String queryDate,
            Integer cycleId) {
        List<String> days = new ArrayList<String>();
        // yyyy-MM-dd HH:mm
        String time = getCycleTime(cycle, 0, cycleId, queryDate);
        days.add(DATATOOL.getFormatDay(time, format));
        int ct = 0;
        String cron = CommonInit.getCollCycleCronById(cycleId);
        String[] cronArr = cron.split(" ");
        String break_flag = "00:00";
        switch (cycle) {
        case "1min":
            ct = 1;
            break;
        case "5min":
            ct = 5;
            break;
        case "10min":
            ct = 10;
            break;
        case "15min":
            ct = 15;
            break;
        case "20min":
            ct = 20;
            break;
        case "30min":
            ct = 30;
            break;
        case "1hour":
            ct = 60;
            break_flag = "00:" + fillParam(cronArr[1]);
            break;
        case "day":
            ct = 60;
            break_flag = fillParam(cronArr[2]) + ":" + fillParam(cronArr[1]);
            break;
        default:
            break;
        }
        int i = -1;
        String datetime = null;
        while (true) {
            datetime = DATATOOL.getOffsetMin(ct * i, time);
            days.add(DATATOOL.getFormatDay(datetime, format));
            if (datetime.contains(break_flag)) {
                break;
            }
            i = i - 1;

        }
        Collections.reverse(days);
        return days;
    }

    /**
     * 根据给定的时间获取相同时钟与分钟的所有月数
     * 
     * @param dateTime
     * @param cronExpress
     * @return
     */
    public static List<String> getEveryMonth(String dateTime, String cronExpress) {
        List<String> allMonth = new ArrayList<>();
        allMonth.add(dateTime);
        String formatMonth = DATATOOL.getFormatDay(dateTime, "yyyy-01-dd HH:mm");
        int i = -1;
        while (!allMonth.contains(formatMonth)) {
            String month = DATATOOL.getOffsetMonth(i, dateTime);
            allMonth.add(month);
            i = i - 1;
        }
        Collections.reverse(allMonth);
        return allMonth;
    }

    public static String chineseMonth(String mark) {
        String month = DATATOOL.getFormatDay(mark, "MM");
        String chineseMonth = month + "月";
        return chineseMonth;
    }

    /**
     * 返回表日期后缀
     * 
     * @param date
     * @return
     */
    public static String getTbsuffixes(String date) {
        if (date == null || date.length() < 10) {
            date = TimeTools.getCurrentTime("yyyy-MM-dd");
        }
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return month + "_" + day;
    }

    /**
     * 根据cron表达式获取时间
     * 
     * @param cronExpress
     * @return
     */
    public static String cronDateTime(String cronExpress, String queryDate) {
        String dateTime = queryDate;
        if (StringUtils.isBlank(dateTime)) {
            dateTime = TimeTools.getCurrentTime("yyyy-MM-dd");
        }
        String[] cronExpressArr = cronExpress.split(" ");
        String dayTimeStr = cronExpressArr[3];
        String hourTime = cronExpressArr[2];
        String secondTime = cronExpressArr[1];
        String day = dayTimeStr.length() == 1 ? "0" + dayTimeStr : dayTimeStr;
        String hour = hourTime.length() == 1 ? "0" + hourTime : hourTime;
        String second = secondTime.length() == 1 ? "0" + secondTime : secondTime;
        int dayOfMonth = DATATOOL_DAY.getDayOfMonth(dateTime);
        int dayTime = Integer.parseInt(dayTimeStr);
        if (dayOfMonth > dayTime) {
            dateTime = dateTime.substring(0, 8) + day + " " + hour + ":" + second;
        } else {
            String offsetMonth = DATATOOL_DAY.getOffsetMonth(-1, dateTime);
            dateTime = offsetMonth.substring(0, 8) + day + " " + hour + ":" + second;
        }
        return dateTime;
    }

    public static String tableSuffixName(String dateTime) {
        String suffixName = DATATOOL.getFormatDay(dateTime, "MM_dd");
        return suffixName;
    }

    public static List<String> getCutTimeStamp(Date date, int auto_date,
            long time_stamp_difference) {
        List<String> resultList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long min = calendar.get(Calendar.MINUTE);
        long coefficient = time_stamp_difference / 60;
        long minOrder = (min / coefficient) * coefficient;
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int minOrderint = (int) minOrder;
        calendar.add(Calendar.MINUTE, minOrderint);
        Date dateEnd = calendar.getTime();
        calendar.add(Calendar.DATE, -(auto_date));
        Date dateStart = calendar.getTime();
        while (dateStart.compareTo(dateEnd) < 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(dateStart);
            int time_stamp_difference_int = (int) time_stamp_difference;
            c.add(Calendar.SECOND, time_stamp_difference_int);
            dateStart = c.getTime();
            resultList.add(date2String(dateStart));
        }
        return resultList;
    }

    /**
     * Date型转string
     * 
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        return long2str(date.getTime(), DEFAULT_FORMAT);
    }

    /**
     * long型日期转成格式化字符串
     * 
     * @param date
     * @param format
     * @return
     */
    public static String long2str(long date, String format) {
        LocalDateTime now = LocalDateTime.ofEpochSecond(date / 1000, 0, ZoneOffset.ofHours(8));
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return now.format(df);
    }

    // public static void main(String[] args) {
    // // String dateTime = cronDateTime("0 45 16 12 * ?", "2019-12-12");
    // // System.out.println(dateTime);
    // // List<String> s = getCutTimeStamp(new Date(), 1, 30 * 60);
    // // System.out.println(s.toString());
    // // String cron_erp = "{}";
    // // int e = cron_erp.length();
    // // String min_rate = cron_erp.substring(1, cron_erp.length() - 1);
    // // MD5密码加盐值
    // try {
    // long startTime = System.nanoTime();
    // PwdDES3 pwd = new PwdDES3();
    // String password = "powieudgsa123456789012345678901234567890secret"
    // + Constant.PASSWORD_SALT;
    // System.out.println(password);
    // String encryptPassword = pwd.encryptPassword(password);
    // System.out.println(encryptPassword);
    // String decryptpwd = pwd.decryptPassword(encryptPassword);
    // System.out.println(decryptpwd);
    //
    // String min_rate = decryptpwd.substring(0,
    // decryptpwd.length() - Constant.PASSWORD_SALT.length());
    // System.out.println(min_rate);
    // if (min_rate.equals("powieudgsa123456789012345678901234567890secret")) {
    // System.out.println("true");
    // }
    // long endTime = System.nanoTime();
    // long executeTime = endTime - startTime;
    // double seconds = (double) executeTime / 1000000000.0;
    // seconds = (double) Math.round(seconds * 10000) / 10000;
    // String callTime = String.valueOf(seconds);
    // System.out.println(callTime);
    // // // MD5密码加盐值
    // // password = password + Constant.PASSWORD_SALT;
    // // String encryptPassword = Password.encryptPassword(password, 1);
    // } catch (PasswordException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
}
