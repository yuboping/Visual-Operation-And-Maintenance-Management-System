package com.asiainfo.lcims.omc.flowmonitor.quartz;

import org.springframework.beans.factory.InitializingBean;

import com.asiainfo.lcims.omc.flowmonitor.common.FlowMonitorCacheSerialNum;
import com.asiainfo.lcims.omc.flowmonitor.model.FlowMonitorContext;
import com.asiainfo.lcims.omc.flowmonitor.model.SerialNumInfo;
import com.asiainfo.lcims.omc.flowmonitor.service.FlowMonitorTaskBusiness;

public class TaskTest implements InitializingBean {

    // <bean class="com.asiainfo.lcims.omc.flowmonitor.quartz.TaskTest" />

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        FlowMonitorTaskBusiness task = new FlowMonitorTaskBusiness();
        FlowMonitorContext context = new FlowMonitorContext();
        SerialNumInfo serialNum = FlowMonitorCacheSerialNum.getSerialNum("1");
        context.setSerialNum(serialNum.getSerialNum());
        context.setTaskId("1");
        task.start(context);
    }
}
