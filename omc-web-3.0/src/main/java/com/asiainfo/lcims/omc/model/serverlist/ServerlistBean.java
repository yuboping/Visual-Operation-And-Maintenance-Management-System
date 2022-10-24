package com.asiainfo.lcims.omc.model.serverlist;

public class ServerlistBean {
	
	private String nodeName;
	
	private String nodeId;
	
	private String serverType;
	
	private String serverName;
	
	private String hostId;
	
	private String hostName;
	
	private String addr;

	
	
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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}


	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		return "ServerlistBean [nodeName=" + nodeName + ", nodeId=" + nodeId + ", serverType=" + serverType
				+ ", serverName=" + serverName + ", hostId=" + hostId + ", hostName=" + hostName + ", addr=" + addr
				+ "]";
	}

}
