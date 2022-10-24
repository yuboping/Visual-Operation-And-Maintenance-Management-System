package com.asiainfo.lcims.omc.agentserver.service.business;

import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.MdHostMetricDao;
import com.asiainfo.lcims.omc.agentserver.model.MetricClient2Server;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;
import com.asiainfo.lcims.omc.common.MetricStatus;
import com.asiainfo.lcims.omc.common.ResultType;

import io.netty.channel.Channel;

/**
 * agentServer向agentClient下发指标后,agentClient向agetnServer返回对应client主机生成指标的结果.
 * <p>
 * 如果client指标更新成功,则将 主机-指标关系表 中的待删除的指标删除.然后更新对应主机的所有指标状态为已下发状态.
 * 
 * @author XHT
 *
 */
public class BusinessMetricClient2Server extends AbstractServerBusiness<MetricClient2Server> {
    private static final Logger log = LoggerFactory.make();

    @Override
    protected void doAction(List<MetricClient2Server> infoList, Channel channel) {
        for (MetricClient2Server info : infoList) {
            String hostId = DbDataCache.getHostIdByIp(info.getHostip(), mark);
            if (hostId == null) {
                log.info("mark:[" + mark + "].hostip:" + info.getHostip() + "} not in the memory.");
                continue;
            }
            if (ResultType.FAILED.equals(info.getResult())) {
                log.info("mark:[" + mark + "].hostip:" + info.getHostip() + "} send_metric error.");
                continue;
            }
            MdHostMetricDao mdHostMetricDao = new MdHostMetricDao();
            mdHostMetricDao.deleteByHostId(MetricStatus.need_delete.getType(), hostId);
            mdHostMetricDao.updateStateByHostId(MetricStatus.send_client_sucess.getType(), hostId);
            log.info("mark:[" + mark + "].update:{" + info.getHostip() + "} send_metric status.");
        }
    }

}
