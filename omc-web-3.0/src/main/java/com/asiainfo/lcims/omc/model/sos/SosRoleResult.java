package com.asiainfo.lcims.omc.model.sos;

import java.util.List;

public class SosRoleResult extends SosBase{

	private static final long serialVersionUID = 1L;

	private List<SosRoleBean> rolelist;

	public List<SosRoleBean> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<SosRoleBean> rolelist) {
		this.rolelist = rolelist;
	}
		
}
