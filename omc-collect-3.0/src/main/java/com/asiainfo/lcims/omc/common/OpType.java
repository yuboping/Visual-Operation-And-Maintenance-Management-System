package com.asiainfo.lcims.omc.common;
/**
 * 定义采集客服端,采集客户端操作类型.
 * @author XHT
 *
 */
public enum OpType {
    HEART_SERVER_CLIENT("001","服务端向客户端发送心跳包"),
    HEART_CLIENT_SERVER("002","客户端向服务端发送心跳响应"),
    
    METRIC_WEB_AGENTSERVER("101", "WEB向采集服务端发送下发指标命令"),
    METRIC_AGENTSERVER_WEB("102", "采集服务端返回下发指标结果给WEB"),
    METRIC_AGENTSERVER_CLIENT("103", "采集服务端向对客户端下发指标信息"),
    METRIC_AGENTCLIENT_SERVER("104", "采集客户端向服务端返回指标下发结果"),
    METRICRESULT_CLIENT_SERVER("105", "采集客户端向服务端发送指标脚本执行的结果"),
    METRIC_AGENTCLIENT_APPLY("106", "采集客户端向服务端申请指标"),
    
    SHELL_WEB_AGENTSERVER("201", "WEB向采集服务端发送下发脚本的命令"),
    SHELL_AGENTSERVER_WEB("202", "采集服务端返回脚本执行结果"),
    SHELL_AGENTSERVER_CLIENT("203", "采集服务端向客户端下发执行脚本命令"),
    SHELL_AGENTCLIENT_SERVER("204", "采集客户端向服务端返回脚本执行结果"),
    
    //一键应急功能：执行故障处理操作
    SHELL_FAULT_WEB_AGENTSERVER("301", "一键应急：WEB向采集服务端发送下发脚本的命令"),
    SHELL_FAULT_AGENTSERVER_WEB("302", "一键应急：采集服务端返回脚本执行结果"),
    SHELL_FAULT_AGENTSERVER_CLIENT("303", "一键应急：采集服务端向客户端下发执行脚本命令"),
    SHELL_FAULT_AGENTCLIENT_SERVER("304", "一键应急：采集客户端向服务端返回脚本执行结果"),
    
    // RADIUS 一键应急功能，调用radius接口
    RADIUS_OPERATE_WEB_AGENTSERVER("401","RADIUS: 一键应急 WEB向采集服务端发送调用radius socket接口命令"),
    RADIUS_OPERATE_AGENTSERVER_WEB("402","RADIUS: 一键应急 采集服务端向WEB端发送执行结果")
    ;
    
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
