package com.asiainfo.lcims.omc.alarm.param;

public enum ClassType {
    CITY("city", "城市"),
    SERVICE("service", "服务平台"),
    NETDEVICE("netdevice", "网元设备"),
    ;
    private String type;
    private String name;

    ClassType(String type, String name) {
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
