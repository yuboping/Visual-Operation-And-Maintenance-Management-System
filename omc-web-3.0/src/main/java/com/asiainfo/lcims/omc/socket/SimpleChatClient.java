package com.asiainfo.lcims.omc.socket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.socket.common.InitParam;
import com.asiainfo.lcims.omc.socket.common.OpType;
import com.asiainfo.lcims.omc.socket.model.BaseValue;
import com.asiainfo.lcims.omc.socket.model.MetricWeb2Server;
import com.asiainfo.lcims.omc.socket.netty.SimpleChatClientInitializer;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * SOCKET连接对应的IP,PORT
 * <p>
 * 向对应客户端发送消息后关闭连接.
 * 
 * @author XHT
 *
 */
public class SimpleChatClient {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleChatClient.class);
    public static final BusinessConf BUSCONF = new BusinessConf();
    private final String host;
    private final int port;

    public SimpleChatClient() {
        this.host = BUSCONF.getStringValue("collect_server_ip");
        this.port = Integer.parseInt(BUSCONF.getStringValue("collect_server_port"));
    }

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendMsg(String msg) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        String result = null;
        try {
            CountDownLatch lathc = new CountDownLatch(1);
            SimpleChatClientInitializer clientInitializer = new SimpleChatClientInitializer(lathc,
                    InitParam.DELIMITERSTR);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(clientInitializer);

            ChannelFuture connect = bootstrap.connect(host, port);
            Channel channel = connect.sync().channel();
            channel.writeAndFlush(msg + InitParam.DELIMITERSTR);
            lathc.await();// 开启等待会等待服务器返回结果之后再执行下面的代码
            result = clientInitializer.getServerResult();
            return result;
        } catch (Exception e) {
            LOG.error("send msg error:", e);
        } finally {
            group.shutdownGracefully();
        }
        return result;
    }

    public String sendMsg(String msg, String delimiterstr) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        String result = null;
        try {
            CountDownLatch lathc = new CountDownLatch(1);
            SimpleChatClientInitializer clientInitializer = new SimpleChatClientInitializer(lathc,
                    delimiterstr);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(clientInitializer);

            ChannelFuture connect = bootstrap.connect(host, port);
            Channel channel = connect.sync().channel();
            channel.writeAndFlush(msg + delimiterstr);
            lathc.await();// 开启等待会等待服务器返回结果之后再执行下面的代码
            result = clientInitializer.getServerResult();
            return result;
        } catch (Exception e) {
            LOG.error("send msg error:", e);
        } finally {
            group.shutdownGracefully();
        }
        return result;
    }

    /**
     * 公用下发接口
     * 
     * @param list
     * @return
     */
    public static void sendMsg(List<String> iplist) {
        if (ToolsUtils.ListIsNull(iplist))
            return;
        LOG.info("创建下发接口线程 create ChatClient interface start");
        Thread thread = new Thread(new ChatClientRunner(iplist));
        thread.start();
    }

    public static void filerHostIp(List<MetricWeb2Server> infoList, List<MdHostMetric> list) {
        List<String> iplist = new ArrayList<String>();
        for (MdHostMetric hostMetric : list) {
            String hostip = hostMetric.getAddr();
            if (!iplist.contains(hostip))
                iplist.add(hostip);
        }
        for (String ip : iplist) {
            MetricWeb2Server tmp = new MetricWeb2Server();
            tmp.setIp(ip);
            infoList.add(tmp);
        }
    }

    public static void main(String[] args) throws Exception {
        BaseValue<MetricWeb2Server> req = new BaseValue<MetricWeb2Server>();
        MetricWeb2Server tmp = new MetricWeb2Server();
        tmp.setIp("10.21.37.197");
        List<MetricWeb2Server> infoList = new LinkedList<MetricWeb2Server>();
        infoList.add(tmp);
        req.setInfo(infoList);
        req.setMark(IDGenerateUtil.getUuid());
        req.setOptype(OpType.METRIC_WEB_AGENTSERVER.getType());

        String str = JSON.toJSONString(req);
        SimpleChatClient client = new SimpleChatClient("10.21.37.197", 9999);
        String result = client.sendMsg(str);
        System.out.println(result);

        result = client.sendMsg(str);
        System.out.println(result);
    }
}