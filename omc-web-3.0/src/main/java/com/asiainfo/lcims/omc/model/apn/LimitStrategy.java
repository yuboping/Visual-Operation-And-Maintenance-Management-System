package com.asiainfo.lcims.omc.model.apn;

/**
 * 限流策略同步
 */
public class LimitStrategy {

    private String stype;

    private String otype;

    private String wid;

    private String apn;

    private String limitvalid;

    private String limitcycle;

    private String auththreshold;

    private String logthreshold;

    private String recordthreshold;

    private String limitthreshold;

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

    public String getLimitvalid() {
        return limitvalid;
    }

    public void setLimitvalid(String limitvalid) {
        this.limitvalid = limitvalid;
    }

    public String getLimitcycle() {
        return limitcycle;
    }

    public void setLimitcycle(String limitcycle) {
        this.limitcycle = limitcycle;
    }

    public String getAuththreshold() {
        return auththreshold;
    }

    public void setAuththreshold(String auththreshold) {
        this.auththreshold = auththreshold;
    }

    public String getLogthreshold() {
        return logthreshold;
    }

    public void setLogthreshold(String logthreshold) {
        this.logthreshold = logthreshold;
    }

    public String getRecordthreshold() {
        return recordthreshold;
    }

    public void setRecordthreshold(String recordthreshold) {
        this.recordthreshold = recordthreshold;
    }

    public String getLimitthreshold() {
        return limitthreshold;
    }

    public void setLimitthreshold(String limitthreshold) {
        this.limitthreshold = limitthreshold;
    }

}
