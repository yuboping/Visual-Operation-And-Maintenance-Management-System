package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.MdHostMetric;

public class MdHostMetricDao {
    private static final Logger log = LoggerFactory.make();

    public List<MdHostMetric> getMdHostMetricByHostId(String hostId) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        List<MdHostMetric> list = new ArrayList<MdHostMetric>();
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "SELECT t1.ID AS ID,t1.HOST_ID AS HOST_ID,t1.METRIC_ID AS METRIC_ID,t1.CYCLE_ID AS CYCLE_ID,"
                    + "t1.SCRIPT AS SCRIPT,t1.SCRIPT_PARAM AS SCRIPT_PARAM,t1.SCRIPT_RETURN_TYPE AS SCRIPT_RETURN_TYPE, "
                    + "t1.STATE AS STATE,t1.UPDATE_TIME AS UPDATE_TIME,t2.METRIC_TYPE AS METRIC_TYPE "
                    + "  FROM MD_HOST_METRIC t1 LEFT JOIN MD_METRIC t2 ON t1.METRIC_ID=t2.ID "
                    + " WHERE t1.HOST_ID=? AND t1.STATE != 3";

            stat = conn.prepareStatement(sql);
            stat.setString(1, hostId);
            rset = stat.executeQuery();
            while (rset.next()) {
                MdHostMetric info = new MdHostMetric();
                info.setId(rset.getString("ID"));
                info.setHostId(rset.getString("HOST_ID"));
                info.setMetricId(rset.getString("METRIC_ID"));
                info.setCycleId(rset.getInt("CYCLE_ID"));
                info.setScript(rset.getString("SCRIPT"));
                info.setScriptParam(rset.getString("SCRIPT_PARAM"));
                info.setScriptReturnType(rset.getInt("SCRIPT_RETURN_TYPE"));
                info.setState(rset.getInt("STATE"));
                info.setUpdateTime(rset.getDate("UPDATE_TIME"));
                info.setMetricType(rset.getString("METRIC_TYPE"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("select error.", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public int updateStateByHostId(String status, String hostId) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        int result = 0;
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "UPDATE MD_HOST_METRIC SET STATE=? WHERE HOST_ID=?";
            stat = conn.prepareStatement(sql);
            stat.setInt(1, Integer.parseInt(status));
            stat.setString(2, hostId);
            result = stat.executeUpdate();
        } catch (SQLException e) {
            log.error("sql{" + sql + "} update error.", e);
        } finally {
            DruidConnPool.close(stat, conn);
        }
        return result;
    }

    public int deleteByHostId(String status, String hostId) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        int result = 0;
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "DELETE FROM MD_HOST_METRIC WHERE STATE=? AND HOST_ID=?";
            stat = conn.prepareStatement(sql);
            stat.setInt(1, Integer.parseInt(status));
            stat.setString(2, hostId);
            result = stat.executeUpdate();
        } catch (SQLException e) {
            log.error("sql{" + sql + "} delete error.", e);
        } finally {
            DruidConnPool.close(stat, conn);
        }
        return result;
    }
}
