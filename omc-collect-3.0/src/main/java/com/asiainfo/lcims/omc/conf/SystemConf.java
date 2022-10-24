package com.asiainfo.lcims.omc.conf;

import com.ailk.lcims.lcbmi.config.Configuration;

/**
 * 读取system.properties配置文件信息.读取后将配置文件信息缓存到InitParam类中.
 * 
 * @author XHT
 *
 */
public class SystemConf extends Configuration {
    /** 默认的server监听IP */
    private static final String DEFAULT_HOST = "127.0.0.1";
    /** 默认的server监听端口 */
    private static final int DEFAULT_PORT = 9999;

    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = "system.properties";

    protected SystemConf() {
        super();
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    protected String getServerIp() {
        return getValueOrDefault("server_ip", DEFAULT_HOST);
    }

    protected int getServerPort() {
        return getIntValueOrDefault("server_port", DEFAULT_PORT);
    }

    protected String getDeLimiter() {
        return getValueOrDefault("delimiter", "$$$");
    }

    protected int getReconnectTime() {
        return getIntValueOrDefault("reconnect_time", DEFAULT_PORT);
    }

    protected int getHeartTime() {
        return getIntValueOrDefault("heart_time", DEFAULT_PORT);
    }

    protected String getHeartStr() {
        return getValueOrDefault("heart_str", "heartstr");
    }

    protected int getServerClientMaxJetlag() {
        return getIntValueOrDefault("server_client_max_jetlag", 30);
    }

    protected String getProcessMetricSplitStr() {
        return getValueOrDefault("process_metric_split_str", "#");
    }

    protected String getProcessMetricType() {
        return getValueOrDefault("process_metric_type", "1");
    }
    
    /**
     * 获取radius服务端ip
     * @return
     */
    public String getRadiusServerIp() {
        return getValueOrDefault("radius_ip", "");
    }
    /**
     * 获取radius服务端端口
     * @return
     */
    public int getRadiusServerPort() {
        return getIntValueOrDefault("radius_port", 0);
    }
    
}
