package com.asiainfo.lcims.omc.model.ems;

import java.util.List;

public class EmsAddHost {
    /** 虚拟机ID */
    private String vmid;
    private String hostips;
    private String hostname;
    private int hosttype;
    private List<EmsMetric> metricData;
    private String hostPages;
    public String getVmid() {
        return vmid;
    }
    public void setVmid(String vmid) {
        this.vmid = vmid;
    }
    public String getHostips() {
        return hostips;
    }
    public void setHostips(String hostips) {
        this.hostips = hostips;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public int getHosttype() {
        return hosttype;
    }
    public void setHosttype(int hosttype) {
        this.hosttype = hosttype;
    }
    public List<EmsMetric> getMetricData() {
        return metricData;
    }
    public void setMetricData(List<EmsMetric> metricData) {
        this.metricData = metricData;
    }
    public String getHostPages() {
        return hostPages;
    }
    public void setHostPages(String hostPages) {
        this.hostPages = hostPages;
    }
}
