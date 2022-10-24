package com.asiainfo.lcims.omc.service.param;

/**
 * 告警规则取值分类
 */
public enum RuleValue {
    NEWEST("newest", "当前值"),
    PRE5MIN("lastest","上个周期值"),
    YESCURRENT("yescurrent","昨日同一时刻采集值"),
    ALLIN12HOUR("collect","采集值"),
    AIS_NOT_ALARM("right","正常"),
    AIS_IN_ALARM("error","异常"),
    ;
    private String type;
    private String name;

    RuleValue(String type,String name) {
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
