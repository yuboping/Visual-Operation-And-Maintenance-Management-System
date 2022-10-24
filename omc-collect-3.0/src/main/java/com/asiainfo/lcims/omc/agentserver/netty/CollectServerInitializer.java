package com.asiainfo.lcims.omc.agentserver.netty;

import java.util.concurrent.TimeUnit;

import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 通道的初始化
 * 
 * @author XHT
 *
 */
public class CollectServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        int heart_time = InitParam.getHeartTime();
        ByteBuf copiedBuffer = Unpooled.copiedBuffer(InitParam.getDeLimiter().getBytes());
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, copiedBuffer));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new IdleStateHandler(heart_time * 3, heart_time, 0, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
        pipeline.addLast(new CollectServerHandler());
    }

}
