package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 在线用户查询响应
 * 
 */
public class QueryOnlineUserResp2 extends BaseData {
    private String account;
    private String starttime;
    private String nasip;
    private String userip;
    private String mac;
    private String sessionid;
    private String lactiveTime;
    private String sessionTime;
    private String status;
    private String nasPort;

    public QueryOnlineUserResp2() {

    }
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getNasip() {
		return nasip;
	}
	public void setNasip(String nasip) {
		this.nasip = nasip;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public String getLactiveTime() {
		return lactiveTime;
	}
	public void setLactiveTime(String lactiveTime) {
		this.lactiveTime = lactiveTime;
	}
	public String getSessionTime() {
		return sessionTime;
	}
	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNasPort() {
		return nasPort;
	}
	public void setNasPort(String nasPort) {
		this.nasPort = nasPort;
	}
	
}
