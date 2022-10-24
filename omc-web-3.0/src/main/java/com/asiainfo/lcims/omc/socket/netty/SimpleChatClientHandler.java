package com.asiainfo.lcims.omc.socket.netty;

import java.util.concurrent.CountDownLatch;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {
    private CountDownLatch lathc;

    public SimpleChatClientHandler(CountDownLatch lathc) {
        this.lathc = lathc;
    }

    private String result;

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String arg1) throws Exception {
        result = arg1;
        lathc.countDown();// 消息收取完毕后释放同步锁
    }

    public void resetLatch(CountDownLatch initLathc) {
        this.lathc = initLathc;
    }

    public String getResult() {
        return result;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
            case ALL_IDLE:
                handleAllIdle(ctx);
                break;
            default:
                break;
            }
        }
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        this.result = "TIME_OUT";
        this.lathc.countDown();
    }
}
