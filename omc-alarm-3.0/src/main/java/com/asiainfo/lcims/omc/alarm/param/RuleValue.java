package com.asiainfo.lcims.omc.alarm.param;

/**
 * 告警规则取值分类
 * 
 * @author luohuawuyin
 *
 */
public enum RuleValue {
    NEWEST("newest", "当前值"),
    PRE5MIN("pre5min","5分钟前采集值"),
    YESCURRENT("yescurrent","昨日同一时刻采集值"),
    ALLIN12HOUR("allin12hour","12小时内数据总量"),
    AVGINDAYS("avgindays","3天内平均值"),
    COLLECT("collect", "当前值"),
    LASTEST("lastest","上个周期值"),
    ALARM_LASTEST("alarm_lastval","告警发生时上个周期值"),
    GREATER_THAN(">","大于"),
    GREATER_THAN_EAUAL(">=","大于等于"),
    LESS_THAN("<","小于"),
    LESS_THAN_EAUAL("<=","小于等于"),
    LOGIC_AND("&&","且"),
    LOGIC_OR("||","或"),
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
