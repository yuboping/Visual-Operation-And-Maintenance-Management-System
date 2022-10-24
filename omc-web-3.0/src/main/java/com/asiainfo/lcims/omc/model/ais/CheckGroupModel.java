package com.asiainfo.lcims.omc.model.ais;

import java.util.List;

public class CheckGroupModel {

    private String groupid;
    private String groupname;
    // 数据库状态
    private String status;

    private String icon;

    private List<CheckItemModel> items;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CheckItemModel> getItems() {
        return items;
    }

    public void setItems(List<CheckItemModel> items) {
        this.items = items;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
