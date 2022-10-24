package com.asiainfo.ais.omcstatistic.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asiainfo.ais.omcstatistic.pojo.TimeCell;

/**
 * 时间，表名后缀等统一管理
 * 
 * @author luohuawuyin
 *
 */
public class TimeControl {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTools DATATOOL = new DateTools(FORMAT);
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
    
    public static String getFiveMinTime(int addNum){
        String lasttime = null;
        String time = DATATOOL.getOffsetMin(addNum);
        if (Integer.parseInt(time.substring(time.length() - 1)) >= 5) {
            lasttime = time.substring(0, time.length() - 1) + "5";
        } else {
            lasttime = time.substring(0, time.length() - 1) + "0";
        }
        return lasttime;
    }
    
    public static String getOneHourTime(int addNum){
        String time = DATATOOL.getOffsetHour(addNum);
        return time.substring(0, time.length() - 2) + "00";
    }
    
    public static String getOneDayTime(int addNum){
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
     * @param cycleNum 周期数 0：当前周期 ，-1：上个周期，1：下个周期
     * @return
     */
    public static String getCycleTime(String cycle,int cycleNum){
        String time = DATATOOL.getOffsetMin(0);
        int ct = 0;
        switch (cycle) {
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
			break;
		default:
			break;
		}
        time = DATATOOL.getOffsetMin(ct*cycleNum);
        String [] timeArr =  time.split(":");
        int min = Integer.parseInt(timeArr[1]);
        int result =0; 
        if(ct!=0) {
        	result =min/ct;
        }	
        String lasttime = timeArr[0]+":";
        if(result==0){
        	lasttime = lasttime+"00:"+timeArr[2];
        }else{
        	lasttime = lasttime+result*ct+":"+timeArr[2];
        }
        return lasttime;
    }

    /**
     * 获取一天时间内周期时间断 HH:mm
     * @param
     * @return
     */
    public static List<String> getCycleTimeOneDayHHmm(String cycle,String format)
    {	
    	List<String> days = new ArrayList<String>();
    	//yyyy-MM-dd HH:mm
        String time = getCycleTime(cycle,0);
        days.add(DATATOOL.getFormatDay(time, format));
        int ct = 0;
        switch (cycle) {
		case "5min":
			ct = 5;
			break;
		case "10min":
			ct = 10;
			break;
		case "15min":
			ct = 15;
			break;
		case "30min":
			ct = 30;
			break;
		case "1hour":
			ct = 60;
			break;
		case "day":
			ct = 60;
			break;
		default:
			break;
		}
        int i = -1;
        String datetime = null;
        while(true){
        	datetime = DATATOOL.getOffsetMin(ct*i, time);
        	days.add(DATATOOL.getFormatDay(datetime, format));
        	if(datetime.contains("00:00")){
        		break;
        	}
        	i = i - 1 ;
        	
        }
        Collections.reverse(days);
        return days;
    }
}
