package com.asiainfo.lcims.omc.model;

import java.util.Date;

public class VerifyCode{
	
	private String id;
	
	private String admin;
	
	private String verifyCode;
	
	private int verifyType;
	
	private String verifyAdd;
	
	private int isApply;
	
	private String createTime;
	
	private String updateTime;
	
	private Date sendDate;
	private String sendTarget;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public int getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(int verifyType) {
		this.verifyType = verifyType;
	}

	public String getVerifyAdd() {
		return verifyAdd;
	}

	public void setVerifyAdd(String verifyAdd) {
		this.verifyAdd = verifyAdd;
	}

	public int getIsApply() {
		return isApply;
	}

	public void setIsApply(int isApply) {
		this.isApply = isApply;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendTarget() {
        return sendTarget;
    }

    public void setSendTarget(String sendTarget) {
        this.sendTarget = sendTarget;
    }
	

}
