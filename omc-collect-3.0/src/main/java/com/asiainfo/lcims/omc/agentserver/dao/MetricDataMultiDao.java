package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.Metric;

public class MetricDataMultiDao {
    private static final Logger log = LoggerFactory.make();

    public boolean insertMoreType(String tab_index_num, List<Metric> metricList, String mark) {
        boolean flag = true;
        String sql = "INSERT INTO METRIC_DATA_MULTI_" + tab_index_num
                + "(HOST_ID,METRIC_ID,MVALUE,ATTR1,ATTR2,ATTR3,ATTR4,STIME,CREATE_TIME) VALUES(?,?,?,?,?,?,?,?,?)";
        DruidPooledConnection conn = null;
        PreparedStatement prep = null;
        try {
            conn = DruidConnPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            prep = conn.prepareStatement(sql);

            for (Metric info : metricList) {
                prep.setString(1, info.getHostid());
                prep.setString(2, info.getMetricid());
                prep.setString(3, info.getVal());
                prep.setString(4, info.getAttr1());
                prep.setString(5, info.getAttr2());
                prep.setString(6, info.getAttr3());
                prep.setString(7, info.getAttr4());
                prep.setTimestamp(8, info.getStime());
                prep.setTimestamp(9, info.getCreateTime());
                prep.addBatch();
            }
            prep.executeBatch();
            conn.commit();
        } catch (Exception e) {
            flag = false;
            log.error("mark:[" + mark + "].insert error:" + e);
        } finally {
            DruidConnPool.close(null, prep, conn);
        }
        return flag;
    }
}
