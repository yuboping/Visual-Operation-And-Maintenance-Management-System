package com.asiainfo.lcims.omc.report.protocol;

public class ProtocolDetail {
    private ProtocolType type;
    private String serverIP;
    private int serverPort = 22;
    private String loginName;
    private String password;
    private int retry = 3;
    
    private String protocolType = "sftp";
    
    /** 上传文件完成后是否上报.ok文件 */
    private boolean uploadOk = false;
    
    /** 上传文件完成后是否追加文件后缀 */
    private boolean addSuffix = false;
    
    private String suffix = "";
    
    public ProtocolDetail() {
    }
    
    public ProtocolDetail(ProtocolType type) {
        this.type = type;
    }
    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProtocolType getType() {
        return type;
    }

    public void setType(ProtocolType type) {
        this.type = type;
    }
    public int getRetry() {
        return retry;
    }
    public void setRetry(int retry) {
        this.retry = retry;
    }

    public boolean isUploadOk() {
        return uploadOk;
    }

    public void setUploadOk(boolean uploadOk) {
        this.uploadOk = uploadOk;
    }
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

    public boolean isAddSuffix() {
        return addSuffix;
    }

    public void setAddSuffix(boolean addSuffix) {
        this.addSuffix = addSuffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
