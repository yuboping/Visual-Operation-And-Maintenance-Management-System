package com.asiainfo.lcims.omc.alarm.model;

import java.util.Locale;

import org.joda.time.DateTime;

/**
 * syslog方式上报告警信息
 * 
 * @author luohuawuyin
 *
 */
public class AlarmSyslog {
    private String sendtime = new DateTime().toString("MMM dd HH:mm:ss", Locale.UK);
    private String device;
    private String alarmtype = "RADIUS_PERF_ALARM";
    private AlarmDetail detail;

    public String getSendtime() {
        return sendtime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAlarmtype() {
        return alarmtype;
    }

    public AlarmDetail getDetail() {
        return detail;
    }

    public void setDetail(AlarmDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "<186>" + sendtime + " " + device + " " + alarmtype + ": " + detail.toString();
    }
}
