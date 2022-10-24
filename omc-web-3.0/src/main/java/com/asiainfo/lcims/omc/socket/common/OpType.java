package com.asiainfo.lcims.omc.socket.common;

public enum OpType {
    METRIC_WEB_AGENTSERVER("101", "WEB向采集服务端发送下发指标命令"),
    SHELL_WEB_AGENTSERVER("201", "WEB向采集服务端发送下发脚本的命令"), 
    
    SHELL_FAULT_WEB_AGENTSERVER("301", "一键应急：WEB向采集服务端发送下发脚本的命令"),
    SHELL_FAULT_AGENTSERVER_WEB("302", "一键应急：采集服务端返回脚本执行结果"),
    
    RADIUS_OPERATE_WEB_AGENTSERVER("401","RADIUS: 一键应急 WEB向采集服务端发送调用radius socket接口命令"),
    RADIUS_OPERATE_AGENTSERVER_WEB("402","RADIUS: 一键应急 采集服务端向WEB端发送执行结果");
    
    
    private String type;
    private String name;

    OpType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}