package com.asiainfo.lcims.omc.agentserver.service.business;

import java.util.List;

import com.asiainfo.lcims.omc.agentserver.model.MetricWeb2Server;
import com.asiainfo.lcims.omc.agentserver.netty.CollectServer;

import io.netty.channel.Channel;

/**
 * agentClient第一次连接到agentServer的时候,向agentServer申请最新的指标信息.
 * 
 * @author XHT
 *
 */
public class BusinessMetricClientApply extends AbstractServerBusiness<MetricWeb2Server> {

    @Override
    protected void doAction(List<MetricWeb2Server> infoList, Channel channel) {
        // 根据内容向对应客户端下发指标
        for (MetricWeb2Server tmp : infoList) {
            // 客户端连接到服务器后，将将客户端的信息记录到缓存信息中
            CollectServer.putClient(tmp.getIp(), channel);
            // 向对应客户端主机下发指标
            this.sendMetric2Client(tmp.getIp());
        }
    }
}
