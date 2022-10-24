package com.asiainfo.lcims.omc.agentserver.enity;

import java.sql.Timestamp;

public class AlarmFaultHandleHis {
    private String id;
    private String alarmFaultId;
    private String hostIp;
    private String faultScript;
    private Integer faultState;
    private String faultResult;
    private Timestamp createDate;
    private Timestamp updateDate;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAlarmFaultId() {
        return alarmFaultId;
    }
    public void setAlarmFaultId(String alarmFaultId) {
        this.alarmFaultId = alarmFaultId;
    }
    public String getHostIp() {
        return hostIp;
    }
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }
    public String getFaultScript() {
        return faultScript;
    }
    public void setFaultScript(String faultScript) {
        this.faultScript = faultScript;
    }
    public Integer getFaultState() {
        return faultState;
    }
    public void setFaultState(Integer faultState) {
        this.faultState = faultState;
    }
    public String getFaultResult() {
        return faultResult;
    }
    public void setFaultResult(String faultResult) {
        this.faultResult = faultResult;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
}
