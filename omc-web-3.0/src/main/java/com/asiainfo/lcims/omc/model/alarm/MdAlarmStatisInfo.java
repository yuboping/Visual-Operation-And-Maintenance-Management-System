package com.asiainfo.lcims.omc.model.alarm;

import org.apache.commons.lang.StringUtils;

/**
 * 告警信息表实体类
 */
public class MdAlarmStatisInfo {

    private String alarm_time;
    private String alarm_level_normal;
    private String alarm_level_warn;
    private String alarm_level_serious;

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getAlarm_level_normal() {
        return alarm_level_normal;
    }

    public void setAlarm_level_normal(String alarm_level_normal) {
        this.alarm_level_normal = alarm_level_normal;
    }

    public String getAlarm_level_warn() {
        return alarm_level_warn;
    }

    public void setAlarm_level_warn(String alarm_level_warn) {
        this.alarm_level_warn = alarm_level_warn;
    }

    public String getAlarm_level_serious() {
        return alarm_level_serious;
    }

    public void setAlarm_level_serious(String alarm_level_serious) {
        this.alarm_level_serious = alarm_level_serious;
    }

    @Override
    public String toString() {
        return "MdAlarmStatisInfo{" +
                "alarm_time='" + alarm_time + '\'' +
                ", alarm_level_normal='" + alarm_level_normal + '\'' +
                ", alarm_level_warn='" + alarm_level_warn + '\'' +
                ", alarm_level_serious='" + alarm_level_serious + '\'' +
                '}';
    }
}
