package com.asiainfo.lcims.omc.common;

public enum MetricStatus {
    need_send("0", "待下发"), send_client_sucess("1", "下发成功"), need_delete("3", "待删除"),;

    private String type;
    private String name;

    MetricStatus(String type, String name) {
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
