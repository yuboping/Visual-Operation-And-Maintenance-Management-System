package com.asiainfo.lcims.omc.flowmonitor.service;

import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorParam;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowAction;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowResult;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowActionDao;

/**
 * 获取nodeId对应的所有action.并对相应action做相关业务处理.
 * 
 * @author XHT
 *
 */
public class FlowMonitorNodeBusiness extends AbstractFlowMonitor {
    private static final Logger logger = LoggerFactory.make();

    private List<MdFlowAction> actionList;

    @Override
    protected void init() {
        try {
            actionList = getDao(MdFlowActionDao.class).selectByNodeId(context.getNodeId());
        } catch (Exception e) {
            logger.error("serialNum:{} ,nodeId:{} select error. error:{}", context.getSerialNum(),
                    context.getNodeId(), e);
        }
    }

    @Override
    protected void begin() {
        logger.info("serialNum:{} ,nodeId:{} start.", context.getSerialNum(), context.getNodeId());
        MdFlowResult result = this.mkNodeResult(context.getNodeId(),
                FlowMonitorParam.RESULT_FLAG_RUNNING, "开始执行", null);
        this.insertResult(result);

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_NODE,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(), null, null, null,
                "开始执行");
        this.insertLog(log);
    }

    @Override
    protected boolean checkInfo() {
        if (actionList == null || actionList.isEmpty()) {
            MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_NODE,
                    FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(), null, null, null,
                    "节点无相关行为");
            this.insertLog(log);
            return false;
        }
        return true;
    }

    @Override
    protected FlowMonitorRJson action() {
        // 遍历当前节点下的所有行为,依次进行相关操作.
        StringBuilder nodeResultData = new StringBuilder("");
        for (MdFlowAction action : actionList) {
            context.setAction(action);
            FlowMonitorActionBusiness actionBusiness = new FlowMonitorActionBusiness();
            FlowMonitorRJson result = actionBusiness.start(context);
            nodeResultData.append("|").append(result.getResult_data());
            if (!FlowMonitorParam.RESULT_FLAG_SUCCESS.equalsIgnoreCase(result.getResult_flag())) {
                // 执行失败则终止循环并返回失败的结果
                // 如果节点执行失败，标记流程结束.taskBusiness中根据此字段判断是否需要继续执行下一节点.
                result.setIsEnd(FlowMonitorParam.FLOW_END_FLAG);
                return result;
            }
        }
        String replaceFirst = nodeResultData.toString().replaceFirst("\\|", "");
        return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_SUCCESS, replaceFirst);
    }

    @Override
    protected void end(FlowMonitorRJson curResult) {
        MdFlowResult result = this.mkNodeResult(context.getNodeId(), curResult.getResult_flag(),
                curResult.getResult_data(), curResult.getIsEnd());
        this.updateResult(result);

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_NODE, curResult.getResult_flag(),
                context.getNodeId(), null, null, null, curResult.getResult_data());
        this.insertLog(log);

        logger.info("serialNum:{} ,nodeId:{} {}.", context.getSerialNum(), context.getNodeId(),
                curResult.getResult_flag());
    }
}
