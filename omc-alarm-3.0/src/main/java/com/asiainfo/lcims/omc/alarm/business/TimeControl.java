package com.asiainfo.lcims.omc.alarm.business;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.lcims.omc.alarm.model.TimeCell;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.ToolsUtils;

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

    // private static String formatTime;
    //
    // public static void setCurrenttime(String time) {
    // formatTime = time;
    // }

    /**
     * 把指定格式的日期转化为对应的毫秒
     * 
     * @param date
     * @return
     */
    public static long getMillSeconds(String date) {
        long time = DATATOOL.getFormatTime(date);
        return time;
    }

    /**
     * 根据周期类型获取当前周期
     * 
     * @param cycleTime
     * @param cycle
     * @return
     */
    public static String currenttime(String cycleTime, String cyclename) {
        String time = null;
        if (!ToolsUtils.StringIsNull(cycleTime)) {
            time = cycleTime;
        } else {
            time = DATATOOL.getOffsetMin(0);
            int ct = 5;
            switch (cyclename) {
            case "1minute":
                ct = 1;
                break;
            case "5minute":
                ct = 5;
                break;
            case "10minute":
                ct = 10;
                break;
            case "15minute":
                ct = 15;
                break;
            case "20minute":
                ct = 20;
                break;
            case "30minute":
                ct = 30;
                break;
            case "1hour":
                ct = 60;
                break;
            default:
                break;
            }
            // 1分钟的不需要处理时间
            if (ct == 1) {
                return time;
            }
            String[] timeArr = time.split(":");
            int min = Integer.parseInt(timeArr[1]);
            int result = min / ct;
            time = timeArr[0] + ":";
            if (result == 0) {
                time = time + "00";
            } else {
                time = result == 1 && ct == 5 ? time + "0" + ct : time + result * ct;
            }
        }
        return time;
    }

    /**
     * 根据周期时间、周期类型获取周期时间
     * 
     * @param cycleTime
     * @param cycle
     * @return
     */
    public static String lasttime(String cycleTime, String cyclename, int cycleNum) {
        String lasttime = null;
        int ct = 5;
        switch (cyclename) {
        case "1minute":
            ct = 1;
            break;
        case "5minute":
            ct = 5;
            break;
        case "10minute":
            ct = 10;
            break;
        case "15minute":
            ct = 15;
            break;
        case "20minute":
            ct = 20;
            break;
        case "30minute":
            ct = 30;
            break;
        case "1hour":
            ct = 60;
            break;
        case "1day":
            lasttime = DATATOOL.getOffsetDay(cycleNum, cycleTime);
            break;
        case "1month":
            lasttime = DATATOOL.getOffsetMonth(cycleNum, cycleTime);
            break;
        default:
            break;
        }
        if (ct > 0) {
            String time = DATATOOL.getOffsetMin(ct * cycleNum, cycleTime);
            String[] timeArr = time.split(":");
            int min = Integer.parseInt(timeArr[1]);
            int result = min / ct;
            lasttime = timeArr[0] + ":";
            if (result == 0) {
                lasttime = lasttime + "00";
            } else {
                int result_ct = result * ct;
                if (result_ct < 10) {// 补成两位
                    lasttime = lasttime + "0" + result_ct;
                } else {
                    lasttime = lasttime + result_ct;
                }
            }
        }
        return lasttime;
    }

    /**
     * 昨天同一时间
     * 
     * @return
     */
    private static TimeCell yesterdaySuffixes(String currenttime, String cyclename) {
        TimeCell cell = new TimeCell();
        cell.setEndtime(currenttime);
        cell.setStarttime(DATATOOL.getOffsetDay(-1, currenttime));
        String startday = getSuffixes(cell.getStarttime());
        List<String> days = new ArrayList<String>();
        days.add(startday);
        String tbsuffixes = getTbsuffixes(currenttime, cyclename);
        days.add(tbsuffixes);
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
    private static TimeCell avgindaysSuffixes(String currenttime, String cyclename) {
        TimeCell cell = new TimeCell();
        String lastTime = getCurrenttime(currenttime, cyclename);
        cell.setEndtime(lastTime);
        List<String> days = new ArrayList<String>();
        for (int i = 3; i > 0; i--) {
            String time = DATATOOL.getOffsetDay(-i, lastTime);
            if (i == 3) {
                cell.setStarttime(time);
            }
            days.add(getSuffixes(time));
        }
        String tbsuffixes = getTbsuffixes(currenttime, cyclename);
        days.add(tbsuffixes);
        cell.setDays(days);
        return cell;
    }

    /**
     * 根据周期类型获取当前周期
     * 
     * @param cycle
     * @return
     */
    public static String getCurrenttime(String currenttime, String cyclename) {
        return currenttime(currenttime, cyclename);
    }

    /**
     * 获取日期表下划线
     * 
     * @param cycle
     * @return
     */
    public static String getTbsuffixes(String currentTime, String cyclename) {
        String lasttime = getCurrenttime(currentTime, cyclename);
        return getSuffixes(lasttime);
    }

    /**
     * 根据时间获取日期表下划线
     * 
     * @param time
     * @return
     */
    public static String getTbsuffixesByTime(String time) {
        return getSuffixes(time);
    }

    public static TimeCell getThreeday(String currenttime, String cyclename) {
        return avgindaysSuffixes(currenttime, cyclename);
    }

    /**
     * 获取昨天同一周期时间
     * 
     * @param cycle
     * @return
     */
    public static TimeCell getYesterday(String currentTime, String cyclename) {
        return yesterdaySuffixes(currentTime, cyclename);
    }

    public static String getCurrenttime(String currentTime, String cyclename, String cron) {
        String currenttime = null;
        String[] crons = cron.split(" ");
        if ("1day".equals(cyclename)) {
            currenttime = DATATOOL_DAY.getOffsetMin(0) + " " + fillParam(crons[2]) + ":"
                    + fillParam(crons[1]);
        } else if ("1month".equals(cyclename)) {
            currenttime = DATATOOL_MONTH.getOffsetMin(0) + "-" + fillParam(crons[3]) + " "
                    + fillParam(crons[2]) + ":" + fillParam(crons[1]);
        } else if ("1hour".equals(cyclename)) {
            String currentMin = DATATOOL_DAY.getCurrentMin();
            currenttime = DATATOOL.getOffsetMin(0);
            if (Integer.parseInt(crons[1]) > Integer.parseInt(currentMin)) {
                // 取上个小时数
                currenttime = DATATOOL.getOffsetMin(-60);
            }
            String[] timeArr = currenttime.split(":");
            currenttime = timeArr[0] + ":" + fillParam(crons[1]);
        } else {
            currenttime = getCurrenttime(null, cyclename);
        }
        return currenttime;
    }

    public static String fillParam(String param) {
        if (param.length() == 1) {
            return "0" + param;
        }
        return param;
    }

    /**
     * 判断当前周期时间是否在 5 分钟之内
     * 
     * @param cyclename
     * @return
     */
    public static String isCycleTimeInFiveMin(String cron, String cyclename) {
        String currenttime = null;
        String[] crons = cron.split(" ");
        if ("1day".equals(cyclename)) {
            currenttime = DATATOOL_DAY.getOffsetMin(0) + " " + crons[2] + ":" + crons[1];
        } else if ("1month".equals(cyclename)) {
            currenttime = DATATOOL_MONTH.getOffsetMin(0) + "-" + crons[3] + " " + crons[2] + ":"
                    + crons[1];
        } else if ("1hour".equals(cyclename)) {
            String currentMin = DATATOOL_DAY.getCurrentMin();
            currenttime = DATATOOL.getOffsetMin(0);
            if (Integer.parseInt(crons[1]) > Integer.parseInt(currentMin)) {
                // 取上个小时数
                currenttime = DATATOOL.getOffsetMin(-60);
            }
            String[] timeArr = currenttime.split(":");
            currenttime = timeArr[0] + ":" + fillParam(crons[1]);
        } else {
            currenttime = getCurrenttime(null, cyclename);
        }
        // 当前时间 减 5 分钟
        String fivetime = DATATOOL.getOffsetMin(-5);
        long t1 = DATATOOL.getFormatTime(currenttime);
        long t2 = DATATOOL.getFormatTime(fivetime);
        long t = t1 - t2;
        if (t <= 300000 && t > 0) {
            return currenttime;
        }
        return null;
    }

    public static String getUploadFileTimeName() {
        DateTools DATATOOL2 = new DateTools("yyyyMMddHH:mm");
        String time = null;
        time = DATATOOL2.getOffsetMin(0);
        int ct = 5;
        String[] timeArr = time.split(":");
        int min = Integer.parseInt(timeArr[1]);
        int result = min / ct;
        time = timeArr[0];
        if (result == 0) {
            time = time + "00";
        } else {
            time = result == 1 && ct == 5 ? time + "0" + ct : time + result * ct;
        }
        return time;
    }

    public static void main(String[] args) {
        // System.out.println(TimeControl.getUploadFileTimeName());
        // System.out.println(DATATOOL_DAY.getOffsetMin(0));
        // System.out.println(DATATOOL_MONTH.getOffsetMin(0));
        // System.out.println(isCycleTimeInFiveMin("0 45 16 * * ?","1day"));
        System.out.println(getCurrenttime(null, "1hour", "0 45 * * * ?"));
    }

}
