package com.asiainfo.lcims.omc.model.hncu;

import java.util.List;

import com.asiainfo.lcims.omc.persistence.po.MonHost;

public class SwitchBean {
	
	 private String hostid;
	 
	 private String addr;
	 
	 private String hostname;
	
	 private List<MonHost> host;

	public String getHostid() {
		return hostid;
	}

	public void setHostid(String hostid) {
		this.hostid = hostid;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public List<MonHost> getHost() {
		return host;
	}

	public void setHost(List<MonHost> host) {
		this.host = host;
	}
	
	

}
