package com.asiainfo.lcims.omc.agentclient.service;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentclient.service.business.AbstractClientBusiness;
import com.asiainfo.lcims.omc.agentclient.service.business.BusinessFaultShellServer2Client;
import com.asiainfo.lcims.omc.agentclient.service.business.BusinessHeartServer2Client;
import com.asiainfo.lcims.omc.agentclient.service.business.BusinessMetricServer2Client;
import com.asiainfo.lcims.omc.agentclient.service.business.BusinessShellServer2Client;
import com.asiainfo.lcims.omc.agentserver.model.BaseData;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.common.OpType;

/**
 * 业务分发
 * 
 * @author XHT
 *
 */
public class ClientBussiness {
    private static final Logger log = LoggerFactory.make();

    // 解析info的内容,并根据解析出的内容做相关操作
    @SuppressWarnings("unchecked")
    public void doActionWithInfo(String remoteIp, String jsonInfo) {
        BaseValue<BaseData> base = null;
        try {
            base = JSON.parseObject(jsonInfo, BaseValue.class);
        } catch (Exception e) {
            log.error("JSON.parseObject error:" + e);
        }

        if (base == null) {
            log.error("jsonInfo:{" + jsonInfo + "}translate to null,please check the data format.");
            return;
        }

        AbstractClientBusiness<?> business = this.switchBusinessClass(base);
        if (business != null) {
            business.doBusiness(base.getMark(), jsonInfo);
        }
    }

    // 根据opType返回对应的业务处理类实例
    private AbstractClientBusiness<?> switchBusinessClass(BaseValue<BaseData> base) {
        @SuppressWarnings("rawtypes")
        AbstractClientBusiness business = null;
        if (OpType.METRIC_AGENTSERVER_CLIENT.getType().equals(base.getOptype())) {
            // 接收服务端下发的指标命令
            business = new BusinessMetricServer2Client();
        } else if (OpType.SHELL_AGENTSERVER_CLIENT.getType().equals(base.getOptype())) {
            // 接收服务端下发的指标命令
            business = new BusinessShellServer2Client();
        } else if (OpType.HEART_SERVER_CLIENT.getType().equals(base.getOptype())) {
            // 接收服务端下发的指标命令
            business = new BusinessHeartServer2Client();
        } else if (OpType.SHELL_FAULT_AGENTSERVER_CLIENT.getType().equals(base.getOptype())) {
            // 一键应急：接收采集服务端向客户端下发执行脚本命令
            business = new BusinessFaultShellServer2Client();
        } else {
            log.error("no optype.");
        }
        return business;
    }
}
