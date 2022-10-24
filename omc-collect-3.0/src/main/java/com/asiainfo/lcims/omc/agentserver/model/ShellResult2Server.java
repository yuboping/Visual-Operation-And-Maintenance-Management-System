package com.asiainfo.lcims.omc.agentserver.model;

public class ShellResult2Server extends BaseData {

    private String id;// PROCESS_OPRATE 操作表中的ID
    private String shellResult;// 对应脚本的执行结果

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShellResult() {
        return shellResult;
    }

    public void setShellResult(String shellResult) {
        this.shellResult = shellResult;
    }

}
