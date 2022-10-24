package com.asiainfo.lcims.omc.model.business;

public class MdHostPerformanceALarm {
	private String hostid;
	private String metric_id;
	private String attr;
	private String alarm_level;
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getMetric_id() {
		return metric_id;
	}
	public void setMetric_id(String metric_id) {
		this.metric_id = metric_id;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
    public String getAlarm_level() {
        return alarm_level;
    }
    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }
	
}
