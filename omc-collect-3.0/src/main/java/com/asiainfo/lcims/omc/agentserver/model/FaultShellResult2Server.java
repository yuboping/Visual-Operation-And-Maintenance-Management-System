package com.asiainfo.lcims.omc.agentserver.model;

public class FaultShellResult2Server extends BaseData {
    private String id;
    private String alarmFaultId;
    private Integer faultState; // 执行状态
    private String shell;
    private String shellResult;// 对应脚本的执行结果
    
    public String getShellResult() {
        return shellResult;
    }

    public void setShellResult(String shellResult) {
        this.shellResult = shellResult;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

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

    public Integer getFaultState() {
        return faultState;
    }

    public void setFaultState(Integer faultState) {
        this.faultState = faultState;
    }

}
