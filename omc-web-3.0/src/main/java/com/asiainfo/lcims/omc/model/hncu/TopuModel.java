package com.asiainfo.lcims.omc.model.hncu;

import java.util.List;

import com.asiainfo.lcims.omc.persistence.po.MonHost;

public class TopuModel {
	
	private List<MonHost> firewall;
	
	private List<SwitchBean> switchs;

	public List<MonHost> getFirewall() {
		return firewall;
	}

	public void setFirewall(List<MonHost> firewall) {
		this.firewall = firewall;
	}

	public List<SwitchBean> getSwitchs() {
		return switchs;
	}

	public void setSwitchs(List<SwitchBean> switchs) {
		this.switchs = switchs;
	}
	
	
	

}
