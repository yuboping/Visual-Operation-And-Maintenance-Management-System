package com.asiainfo.lcims.omc.flowmonitor.service;

import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheSerialNum;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorParam;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowNode;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowResult;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowNodeDao;

/**
 * 获取task对应的node.并对相应node做相关业务处理.
 * 
 * @author XHT
 *
 */
public class FlowMonitorTaskBusiness extends AbstractFlowMonitor {
    private static final Logger logger = LoggerFactory.make();

    private List<MdFlowNode> nodeList;

    // 初始化加载数据
    @Override
    protected void init() {
        try {
            logger.info("------serialNum:{} ,taskId:{} start.------", context.getSerialNum(),
                    context.getTaskId());

            nodeList = getDao(MdFlowNodeDao.class).selectByTaskId(context.getTaskId());
        } catch (Exception e) {
            logger.error("context.getSerialNum():{} ,taskId:{}.select error.",
                    context.getSerialNum(), context.getTaskId(), e);
        }
    }

    // 记录 相关数据入库
    @Override
    protected void begin() {
        // 流程开始的时候,默认添加一个"开始"节点.
        MdFlowResult result = this.mkNodeResult(FlowMonitorParam.FLOW_NODE_BEGIN,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, "开始执行", null);
        this.insertResult(result);// 记录数据到结果表中

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_FLOW,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, FlowMonitorParam.FLOW_NODE_BEGIN, null, null,
                null, "流程开始");
        this.insertLog(log);// 记录数据到日志表中
    }

    // 校验加载的数据是否为空
    @Override
    protected boolean checkInfo() {
        if (nodeList == null || nodeList.isEmpty()) {
            MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_FLOW,
                    FlowMonitorParam.RESULT_FLAG_SUCCESS, FlowMonitorParam.FLOW_NODE_BEGIN, null,
                    null, null, "流程无相关节点");
            this.insertLog(log);
            return false;
        }
        return true;
    }

    // 遍历task中的所有节点并做对应的节点处理
    protected FlowMonitorRJson action() {
        for (MdFlowNode node : nodeList) {
            context.setNodeId(node.getNode_id());
            FlowMonitorNodeBusiness nodeBusiness = new FlowMonitorNodeBusiness();
            FlowMonitorRJson result = nodeBusiness.start(context);
            if (FlowMonitorParam.FLOW_END_FLAG.equalsIgnoreCase(result.getIsEnd())) {
                // 如果某个节点标记流程结束,则流程中断.
                return result;
            }
        }
        return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_SUCCESS);
    }

    // 流程结束,记录'结束节点'
    @Override
    protected void end(FlowMonitorRJson curResult) {
        if (FlowMonitorParam.RESULT_FLAG_SUCCESS.equalsIgnoreCase(curResult.getResult_flag())) {
            // 如果整个流程的节点执行都是成功,则记录END节点。
            MdFlowResult end = this.mkNodeResult(FlowMonitorParam.FLOW_NODE_END,
                    curResult.getResult_flag(), curResult.getResult_data(),
                    FlowMonitorParam.FLOW_END_FLAG);
            this.insertResult(end);// 记录数据到结果表中
        }

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_FLOW, curResult.getResult_flag(),
                FlowMonitorParam.FLOW_NODE_END, null, null, null, curResult.getResult_data());
        this.insertLog(log);// 记录数据到日志表中

        // 结束之后移除task_id的缓存数据
        FlowMonitorCacheSerialNum.removeTaskId(context.getTaskId());
        logger.info("------serialNum:{} ,taskId:{} {}.------", context.getSerialNum(),
                context.getTaskId(), curResult.getResult_flag());
    }
}
