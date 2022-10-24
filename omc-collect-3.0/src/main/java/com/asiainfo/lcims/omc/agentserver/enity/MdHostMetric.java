package com.asiainfo.lcims.omc.agentserver.enity;

import java.sql.Date;

/**
 * MD_HOST_METRIC
 * 
 * @author XHT
 *
 */
public class MdHostMetric {
    private String id;
    private String hostId;
    private String metricId;
    private Integer cycleId;
    private String script;
    private String scriptParam;
    private Integer scriptReturnType;
    private Integer state;
    private Date updateTime;
    private String metricType;// MD_METRIC中字段,通过ID关联

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptParam() {
        return scriptParam;
    }

    public void setScriptParam(String scriptParam) {
        this.scriptParam = scriptParam;
    }

    public Integer getScriptReturnType() {
        return scriptReturnType;
    }

    public void setScriptReturnType(Integer scriptReturnType) {
        this.scriptReturnType = scriptReturnType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    @Override
    public String toString() {
        return "MdHostMetric [id=" + id + ", hostId=" + hostId + ", metricId=" + metricId
                + ", cycleId=" + cycleId + ", script=" + script + ", scriptParam=" + scriptParam
                + ", scriptReturnType=" + scriptReturnType + ", state=" + state + ", updateTime="
                + updateTime + ", metricType=" + metricType + "]";
    }

}
