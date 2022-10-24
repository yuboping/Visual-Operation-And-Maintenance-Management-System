package com.asiainfo.lcims.omc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.param.common.CycleType;

public class DateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String WID_TIME_FORMAT = "yyyyMMddHHmmss";

    public static final String TABLE_SUFFIX = "MM_dd";

    /**
     * 根据时间格式获取指定日期时间
     * 
     * @param dateFormat
     * @return date
     */
    public static String getFormatTime(int interval, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -interval);
        Date date = calendar.getTime();
        String formatDate = sdf.format(date);

        return formatDate;
    }

    public static String getFormatTimeByDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatDate = sdf.format(date);
        return formatDate;
    }

    /**
     * 
     * @Title: stampToDate @Description: (时间戳转指定格式的日期) @param @param
     *         format @param @return 参数 @return 返回类型 @throws
     */
    public static String stampToDate(long millis, String dateFormat) {
        long timeMillis = millis;
        DateTime dateTime = new DateTime(timeMillis);
        String dateTimeStr = dateTime.toString(dateFormat);
        return dateTimeStr;
    }

    /**
     * 
     * @Title: getFormatDate @Description: (转换指定日期为指定的格式) @param @param
     *         format @param @return 参数 @return 返回类型 @throws
     */
    public static String getFormatDate(String date, String format, String returFormat) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(format);
        DateTime datetime = dtf.parseDateTime(date);
        DateTimeFormatter returnDtf = DateTimeFormat.forPattern(returFormat);
        return datetime.toString(returnDtf);
    }

    /**
     * 
     * @Title: getSimpleDateFormat @Description: (格式化日期) @param @param
     *         format @param @return 参数 @return SimpleDateFormat 返回类型 @throws
     */
    private static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sdf;
        if ("".equals(format) || null == format) {
            sdf = new SimpleDateFormat(C_TIME_PATTON_DEFAULT);
        } else {
            sdf = new SimpleDateFormat(format);
        }
        return sdf;
    }

    /**
     * 
     * @Title: parseStr @Description: (日期转字符串) @param @param date @param @param
     *         format @param @return 参数 @return String 返回类型 @throws
     */
    public static String parseStr(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 
     * @Title: parseDate @Description: (字符串转日期) @param @param
     *         dateStr @param @param format @param @return 参数 @return Date
     *         返回类型 @throws
     */
    public static Date parseDate(String dateStr, String format) {
        if (dateStr == null) {
            return null;
        }
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.parse(dateStr, new ParsePosition(0));
    }

    /**
     * 
     * @Title: getFormatTimeIntegration @Description: (每五分钟归整) @param @param
     *         interval @param @param dateFormat @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public static String getFormatTimeIntegration() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(new Date());
        String mmformatDate = getCycleDateTimeToFormatter(DateTime.now(), CycleType.FIVE_MINUTES,
                "HH:mm");
        String result = formatDate + " " + mmformatDate + ":00";
        return result;
    }

    /**
     * 
     * @Title: nowDateString @Description: (获取当前时间字符串) @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public static String nowDateString() {
        return parseStr(new Date(), C_TIME_PATTON_DEFAULT);
    }

    /**
     * 
     * @Title: nowDateString @Description: (获取当前时间字符串) @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public static String getMinusTime(String format, int N, String time) {
        if (ToolsUtils.StringIsNull(time)) {
            time = TimeTools.getCurrentTime(format);
        }
        String resultDate = time;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -N);
            resultDate = dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            LOG.error(e.getMessage());
        }
        return resultDate;
    }

    /**
     * 
     * @Title: from @Description: (date转instant) @param @param
     *         instant @param @return 参数 @return Date 返回类型 @throws
     */
    public static Date from(Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * 
     * @Title: calculateTimeDifferenceByDuration @Description:
     *         (获取2个时间的秒差值) @param @param dateBefore @param @param
     *         dateAfter @param @return 参数 @return Long 返回类型 @throws
     */
    public static String calculateTimeDifferenceByDuration(Date dateBefore, Date dateAfter) {
        Instant instBefore = dateBefore.toInstant();
        Instant instAfter = dateAfter.toInstant();
        return String.valueOf(Duration.between(instBefore, instAfter).getSeconds());
    }

    /**
     * 根据采集周期获取给定时间规整后的时间(向下取整)
     * 
     * @param dateTime
     * @param cycleType
     * @return
     */
    public static DateTime getCycleDateTime(DateTime dateTime, CycleType cycleType) {
        long cycleTypeillis = cycleType.getMillis();
        return new DateTime(dateTime.getMillis() / cycleTypeillis * cycleTypeillis);
    }

    /**
     * * 根据采集周期获取规整后的时间,并返回指定格式的字符串数据(向下取整)
     * <p>
     * 例:formatter 的格式可以为： HH_mm ; HH:mm; 具体可参考类说明
     * 
     * @param dateTime
     * @param cycleType
     * @param formatter
     * @return
     */
    public static String getCycleDateTimeToFormatter(DateTime dateTime, CycleType cycleType,
            String formatter) {
        return getCycleDateTime(dateTime, cycleType).toString(formatter);
    }

    /**
     * 根据时间格式获取指定日期时间
     * 
     * @param dateFormat
     * @return date
     */
    public static String getFormatTimeByFormat(String nowdate, String dateType, String dateFormat) {
        SimpleDateFormat norsdf = new SimpleDateFormat(Constant.CREATE_FORMAT);
        String formatDate = null;
        try {
            Date date = norsdf.parse(nowdate);

            if (Constant.YESTERDAY_CONSTANT.equals(dateType)) {
                // 获取昨天日期
                date = new Date(date.getTime() - 24 * 60 * 60 * 1000);
            }

            // 设置时间格式
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            // 获取String类型的时间
            formatDate = sdf.format(date);

        } catch (ParseException e) {
            LOG.error("日期解析出错：" + e.getMessage());
        }
        return formatDate;
    }

    /**
     * 根据时间格式获取指定日期时间
     * 
     * @param dateFormat
     * @return date
     */
    public static String getFormatTimeByFormat(String nowdate, String dateFormat) {
        SimpleDateFormat norsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = null;
        try {
            Date date = norsdf.parse(nowdate);

            // 设置时间格式
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            // 获取String类型的时间
            formatDate = sdf.format(date);

        } catch (ParseException e) {
            LOG.error("日期解析出错：" + e.getMessage());
        }
        return formatDate;
    }

    /**
     * 根据时间格式获取指定日期时间
     * 
     * @param dateFormat
     * @return date
     */
    public static String getFormatTimeWithNumber(String dateFormat, int i) {
        Date date = new Date();
        date = new Date(date.getTime() - 24 * 60 * 60 * 1000 * i);

        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        // 获取String类型的时间
        String formatDate = sdf.format(date);

        return formatDate;
    }

    public static int dayCompare(String fromDate, String toDate) {
        SimpleDateFormat norsdf = new SimpleDateFormat("yyyy-MM");
        Date fDate = new Date();
        Date tDate = new Date();
        try {
            fDate = norsdf.parse(fromDate);
            tDate = norsdf.parse(toDate);
        } catch (ParseException e) {
            LOG.error("日期解析出错：" + e.getMessage());
        }
        Calendar from = Calendar.getInstance();
        from.setTime(fDate);
        Calendar to = Calendar.getInstance();
        to.setTime(tDate);
        // 只要年月
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);

        int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
        return month;
    }

    public static String monthAddNum(String startDate, int num) {
        SimpleDateFormat norsdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date fDate = new Date();
        try {
            fDate = norsdf.parse(startDate);
        } catch (ParseException e) {
            LOG.error("日期解析出错：" + e.getMessage());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fDate);
        calendar.add(Calendar.MONTH, num);
        Date newTime = calendar.getTime();
        String formatDate = sdf.format(newTime);
        return formatDate;
    }

    /**
     * 根据 startDate 、endDate 之间 返回所有日期列表 格式：yyyy-MM-dd
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getAllDateCycleListByDay(String startDate, String endDate) {
        List<String> datelist = new ArrayList<>();
        DateTools dataTool = new DateTools("yyyy-MM-dd");
        if (ToolsUtils.StringIsNull(startDate)) {
            startDate = dataTool.getCurrentDate();
        }
        if (ToolsUtils.StringIsNull(endDate)) {
            datelist.add(startDate);
            return datelist;
        }
        boolean isNodtEndDate = true;
        int i = 0;
        while (isNodtEndDate) {
            String day = dataTool.getOffsetDay(1 * i, startDate);
            datelist.add(day);
            if (day.endsWith(endDate)) {
                isNodtEndDate = false;
            }
            i++;
        }
        return datelist;
    }

    /**
     * 根据 startDate 、endDate 之间 返回所有日期列表 格式：yyyy-MM
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getAllDateCycleListByMonth(String startDate, String endDate) {
        List<String> datelist = new ArrayList<>();
        DateTools dataTool = new DateTools("yyyy-MM");
        if (ToolsUtils.StringIsNull(startDate)) {
            startDate = dataTool.getCurrentDate();
        }
        if (ToolsUtils.StringIsNull(endDate)) {
            datelist.add(startDate);
            return datelist;
        }
        boolean isNodtEndDate = true;
        int i = 0;
        while (isNodtEndDate) {
            String month = dataTool.getOffsetMonth(1 * i, startDate);
            datelist.add(month);
            if (month.endsWith(endDate)) {
                isNodtEndDate = false;
            }
            i++;
        }
        return datelist;
    }

    /**
     * 
     * @Title: strToDate @Description: TODO(String转换DateTime) @param @param
     * dateTimeStr @param @param format @param @return 参数 @return DateTime
     * 返回类型 @throws
     */
    public static DateTime strToDate(String dateTimeStr, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return dateTimeFormatter.parseDateTime(dateTimeStr);
    }
}
