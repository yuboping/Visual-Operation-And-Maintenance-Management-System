package com.asiainfo.lcims.omc.model.business;

public class MdMetricDataSingle {
	private String hostid;
	private String metric_id;
	private String mvalue;
	private String item;
	private String itemname;
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
	public String getMvalue() {
		return mvalue;
	}
	public void setMvalue(String mvalue) {
		this.mvalue = mvalue;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
    public String getItemname() {
        return itemname;
    }
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
