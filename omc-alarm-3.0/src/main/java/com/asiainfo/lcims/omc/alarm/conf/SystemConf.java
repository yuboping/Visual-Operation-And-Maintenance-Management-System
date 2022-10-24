package com.asiainfo.lcims.omc.alarm.conf;

import com.ailk.lcims.lcbmi.config.Configuration;

public class SystemConf extends Configuration {

    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = "system.properties";

    public SystemConf() {
        super();
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    public SystemConf(String configName) {
        super();
        this.configLoader = initConfigLoader(configName);
        configLoader.loadConfig();
    }

    /**
     * 告警信息是否需要上报
     */
    public boolean needReport() {
        return "true".equals(getValueOrDefault("sendReport", "false"));
    }

    public String getSmtpHost() {
        return getValueOrDefault("smtp_host", "smtp.sina.cn");
    }

    public String getSmtpUser() {
        return getValueOrDefault("smtp_username", "omcalarm@sina.cn");
    }

    public String getSmtpPwd() {
        return getValueOrDefault("smtp_password", "omc_admin");
    }

    public String getSmsShellPaht() {
        return getValueOrDefault("smsshellpath", "/data/omc/omc-web/sh/yncusms");
    }

    /**
     * 数据库
     */
    public String getDbName() {
        return getValueOrDefault("db_name", "mysql");
    }

    public String getProvince() {
        return getValueOrDefault("province", "");
    }

    public String getUserName() {
        return getValueOrDefault("sms_username", "radius");
    }

    public String getPasswd() {
        return getValueOrDefault("sms_password", "d1106ccca74e98877ed6d7890c70bb2c");
    }

    /**
     * 
     */
    public Integer getAlarmMinNum() {
        return getIntValueOrDefault("alarm_min_num", 1);
    }
    
    public boolean outDbExist() {
        return "true".equals(getValueOrDefault("outDbExist", "false"));
    }

    /**
     * 河南移动sms
     */
    public String getSmsIp() {
        return getValueOrDefault("sms_ip", "192.169.0.10");
    }

    public String getSmsPort() {
        return getValueOrDefault("sms_port", "7123");
    }

    public String getPushDataLoginUrl() {
        return getValueOrDefault("pushdata_loginurl", "");
    }

    public String getPushDataUsername() {
        return getValueOrDefault("pushdata_username", "");
    }

    public String getPushDataPassword() {
        return getValueOrDefault("pushdata_password", "");
    }

    public String getPushDataPushUrl() {
        return getValueOrDefault("pushdata_pushurl", "");
    }

}