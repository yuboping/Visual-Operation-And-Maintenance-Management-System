package com.asiainfo.lcims.omc.model.business;

public class MdMetricValue {
	private String metricid;
	private String mvalue;
	private String item;
	private String itemname;
	private boolean alarmflag;
	private String alarm_level;
	public String getMetricid() {
		return metricid;
	}
	public MdMetricValue setMetricid(String metricid) {
		this.metricid = metricid;
		return this;
	}
	public String getMvalue() {
		return mvalue;
	}
	public MdMetricValue setMvalue(String mvalue) {
		this.mvalue = mvalue;
		return this;
	}
    public String getItem() {
        return item;
    }
    public MdMetricValue setItem(String item) {
        this.item = item;
        return this;
    }
    public String getItemname() {
        return itemname;
    }
    public MdMetricValue setItemname(String itemname) {
        this.itemname = itemname;
        return this;
    }
    public boolean isAlarmflag() {
        return alarmflag;
    }
    public MdMetricValue setAlarmflag(boolean alarmflag) {
        this.alarmflag = alarmflag;
        return this;
    }
    public String getAlarm_level() {
        return alarm_level;
    }
    public MdMetricValue setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
        return this;
    }
}
