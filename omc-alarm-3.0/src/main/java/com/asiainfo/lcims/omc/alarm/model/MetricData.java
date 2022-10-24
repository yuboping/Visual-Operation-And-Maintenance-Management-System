package com.asiainfo.lcims.omc.alarm.model;

public class MetricData {
    private String classtype;
    private Integer moduleid;
    private String item;
    private String attribute;
    private Integer metricid;
    private String mvalue;
    private Integer hostid;
    private String nodeid;
    private String stime;
    private Integer cycleid;
    private Integer sourcetype;

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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getMetricid() {
        return metricid;
    }

    public void setMetricid(Integer metricid) {
        this.metricid = metricid;
    }

    public String getMvalue() {
        return mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }

    public Integer getHostid() {
        return hostid;
    }

    public void setHostid(Integer hostid) {
        this.hostid = hostid;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
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

}
