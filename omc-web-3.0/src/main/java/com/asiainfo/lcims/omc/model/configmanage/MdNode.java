package com.asiainfo.lcims.omc.model.configmanage;

import java.util.List;

import com.asiainfo.lcims.omc.persistence.po.MonHost;

public class MdNode {
    private String id;

    private String node_name;

    private String description;

    private String checked;

    private String disabled;
    
    private List<MonHost> hostlist;
    private int alarmnum;//告警数量
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public List<MonHost> getHostlist() {
        return hostlist;
    }

    public void setHostlist(List<MonHost> hostlist) {
        this.hostlist = hostlist;
    }

    public int getAlarmnum() {
        return alarmnum;
    }

    public void setAlarmnum(int alarmnum) {
        this.alarmnum = alarmnum;
    }
}