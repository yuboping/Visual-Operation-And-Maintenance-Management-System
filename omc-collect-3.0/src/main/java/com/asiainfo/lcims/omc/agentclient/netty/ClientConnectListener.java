package com.asiainfo.lcims.omc.agentclient.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentclient.service.business.BussinessFirstConnnectServer;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 客户端连接监听,连接中断时进行重连.
 * 
 * @author XHT
 *
 */
public class ClientConnectListener implements ChannelFutureListener {
    private static final Logger log = LoggerFactory.make();

    private Channel channel;

    public ClientConnectListener(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        int reconnect_time = InitParam.getReconnectTime();
        String serverIp = InitParam.getServerIp();
        int serverPort = InitParam.getServerPort();
        channel = future.channel();

        CollectClient.getInstance().setChannel(channel);
        if (future.isSuccess()) {
            log.info("server{}:{} is connected...", serverIp, serverPort);
            new BussinessFirstConnnectServer().applyMetric();
        } else {
            log.error("failed to connect to server{}:{}, try connect after {}s.", serverIp,
                    serverPort, reconnect_time);
            channel.eventLoop().schedule(new Runnable() {
                public void run() {
                    CollectClient.getInstance().reConnect();
                }
            }, reconnect_time, TimeUnit.SECONDS);
        }
    }

}
