package com.asiainfo.lcims.omc.agentserver.model;

public class FaultShellServer2Client extends BaseData {
    private String id;
    private String alarmFaultId;
    private String shell;// 脚本

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getAlarmFaultId() {
        return alarmFaultId;
    }

    public void setAlarmFaultId(String alarmFaultId) {
        this.alarmFaultId = alarmFaultId;
    }
}
