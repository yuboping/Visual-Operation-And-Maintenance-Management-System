package com.asiainfo.lcims.omc.alarm.model;

/**
 * 每日限流开启通知
 */
public class DailyLimitData {

    private String stype;

    private String otype;

    private String wid;

    private String apn;

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

}
