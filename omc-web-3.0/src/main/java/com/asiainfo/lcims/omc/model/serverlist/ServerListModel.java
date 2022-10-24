package com.asiainfo.lcims.omc.model.serverlist;

import java.util.List;

public class ServerListModel {
	
	private String nodeName;
	
	private String nodeId;

	private List<ServerTypeBean> serverType;
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<ServerTypeBean> getServerType() {
		return serverType;
	}

	public void setServerType(List<ServerTypeBean> serverType) {
		this.serverType = serverType;
	}
	
	
	

}
