package com.asiainfo.lcims.omc.persistence.po.ais;

public class INSThreshold {
    private Integer alarmid;
    private String classtype;
    private Integer moduleid;
    private String item;
    private Integer metricid;
    private String alarmexpr;
    private Integer alarmlevel;
    private String modes;
    private String modeattrs;
    private String alarmmsg;
    private Integer cycleid;
    private Integer sourcetype;
    private String average;
    private String mvalue;
    
    private String newest;
    private String rulevalue;
    private String solvemsg;

    public Integer getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(Integer alarmid) {
        this.alarmid = alarmid;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public Integer getModuleid() {
        return moduleid;
    }

    public void setModuleid(Integer moduleid) {
        this.moduleid = moduleid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getMetricid() {
        return metricid;
    }

    public void setMetricid(Integer metricid) {
        this.metricid = metricid;
    }

    public String getAlarmexpr() {
        return alarmexpr;
    }

    public void setAlarmexpr(String alarmexpr) {
        this.alarmexpr = alarmexpr;
    }

    public Integer getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(Integer alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public String getModes() {
        return modes;
    }

    public void setModes(String modes) {
        this.modes = modes;
    }

    public String getModeattrs() {
        return modeattrs;
    }

    public void setModeattrs(String modeattrs) {
        this.modeattrs = modeattrs;
    }

    public String getAlarmmsg() {
        return alarmmsg;
    }

    public void setAlarmmsg(String alarmmsg) {
        this.alarmmsg = alarmmsg;
    }

    public Integer getCycleid() {
        return cycleid;
    }

    public void setCycleid(Integer cycleid) {
        this.cycleid = cycleid;
    }

    public Integer getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Integer sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getMvalue() {
        return mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }

    public String getNewest() {
        return newest;
    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    public String getRulevalue() {
        return rulevalue;
    }

    public void setRulevalue(String rulevalue) {
        this.rulevalue = rulevalue;
    }

    public String getSolvemsg() {
        return solvemsg;
    }

    public void setSolvemsg(String solvemsg) {
        this.solvemsg = solvemsg;
    }
}
