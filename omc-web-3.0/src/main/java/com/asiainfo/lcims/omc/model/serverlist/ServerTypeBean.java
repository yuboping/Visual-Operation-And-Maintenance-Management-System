package com.asiainfo.lcims.omc.model.serverlist;

import java.util.List;

public class ServerTypeBean {
	
	private String serverType;
	
	private String serverName;
	
	private List<HostBean> hostList;

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
	

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public List<HostBean> getHostList() {
		return hostList;
	}

	public void setHostList(List<HostBean> hostList) {
		this.hostList = hostList;
	}
	
	

}
