package com.asiainfo.lcims.omc.model.gdcu;

public class GdcuReportInfo {

    private String id;

    private String groupid;

    private String sheet_type;

    private String sheet_name;

    private String reportremarks;

    private String selectsql;

    private String createtime;

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

    public String getSheet_type() {
        return sheet_type;
    }

    public void setSheet_type(String sheet_type) {
        this.sheet_type = sheet_type;
    }

    public String getSheet_name() {
        return sheet_name;
    }

    public void setSheet_name(String sheet_name) {
        this.sheet_name = sheet_name;
    }

    public String getReportremarks() {
        return reportremarks;
    }

    public void setReportremarks(String reportremarks) {
        this.reportremarks = reportremarks;
    }

    public String getSelectsql() {
        return selectsql;
    }

    public void setSelectsql(String selectsql) {
        this.selectsql = selectsql;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "GdcuReportInfo [id=" + id + ", groupid=" + groupid + ", sheet_type=" + sheet_type
                + ", sheet_name=" + sheet_name + ", reportremarks=" + reportremarks + ", selectsql="
                + selectsql + ", createtime=" + createtime + "]";
    }

}
