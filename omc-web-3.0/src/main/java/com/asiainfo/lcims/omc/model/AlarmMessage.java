package com.asiainfo.lcims.omc.model;

import java.util.List;

/**
 * mark,value,areano,alarmNum,alarmInfos
 * 
 */

public class AlarmMessage {
    private String mark;
    private String value;
    private Integer alarmNum;
    private List<MessageData> data;

    public AlarmMessage() {

    }
    
    public AlarmMessage(String mark, String value) {
        this.mark = mark;
        this.value = value;
    }
    
    public AlarmMessage(String mark, String value, Integer alarmNum) {
        this.mark = mark;
        this.value = value;
        this.alarmNum = alarmNum;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(Integer alarmNum) {
        this.alarmNum = alarmNum;
    }

    public List<MessageData> getData() {
        return data;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }

}
