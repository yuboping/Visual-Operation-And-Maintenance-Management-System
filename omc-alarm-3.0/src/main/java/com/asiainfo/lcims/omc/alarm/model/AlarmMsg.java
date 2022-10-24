package com.asiainfo.lcims.omc.alarm.model;

/**
 * 安徽电信发送告警的格式
 * 
 * @author zhujiansheng
 * @date 2019年3月26日 下午9:12:13
 * @version V1.0
 */
public class AlarmMsg {

    private String hostName;

    private String hostIp;

    private String alarmType;

    private String alarmPort;

    private String alarmCreateTime;

    private String alarmCleanTime;

    private String alarmDetail;

    private String alarmLevel;

    private String alarmRegion;

    private String alarmFlag;

    private AlarmMsg(Builder builder) {
        this.hostName = builder.hostName;
        this.hostIp = builder.hostIp;
        this.alarmType = builder.alarmType;
        this.alarmPort = builder.alarmPort;
        this.alarmCreateTime = builder.alarmCreateTime;
        this.alarmCleanTime = builder.alarmCleanTime;
        this.alarmDetail = builder.alarmDetail;
        this.alarmLevel = builder.alarmLevel;
        this.alarmRegion = builder.alarmRegion;
        this.alarmFlag = builder.alarmFlag;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String hostName = "{NULL}";

        private String hostIp = "{NULL}";

        private String alarmType = "{NULL}";

        private String alarmPort = "{NULL}";

        private String alarmCreateTime = "{NULL}";

        private String alarmCleanTime = "{NULL}";

        private String alarmDetail = "{NULL}";

        private String alarmLevel = "{NULL}";

        private String alarmRegion = "{NULL}";

        private String alarmFlag = "{NULL}";

        public Builder setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder setHostIp(String hostIp) {
            this.hostIp = hostIp;
            return this;
        }

        public Builder setAlarmType(String alarmType) {
            this.alarmType = alarmType;
            return this;
        }

        public Builder setAlarmPort(String alarmPort) {
            this.alarmPort = alarmPort;
            return this;
        }

        public Builder setAlarmCreateTime(String alarmCreateTime) {
            this.alarmCreateTime = alarmCreateTime;
            return this;
        }

        public Builder setAlarmCleanTime(String alarmCleanTime) {
            this.alarmCleanTime = alarmCleanTime;
            return this;
        }

        public Builder setAlarmDetail(String alarmDetail) {
            this.alarmDetail = alarmDetail;
            return this;
        }

        public Builder setAlarmLevel(String alarmLevel) {
            this.alarmLevel = alarmLevel;
            return this;
        }

        public Builder setAlarmRegion(String alarmRegion) {
            this.alarmRegion = alarmRegion;
            return this;
        }

        public Builder setAlarmFlag(String alarmFlag) {
            this.alarmFlag = alarmFlag;
            return this;
        }

        public AlarmMsg build() {
            return new AlarmMsg(this);
        }

    }

    @Override
    public String toString() {
        return hostName + "|||" + hostIp + "|||" + alarmType + "|||" + alarmPort + "|||"
                + alarmCreateTime + "|||" + alarmCleanTime + "|||" + alarmDetail + "|||"
                + alarmLevel + "|||" + alarmRegion + "|||" + alarmFlag;
    }

}