package com.asiainfo.lcims.omc.agentserver.enity;

/**
 * MD_METRIC
 * 
 * @author XHT
 *
 */
public class MdMetric {
    private String id;
    private String metricIdentity;
    private String metricName;
    private Integer cycleId;
    private String script;
    private String scriptParam;
    private Integer scriptReturnType;
    private String metricType;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetricIdentity() {
        return metricIdentity;
    }

    public void setMetricIdentity(String metricIdentity) {
        this.metricIdentity = metricIdentity;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
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

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
