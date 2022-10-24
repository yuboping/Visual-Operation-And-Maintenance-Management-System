package com.asiainfo.lcims.omc.agentclient.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentclient.service.ClientBussiness;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CollectClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.make();
    private ClientBussiness cServer = new ClientBussiness();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteIp = remoteAddress.getAddress().getHostAddress();
        String info = (String) msg;
        log.info("recive from [" + remoteIp + "],info:" + info);
        cServer.doActionWithInfo(remoteIp, info);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn(
                "Disconnected from server.Check if the client IP is error or if the server is closed.");
        super.channelInactive(ctx);
        Thread.sleep(1000 * 10L);
        CollectClient.getInstance().reConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            super.exceptionCaught(ctx, cause);
            ctx.close();
        } catch (Exception e) {
            log.error("Unexpected Exception : ", e);
        }
    }
}
