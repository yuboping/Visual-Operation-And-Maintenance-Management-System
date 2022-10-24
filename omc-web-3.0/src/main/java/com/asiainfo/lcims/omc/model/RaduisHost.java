package com.asiainfo.lcims.omc.model;

import java.util.List;

import com.asiainfo.lcims.omc.model.configmanage.MdNode;

public class RaduisHost {
	private int hostnum;
	private List<MdNode> nodelist;
	public int getHostnum() {
		return hostnum;
	}
	public void setHostnum(int hostnum) {
		this.hostnum = hostnum;
	}
	public List<MdNode> getNodelist() {
		return nodelist;
	}
	public void setNodelist(List<MdNode> nodelist) {
		this.nodelist = nodelist;
	}
}
