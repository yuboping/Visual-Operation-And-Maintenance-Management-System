package com.asiainfo.lcims.omc.agentserver.service.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.asiainfo.lcims.omc.agentserver.dao.MdMetricDao;
import com.asiainfo.lcims.omc.agentserver.dao.MetricDataMultiDao;
import com.asiainfo.lcims.omc.agentserver.dao.MetricDataSingleDao;
import com.asiainfo.lcims.omc.agentserver.dao.StatisDataMonthDao;
import com.asiainfo.lcims.omc.agentserver.enity.MdMetric;
import com.asiainfo.lcims.omc.agentserver.enity.Metric;
import com.asiainfo.lcims.omc.agentserver.model.MetricResult;
import com.asiainfo.lcims.omc.agentserver.model.MetricResult2Server;
import com.asiainfo.lcims.omc.agentserver.model.MetricResultData;
import com.asiainfo.lcims.omc.agentserver.service.DbDataCache;
import com.asiainfo.lcims.omc.common.MetricType;
import com.asiainfo.lcims.omc.utils.TimeUtil;

import io.netty.channel.Channel;

/**
 * 接收到client指标采集到的数据,根据指标类型解析json并记录入库.
 * 
 * @author XHT
 *
 */
public class BusinessMetricResult extends AbstractServerBusiness<MetricResult2Server> {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessMetricResult.class);

    private final Date CUR_DATE = new Date();

    @Override
    protected void doAction(List<MetricResult2Server> infoList, Channel channel) {
        // 解析结果并记录入库
        for (MetricResult2Server metricResult2Server : infoList) {
            String resulttype = metricResult2Server.getResulttype();
            if (resulttype == null) {
                LOG.error("mark:[" + mark + "].resulttype is null:" + metricResult2Server);
                continue;
            }
            List<Metric> metricList = this.analysis(metricResult2Server);
            this.insertMetric(resulttype, metricList);
        }
    }

    // 解析JSON,并处理成数据库bean对应的结构对象
    private List<Metric> analysis(MetricResult2Server metricResult2Server) {
        List<Metric> metricList = Collections.emptyList();
        String jsonInfo = metricResult2Server.getResultinfo();
        if (jsonInfo == null || jsonInfo.isEmpty()) {
            LOG.error("mark:[" + mark + "].jsonInfo is null:" + metricResult2Server);
            return metricList;
        }
        List<MetricResult> metricResultList = Collections.emptyList();
        try {
            metricResultList = JSON.parseObject(jsonInfo, new TypeReference<List<MetricResult>>() {
            });
        } catch (Exception e) {
            LOG.error("mark:[" + mark + "].metric parse error:" + metricResult2Server);
            return metricList;
        }

        return mkMetricList(metricResult2Server, metricResultList);
    }

    // 构造成可入库的指标数据.
    private List<Metric> mkMetricList(MetricResult2Server metricResult2Server,
            List<MetricResult> metricResultList) {
        List<Metric> metricList = new ArrayList<Metric>();
        String hostId = DbDataCache.getHostIdByIp(metricResult2Server.getHostip(), mark);
        Timestamp currentTime = new Timestamp(CUR_DATE.getTime());
        Timestamp stime = this.mkStime();
        for (MetricResult metricResult : metricResultList) {
            // 如果脚本中的metricid不为空,则入库脚本中传的metricid.
            // 脚本中的metricid是指标的Identity,需要先将Identity转换成指标的Id再进行入库操作.
            String metricId = metricResult2Server.getMetricid();
            if (!StringUtils.isEmpty(metricResult.getMetricid())) {
                metricId = DbDataCache.getMetricIdByIdentity(metricResult.getMetricid(), mark);
            }

            MdMetric mdMetric = MdMetricDao.getMdMetricByMetricId(metricId);
            Integer cycleId = mdMetric.getCycleId();
            if (cycleId == 1) {
                String metricIdentity = mdMetric.getMetricIdentity();
                LOG.info("cycle id 1 of metric name is : {}", metricIdentity);
                DateTime dateTime = new DateTime(CUR_DATE.getTime());
                DateTime downDateTime = dateTime.withField(DateTimeFieldType.secondOfMinute(), 0)
                        .withField(DateTimeFieldType.millisOfSecond(), 0);
                stime = new Timestamp(downDateTime.getMillis());
            }

            List<MetricResultData> metricResultDataList = metricResult.getData();
            if (metricResultDataList == null || metricResultDataList.isEmpty()) {
                LOG.warn("mark:[" + mark + "].metricresult data is null.");
                continue;
            }
            for (MetricResultData info : metricResultDataList) {
                Metric metric = new Metric();
                metric.setCreateTime(currentTime);
                metric.setHostid(hostId);
                metric.setAttr1(info.getAttr1());
                metric.setAttr2(info.getAttr2());
                metric.setAttr3(info.getAttr3());
                metric.setAttr4(info.getAttr4());
                metric.setItem(info.getItem());
                metric.setVal(info.getVal());
                metric.setMetricid(metricId);
                metric.setStime(stime);
                metricList.add(metric);
            }
        }
        return metricList;
    }

    // 插入数据库
    private void insertMetric(String resultType, List<Metric> metricList) {
        if (metricList == null || metricList.isEmpty()) {
            return;
        }
        String mkTableIndexNum = this.mkTableIndexNum();
        if (resultType.equals(MetricType.SINGLE_TYPE.getType())
                || resultType.equals(MetricType.DOUBLE_TYPE.getType())) {
            boolean flag = new MetricDataSingleDao().insertSingleType(mkTableIndexNum, metricList,
                    mark);
            LOG.info("mark:[" + mark + "].insert to single table[" + mkTableIndexNum + "]:" + flag);
        } else if (resultType.equals(MetricType.MORE_TYPE.getType())) {
            boolean flag = new MetricDataMultiDao().insertMoreType(mkTableIndexNum, metricList,
                    mark);
            LOG.info("mark:[" + mark + "].insert to multi table[" + mkTableIndexNum + "]:" + flag);
        } else if (resultType.equals(MetricType.MONTH_MORE_TYPE.getType())) {
            mkTableIndexNum = this.mkMonthTableIndexNum();
            boolean flag = new StatisDataMonthDao().insertMonthMoreType(mkTableIndexNum, metricList,
                    mark);
            LOG.info("mark:[" + mark + "].insert to multi table[" + mkTableIndexNum + "]:" + flag);
        } else {
            LOG.error("mark:[" + mark + "].resulttype:{} undefined.", resultType);
            return;
        }
    }

    // 规整stime
    private Timestamp mkStime() {
        return TimeUtil.getColCurrentTime(CUR_DATE, null);
    }

    // 根据规整的stime计算需要如果的表后缀
    private String mkTableIndexNum() {
        return TimeUtil.colTime(this.mkStime());
    }

    // 根据规整的stime计算需要如果的表后缀
    private String mkMonthTableIndexNum() {
        return TimeUtil.colMonthTime(this.mkStime());
    }
}
