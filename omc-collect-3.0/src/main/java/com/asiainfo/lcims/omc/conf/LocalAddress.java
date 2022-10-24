package com.asiainfo.lcims.omc.conf;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;

import com.ailk.lcims.lcbmi.config.Configuration;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;

/**
 * 读取localip.properties中的ip信息.读取后将配置信息统一缓存到InitParam类中.
 * 
 * @author XHT
 *
 */
public class LocalAddress extends Configuration {
    private static final Logger log = LoggerFactory.make();

    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = "localip.properties";

    protected LocalAddress() {
        super();
        this.configLoader = initConfigLoader(DEFAULT_CONFIG_NAME);
        configLoader.loadConfig();
    }

    /**
     * localip.properties文件 可在libs文件夹下面直接配置,无需更新在jar包中
     * 先读取localip配置文件,如果配置文件中配置有指定的当前客户端IP的话,则返回配置文件中的客户端IP
     * <p>
     * 如果没有配置客户端IP,则读取系统网关中配置的第一个IP
     * 
     * @return
     */
    protected String getLocalAddr() {
        String ip = this.getIp();
        if (ip == null || ip.isEmpty()) {
            List<String> list = getAllLocalAddr();
            if (list.isEmpty()) {
                return null;
            } else {
                ip = list.get(0);
            }
        }
        log.info("------------------LOCAL ADDRESS IP:" + ip + "------------------");
        return ip;
    }

    private String getIp() {
        return getValueOrDefault("ip", "");
    }

    private static List<String> getAllLocalAddr() {
        List<String> addrs = new ArrayList<String>();
        Enumeration<NetworkInterface> allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("NetworkInterface error:", e);
            return addrs;
        }
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = (InetAddress) addresses.nextElement();
                if (null != ip && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                    log.info("NetworkInterface Address IP:" + ip.getHostAddress());
                    addrs.add(ip.getHostAddress());
                }
            }
        }
        List<String> newAddrList = new ArrayList<String>();
        for (String address : addrs) {
            if (!newAddrList.contains(address)) {
                newAddrList.add(address);
            }
        }
        return newAddrList;
    }
}
