package com.asiainfo.lcims.util;

public class ConstantUtil {
    public static final String SFTP = "sftp";
    public static final String FTP = "ftp";
    
    public static final String REPLACE_ITEM = "$item$";
    public static final String REPLACE_VAL = "$value$";

    // 陕西移动Trap上报需要的OID
    /** 告警流水号，动态唯一标识一条告警 **/
    public static final String TRAP_ALARM_CSN = "1.3.6.1.4.1.9555.2.15.2.4.3.3.1";

    /** 告警类别 **/
    public static final String TRAP_ALARM_CATEGORY = "1.3.6.1.4.1.9555.2.15.2.4.3.3.2";

    /** 告警产生时间 **/
    public static final String TRAP_ALARM_OCCURTIME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.3";

    /** 产生告警的网元名称 **/
    public static final String TRAP_ALARM_MONAME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.4";

    /** 告警ID，同类型网元中告警的静态唯一标识 **/
    public static final String TRAP_ALARM_ID = "1.3.6.1.4.1.9555.2.15.2.4.3.3.9";

    /** 告警分类 **/
    public static final String TRAP_ALARM_TYPE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.10";

    /** 告警级别 **/
    public static final String TRAP_ALARM_LEVEL = "1.3.6.1.4.1.9555.2.15.2.4.3.3.11";

    /** 告警清除状态 **/
    public static final String TRAP_ALARM_RESTORE = "1.3.6.1.4.1.9555.2.15.2.4.3.3.12";

    /** 告警清除时间 **/
    public static final String TRAP_ALARM_RESTORETIME = "1.3.6.1.4.1.9555.2.15.2.4.3.3.15";

    /** 告警详细原因 **/
    public static final String TRAP_ALARM_SPECIFIC_PROBLEMS = "1.3.6.1.4.1.9555.2.15.2.4.3.3.30";

    // 陕西移动Trap告警类别
    /** 未清除告警 **/
    public static final String TRAP_ALARM_FLAG = "2";

    /** 已清除告警 **/
    public static final String TRAP_CLEAN_FLAG = "1";

    /** 告警类别 1：故障告警 **/
    public static final String TRAP_ALARM_CATEGORY_FAULT = "1";

    /** 告警类别 2：清除告警 **/
    public static final String TRAP_ALARM_CATEGORY_CLEAN = "2";

    // 图表名称
    public static final String CPU_CHART_NAME = "cpu_use_rate";
    public static final String MEMORY_CHART_NAME = "memory_use_rate";
    public static final String FS_CHART_NAME = "host_file_system";
    public static final String TIME_CHART_NAME = "host_time";
    public static final String PROCESS_CHART_NAME = "process_total";
    public static final String ORACLE_CHART_NAME = "oracle";

    // 安徽电信告警标识

    /** 未清除告警 **/
    public static final String ALARM_FLAG = "0";

    /** 已清除告警 **/
    public static final String CLEAN_FLAG = "1";
    
    /** 告警上报 1  **/
    public static final int NMS_ALARM_STATUS_REPORT = 1;
    /** 清除告警上报 0  **/
    public static final int NMS_ALARM_STATUS_CLEAR = 0;
    
    /** HTTP告警上报告警来源 **/
    public static final String HTTP_ALARM_SOURCE = "OMC";

    /** HTTP告警上报 0 **/
    public static final int HTTP_ALARM_STATUS_REPORT = 0;
    /** HTTP清除告警上报 1 **/
    public static final int HTTP_ALARM_STATUS_CLEAR = 1;

    /** 告警上报标识 **/
    public static final String ALARM_REPORT_RULE = "1";

    public static final String GZCM_TRAP_EMPTY = ".1.3.6.1.4.1.5900.4901.1.4.1.1";

    /** 产生告警的主机IP **/
    public static final String GZCM_TRAP_ALARM_IP = ".1.3.6.1.4.1.5900.4901.1.4.1.13";

    /** 告警ID **/
    public static final String GZCM_TRAP_ALARM_ID = ".1.3.6.1.4.1.5900.4901.1.4.1.11";

    /** 告警内容 **/
    public static final String GZCM_TRAP_ALARM_CONTENT = ".1.3.6.1.4.1.5900.4901.1.4.1.12";

    /** 告警类型：1 则为告警 0 则为消除告警 **/
    public static final String GZCM_TRAP_ALARM_TYPE = ".1.3.6.1.4.1.5900.4901.1.4.1.14";
}
