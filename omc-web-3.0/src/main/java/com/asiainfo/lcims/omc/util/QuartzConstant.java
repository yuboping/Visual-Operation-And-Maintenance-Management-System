package com.asiainfo.lcims.omc.util;

public class QuartzConstant {

    /** trigger key 定时任务唯一键 */
    public static final String TIME_CLEANING_TRIGGER_KEY = "timeCleaning";
    
    /** trigger key 定时任务唯一键 */
    public static final String TIME_BRASIP_TRIGGER_KEY = "shcmbrasIpAdd";

    /** APN定时任务唯一键 */
    public static final String TIME_APN_TRIGGER_KEY = "ANPRedisAdd";

    /** 添加APN定时任务唯一键 */
    public static final String TIME_ADD_APN_TRIGGER_KEY = "AddANPTriggerId";

    /** Cron 表达式 每天执行1次 */
    public static final String TIME_CLEANING_CRON = "0 0 1 * * ?";
    
    /** Cron 表达式 每小时执行1次  */
    public static final String TIME_HOUR_CRON = "0 0 0/1 * * ?";
    
    /** Cron 表达式 每分钟执行1次 */
    public static final String TIME_MINUTE_CRON = "0 0/1 * * * ?";

//    public static final String TIME_CLEANING_CRON = "*/5 * * * * ?";

    /** 定时清理普通表 */
    public static final Integer NORMAL_TABLE_TYPE = 1;

    /** 定时清理日期分表 */
    public static final Integer SPECIAL_TABLE_TYPE = 0;

    /** 表名拼接固定字符串 */
    public static final String SPLIT_FORMAT = "_";

    /** 日期格式化之 月 */
    public static final String DATE_FORMATE_MONTH = "MM";

    /** 日期格式化之 日 */
    public static final String DATE_FORMATE_DAY = "dd";

    /** 日期格式化之 标准格式 */
    public static final String DATE_FORMATE_NORMAL = "yyyy-MM-dd HH:mm:ss";

}
