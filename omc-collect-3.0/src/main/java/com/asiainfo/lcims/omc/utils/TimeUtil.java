package com.asiainfo.lcims.omc.utils;

import java.sql.Timestamp;
import java.util.Date;

import com.asiainfo.lcims.omc.common.TimeConstants;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtil {

    public static final Logger LOG = LoggerFactory.getLogger(TimeUtil.class);

    private static final String TIME_FORMAT = "MM_dd";

    // 月表后缀
    private static final String TIME_MONTH_FORMAT = "MM";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前采集规整时间
     * <p>
     * date:需要规整的时间
     * <p>
     * type:规整的规则,1：按1分钟规整; other:按5分钟规整;5分钟规整的话采用就近规整原则
     * <P>
     * 例:type=1,date= 20180724092855123.规整之后为 20180724092800000
     * <p>
     * 例:type!=1,date= 20180724092655123.规整之后为 20180724092500000
     * 
     * 例:type!=2,date= 20180724092855123.规整之后为 20180724093000000
     * 
     * @param date
     * @param type
     * @return
     */
    public static Timestamp getColCurrentTime(Date date, String type) {
        DateTime dateTime = new DateTime(date.getTime());
        DateTime downDateTime = null;
        if (type != null && "1".equals(type)) {
            downDateTime = dateTime.withField(DateTimeFieldType.secondOfMinute(), 0)
                    .withField(DateTimeFieldType.millisOfSecond(), 0);
        } else {
            // 向下规整成5分钟整数时间,获得时间 downDateTime.
            // upDateTime再向上增加一个5分钟时间.
            // 比较 upDateTime-dateTime 和 dateTime-downDateTime,
            // 取距离dateTime时间最近的一个规整时间.
            int minute = dateTime.getMinuteOfHour();
            int cycleTime = 5;
            minute = (minute / cycleTime) * cycleTime;
            downDateTime = dateTime.withField(DateTimeFieldType.minuteOfHour(), minute)
                    .withField(DateTimeFieldType.secondOfMinute(), 0)
                    .withField(DateTimeFieldType.millisOfSecond(), 0);
            DateTime upDateTime = downDateTime.plusMinutes(cycleTime);
            if (upDateTime.getMillis() - dateTime.getMillis() < dateTime.getMillis()
                    - downDateTime.getMillis()) {
                downDateTime = upDateTime;
            }
        }

        return new Timestamp(downDateTime.getMillis());
    }

    /**
     * 获取表的后缀时间
     */
    public static String colTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        return dateTime.toString(TIME_FORMAT);
    }

    /**
     * 获取表的后缀时间
     */
    public static String colMonthTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        return dateTime.toString(TIME_MONTH_FORMAT);
    }

    /**
     * 获取周期时间
     * @param cycleNum 周期数 0：当前周期 ，-1：上个周期，1：下个周期
     * @return
     */
    public static String getCycleTime(String cycle,int cycleNum){
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
            default:
                break;
        }
        String time = getOffsetMin(ct*cycleNum);
        String [] timeArr =  time.split(":");
        int min = Integer.parseInt(timeArr[1]);
        int result = min/ct;
        String lasttime = timeArr[0]+":";
        int minCycle = result*ct;
        String minCycleStr = String.valueOf(minCycle);
        if (minCycle < 10) {
            minCycleStr = "0" + minCycleStr;
        }
        lasttime = lasttime + minCycleStr + ":" + "00";
        return lasttime;
    }

    /**
     * 获取规整秒值
     * @return
     */
    public static String getCycleSecondTime(){
        // 是否规整至下一周期标志符
        Boolean plusFlag = false;
        // 设置秒级间隔 暂时为20s
        int ct = TimeConstants.TIME_SECOND_SPACE;
        // 获取当前时间
        String time = getOffsetMin(0);
        String [] timeArr =  time.split(":");

        int second = Integer.parseInt(timeArr[2]);
        int result = second/ct;
        if ((second - result*ct) > (ct/2)){
            plusFlag =true;
        }
        String secondStr = String.valueOf(result * ct);
        if (result * ct == 0){
            secondStr = "00";
        }
        String lasttime = timeArr[0]+":"+timeArr[1]+":"+secondStr;

        if (plusFlag){
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
            DateTime date = getStr2Date(lasttime,formatter);
            date = date.plusSeconds(ct);
            lasttime = date.toString(formatter);
        }

        return lasttime;
    }

    /**
     * 按分钟相加减
     *
     * @return 格式为{@link DateTools#DEFAULT_FORMAT}的字符串
     */
    public static String getOffsetMin(int offset) {
        DateTime date = new DateTime().plusMinutes(offset);
        DateTimeFormatter dtf  = DateTimeFormat.forPattern(DATE_FORMAT);
        return date.toString(dtf);
    }

    public static DateTime getStr2Date(String str, DateTimeFormatter formatter){

        DateTime date = null;
        try {
            date = DateTime.parse(str,formatter);
        } catch (Exception e) {
            LOG.error("parseTime error - > {}", e.getMessage());
        }
        return date;
    }


    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
//        Date date = new Date();
//        date.setSeconds(30);
//        for (int i = 0; i < 60; i++) {
//            date.setMinutes(i);
//            Timestamp tmp = TimeUtil.getColCurrentTime(date, "2");
//            System.out.print(i + ": ");
//            System.out.println(tmp);
//        }
        System.out.println(TimeUtil.getCycleSecondTime());

    }

}
