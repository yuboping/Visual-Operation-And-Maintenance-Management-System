package com.asiainfo.lcims.omc.agentserver.service.business;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.asiainfo.lcims.omc.agentserver.dao.MetricDataSingleDao;
import com.asiainfo.lcims.omc.agentserver.enity.Metric;
import com.asiainfo.lcims.omc.agentserver.model.HeartMsg;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;
import com.asiainfo.lcims.omc.common.TimeConstants;
import com.asiainfo.lcims.omc.conf.InitParam;
import com.asiainfo.lcims.omc.utils.TimeUtil;

import io.netty.channel.Channel;

/**
 * 服务端接收到客户端心跳包反馈的处理.
 * 
 * @author XHT
 *
 */
public class BusinessHeartClient2Server extends AbstractServerBusiness<HeartMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessHeartClient2Server.class);

    @Override
    protected void doAction(List<HeartMsg> infoList, Channel channel) {
        // 关于心跳包中信息的处理
        try {
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            String clientIp = remoteAddress.getAddress().getHostAddress();
            String currentSecondCycle = TimeUtil.getCycleSecondTime();
            LOG.info("current heart time -> {}", currentSecondCycle);
            if (currentSecondCycle.equals(
                    TimeUtil.getCycleTime(TimeConstants.FIVE_MIN_DESC,0))){
                LOG.info("start to insert client_connect metric");
                List<Metric> metricList = getMetricList(clientIp,
                        TimeConstants.CLIENT_CONNECT_ON,
                        TimeConstants.METRIC_CLIENT_CONNECT);
                insertMetric(metricList);
            }
            if (infoList.size() == 0) {
                LOG.warn("mark:[" + mark + "].agent client {} heart msg is empty.", clientIp);
                return;
            }
            String clientDate_str = infoList.get(0).getDateTimeStr();
            long clientTime = Long.parseLong(clientDate_str);
            long serverTime = new DateTime().getMillis();
            long time_dif = Math.abs(serverTime - clientTime) / 1000;// 客户端和服务端时间的时差,单位:s
            if (currentSecondCycle.equals(
                    TimeUtil.getCycleTime(TimeConstants.FIVE_MIN_DESC,0))){
                LOG.info("start to insert client_time_diff metric");
                List<Metric> metricList = getMetricList(clientIp,
                        String.valueOf(time_dif),
                        TimeConstants.METRIC_CLIENT_TIME_DIFF);
                insertMetric(metricList);
            }
            if (time_dif > InitParam.getServerClientMaxJetlag()) {
                LOG.warn("mark:[" + mark + "].agent client {} clock is out of sync.", clientIp);
            }
        } catch (Exception e) {
            LOG.error("mark:[" + mark + "].error:", e);
        }
    }

    private List<Metric> getMetricList(String hostIp, String value, String metricId){
        List<Metric> metricList = new ArrayList<>();
        Metric metric = new Metric();
        metric.setHostid(DbDataCache.getHostIdByIp(hostIp, mark));
        metric.setVal(value);
        metric.setCreateTime( new Timestamp(new Date().getTime()));
        metric.setStime(TimeUtil.getColCurrentTime(new Date(), null));
        if (!StringUtils.isEmpty(metricId)) {
            metricId = DbDataCache.getMetricIdByIdentity(metricId, mark);
        }
        metric.setMetricid(metricId);
        metricList.add(metric);
        return metricList;
    }

    // 插入数据库
    private void insertMetric(List<Metric> metricList) {
        if (metricList == null || metricList.isEmpty()) {
            return;
        }
        String mkTableIndexNum = TimeUtil.colTime(TimeUtil.getColCurrentTime(new Date(), null));

        boolean flag = new MetricDataSingleDao().insertSingleType(mkTableIndexNum, metricList,
                mark);
        LOG.info("mark:[" + mark + "].insert to single table[" + mkTableIndexNum + "]:" + flag);

    }

}
