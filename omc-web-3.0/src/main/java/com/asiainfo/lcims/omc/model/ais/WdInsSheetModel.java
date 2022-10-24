package com.asiainfo.lcims.omc.model.ais;

import java.util.List;

public class WdInsSheetModel {
    private String id;
    private String groupid;
    private String sheetname;
    private String sheettype;
    private String selectsql;
    private List<WdInsSheetHeaderModel> sheetheaderlist;

    private List<AisGroupMetricModel> groupMetrics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getSheetname() {
        return sheetname;
    }

    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    public String getSheettype() {
        return sheettype;
    }

    public void setSheettype(String sheettype) {
        this.sheettype = sheettype;
    }

    public String getSelectsql() {
        return selectsql;
    }

    public void setSelectsql(String selectsql) {
        this.selectsql = selectsql;
    }

    public List<WdInsSheetHeaderModel> getSheetheaderlist() {
        return sheetheaderlist;
    }

    public void setSheetheaderlist(List<WdInsSheetHeaderModel> sheetheaderlist) {
        this.sheetheaderlist = sheetheaderlist;
    }

    public List<AisGroupMetricModel> getGroupMetrics() {
        return groupMetrics;
    }

    public void setGroupMetrics(List<AisGroupMetricModel> groupMetrics) {
        this.groupMetrics = groupMetrics;
    }
}
