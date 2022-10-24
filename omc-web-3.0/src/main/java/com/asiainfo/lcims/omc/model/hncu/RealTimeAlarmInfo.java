package com.asiainfo.lcims.omc.model.hncu;

/**
 * MD_REAL_ALARM_INFO表对应的java类
 * 
 * @author zhujiansheng
 * @date 2020年12月22日 下午5:11:14
 * @version V1.0
 */
public class RealTimeAlarmInfo {

    private String uuid;

    private String alarmmsg;

    private String alarmtype;

    private String createtime;

    private String hostname;

    private String alarmid;

    private String alarmlevel;

    private String clearstatus;

    private String cleartime;

    private String update;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAlarmmsg() {
        return alarmmsg;
    }

    public void setAlarmmsg(String alarmmsg) {
        this.alarmmsg = alarmmsg;
    }

    public String getAlarmtype() {
        return alarmtype;
    }

    public void setAlarmtype(String alarmtype) {
        this.alarmtype = alarmtype;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(String alarmid) {
        this.alarmid = alarmid;
    }

    public String getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(String alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public String getClearstatus() {
        return clearstatus;
    }

    public void setClearstatus(String clearstatus) {
        this.clearstatus = clearstatus;
    }

    public String getCleartime() {
        return cleartime;
    }

    public void setCleartime(String cleartime) {
        this.cleartime = cleartime;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

}
