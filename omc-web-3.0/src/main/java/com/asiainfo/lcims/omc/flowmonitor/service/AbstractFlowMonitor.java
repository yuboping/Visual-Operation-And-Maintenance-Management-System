package com.asiainfo.lcims.omc.flowmonitor.service;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorParam;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorContext;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowResult;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowLogDao;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowResultDao;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.SpringContextUtil;

/**
 * 
 * @author XHT
 *
 */
public abstract class AbstractFlowMonitor {
    private static final Logger logger = LoggerFactory.make();
    private MdFlowLogDao logDao;
    private MdFlowResultDao resultDao;

    public AbstractFlowMonitor() {
        logDao = SpringContextUtil.getBean(MdFlowLogDao.class);
        resultDao = SpringContextUtil.getBean(MdFlowResultDao.class);
    }

    protected FlowMonitorContext context;

    public FlowMonitorRJson start(FlowMonitorContext context) {
        this.context = context;
        FlowMonitorRJson curResult = new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_SUCCESS);
        try {
            this.init();
            this.begin();
            if (this.checkInfo()) {
                curResult = this.action();
            }
            this.end(curResult);
        } catch (Exception e) {
            curResult.setResult_flag(FlowMonitorParam.RESULT_FLAG_FAILED);
            curResult.setResult_data("action error.");
            logger.error("serialNum:{} exception.", context.getSerialNum(), e);
        }
        return curResult;
    }

    protected abstract void init();

    protected abstract void begin();

    protected abstract boolean checkInfo();

    protected abstract FlowMonitorRJson action();

    protected abstract void end(FlowMonitorRJson curResult);

    protected void insertLog(MdFlowLog log) {
        logDao.insert(log);
    }

    protected void insertResult(MdFlowResult result) {
        resultDao.insert(result);
    }

    protected void updateResult(MdFlowResult result) {
        resultDao.updateResult(result);
    }

    protected <T> T getDao(Class<T> clazz) {
        return SpringContextUtil.getBean(clazz);
    }

    protected MdFlowResult mkNodeResult(String nodeId, String resultFlag, String resultDescript,
            String isend) {
        MdFlowResult result = new MdFlowResult();
        result.setTask_id(context.getTaskId());
        result.setId(IDGenerateUtil.getUuid());
        result.setIsend(isend);
        result.setNode_id(nodeId);
        result.setResult_flag(resultFlag);
        result.setResult_descript(resultDescript);
        result.setSerial_num(context.getSerialNum());
        return result;
    }

    // 构造向OMC_SER_LOG中记录数据的数据
    protected MdFlowLog mkLog(String resultType, String resultFlag, String nodeId, String actionId,
            String detailId, String resultData, String resultDescript) {
        MdFlowLog log = new MdFlowLog();
        log.setId(IDGenerateUtil.getUuid());
        log.setTask_id(context.getTaskId());
        log.setNode_id(nodeId);
        log.setAction_id(actionId);
        log.setDetail_id(detailId);
        log.setResult_data(resultData);
        log.setResult_descript(resultDescript);
        log.setResult_flag(resultFlag);
        log.setResult_type(resultType);
        log.setSerial_num(context.getSerialNum());

        return log;
    }
}
