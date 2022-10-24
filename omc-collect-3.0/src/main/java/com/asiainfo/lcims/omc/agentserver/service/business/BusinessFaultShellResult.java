package com.asiainfo.lcims.omc.agentserver.service.business;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.AlarmFaultHandleHisDao;
import com.asiainfo.lcims.omc.agentserver.enity.AlarmFaultHandleHis;
import com.asiainfo.lcims.omc.agentserver.model.FaultShellResult2Server;
import io.netty.channel.Channel;

/**
 * 接收到client指标采集到的数据,根据指标类型解析json并记录入库.
 */
public class BusinessFaultShellResult extends AbstractServerBusiness<FaultShellResult2Server> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<FaultShellResult2Server> infoList, Channel channel) {
        // 更新对应ID的状态
        List<AlarmFaultHandleHis> list = this.mkInfoList(infoList);
        if (list == null || list.isEmpty()) {
            log.info("mark:[" + mark + "]. shell result list is null");
            return;
        }
        boolean flag = new AlarmFaultHandleHisDao().updateInfoById(list);
        log.info("mark:[" + mark + "]. shell result updated:" + flag);
    }

    private List<AlarmFaultHandleHis> mkInfoList(List<FaultShellResult2Server> infoList) {
        if (infoList == null) {
            return null;
        }
        Timestamp currentTime = new Timestamp(new Date().getTime());
        List<AlarmFaultHandleHis> list = new LinkedList<AlarmFaultHandleHis>();
        for (FaultShellResult2Server info : infoList) {
            AlarmFaultHandleHis faultHandle = new AlarmFaultHandleHis();
            faultHandle.setUpdateDate(currentTime);
            faultHandle.setId(info.getId());
            faultHandle.setAlarmFaultId(info.getAlarmFaultId());
            faultHandle.setFaultState(info.getFaultState());
            faultHandle.setFaultResult(info.getShellResult());
            list.add(faultHandle);
        }
        return list;
    }
}
