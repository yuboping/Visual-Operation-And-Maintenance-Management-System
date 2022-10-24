package com.asiainfo.lcims.omc.alarm.model;
/**
 * 采集周期对应当前周期时间
 * @author zhul
 *
 */
public class MdCollCycleTime {
	private Integer cycleid;
	private String currenttime;
	private String cyclename;
	public Integer getCycleid() {
		return cycleid;
	}
	public void setCycleid(Integer cycleid) {
		this.cycleid = cycleid;
	}
	public String getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(String currenttime) {
		this.currenttime = currenttime;
	}
	public String getCyclename() {
		return cyclename;
	}
	public void setCyclename(String cyclename) {
		this.cyclename = cyclename;
	}
	
	@Override
	public String toString() {
		return "MdCollCycleTime: {cycleid="+cycleid+",cyclename="+cyclename+",currenttime="+currenttime+"}";
	}
}
