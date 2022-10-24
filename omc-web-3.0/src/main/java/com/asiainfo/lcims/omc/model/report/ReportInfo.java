package com.asiainfo.lcims.omc.model.report;

public class ReportInfo {
    private String reportId;
    private String reportType;
    private String datasourceId;
    private String reportName;
    private String dayReportSql;
    private String weekReportSql;
    private String monthReportSql;
    private String description;
    private String createTime;
    private String menuTreeName;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDayReportSql() {
        return dayReportSql;
    }

    public void setDayReportSql(String dayReportSql) {
        this.dayReportSql = dayReportSql;
    }

    public String getWeekReportSql() {
        return weekReportSql;
    }

    public void setWeekReportSql(String weekReportSql) {
        this.weekReportSql = weekReportSql;
    }

    public String getMonthReportSql() {
        return monthReportSql;
    }

    public void setMonthReportSql(String monthReportSql) {
        this.monthReportSql = monthReportSql;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMenuTreeName() {
        return menuTreeName;
    }

    public void setMenuTreeName(String menuTreeName) {
        this.menuTreeName = menuTreeName;
    }

}
