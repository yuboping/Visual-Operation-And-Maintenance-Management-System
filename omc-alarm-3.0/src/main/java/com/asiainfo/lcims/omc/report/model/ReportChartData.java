package com.asiainfo.lcims.omc.report.model;


public class ReportChartData {
    private String mark;
    private String metricid;
    private String value;
    private String attr;
    private String item;
    private String attr1;
    private String stime;
    
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getAttr() {
        return attr;
    }
    public void setAttr(String attr) {
        this.attr = attr;
    }
    public String getMetricid() {
        return metricid;
    }
    public void setMetricid(String metricid) {
        this.metricid = metricid;
    }
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }
}
