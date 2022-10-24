package com.asiainfo.lcims.omc.alarm.param;

/**
 * 告警信息上报方式
 * 
 * @author luohuawuyin
 *
 */
public enum SendMode {
    SYSLOG(2),
    EMAIL(1),
    SMS(3),
    SNMPTRAP(4),
    //甘肃移动 NMS 上报
    REPORTNMS(5),
    // HTTP告警上报
    REPORTHTTP(6);
    private int type;

    SendMode(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
