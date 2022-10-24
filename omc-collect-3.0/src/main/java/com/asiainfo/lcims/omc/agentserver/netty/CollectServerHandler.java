package com.asiainfo.lcims.omc.agentserver.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.service.ServerBussiness;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义业务handler
 * 
 * @author XHT
 *
 */
public class CollectServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.make();
    private ServerBussiness bServer = new ServerBussiness();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteIp = insocket.getAddress().getHostAddress();
        String info = (String) msg;
        String hostname = insocket.getAddress().getHostName();
        log.info("recive ip:"+remoteIp+",hostname:"+hostname);
        log.info("recive from [" + remoteIp + "],info:" + info);
        try {
            /**
             * 增加客户端连通性指标数据入库
                             心跳周期数据入库规则
                1、心跳周期20s
                    16:55:02.265 recive from [10.21.37.197],info:{"info":[{"dateTimeStr":"1565168102264","msg":"HEART_MSG"}],"mark":"78478720263547340060","optype":"002"}
                    16:55:22.265 recive from [10.21.37.197],info:{"info":[{"dateTimeStr":"1565168122264","msg":"HEART_MSG"}],"mark":"78480720278398663676","optype":"002"}
                    16:55:42.265 recive from [10.21.37.197],info:{"info":[{"dateTimeStr":"1565168142265","msg":"HEART_MSG"}],"mark":"78482720299693953311","optype":"002"}
                    16:56:02.265 recive from [10.21.37.197],info:{"info":[{"dateTimeStr":"1565168162265","msg":"HEART_MSG"}],"mark":"78484720314704899850","optype":"002"}
                    16:56:22.265 recive from [10.21.37.197],info:{"info":[{"dateTimeStr":"1565168182265","msg":"HEART_MSG"}],"mark":"78486720333446696613","optype":"002"}
                            以上是服务端接收客户端心跳返回的数据，及对应的时间周期
              2、时间规整规则
                                        正常20s周期时间是 16:55:00、16:55:20、16:55:40、16:56:00 等
                    a、时间：16:55:02  对应的周期时间是  16:55:00 ，规整至 16:55:00
                    b、时间：16:55:17  对应的周期时间是  16:55:20 ，规整至 16:55:20
                    c、时间：16:55:57  对应的周期时间是  16:56:00 ，规整至 16:56:00 
                                        判断 时间秒值-当前周期秒值 > 20/2 则规整至 下个周期时间 
                                        比如 01:57  当前周期秒： 40s   57-40 > 20/2  则规整至 02:00
                                        其他规整至当前周期
                3、获取规整的周期时间，除以 5分钟， 判断余数是否为 0 ，为 0 则入库
                4、入库格式： hostId , val=1、 指标id  、 时间  单维度
                                待开发
             */
            
            
            bServer.doActionWithInfo(remoteIp, info, ctx.channel());
        } catch (Exception e) {
            log.error("ServerBussiness Exception : " + e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 客户端连接到服务器后，将将客户端的信息记录到缓存信息中
        ClientChannelMap.removeClientByChannel(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            super.exceptionCaught(ctx, cause);
        } catch (Exception e) {
            log.error("Unexpected Exception : " + e);
        }
    }
}
