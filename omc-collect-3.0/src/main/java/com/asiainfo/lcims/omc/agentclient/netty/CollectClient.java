package com.asiainfo.lcims.omc.agentclient.netty;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CollectClient {
    private static final Logger log = LoggerFactory.make();

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private Channel channel;

    private volatile static CollectClient client;

    private CollectClient() {
        this.init();
        this.doConnect();
    }

    // 客户端通道初始化信息
    private void init() {
        // 参数配置
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 通道初始化配置
        bootstrap.handler(new CollectClientInitializer());
    }

    protected void setChannel(Channel channel) {
        this.channel = channel;
    }

    // 连接操作
    private void doConnect() {
        if (channel != null && channel.isActive()) {
            log.info("##channel is active##");
            return;
        }

        // 连接服务器
        ChannelFuture future = bootstrap.connect(InitParam.getServerIp(),
                InitParam.getServerPort());

        // 客户端连接监听
        future.addListener(new ClientConnectListener(channel));
    }

    // 客户端重新连接
    public void reConnect() {
        this.doConnect();
    }

    // 向服务端发送消息
    public void sendData(String info) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(info + InitParam.getDeLimiter());
            log.info("sendData success:" + info);
        } else {
            log.error("sendData error:" + info);
        }
    }

    // 初始化client.
    public static CollectClient getInstance() {
        if (client == null) {
            synchronized (CollectClient.class) {
                if (client == null) {
                    client = new CollectClient();
                }
            }
        }
        return client;
    }

    // 启动
    public static void start() {
        CollectClient.getInstance();
    }
}
