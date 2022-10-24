package com.asiainfo.lcims.omc.model.sos;

/**
 * 校验3A token返回
 * @author zhul
 *
 */
public class SosCheckTokenResponse {
    private String token;
    private String userName;
    private String roleId;
    private String mesCode;
    
    
    public String getMesCode() {
		return mesCode;
	}
	public void setMesCode(String mesCode) {
		this.mesCode = mesCode;
	}
	public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
