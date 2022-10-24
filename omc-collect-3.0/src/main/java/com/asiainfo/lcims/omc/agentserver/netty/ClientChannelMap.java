package com.asiainfo.lcims.omc.agentserver.netty;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;

import io.netty.channel.Channel;

/**
 * 保存服务端连接的客户端信息
 */

public class ClientChannelMap {
    private static final Logger log = LoggerFactory.make();

    // 缓存 clientIp和长连接中对应client的Channel的数据
    private static Map<String, Channel> clientIpChannelMap = new ConcurrentHashMap<String, Channel>();

    // 缓存clientIp和Channel一一对应关系的数据
    private static Map<String, Integer> clientIpChannelStrMap = new ConcurrentHashMap<String, Integer>();

    /**
     * 向缓存中存入client信息
     * <p>
     * clientIp:客户端IP;
     * <p>
     * Channel:客户端通道
     */
    protected synchronized static void putClient(String clientIp, Channel channel) {
        Channel tmp = clientIpChannelMap.remove(clientIp);
        if (tmp != null) {
            tmp.close();
        }
        clientIpChannelStrMap.put(clientIp, channel.hashCode());
        clientIpChannelMap.put(clientIp, channel);
        log.info("clientIpChannelStrMap size is:" + clientIpChannelStrMap.size());
        log.info("clientIpChannelMap size is:" + clientIpChannelMap.size());
    }

    /**
     * 根据clientip移除缓存数据
     * <p>
     * clientIp客户端IP;
     */
    protected synchronized static void removeClient(String clientIp) {
        clientIpChannelStrMap.remove(clientIp);
        Channel tmp = clientIpChannelMap.remove(clientIp);
        if (tmp != null) {
            tmp.close();
        }
        log.info("client {} remove.", clientIp);
        log.info("clientIpChannelStrMap size is:" + clientIpChannelStrMap.size());
        log.info("clientIpChannelMap size is:" + clientIpChannelMap.size());
    }

    /**
     * 根据Channel移除缓存数据
     * <p>
     * Channel,长连接是服务端保存的Channel;
     */
    protected synchronized static void removeClientByChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        int channelHashCode = channel.hashCode();
        for (Entry<String, Integer> entry : clientIpChannelStrMap.entrySet()) {
            if (channelHashCode == entry.getValue().intValue()) {
                removeClient(entry.getKey());
            }
        }
    }

    /**
     * Channel:客户端通道
     * 
     * @param 根据客户端IP获取客户端通信通道
     * @return
     */
    public static Channel getChannelByIp(String clientIp) {
        return clientIpChannelMap.get(clientIp);
    }
}
