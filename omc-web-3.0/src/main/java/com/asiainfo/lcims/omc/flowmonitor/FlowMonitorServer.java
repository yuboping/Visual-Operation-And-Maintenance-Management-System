package com.asiainfo.lcims.omc.flowmonitor;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheSerialNum;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorContext;
import com.asiainfo.lcims.omc.flowmonitor.model.SerialNumInfo;
import com.asiainfo.lcims.omc.flowmonitor.service.FlowMonitorTaskBusiness;
import com.asiainfo.lcims.omc.model.flowmonitor.FlowSerialNum;

@Service(value = "flowMonitorServer")
public class FlowMonitorServer {
    private static final Logger logger = LoggerFactory.make();

    /**
     * 获取task_id的最新serialNum,如果缓存中没有task_id缓存的serialNum,则启动对应的流程监控。
     * 
     * @param task_id
     * @return
     */
    public FlowSerialNum getNewestSerialNumForTaskId(String task_id) {
        FlowSerialNum num = new FlowSerialNum();
        if (task_id == null || task_id.isEmpty()) {
            num.setMessage("task_id is null.");
            return num;
        }
        SerialNumInfo serialNumInfo = FlowMonitorCacheSerialNum.getSerialNum(task_id);
        String serialNum = serialNumInfo.getSerialNum();
        if (serialNumInfo.isNewFlag()) {
            FlowMonitorContext context = new FlowMonitorContext(task_id, serialNum);
            this.startFlowMonitor(context);
            num.setMessage("已启动全流程监控业务,SERIAL_NUM:" + serialNum);
        } else {
            num.setMessage("全流程业务正在执行中,SERIAL_NUM:" + serialNum);
        }
        num.setSerial_num(serialNum);
        logger.info("flowmonitor taskId:{} start.newflag:{} ,serialNum:", task_id,
                serialNumInfo.isNewFlag(), serialNum);
        return num;
    }

    private void startFlowMonitor(FlowMonitorContext context) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FlowMonitorTaskBusiness task = new FlowMonitorTaskBusiness();
                task.start(context);
            }
        });
        t.start();
    }
}
