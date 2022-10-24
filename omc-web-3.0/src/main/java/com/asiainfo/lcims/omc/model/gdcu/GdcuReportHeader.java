package com.asiainfo.lcims.omc.model.gdcu;

public class GdcuReportHeader {

    private String id;

    private String groupid;

    private String sheet_type;

    private String sheet_name;

    private String isusesql;

    private String selectsql;

    private String headercontent;

    private Integer firstrow;

    private Integer lastrow;

    private Integer firstcol;

    private Integer lastcol;

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

    public String getIsusesql() {
        return isusesql;
    }

    public void setIsusesql(String isusesql) {
        this.isusesql = isusesql;
    }

    public String getSelectsql() {
        return selectsql;
    }

    public void setSelectsql(String selectsql) {
        this.selectsql = selectsql;
    }

    public String getHeadercontent() {
        return headercontent;
    }

    public void setHeadercontent(String headercontent) {
        this.headercontent = headercontent;
    }

    public Integer getFirstrow() {
        return firstrow;
    }

    public void setFirstrow(Integer firstrow) {
        this.firstrow = firstrow;
    }

    public Integer getLastrow() {
        return lastrow;
    }

    public void setLastrow(Integer lastrow) {
        this.lastrow = lastrow;
    }

    public Integer getFirstcol() {
        return firstcol;
    }

    public void setFirstcol(Integer firstcol) {
        this.firstcol = firstcol;
    }

    public Integer getLastcol() {
        return lastcol;
    }

    public void setLastcol(Integer lastcol) {
        this.lastcol = lastcol;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "GdcuReportHeader [id=" + id + ", groupid=" + groupid + ", sheet_type=" + sheet_type
                + ", sheet_name=" + sheet_name + ", isusesql=" + isusesql + ", selectsql="
                + selectsql + ", headercontent=" + headercontent + ", firstrow=" + firstrow
                + ", lastrow=" + lastrow + ", firstcol=" + firstcol + ", lastcol=" + lastcol
                + ", createtime=" + createtime + "]";
    }

}
