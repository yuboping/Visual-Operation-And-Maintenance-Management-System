package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.Metric;

public class MetricDataSingleDao {
    private static final Logger log = LoggerFactory.make();

    public boolean insertSingleType(String tab_index_num, List<Metric> metricList, String mark) {
        boolean flag = true;
        String sql = "INSERT INTO METRIC_DATA_SINGLE_" + tab_index_num
                + "(HOST_ID,METRIC_ID,MVALUE,ITEM,STIME,CREATE_TIME) VALUES(?,?,?,?,?,?)";
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
                prep.setString(4, info.getItem());
                prep.setTimestamp(5, info.getStime());
                prep.setTimestamp(6, info.getCreateTime());
                prep.addBatch();
            }
            prep.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.error("mark:[" + mark + "].insert error:" + e);
            flag = false;
        } finally {
            DruidConnPool.close(null, prep, conn);
        }
        return flag;
    }
}
