package com.asiainfo.lcims.omc.agentserver.dao;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.StringUtils;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.Metric;
import org.slf4j.Logger;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class StatisDataMonthDao {
    private static final Logger log = LoggerFactory.make();

    public boolean insertMonthMoreType(String tab_index_num, List<Metric> metricList, String mark) {
        boolean flag = true;
        String sql = "INSERT INTO STATIS_DATA_MONTH_" + tab_index_num
                + "(STIME,METRIC_ID,ATTR1,ATTR2,ATTR3,ATTR4,MVALUE,CREATE_TIME) VALUES(?,?,?,?,?,?,?,?)";
        DruidPooledConnection conn = null;
        PreparedStatement prep = null;
        try {
            conn = DruidConnPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            prep = conn.prepareStatement(sql);

            for (Metric info : metricList) {
                if (!StringUtils.isEmpty(info.getAttr4())) {
                    info.setStime(Timestamp.valueOf(info.getAttr4()));
                }
                prep.setTimestamp(1, info.getStime());
                prep.setString(2, info.getMetricid());
                prep.setString(3, info.getAttr1());
                prep.setString(4, info.getAttr2());
                prep.setString(5, info.getAttr3());
                prep.setString(6, info.getAttr4());
                prep.setString(7, info.getVal());
                prep.setTimestamp(8, info.getCreateTime());
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
