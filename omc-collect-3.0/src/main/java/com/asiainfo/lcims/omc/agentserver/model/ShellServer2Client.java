package com.asiainfo.lcims.omc.agentserver.model;

public class ShellServer2Client extends BaseData {
    private String id;// PROCESS_OPRATE
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
}
