package com.asiainfo.lcims.omc.agentserver.model;

public class HeartMsg extends BaseData {
    private String dateTimeStr;
    private String msg;

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
