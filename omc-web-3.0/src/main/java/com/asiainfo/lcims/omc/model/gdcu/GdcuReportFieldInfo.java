package com.asiainfo.lcims.omc.model.gdcu;

public class GdcuReportFieldInfo {

    private String id;

    private String reportid;

    private String showname;

    private String sqlfield;

    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportid() {
        return reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getSqlfield() {
        return sqlfield;
    }

    public void setSqlfield(String sqlfield) {
        this.sqlfield = sqlfield;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "GdcuReportFieldInfo [id=" + id + ", reportid=" + reportid + ", showname=" + showname
                + ", sqlfield=" + sqlfield + ", create_time=" + create_time + "]";
    }

}
