package com.asiainfo.lcims.omc.agentserver.service;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.model.BaseData;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.service.business.AbstractServerBusiness;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessFaultShellResult;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessFaultShellWeb2Server;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessHeartClient2Server;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessMetricClient2Server;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessMetricClientApply;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessMetricResult;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessMetricWeb2Server;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessRadiusWeb2Server;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessShellResult;
import com.asiainfo.lcims.omc.agentserver.service.business.BusinessShellWeb2Server;
import com.asiainfo.lcims.omc.common.OpType;

import io.netty.channel.Channel;

/**
 * 业务分发,根据OPTYPE分发给对应的类处理业务.
 * 
 * @author XHT
 *
 */
public class ServerBussiness {
    private static final Logger log = LoggerFactory.make();

    // 解析info的内容,并根据解析出的内容做相关操作
    @SuppressWarnings("unchecked")
    public void doActionWithInfo(String ip, String jsonInfo, Channel channel) {
        BaseValue<BaseData> base = null;
        try {
            base = new BaseValue<BaseData>();
            base = JSON.parseObject(jsonInfo, BaseValue.class);
        } catch (Exception e) {
            log.error("JSON.parseObject error:" + e);
        }
        if (base == null) {
            log.error("jsonInfo:{" + jsonInfo + "}translate to null,please check the data format.");
            return;
        }

        AbstractServerBusiness<?> business = switchBusinessClass(base);
        if (business != null) {
            business.doBusiness(base.getMark(), jsonInfo, channel);
        }
    }

    // 根据opType返回对应的业务处理类实例
    private AbstractServerBusiness<?> switchBusinessClass(BaseValue<BaseData> base) {
        AbstractServerBusiness<?> business = null;
        if (OpType.METRIC_AGENTCLIENT_APPLY.getType().equals(base.getOptype())) {
            // 接收：客户端申请指标
            business = new BusinessMetricClientApply();
        } else if (OpType.METRIC_WEB_AGENTSERVER.getType().equals(base.getOptype())) {
            // 接收：WEB端的下发指标命令
            business = new BusinessMetricWeb2Server();
        } else if (OpType.METRIC_AGENTCLIENT_SERVER.getType().equals(base.getOptype())) {
            // 接收：客户端返回下发指标的结果
            business = new BusinessMetricClient2Server();
        } else if (OpType.METRICRESULT_CLIENT_SERVER.getType().equals(base.getOptype())) {
            // 接收：客户端返回指标执行结果
            business = new BusinessMetricResult();
        } else if (OpType.SHELL_WEB_AGENTSERVER.getType().equals(base.getOptype())) {
            // 接收：WEB端的下发脚本命令
            business = new BusinessShellWeb2Server();
        } else if (OpType.SHELL_AGENTCLIENT_SERVER.getType().equals(base.getOptype())) {
            // 接收：WEB端的下发脚本命令
            business = new BusinessShellResult();
        } else if (OpType.HEART_CLIENT_SERVER.getType().equals(base.getOptype())) {
            // 接收：WEB端的下发脚本命令
            business = new BusinessHeartClient2Server();
        } else if (OpType.SHELL_FAULT_WEB_AGENTSERVER.getType().equals(base.getOptype())) {
            // 一键应急：接收：WEB端的下发故障脚本命令
            business = new BusinessFaultShellWeb2Server();
        } else if (OpType.SHELL_FAULT_AGENTCLIENT_SERVER.getType().equals(base.getOptype())) {
            // 一键应急：采集客户端向服务端返回脚本执行结果
            business = new BusinessFaultShellResult();
        } else if (OpType.RADIUS_OPERATE_WEB_AGENTSERVER.getType().equals(base.getOptype())) {
            // RADIUS业务 一键应急: WEB端向采集服务端发送调用radius socket 接口
            business = new BusinessRadiusWeb2Server();
        } else {
            log.error("no optype.");
        }
        return business;
    }
}
