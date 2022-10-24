package com.asiainfo.lcims.omc.flowmonitor.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheDetailResult;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorParam;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowAction;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowDetail;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowDetailDao;

public class FlowMonitorActionBusiness extends AbstractFlowMonitor {
    private static final Logger logger = LoggerFactory.make();
    private static final int DEF_SLEEP_TIME = 1000;
    private static final int THREAD_SLEEP_TIME = 200;// 等待多线程执行结果时睡眠时间
    private MdFlowAction action;
    private List<MdFlowDetail> detailList;

    @Override
    protected void init() {
        action = context.getAction();
        if (FlowMonitorParam.ACTION_TYPE_LOCAL_SHELL.equalsIgnoreCase(action.getAction_type())
                || FlowMonitorParam.ACTION_TYPE_SEND_SHELL
                        .equalsIgnoreCase(action.getAction_type())) {
            try {
                detailList = getDao(MdFlowDetailDao.class).selectByActionId(action.getAction_id());
            } catch (Exception e) {
                logger.error("serialNum:{},actionId:{} select error.", context.getSerialNum(),
                        action.getAction_id(), e);
            }
        }
    }

    @Override
    protected void begin() {
        logger.info("serialNum:{} ,actionId:{} start.", context.getSerialNum(),
                action.getAction_id());

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_ACTION,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, action.getNode_id(), action.getAction_id(),
                null, null, "开始执行");
        this.insertLog(log);
    }

    @Override
    protected boolean checkInfo() {
        if (!FlowMonitorParam.ACTION_TYPE_SLEEP.equalsIgnoreCase(action.getAction_type())
                && (detailList == null || detailList.isEmpty())) {
            MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_ACTION,
                    FlowMonitorParam.RESULT_FLAG_SUCCESS, action.getNode_id(),
                    action.getAction_id(), null, null, "行为无对应详情");
            this.insertLog(log);
            return false;
        }
        return true;
    }

    @Override
    protected FlowMonitorRJson action() {
        if (FlowMonitorParam.ACTION_TYPE_SLEEP.equalsIgnoreCase(action.getAction_type())) {
            // 如果是slepp类型,则直接进行sleep操作.
            return this.actionForSleep();
        } else if (FlowMonitorParam.ACTION_TYPE_LOCAL_SHELL
                .equalsIgnoreCase(action.getAction_type())
                || FlowMonitorParam.ACTION_TYPE_SEND_SHELL
                        .equalsIgnoreCase(action.getAction_type())) {
            return this.actionForShell();
        } else {
            logger.error("serialNum:{},actionId:{},action_type{} undefined .",
                    context.getSerialNum(), action.getAction_id(), action.getAction_type());
            return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_FAILED,
                    "action_type" + action.getAction_type() + " undefined");
        }
    }

    private FlowMonitorRJson actionForShell() {
        // 统计上个缓存的执行结果.
        String previousCacheData = converPreviousDetailCacheData();
        // 开辟多线程执行任务
        ExecutorService service = Executors.newFixedThreadPool(10);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        for (MdFlowDetail detail : detailList) {
            context.setDetail(detail);
            FlowMonitorDetailRunnable detailRunnable = new FlowMonitorDetailRunnable(context,
                    previousCacheData, action.getAction_type(), atomicBoolean);
            service.execute(detailRunnable);
        }
        // 阻塞等待结果
        this.shutdownAndWait(atomicBoolean, service);
        // 统计多线程的执行结果
        return this.statisticThreadResult();
    }

    // 获取上个周期的结果，将result_data合并成字符串
    private String converPreviousDetailCacheData() {
        List<FlowMonitorRJson> previousDetailResultList = FlowMonitorCacheDetailResult
                .getInfoBySerialNum(context.getSerialNum());
        FlowMonitorCacheDetailResult.removeBySerialNum(context.getSerialNum());
        StringBuilder strb = new StringBuilder("");
        if (previousDetailResultList != null && !previousDetailResultList.isEmpty()) {
            for (FlowMonitorRJson info : previousDetailResultList) {
                strb.append(" ").append(info.getCache_data());
            }
        }
        return strb.toString();
    }

    // 统计并计算多线程计算的结果
    // 如果 break_flag为1,2，"或"计算，其他 则"与"计算
    private FlowMonitorRJson statisticThreadResult() {
        List<FlowMonitorRJson> resultList = FlowMonitorCacheDetailResult
                .getInfoBySerialNum(context.getSerialNum());
        if (resultList == null || resultList.isEmpty()) {
            return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_SUCCESS);
        }
        if (FlowMonitorParam.BREAK_FLAG_SUCCESS_BREAK.equals(action.getBreak_flag())) {
            return this.statisBySuccessBreak(resultList);
        } else if (FlowMonitorParam.BREAK_FLAG_FAILED_BREAK.equals(action.getBreak_flag())) {
            return this.statisByFailedBreak(resultList);
        } else {
            return this.statisByDef(resultList);
        }
    }

    // 如果有success,跳出循环并返回success.
    private FlowMonitorRJson statisBySuccessBreak(List<FlowMonitorRJson> resultList) {
        String result_flag = FlowMonitorParam.RESULT_FLAG_FAILED;
        StringBuilder tmp_result_info = new StringBuilder();
        for (FlowMonitorRJson info : resultList) {
            tmp_result_info.append(info.getResult_data()).append(";");
            if (FlowMonitorParam.RESULT_FLAG_SUCCESS.equalsIgnoreCase(info.getResult_flag())) {
                result_flag = FlowMonitorParam.RESULT_FLAG_SUCCESS;
                break;
            }
        }
        return new FlowMonitorRJson(result_flag, tmp_result_info.toString());
    }

    // 如果结果不为success,跳出循环并返回failed.
    private FlowMonitorRJson statisByFailedBreak(List<FlowMonitorRJson> resultList) {
        String result_flag = FlowMonitorParam.RESULT_FLAG_SUCCESS;
        StringBuilder tmp_result_info = new StringBuilder();
        for (FlowMonitorRJson info : resultList) {
            tmp_result_info.append(info.getResult_data()).append(";");
            if (!FlowMonitorParam.RESULT_FLAG_SUCCESS.equalsIgnoreCase(info.getResult_flag())) {
                result_flag = FlowMonitorParam.RESULT_FLAG_FAILED;
                break;
            }
        }
        return new FlowMonitorRJson(result_flag, tmp_result_info.toString());
    }

    // 统计所有结果数据。如果有结果不为success,则整体结果为faild.
    private FlowMonitorRJson statisByDef(List<FlowMonitorRJson> resultList) {
        String result_flag = FlowMonitorParam.RESULT_FLAG_SUCCESS;
        StringBuilder tmp_result_info = new StringBuilder();
        for (FlowMonitorRJson info : resultList) {
            if (!FlowMonitorParam.RESULT_FLAG_SUCCESS.equalsIgnoreCase(info.getResult_flag())) {
                result_flag = FlowMonitorParam.RESULT_FLAG_FAILED;
            }
            tmp_result_info.append(info.getResult_data()).append(";");
        }
        return new FlowMonitorRJson(result_flag, tmp_result_info.toString());
    }

    // 阻塞关闭子线程.
    private void shutdownAndWait(AtomicBoolean atomicBoolean, ExecutorService service) {
        service.shutdown();
        while (true) {
            // 如果break_flag为1,2.则有一个线程的执行结果为true就终止多线程
            // 如果break_flag为3(其他),则等待多线程都执行完毕
            if ((FlowMonitorParam.BREAK_FLAG_FAILED_BREAK.equals(action.getBreak_flag())
                    || FlowMonitorParam.BREAK_FLAG_SUCCESS_BREAK.equals(action.getBreak_flag()))
                    && atomicBoolean.get()) {
                service.shutdownNow();
                break;
            } else if (service.isTerminated()) {
                break;
            } else {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (Exception e) {
                    logger.error("serialNum:{},actionid:{},Exception.", e);
                }
            }
        }
    }

    // 根据对应的时间sleep
    private FlowMonitorRJson actionForSleep() {
        try {
            int sleep_time = DEF_SLEEP_TIME;
            if (action.getSleep_time() != null) {
                sleep_time = action.getSleep_time();
            }

            Thread.sleep(sleep_time * 1000L);
            String resultDescript = "[actionId:" + action.getAction_id() + "] sleep " + sleep_time
                    + " seconds]";
            MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_ACTION,
                    FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(),
                    action.getAction_id(), null, null, resultDescript);
            this.insertLog(log);
            return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_SUCCESS, resultDescript);
        } catch (Exception e) {
            logger.error("serialNum:{},actionid:{},Thread sleep error.", context.getSerialNum(),
                    action.getAction_id(), e);
            return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_FAILED,
                    "NodeActionBusiness sleep error");
        }
    }

    @Override
    protected void end(FlowMonitorRJson curResult) {
        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_ACTION,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(), action.getAction_id(),
                null, null, "执行结束");
        this.insertLog(log);

        logger.info("serialNum:{} ,actionId:{} {}.", context.getSerialNum(), action.getAction_id(),
                curResult.getResult_flag());
    }

}
