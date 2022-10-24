package com.asiainfo.lcims.util;

public class MetricUtil {
    
    public static final String RULE_ALARM_LASTVAL = "alarm_lastval";
    
    /** olt在线用户数指标标识  olt_onlineuser aggregate_olt */
    public static final String METRIC_OLT = "aggregate_olt";
    
    /** pon在线用户数指标标识 */
    public static final String METRIC_PON = "pon_onlineuser";
    
    public static final String SpecificProblemID_OLT = "1";
    
    public static final String SpecificProblemID_PON = "2";
    
    public static String getSpecificProblemID(String metricIdentity) {
        if(metricIdentity.equals(METRIC_OLT))
            return SpecificProblemID_OLT;
        if(metricIdentity.equals(METRIC_PON))
            return SpecificProblemID_OLT;
        return "0";
    }
    
}
