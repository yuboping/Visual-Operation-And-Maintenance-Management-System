package com.asiainfo.lcims.omc.agentserver.service.business;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.agentserver.dao.ProcessOperateDao;
import com.asiainfo.lcims.omc.agentserver.enity.ProcessOperate;
import com.asiainfo.lcims.omc.agentserver.model.ShellResult2Server;
import com.asiainfo.lcims.omc.common.OperateState;

import io.netty.channel.Channel;

/**
 * 接收到client指标采集到的数据,根据指标类型解析json并记录入库.
 * 
 * @author XHT
 *
 */
public class BusinessShellResult extends AbstractServerBusiness<ShellResult2Server> {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessShellResult.class);

    @Override
    protected void doAction(List<ShellResult2Server> infoList, Channel channel) {
        // 更新对应ID的状态
        List<ProcessOperate> oprateList = this.mkInfoList(infoList);
        if (oprateList == null || oprateList.isEmpty()) {
            return;
        }
        boolean flag = new ProcessOperateDao().updateInfoById(oprateList);
        LOG.info("mark:[{}]. shell result updated: {}", mark, flag);
    }

    private List<ProcessOperate> mkInfoList(List<ShellResult2Server> infoList) {
        if (infoList == null) {
            return null;
        }
        Timestamp currentTime = new Timestamp(new Date().getTime());
        List<ProcessOperate> operateList = new LinkedList<ProcessOperate>();
        for (ShellResult2Server info : infoList) {
            ProcessOperate operate = new ProcessOperate();
            operate.setUpdateTime(currentTime);
            String shellResult = info.getShellResult();
            operate.setOperateResult(shellResult);
            operate.setId(info.getId());
            String operateResult = StringUtils.lowerCase(shellResult);
            if (StringUtils.contains(operateResult, "error")) {
                operate.setOperateState(OperateState.FAILED);
            } else {
                operate.setOperateState(OperateState.SUCCESS);
            }
            operateList.add(operate);
        }
        return operateList;
    }
}
