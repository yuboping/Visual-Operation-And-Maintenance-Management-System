package com.asiainfo.lcims.omc.agentserver.netty;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 采集服务端
 * 
 * @author XHT
 *
 */
public class CollectServer {
    private static final Logger log = LoggerFactory.make();

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            // 通道初始化设置
            bootstrap.childHandler(new CollectServerInitializer());

            // socket参数配置
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 1024 * 512);
            bootstrap.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 1024 * 256);

            // 绑定监听端口
            ChannelFuture f = bootstrap.bind(InitParam.getServerPort()).sync();
            log.info("collect server is start.server port is:" + InitParam.getServerPort());
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("collect server start error:", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("collect server is shutdown...");
        }
    }

    // 向对应客户端主机发送消息
    public static void sendInfo2Client(String ip, String info) {
        Channel channel = ClientChannelMap.getChannelByIp(ip);
        if (channel == null) {
            log.error("client:{" + ip + "} not a collect_client.");
            return;
        }
        try {
            channel.writeAndFlush(info + InitParam.getDeLimiter());
            log.info("send to [" + ip + "], info:{" + info + "}.");
        } catch (Exception e) {
            log.info("send to [" + ip + "], info:{" + info + "}.error." + e);
        }
    }

    /**
     * 向缓存中存入client信息
     * <p>
     * clientIp:客户端IP;
     * <p>
     * Channel:客户端通道
     */
    public static void putClient(String clientIp, Channel channel) {
        ClientChannelMap.putClient(clientIp, channel);
    }
}
