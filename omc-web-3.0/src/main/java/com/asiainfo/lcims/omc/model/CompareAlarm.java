package com.asiainfo.lcims.omc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 对比对象
 */
public class CompareAlarm {
    private String title;
    private String group;
    private List<AlarmMessage> data = new ArrayList<AlarmMessage>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AlarmMessage> getData() {
        return data;
    }

    public void setData(List<AlarmMessage> data) {
        this.data = data;
    }

}
