package com.asiainfo.lcims.omc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;

public class TimerConvert {

    private static final String[] LOCLIST = new String[] { "min", "hour", "day", "month", "year",
            "week" };

    /**
     * 根据timer计算显示的值
     */
    public void calshowinfofromTimer(String timer) {
    }

    /**
     * 计算指标 返回 year,month,week,day
     */
    public String calPerIdentify(String timer) {
        if (!StringUtils.isEmpty(timer)) {
            String[] timerarray = this.getTimerList(timer);
            if (timerarray.length == 5) {
                return calPerIdentify(timerarray);
            }
        }
        return null;
    }

    private String calPerIdentify(String[] timerarray) {
        for (int i = timerarray.length - 1; i >= 0; i--) {
            if (!(timerarray[i].equals("*") || timerarray[i].equals("?"))) {
                return TimerConvert.LOCLIST[i + 1];
            }
        }
        return null;
    }

    /**
     * 获取下一次执行时间
     * 
     * @return
     * @throws ParseException
     */
    public String getNextExecTime(String timer) throws ParseException {
        if (!StringUtils.isEmpty(timer)) {
            String[] timerarray = this.getTimerList(timer);
            if (timerarray.length == 5) {
                DateTools datetools = new DateTools("yyyyMMddHHmmss");
                String nowdate = datetools.getCurrentDate();
                String neardate = getnearbyExecTime(timerarray);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                if (df.parse(neardate).after(df.parse(nowdate))
                        || df.parse(neardate).equals(df.parse(nowdate))) {
                    return neardate;
                } else {
                    String perIdentify = calPerIdentify(timerarray);

                    if(perIdentify == null) {
                        return neardate;
                    }
                    if (StringUtils.equals("week", perIdentify)) {
                        return datetools.getOffsetDay(7, neardate);
                    } else {
                        Integer nearyear = Integer.valueOf(neardate.substring(0, 4));
                        Integer nearmonth = Integer.valueOf(neardate.substring(4, 6));
                        if (StringUtils.equals("year", perIdentify)) {
                            return "" + (nearyear + 1) + neardate.substring(4);
                        } else if (StringUtils.equals("month", perIdentify)) {
                            if (nearmonth + 1 > 12) {
                                return "" + (nearyear + 1) + formatdata((nearmonth + 1) % 12)
                                        + neardate.substring(6);
                            } else {
                                return "" + neardate.substring(0, 4) + formatdata((nearmonth + 1))
                                        + neardate.substring(6);
                            }
                        } else if (StringUtils.equals("day", perIdentify)) {
                            return datetools.getOffsetDay(1, neardate);
                        }

                    }

                }
                return neardate;
            }
        }
        return null;
    }

    /**
     * 获取附近的某个执行时间点
     * 
     * @return
     */
    private String getnearbyExecTime(String[] timerarray) {
        String perIdentify = calPerIdentify(timerarray);
        // 根据timer确定时分秒部分
        String subtime = formatdata(Integer.valueOf(timerarray[1]))
                + formatdata(Integer.valueOf(timerarray[0])) + "00";
        String neardate = "";
        if (StringUtils.isEmpty(perIdentify)) {
            return neardate;
        }
        if (StringUtils.equals("week", perIdentify)) {
            neardate = getnearweekExectime(timerarray[4], subtime);
        } else if (StringUtils.equals("year", perIdentify)) {
            neardate = getnearyearExectime(Integer.valueOf(timerarray[3]),
                    Integer.valueOf(timerarray[2]), subtime);
        } else if (StringUtils.equals("month", perIdentify)) {
            neardate = getnearmonthExectime(Integer.valueOf(timerarray[2]), subtime);
        } else if (StringUtils.equals("day", perIdentify)) {
            neardate = getneardayExectime(subtime);
        }

        return neardate;
    }

    /**
     * 每周执行，获取执行时间点
     * 
     * @return
     */
    private String getnearweekExectime(String selweek, String subtime) {

        Calendar calendar = Calendar.getInstance();
        int nowweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        DateTools datetool = new DateTools("yyyyMMdd");
        return datetool.getOffsetDay(Integer.valueOf(selweek) - nowweek) + subtime;
    }

    /**
     * 获取每年的执行时间点
     * 
     * @param selmonth
     * @param selday
     * @param subtime
     * @return
     */
    private String getnearyearExectime(Integer selmonth, Integer selday, String subtime) {
        String nowdate = new DateTools("yyyy").getCurrentDate();

        return nowdate + formatdata(selmonth) + formatdata(selday) + subtime;
    }

    /**
     * 获取每月的执行时间点
     * 
     * @param selday
     * @param subtime
     * @return
     */
    private String getnearmonthExectime(Integer selday, String subtime) {
        String nowdate = new DateTools("yyyyMM").getCurrentDate();
        return nowdate + formatdata(selday) + subtime;
    }

    /**
     * 获取每天的执行时间点
     * 
     * @param subtime
     * @return
     */
    private String getneardayExectime(String subtime) {
        String nowdate = new DateTools("yyyyMMdd").getCurrentDate();
        return nowdate + subtime;
    }

    /**
     * 不足两位补0
     * 
     * @param data
     * @return
     */
    private String formatdata(Integer data) {
        return String.format("%02d", data);
    }

    private String[] getTimerList(String timer) {
        return timer.split(" ");
    }

    /**
     * 根据前台的值，计算timer 存放到数据库中的值
     */
    public String calTimerFromShowInfo(String perIdentify, Map<String, String> seltimer) {
        int locindex = TimerConvert.LOCLIST.length - 1;
        String[] timer = new String[] { "*", "*", "*", "*", "*" };
        for (int i = TimerConvert.LOCLIST.length - 1; i >= 0; i--) {
            if (perIdentify.equals(TimerConvert.LOCLIST[i])) {
                locindex = i;
                break;
            }
        }

        timer[0] = seltimer.get("selmin");
        timer[1] = seltimer.get("selhour");
        if (TimerConvert.LOCLIST[locindex].equals("week")) {
            timer[4] = seltimer.get("selweek");
            timer[2] = "?";
        } else {
            timer[4] = "?";
            if (locindex > 2) {
                timer[2] = seltimer.get("selday");
            }
            if (locindex > 3) {
                timer[3] = seltimer.get("selmonth");
            }
        }

        return StringUtils.join(timer, ' ');
    }

    public String formatTimeForShow(String time) {
        DateTime datetime = TimeTools.getDateTime(time, "YYYYMMddHHmmss");

        DateTimeFormatter matter = DateTimeFormat.forPattern("yy-MM-dd HH:mm EEEE");
        return datetime.toString(matter);
    }

}
