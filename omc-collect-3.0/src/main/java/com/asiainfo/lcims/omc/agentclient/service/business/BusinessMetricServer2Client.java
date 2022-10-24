package com.asiainfo.lcims.omc.agentclient.service.business;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentclient.service.task.QuartzManager;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.MetricClient2Server;
import com.asiainfo.lcims.omc.agentserver.model.MetricServer2Client;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.conf.InitParam;

/**
 * 接收到agentServer下发指标的命令后,清空原有的定时任务后重新生成新的定时任务.
 * <p>
 * 如果定时任务生成失败,则返回给AgentServer失败标识.成功则返回成功标识.
 * 
 * @author XHT
 *
 */
public class BusinessMetricServer2Client extends AbstractClientBusiness<MetricServer2Client> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<MetricServer2Client> infoList) {
        String localIp = InitParam.getLoaclIp();
        String result = this.mkCron(infoList, localIp);
        this.sendInfo2Server(this.mkMsg2Server(localIp, result));
    }

    // 构造返回结果
    private String mkMsg2Server(String localIp, String result) {
        MetricClient2Server metricClient2Server = new MetricClient2Server();
        metricClient2Server.setHostip(localIp);
        metricClient2Server.setResult(result);
        List<MetricClient2Server> resultList = new LinkedList<MetricClient2Server>();
        resultList.add(metricClient2Server);
        BaseValue<MetricClient2Server> baseValue = new BaseValue<MetricClient2Server>();
        baseValue.setInfo(resultList);
        baseValue.setMark(mark);
        baseValue.setOptype(OpType.METRIC_AGENTCLIENT_SERVER.getType());
        // 返回操作结果给服务端
        String jsonInfo = JSON.toJSONString(baseValue);
        return jsonInfo;
    }

    // 根据指标信息生成定时任务,并返回生成成功失败结果
    private String mkCron(List<MetricServer2Client> infoList, String localIp) {
        String result = ResultType.SUCCESS;
        // 根据指标信息生成定时任务
        try {
            QuartzManager.startTask(infoList, localIp);
        } catch (Exception e) {
            result = ResultType.FAILED;
            log.error("mark:[" + mark + "].scheduler task error, reason : {}", e);
        }
        return result;
    }

}
