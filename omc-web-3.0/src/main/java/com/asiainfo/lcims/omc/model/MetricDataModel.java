package com.asiainfo.lcims.omc.model;

/**
 * 指标数据对象某指标下，在当天24小时的记录值。 shour是指标groupby对象
 * 
 * @author nuannuan
 * 
 */
public class MetricDataModel {
    /**
     * 如 00,01，。。。23 (00:00,00:01)
     */
    String showtime;

    String label;
    /**
     * 小数点两位。
     */
    String mvalue;

    private int metricid;

    private String classtype;
    private int moduleid;
    private String item;
    private String stime;

    private String attribute;
    private Integer hostid;
    private String nodeid;
    private Integer cycleid;
    private Integer sourcetype;
    private String hostname;

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getMvalue() {
        return mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMetricid() {
        return metricid;
    }

    public void setMetricid(int metricid) {
        this.metricid = metricid;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public int getModuleid() {
        return moduleid;
    }

    public void setModuleid(int moduleid) {
        this.moduleid = moduleid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
