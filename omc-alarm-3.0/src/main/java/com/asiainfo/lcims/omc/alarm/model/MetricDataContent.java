package com.asiainfo.lcims.omc.alarm.model;

/**
 * 集中业务监控平台指标接入
 * 
 * @author zhujiansheng
 * @date 2020年7月6日 下午3:37:29
 * @version V1.0
 */
public class MetricDataContent {

    private String pushPortId;

    private String endpoint;

    private String metric;

    private String tags;

    private int step;

    private String timestamp;

    private int value;

    private String counterType;

    public String getPushPortId() {
        return pushPortId;
    }

    public void setPushPortId(String pushPortId) {
        this.pushPortId = pushPortId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    @Override
    public String toString() {
        return "MetricData [pushPortId=" + pushPortId + ", endpoint=" + endpoint + ", metric="
                + metric + ", tags=" + tags + ", step=" + step + ", timestamp=" + timestamp
                + ", value=" + value + ", counterType=" + counterType + "]";
    }

}
