package com.asiainfo.lcims.omc.socket.netty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.asiainfo.lcims.omc.socket.common.InitParam;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class SimpleChatClientInitializer extends ChannelInitializer<SocketChannel> {
    private CountDownLatch lathc;
    private String DELIMITERSTR;

    public SimpleChatClientInitializer(CountDownLatch lathc, String delimiterstr) {
        this.lathc = lathc;
        this.DELIMITERSTR = delimiterstr;
    }

    private SimpleChatClientHandler handler;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        handler = new SimpleChatClientHandler(lathc);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 0, InitParam.TIMEOUT_TIME, TimeUnit.SECONDS));
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(81920,
                Unpooled.copiedBuffer(DELIMITERSTR.getBytes())));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", handler);
    }

    public String getServerResult() {
        return handler.getResult();
    }

    // 重置同步锁
    public void resetLathc(CountDownLatch initLathc) {
        handler.resetLatch(initLathc);
    }

}