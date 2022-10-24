package com.asiainfo.lcims.omc.agentclient.service.task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.agentclient.netty.CollectClient;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.MetricResult2Server;
import com.asiainfo.lcims.omc.agentserver.model.MetricServer2Client;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;
import com.asiainfo.lcims.omc.utils.RunShellUtil;

public class CollectTask {

    private static final Logger logger = LoggerFactory.getLogger(CollectTask.class);

    private MetricServer2Client metricJob;
    private String localIp;
    private String runShell;

    public CollectTask(MetricServer2Client metricJob, String localIp, String uuid) {
        this.metricJob = metricJob;
        this.localIp = localIp;

        String shell = metricJob.getShell();
        String param = metricJob.getParams();
        if (param != null) {
            shell = shell + " " + param;
        }
        logger.info("uuid : {}, shell is : [{}]", uuid, shell);
        runShell = shell;
    }

    public void collect(String uuid) {
        logger.info("uuid: {} collect task start, {}", uuid, runShell);
        String datas = null;
        try {
            datas = runShell();
            if ("".equals(datas)) {
                logger.warn("uuid : {}, script [{}] retValue is blank, please check.", uuid,
                        runShell);
            } else {
                logger.info("uuid : {}, script [{}] retValue is {}.", uuid, runShell, datas);
            }
            BaseValue<MetricResult2Server> retValue = sendData(datas);
            String retData = JSON.toJSONString(retValue);
            // agent_client返回采集结果给agent_server
            CollectClient.getInstance().sendData(retData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 把BaseValue对象转化为json字符串
     * 
     * @param datas
     * @return
     */
    private BaseValue<MetricResult2Server> sendData(String datas) {
        MetricResult2Server result = new MetricResult2Server();
        result.setHostip(localIp);
        result.setMetricid(metricJob.getMetricid());
        result.setResulttype(metricJob.getResulttype());
        result.setResultinfo(datas);
        List<MetricResult2Server> resultList = new LinkedList<MetricResult2Server>();
        resultList.add(result);
        BaseValue<MetricResult2Server> retValue = new BaseValue<MetricResult2Server>();
        retValue.setInfo(resultList);
        retValue.setMark(IDGenerateUtil.getUuid());
        retValue.setOptype(OpType.METRICRESULT_CLIENT_SERVER.getType());
        return retValue;
    }

    private String runShell() throws IOException {
        String ret = RunShellUtil.runShell(runShell);
        return ret;
    }
}
