package com.asiainfo.lcims.omc.alarm.model;

/**
 * syslog方式上报告警信息中详细告警信息
 */
public class AlarmDetail {
    private String hostname;
    private String hostip;
    private String alarmdesc;
    private String alarmlevel;
    private String alarmtime;
    private String uniquemark;
    private String flag = "active";

    public String getAlarmdesc() {
        return alarmdesc;
    }

    public void setAlarmdesc(String alarmdesc) {
        this.alarmdesc = alarmdesc;
    }

    public String getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(String alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }

    public String getUniquemark() {
        return uniquemark;
    }

    public void setUniquemark(String uniquemark) {
        this.uniquemark = uniquemark;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    @Override
    public String toString() {
        return hostname + "," + alarmdesc + "," + alarmlevel + "," + alarmtime + ","
                + uniquemark + "," + flag;
    }
}
