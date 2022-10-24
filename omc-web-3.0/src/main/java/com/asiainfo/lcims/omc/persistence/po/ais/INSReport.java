package com.asiainfo.lcims.omc.persistence.po.ais;

import java.sql.Timestamp;

public class INSReport {
    private String id;
    private String title;
    private Timestamp create_time;
    private String reportlink;
    private String showcreatetime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportlink() {
        return reportlink;
    }

    public void setReportlink(String reportlink) {
        this.reportlink = reportlink;
    }

    public String getShowcreatetime() {
        return showcreatetime;
    }

    public void setShowcreatetime(String showcreatetime) {
        this.showcreatetime = showcreatetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

}
