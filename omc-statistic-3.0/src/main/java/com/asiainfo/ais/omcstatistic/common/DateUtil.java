package com.asiainfo.ais.omcstatistic.common;

import com.asiainfo.ais.omcstatistic.statistic.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
    
    private DateUtil() {}

    /**
     * 根据时间格式获取当前时间
     * @param dateFormat
     * @return date
     */
    public static String getFormatTime(String dateFormat){
        //获取当前日期
        Date nowdate = new Date();

        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //获取String类型的时间
        String date = sdf.format(nowdate);

        return date;
    }

    public static String getNormalTime(String nowdate, String dateFormat){
        Date date;
        String normaldate = null;
        try{
            //设置时间格式
            SimpleDateFormat normalsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            date = normalsdf.parse(nowdate);
            normaldate = sdf.format(date);

        } catch (Exception e){
            LOG.error("date parse error : " + e.getMessage());
        }
        return normaldate;
    }

    /**
    * 把long 转换成 日期 再转换成String类型
    */
    public static String getLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 获取昨天或前一小时格式化日期
     * @param dateFormat
     * @return
     */
    public static String getFormatTime(String dateType, String dateFormat){
        Date date = null;
        if(Constant.YESTERDAY_FORMAT.equals(dateType)) {
            //获取昨天日期
            date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        }else if(Constant.PREHOUR_FORMAT.equals(dateType)){
            //获取上一小时日期
            date = new Date(new Date().getTime()- 60 * 60 * 1000);
        }

        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        //获取String类型的时间
        String formatDate = sdf.format(date);

        return formatDate;
    }

    /**
     * 根据时间格式获取指定日期时间
     * @param dateFormat
     * @return date
     */
    public static String getFormatTime(String nowdate, String dateType, String dateFormat, String offset){
        SimpleDateFormat norsdf = new SimpleDateFormat(Constant.CREATE_TIME_FORMAT);
        String formatDate = null;
        if(StringUtils.isEmpty(offset)){
            offset = "-1";
        }
        int offsetInt = Integer.parseInt(offset);

        try {
            Date date = norsdf.parse(nowdate);

            if(Constant.YESTERDAY_FORMAT.equals(dateType)) {
                //获取昨天日期
                date = new Date(date.getTime() + offsetInt * 24 * 60 * 60 * 1000);
            }else if(Constant.PREHOUR_FORMAT.equals(dateType)){
                //获取上一小时日期
                date = new Date(date.getTime() + offsetInt * 60 * 60 * 1000);
            }else if(Constant.YESTERWEEK_FORMAT.equals(dateType)){
                //获取上一周日期
                date = new Date(date.getTime() + offsetInt * 7 * 24 * 60 * 60 * 1000);
            }else if(Constant.YESTERMONTH_FORMAT.equals(dateType)){
                //获取上一月日期
                date = new Date(date.getTime() + offsetInt * 15 * 24 * 60 * 60 * 1000);
            }

            //设置时间格式
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            //获取String类型的时间
            formatDate = sdf.format(date);

        }catch (ParseException e){
            LOG.error("日期解析出错："+e.getMessage());
        }
        return formatDate;
    }

    /**
     * 根据时间格式获取指定日期时间
     * @param dateFormat
     * @return date
     */
    public static String getFormatTimeWithCal(String nowdate, String dateType, String dateFormat, String timeOffSet){
        SimpleDateFormat norsdf = new SimpleDateFormat(Constant.CREATE_TIME_FORMAT);
        String formatDate = null;
        int offsetInt = Integer.parseInt(timeOffSet);

        try {
            Date date = norsdf.parse(nowdate);

            Calendar c = Calendar.getInstance();
            c.setTime(date);

            if(Constant.YESTERDAY_FORMAT.equals(dateType)) {
                //获取昨天日期
                c.add(Calendar.DAY_OF_MONTH, offsetInt);
            }else if(Constant.YESTERMONTH_FORMAT.equals(dateType)){
                //获取上月日期
                c.add(Calendar.MONTH, offsetInt);
            }

            date = c.getTime();
            //设置时间格式
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            //获取String类型的时间
            formatDate = sdf.format(date);

        }catch (ParseException e){
            LOG.error("日期解析出错："+e.getMessage());
        }
        return formatDate;
    }

    public static String getCronFormatDate(String nowDate, Integer cycleId){
        HashMap<Integer, String> cycleHashMap = MemoryUtil.getMdCollCycleHashMap();
        String[] dateArray = nowDate.split(":");
        StringBuffer formatDate = new StringBuffer();
        String formateDateStr;
        try {
            String[] cycleInfo = cycleHashMap.get(cycleId).split(" ");
            dateArray[1] = cycleInfo[1];
            for (String temp : dateArray) {
                formatDate.append(temp + ":");
            }
            formateDateStr = formatDate.substring(0, formatDate.length() - 1);
        }catch (ArrayIndexOutOfBoundsException ex) {
            LOG.error("日期解析出错："+ ex.getMessage());
            return nowDate;
        }
        return formateDateStr;
    }

}
