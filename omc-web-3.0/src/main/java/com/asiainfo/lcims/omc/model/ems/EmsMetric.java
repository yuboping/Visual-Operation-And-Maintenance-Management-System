package com.asiainfo.lcims.omc.model.ems;

public class EmsMetric {
    private String metricIdnntity;
    private String shell;
    private String params;
    private Integer returnType;
    public String getMetricIdnntity() {
        return metricIdnntity;
    }
    public void setMetricIdnntity(String metricIdnntity) {
        this.metricIdnntity = metricIdnntity;
    }
    public String getShell() {
        return shell;
    }
    public void setShell(String shell) {
        this.shell = shell;
    }
    public String getParams() {
        return params;
    }
    public void setParams(String params) {
        this.params = params;
    }
    public Integer getReturnType() {
        return returnType;
    }
    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }
}
