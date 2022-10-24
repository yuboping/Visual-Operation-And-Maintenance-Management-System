package com.asiainfo.lcims.omc.conf;

/**
 * 读取配置文件后,缓存所有相关配置信息
 * 
 * @author XHT
 *
 */
public class InitParam {
    // 配置文件
    private static SystemConf systemConf;
    // 当前主机IP
    private static String localIp;
    // 采集服务器IP
    private static String serverIp;
    // 采集服务器端口
    private static int serverPort;
    // Netty通信结束符
    private static String delimiter;
    // 客户端断线重连时间间隔.单位:秒
    private static int reconnectTime;
    // 长连接,客户端向服务端发送心跳包的频率.单位：秒
    private static int heartTime;
    // 长连接,客户端向服务端发送心跳包内容
    private static String heartStr;
    // 采集服务端和采集客户端最大时差
    private static int serverClientMaxJetlag;
    // 进程指标的类型
    private static String processMetricType;
    // 进程指标.进程关键字分隔符
    private static String processMetricSplitStr;
    static {
        localIp = new LocalAddress().getLocalAddr();
        systemConf = new SystemConf();

        serverIp = systemConf.getServerIp();
        serverPort = systemConf.getServerPort();
        delimiter = systemConf.getDeLimiter();
        reconnectTime = systemConf.getReconnectTime();
        heartTime = systemConf.getHeartTime();
        heartStr = systemConf.getHeartStr();
        serverClientMaxJetlag = systemConf.getServerClientMaxJetlag();

        processMetricSplitStr = systemConf.getProcessMetricSplitStr();
        processMetricType = systemConf.getProcessMetricType();// 默认进程指标的类型是1,每次初始化数据的时候定义,后期不允许改变.
    }

    public static String getLoaclIp() {
        return localIp;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getDeLimiter() {
        return delimiter;
    }

    public static int getReconnectTime() {
        return reconnectTime;
    }

    public static int getHeartTime() {
        return heartTime;
    }

    public static String getHeartStr() {
        return heartStr;
    }

    public static int getServerClientMaxJetlag() {
        return serverClientMaxJetlag;
    }

    public static String getProcessMetricSplitStr() {
        return processMetricSplitStr;
    }

    public static String getProcessMetricType() {
        return processMetricType;
    }
    
    public static SystemConf getSystemConf() {
        return systemConf;
    }
}
