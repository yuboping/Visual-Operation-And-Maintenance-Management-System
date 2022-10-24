package com.asiainfo.lcims.omc.alarm.model;

/**
 * 集中业务监控平台告警接入
 * 
 * @author zhujiansheng
 * @date 2020年7月6日 下午3:37:39
 * @version V1.0
 */
public class AlarmDataContent {

    private String plat;

    private String product;

    private String originalMetric;

    private int alarmType;

    private String message;

    private long time;

    private int priority;

    private String eventId;

    private int state;

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOriginalMetric() {
        return originalMetric;
    }

    public void setOriginalMetric(String originalMetric) {
        this.originalMetric = originalMetric;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AlarmDataContent [plat=" + plat + ", product=" + product + ", originalMetric="
                + originalMetric + ", alarmType=" + alarmType + ", message=" + message + ", time="
                + time + ", priority=" + priority + ", eventId=" + eventId + ", state=" + state
                + "]";
    }

}
