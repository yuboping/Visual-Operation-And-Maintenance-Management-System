package com.asiainfo.lcims.omc.alarm.model;
/**
 * 告警级别
 * @author zhul
 * MD_ALARM_LEVEL
 */
public class MdAlarmLevel {
	private String alarmlevel;
	private String alarmname;
	private String alarmlable;
	
	public String getAlarmlevel() {
		return alarmlevel;
	}
	public void setAlarmlevel(String alarmlevel) {
		this.alarmlevel = alarmlevel;
	}
	public String getAlarmname() {
		return alarmname;
	}
	public void setAlarmname(String alarmname) {
		this.alarmname = alarmname;
	}
	public String getAlarmlable() {
		return alarmlable;
	}
	public void setAlarmlable(String alarmlable) {
		this.alarmlable = alarmlable;
	}
}
