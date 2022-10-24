package com.asiainfo.ais.omcstatistic.common;

public class Constant {

	private Constant() {}
    //统计时间 日表（按分钟统计）
    public static final String DAY_MINUTE_REPORT = "HH:mm";

    //统计时间 日表（按小时统计）
    public static final String DAY_HOUR_REPORT = "HH";

    //统计时间 月表
    public static final String MONTH_REPORT = "yyyy-MM-dd";

    //按月统计
    public static final String MONTH_REPORT_MM = "yyyy-MM";

    //按年统计
    public static final String MONTH_REPORT_YY = "yyyy";

    //规则执行间隔 5分钟
    public static final String EXCUTE_SPACE_MINUTE_SHELL = "5minute";

    // 规则执行间隔 5分钟
    public static final String EXCUTE_SPACE_MINUTE = "5min";

    // 规则执行间隔 10分钟
    public static final String EXCUTE_SPACE_MINUTE_10 = "10min";

    // 规则执行间隔 15分钟
    public static final String EXCUTE_SPACE_MINUTE_15 = "15min";

    // 规则执行间隔 20分钟
    public static final String EXCUTE_SPACE_MINUTE_20 = "20min";

    // 规则执行间隔 30分钟
    public static final String EXCUTE_SPACE_MINUTE_30 = "30min";

    //规则执行间隔 1小时
    public static final String EXCUTE_SPACE_HOUR = "1hour";

    //规则执行间隔 1天
    public static final String EXCUTE_SPACE_DAY = "1day";

    //规则执行间隔 1周
    public static final String EXCUTE_SPACE_WEEK = "1week";

    //规则执行间隔 1月
    public static final String EXCUTE_SPACE_MONTH = "1month";

    //按日统计
    public static final String DAY_STATISTIC = "STATIS_DATA_DAY";

    //按月统计
    public static final String MONTH_STATISTIC = "STATIS_DATA_MONTH";

    //表名日期格式
    public static final String REPORT_SUFFIX_DAY_FORMAT = "MM_dd";

    //表名日期格式
    public static final String REPORT_SUFFIX_MONTH_FORMAT = "MM";

    //表名日期格式 替换前
    public static final String REPORT_SUFFIX_DAY = "#{TIME.%m_%d}";

    //当前时间格式
    public static final String REPLACE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    //当前时间createtime
    public static final String CREATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //固定ID格式
    public static final String ID_FORMAT = "#{ID.";

    //固定日期格式
    public static final String TIME_FORMAT = "#{TIME.";

    //前一天
    public static final String YESTERDAY_FORMAT = "yesterday";

    //前一小时
    public static final String PREHOUR_FORMAT = "prehour";

    //前一周
    public static final String YESTERWEEK_FORMAT = "yesterweek";

    //前一月
    public static final String YESTERMONTH_FORMAT = "yestermonth";

    //当前
    public static final String NOW_FORMAT = "now";

    //coll_cycle表 5分钟
    public static final Integer COLL_CYCLE_FIVE_MIN = 2;

    // coll_cycle表 10分钟
    public static final Integer COLL_CYCLE_MIN_10 = 3;

    // coll_cycle表 15分钟
    public static final Integer COLL_CYCLE_MIN_15 = 15;

    // coll_cycle表 20分钟
    public static final Integer COLL_CYCLE_MIN_20 = 20;

    // coll_cycle表 30分钟
    public static final Integer COLL_CYCLE_MIN_30 = 7;

    //coll_cycle表 小时
    public static final Integer COLL_CYCLE_HOUR = 4;

    //coll_cycle表 天
    public static final Integer COLL_CYCLE_DAY = 5;

    //coll_cycle表 月
    public static final Integer COLL_CYCLE_MONTH = 6;

    // cron 5分钟
    public static final String CRON_DEFAULT_MIN_5 = "0 0/5 * * * ?";
    // cron 10分钟
    public static final String CRON_DEFAULT_MIN_10 = "0 0/10 * * * ?";
    // cron 15分钟
    public static final String CRON_DEFAULT_MIN_15 = "0 0/15 * * * ?";
    // cron 20分钟
    public static final String CRON_DEFAULT_MIN_20 = "0 0/20 * * * ?";
    // cron 30分钟
    public static final String CRON_DEFAULT_MIN_30 = "0 0/30 * * * ?";
    // cron 小时
    public static final String CRON_DEFAULT_HOUR = "0 10 0/1 * * ?";
    // cron 天
    public static final String CRON_DEFAULT_DAY = "0 0 5 * * ?";
    // cron 月
    public static final String CRON_DEFAULT_MONTH = "0 0 15 12 * ?";
}
