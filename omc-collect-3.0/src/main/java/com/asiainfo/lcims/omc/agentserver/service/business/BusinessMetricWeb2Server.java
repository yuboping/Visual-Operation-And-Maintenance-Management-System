package com.asiainfo.lcims.omc.agentserver.service.business;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.MetricWeb2Server;
import com.asiainfo.lcims.omc.agentserver.model.Result;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.conf.InitParam;

import io.netty.channel.Channel;

/**
 * WEB端向agentServer发送需要下发指标的clientIp,agentServer根据ip查找最新指标信息并下发到对应的agentClient.
 * 
 * @author XHT
 *
 */
public class BusinessMetricWeb2Server extends AbstractServerBusiness<MetricWeb2Server> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<MetricWeb2Server> infoList, Channel channel) {
        // 下发指标前重新加载主机,采集周期等缓存信息.
        DbDataCache.loading();

        for (MetricWeb2Server info : infoList) {
            // 向对应客户端主机下发指标
            this.sendMetric2Client(info.getIp());
        }

        String msg = mkResultForWeb(ResultType.SUCCESS);
        // 非采集客户端的通信通道,所以需要用传入进来的当前正在通信的通道进行通话.
        channel.writeAndFlush(msg);
        log.info("mark:[" + mark + "].has returned msg to client.");
    }

    private String mkResultForWeb(String resultType) {
        // 向调用方返回操作结果
        BaseValue<Result> resp = new BaseValue<Result>();
        List<Result> res_list = new LinkedList<Result>();
        Result result = new Result();
        result.setResult(resultType);
        res_list.add(result);
        resp.setInfo(res_list);
        resp.setMark(mark);
        resp.setOptype(OpType.METRIC_AGENTSERVER_WEB.getType());
        String msg = JSON.toJSONString(resp) + InitParam.getDeLimiter();
        return msg;
    }

}
