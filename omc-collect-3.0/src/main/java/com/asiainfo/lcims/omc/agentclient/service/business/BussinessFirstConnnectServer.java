package com.asiainfo.lcims.omc.agentclient.service.business;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.agentclient.netty.CollectClient;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.MetricWeb2Server;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.conf.InitParam;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;

/**
 * 客户端第一次连接上服务端时做的业务操作
 * 
 * @author XHT
 *
 */
public class BussinessFirstConnnectServer {
    /**
     * 向服务端申请当前主机的指标信息.
     */
    public void applyMetric() {
        // 构造向服务端申请 下发指标 的命令
        BaseValue<MetricWeb2Server> req = new BaseValue<MetricWeb2Server>();
        MetricWeb2Server tmp = new MetricWeb2Server();
        tmp.setIp(InitParam.getLoaclIp());
        req.setMark(IDGenerateUtil.getUuid());
        req.setOptype(OpType.METRIC_AGENTCLIENT_APPLY.getType());
        List<MetricWeb2Server> tmpList = new LinkedList<MetricWeb2Server>();
        tmpList.add(tmp);
        req.setInfo(tmpList);
        String jsonStr = JSON.toJSONString(req);

        CollectClient.getInstance().sendData(jsonStr);
    }
}
