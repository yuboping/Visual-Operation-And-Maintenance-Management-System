package com.asiainfo.lcims.omc.model.ais;

public class WdInsSheetHeaderModel {
    private String id;
    private String sheetid;
    private String headertype;
    private String isusesql;
    private String selectsql;
    private String content;
    private String firstrow;
    private String lastrow;
    private String firstcol;
    private String lastcol;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSheetid() {
        return sheetid;
    }

    public void setSheetid(String sheetid) {
        this.sheetid = sheetid;
    }

    public String getHeadertype() {
        return headertype;
    }

    public void setHeadertype(String headertype) {
        this.headertype = headertype;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstrow() {
        return firstrow;
    }

    public void setFirstrow(String firstrow) {
        this.firstrow = firstrow;
    }

    public String getLastrow() {
        return lastrow;
    }

    public void setLastrow(String lastrow) {
        this.lastrow = lastrow;
    }

    public String getFirstcol() {
        return firstcol;
    }

    public void setFirstcol(String firstcol) {
        this.firstcol = firstcol;
    }

    public String getLastcol() {
        return lastcol;
    }

    public void setLastcol(String lastcol) {
        this.lastcol = lastcol;
    }
}
