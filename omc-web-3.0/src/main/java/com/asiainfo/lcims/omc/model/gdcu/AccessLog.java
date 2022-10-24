package com.asiainfo.lcims.omc.model.gdcu;

/**
 * bms查询认证日志
 * @author ZP
 *
 */
public class AccessLog {

	private String username;
	private String domainname;
	private String logtime;
	private String svctype;
	private String operatetype;
	private String reason;
	private String nasip;
	private String nasport;
	private String porttype;
	private String frameip;
	private String callerid;
	private String calleeid;
	private String naslocation;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDomainname() {
		return domainname;
	}
	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}
	
	public String getLogtime() {
		return logtime;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNasip() {
		return nasip;
	}
	public void setNasip(String nasip) {
		this.nasip = nasip;
	}
	public String getNasport() {
		return nasport;
	}
	public void setNasport(String nasport) {
		this.nasport = nasport;
	}
	public String getPorttype() {
		return porttype;
	}
	public void setPorttype(String porttype) {
		this.porttype = porttype;
	}
	public String getFrameip() {
		return frameip;
	}
	public void setFrameip(String frameip) {
		this.frameip = frameip;
	}
	public String getCallerid() {
		return callerid;
	}
	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}
	public String getCalleeid() {
		return calleeid;
	}
	public void setCalleeid(String calleeid) {
		this.calleeid = calleeid;
	}
	public String getNaslocation() {
		return naslocation;
	}
	public void setNaslocation(String naslocation) {
		this.naslocation = naslocation;
	}
    public String getSvctype() {
        return svctype;
    }
    public void setSvctype(String svctype) {
        this.svctype = svctype;
    }
    public String getOperatetype() {
        return operatetype;
    }
    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }
}
