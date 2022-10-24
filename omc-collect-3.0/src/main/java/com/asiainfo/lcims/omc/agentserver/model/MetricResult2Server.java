package com.asiainfo.lcims.omc.agentserver.model;

public class MetricResult2Server extends BaseData {
    private String hostip;
    private String metricid;
    private String resulttype;
    private String resultinfo;

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public String getMetricid() {
        return metricid;
    }

    public void setMetricid(String metricid) {
        this.metricid = metricid;
    }

    public String getResulttype() {
        return resulttype;
    }

    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }

    public String getResultinfo() {
        return resultinfo;
    }

    public void setResultinfo(String resultinfo) {
        this.resultinfo = resultinfo;
    }

    @Override
    public String toString() {
        StringBuffer strb = new StringBuffer();
        strb.append("hostip:").append(this.hostip).append(",metricid:").append(this.metricid)
                .append(",resulttype:").append(this.resulttype).append(",resultinfo:")
                .append(this.resultinfo);
        return strb.toString();
    }

}
