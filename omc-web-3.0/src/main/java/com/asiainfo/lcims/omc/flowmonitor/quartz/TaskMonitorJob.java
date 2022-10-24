package com.asiainfo.lcims.omc.flowmonitor.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheSerialNum;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorContext;
import com.asiainfo.lcims.omc.flowmonitor.model.SerialNumInfo;
import com.asiainfo.lcims.omc.flowmonitor.service.FlowMonitorTaskBusiness;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowTask;

public class TaskMonitorJob implements Job {
    private static final Logger logger = LoggerFactory.make();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            MdFlowTask task = (MdFlowTask) context.getMergedJobDataMap().get("scheduleJob");

            String task_id = task.getTask_id();
            logger.info("taskId:{} flowmonitor_start", task_id);

            FlowMonitorContext flowMonitorContext = new FlowMonitorContext();
            flowMonitorContext.setTaskId(task_id);
            SerialNumInfo serialNum = FlowMonitorCacheSerialNum.getSerialNum(task_id);
            if (serialNum.isNewFlag()) {
                flowMonitorContext.setSerialNum(serialNum.getSerialNum());
                FlowMonitorTaskBusiness taskBusiness = new FlowMonitorTaskBusiness();
                taskBusiness.start(flowMonitorContext);
            } else {
                logger.info("taskId:{} flowmonitor_is_starting ", task_id);
            }
            logger.info("taskId:{} flowmonitor_end", task_id);
        } catch (Exception e) {
            logger.error("flowmonitor_error", e);
        }
    }
}
