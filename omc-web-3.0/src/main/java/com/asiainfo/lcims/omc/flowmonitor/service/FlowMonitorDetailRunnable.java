package com.asiainfo.lcims.omc.flowmonitor.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheDetailResult;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorParam;
import com.asiainfo.lcims.omc.flowmonitor.common.RunShellUtil;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorContext;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorRJson;
import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowDetail;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.persistence.configmanage.ProcessOperateDAO;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.socket.common.InitParam;
import com.asiainfo.lcims.omc.socket.common.OpType;
import com.asiainfo.lcims.omc.socket.model.BaseValue;
import com.asiainfo.lcims.omc.socket.model.ShellWeb2Server;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.SpringContextUtil;

/**
 * 业务详情操作.
 * <p>
 * local_shell 操作 和send_shell操作
 * 
 * @author XHT
 *
 */
public class FlowMonitorDetailRunnable extends AbstractFlowMonitor implements Runnable {
    private static final Logger logger = LoggerFactory.make();
    private static final int SCAN_OPERATE_WAITING_TIME = 100;
    private static final int THREAD_CONTINUE_TIMES = 100;
    private MdFlowDetail detail;
    private String previousCacheResultData;
    private String action_type;
    private String detailResultData;
    private AtomicBoolean atomicBoolean;
    private ProcessOperateDAO processOperateDAO;

    public FlowMonitorDetailRunnable(FlowMonitorContext context, String previousCacheResultData,
            String action_type, AtomicBoolean atomicBoolean) {
        this.context = context;
        this.detail = context.getDetail();
        this.previousCacheResultData = previousCacheResultData;
        this.action_type = action_type;
        this.atomicBoolean = atomicBoolean;
        processOperateDAO = SpringContextUtil.getBean(ProcessOperateDAO.class);
    }

    @Override
    public void run() {
        FlowMonitorRJson result = this.start(context);
        // 将当前线程的执行结果进行缓存，用于下个action使用.
        FlowMonitorCacheDetailResult.putValue(context.getSerialNum(), result);

        boolean isSuccess = false;
        if (FlowMonitorParam.BREAK_FLAG_FAILED_BREAK.equals(context.getAction().getBreak_flag())) {
            // break_flag=2,只要有一个进程的执行结果不为SUCCESS，则中断进程的执行
            isSuccess = !FlowMonitorParam.RESULT_FLAG_SUCCESS
                    .equalsIgnoreCase(result.getResult_flag());
        } else if (FlowMonitorParam.BREAK_FLAG_SUCCESS_BREAK
                .equals(context.getAction().getBreak_flag())) {
            // break_flag=1,只要有一个进程的执行结果为 success，则中断进程的执行
            isSuccess = FlowMonitorParam.RESULT_FLAG_SUCCESS
                    .equalsIgnoreCase(result.getResult_flag());
        }
        atomicBoolean.compareAndSet(false, isSuccess);// 执行后标记结果
    }

    @Override
    protected void init() {
    }

    @Override
    protected void begin() {
        logger.info("serialNum:{} ,detailId:{} start.", context.getSerialNum(),
                detail.getDetail_id());

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_DETAIL,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(),
                context.getAction().getAction_id(), detail.getDetail_id(), null, "开始执行");
        this.insertLog(log);
    }

    @Override
    protected boolean checkInfo() {
        return true;
    }

    @Override
    protected FlowMonitorRJson action() {
        if (FlowMonitorParam.ACTION_TYPE_LOCAL_SHELL.equalsIgnoreCase(action_type)) {
            detailResultData = this.actionForLocalShell();
        } else if (FlowMonitorParam.ACTION_TYPE_SEND_SHELL.equalsIgnoreCase(action_type)) {
            detailResultData = this.actionForSendShell();
        }
        FlowMonitorRJson result = this.parseInfo(detailResultData);
        return result;
    }

    private String actionForLocalShell() {
        String command = detail.getShell() + previousCacheResultData;
        logger.info("serialNum:{} ,detailid:{} local_shell: {}.", context.getSerialNum(),
                detail.getDetail_id(), command);
        return RunShellUtil.runShell(command);
    }

    private String actionForSendShell() {
        // 首先向processOperate表中添加需要下发执行脚本的数据
        String id = this.insertOperateInfo();

        // 然后构造执行脚本的命名并向采集服务端下发
        try {
            String msg = this.mkSendInfo(id);
            logger.info("serialNum:{} ,detailid:{} send msg{} to collect_server..",
                    context.getSerialNum(), detail.getDetail_id(), msg);
            String result = new SimpleChatClient().sendMsg(msg);
            logger.info("serialNum:{} ,detailid:{} receive {} from collect_server.",
                    context.getSerialNum(), detail.getDetail_id(), result);
            if (!result.contains(InitParam.RESULT_SUCCESS)) {
                return "send msg to collect_server error";
            }
        } catch (Exception e) {
            logger.error("send msg to collect_server error.", e);
            return "send msg to collect_server error.";
        }

        // 最后扫描processOperate表中的执行结果判断脚本的执行情况
        String operateResult = this.scanOperateResult(id);
        logger.info("serialNum:{} ,detailId:{} ,operate Id:{},result is :{}.",
                context.getSerialNum(), detail.getDetail_id(), id, operateResult);
        return operateResult;
    }

    private String insertOperateInfo() {
        ProcessOperate processOperate = new ProcessOperate();
        String operateId = IDGenerateUtil.getUuid();
        processOperate.setId(operateId);
        processOperate.setHost_ip(detail.getHost_ip());
        processOperate.setOperate_id(detail.getDetail_id());
        processOperate.setProcess_script(detail.getShell() + previousCacheResultData);
        processOperate.setOperate_state(ConstantUtill.PROCESS_START);
        processOperate.setUpdate_time(DateTools.getCurrentFormatDate());
        processOperate.setCreate_time(DateTools.getCurrentFormatDate());
        processOperateDAO.insertProcessOpereate(processOperate);
        return operateId;
    }

    // 根据detail构造成采集服务端可识别的消息体.
    private String mkSendInfo(String operateId) {
        ShellWeb2Server sw = new ShellWeb2Server();
        sw.setId(operateId);
        List<ShellWeb2Server> list = new LinkedList<>();
        list.add(sw);
        BaseValue<ShellWeb2Server> req = new BaseValue<ShellWeb2Server>();
        req.setInfo(list);
        req.setMark(IDGenerateUtil.getUuid());
        req.setOptype(OpType.SHELL_WEB_AGENTSERVER.getType());
        return JSON.toJSONString(req);
    }

    private String scanOperateResult(String id) {
        ProcessOperate operateInfo = null;
        int num = 0;
        while (true) {
            operateInfo = processOperateDAO.getProcessOperateById(id);
            if (operateInfo != null
                    && ConstantUtill.PROCESS_START.intValue() != operateInfo.getOperate_state()) {
                return operateInfo.getOperate_result();
            } else if (num > THREAD_CONTINUE_TIMES) {
                return "SCAN OPERATE MORE THEN {" + THREAD_CONTINUE_TIMES
                        + "} TIMES.CHECK OPERATE_ID[" + id + "]RESULT.";
            }
            try {
                num++;
                Thread.sleep(SCAN_OPERATE_WAITING_TIME);
            } catch (Exception e) {
                logger.error("serialNum:{} ,Thread sleep error.", e);
            }
        }
    }

    @Override
    protected void end(FlowMonitorRJson curResult) {
        logger.info("serialNum:{} ,detailId:{} {}.", context.getSerialNum(), detail.getDetail_id(),
                curResult.getResult_flag());

        MdFlowLog log = this.mkLog(FlowMonitorParam.FLOW_TYPE_DETAIL,
                FlowMonitorParam.RESULT_FLAG_SUCCESS, context.getNodeId(),
                context.getAction().getAction_id(), detail.getDetail_id(), detailResultData,
                curResult.getResult_data());
        this.insertLog(log);
    }

    /**
     * 解析JSON. 返回值不会为空.
     * 
     * @param info
     * @return
     */
    private FlowMonitorRJson parseInfo(String info) {
        if (info == null || info.isEmpty()) {
            return new FlowMonitorRJson(FlowMonitorParam.RESULT_FLAG_FAILED,
                    "[detailId:" + detail.getDetail_id() + "] json is empty");
        }

        FlowMonitorRJson json = null;
        try {
            json = JSON.parseObject(info, FlowMonitorRJson.class);
            // 如果执行成功,则先将执行结果放在cache_data中,action中收集数据的时候使用.
            json.setCache_data(json.getResult_data());
            json.setResult_data(
                    "[detailId:" + detail.getDetail_id() + "] " + json.getResult_data());
        } catch (Exception e) {
            logger.error("serialNum:{} ,detailId:{} parse json:{} error. error:{}", context.getSerialNum(),
                    detail.getDetail_id(), info, e);
            json = new FlowMonitorRJson();
            json.setResult_flag(FlowMonitorParam.RESULT_FLAG_FAILED);
            json.setResult_data(
                    "[detailId:" + detail.getDetail_id() + "] json:" + info + " parse error.");
        }
        return json;
    }
}
