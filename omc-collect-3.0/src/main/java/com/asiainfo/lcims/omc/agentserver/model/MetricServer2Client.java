package com.asiainfo.lcims.omc.agentserver.model;

public class MetricServer2Client extends BaseData {
    private String metricid;
    private String shell;
    private String params;
    private String expr;
    private String resulttype;

    public String getMetricid() {
        return metricid;
    }

    public void setMetricid(String metricid) {
        this.metricid = metricid;
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

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public String getResulttype() {
        return resulttype;
    }

    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }

    @Override
    public String toString() {
        return "MetricServer2Client [metricid=" + metricid + ", shell=" + shell + ", params="
                + params + ", expr=" + expr + ", resulttype=" + resulttype + "]";
    }

}
