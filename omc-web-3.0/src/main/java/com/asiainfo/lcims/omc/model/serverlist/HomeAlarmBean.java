package com.asiainfo.lcims.omc.model.serverlist;



public class HomeAlarmBean {

	private String alarmId;
	private String url;
	private String dimensionType;
	private String alarmLevel;
	private String alarmMsg;
	private String dimension1;
	private String dimension2;
	private String dimension1Name;
	private String dimension2Name;
	private String serverType;
	private String metricId;
	private String hostName;
	private String nodeName;
    private String lastTime;
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDimensionType() {
		return dimensionType;
	}
	public void setDimensionType(String dimensionType) {
		this.dimensionType = dimensionType;
	}
	public String getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	public String getDimension1() {
		return dimension1;
	}
	public void setDimension1(String dimension1) {
		this.dimension1 = dimension1;
	}
	public String getDimension2() {
		return dimension2;
	}
	public void setDimension2(String dimension2) {
		this.dimension2 = dimension2;
	}
	public String getDimension1Name() {
		return dimension1Name;
	}
	public void setDimension1Name(String dimension1Name) {
		this.dimension1Name = dimension1Name;
	}
	public String getDimension2Name() {
		return dimension2Name;
	}
	public void setDimension2Name(String dimension2Name) {
		this.dimension2Name = dimension2Name;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getMetricId() {
		return metricId;
	}
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
