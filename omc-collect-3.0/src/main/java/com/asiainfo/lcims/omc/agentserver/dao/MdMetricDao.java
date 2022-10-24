package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.omc.agentserver.enity.MdMetric;

public class MdMetricDao {

    private static final Logger LOG = LoggerFactory.getLogger(MdMetricDao.class);

    public List<MdMetric> getAllMdMetric() {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        List<MdMetric> list = new ArrayList<MdMetric>();
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            String sql = "SELECT ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,METRIC_TYPE,DESCRIPTION FROM MD_METRIC";
            stat = conn.prepareStatement(sql);
            rset = stat.executeQuery();
            while (rset.next()) {
                MdMetric info = new MdMetric();
                info.setId(rset.getString("ID"));
                info.setMetricIdentity(rset.getString("METRIC_IDENTITY"));
                info.setMetricName(rset.getString("METRIC_NAME"));
                info.setCycleId(rset.getInt("CYCLE_ID"));
                info.setScript(rset.getString("SCRIPT"));
                info.setScriptParam(rset.getString("SCRIPT_PARAM"));
                info.setScriptReturnType(rset.getInt("SCRIPT_RETURN_TYPE"));
                info.setMetricType(rset.getString("METRIC_TYPE"));
                info.setDescription(rset.getString("DESCRIPTION"));
                list.add(info);
            }
        } catch (SQLException e) {
            LOG.error("select error:", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public List<MdMetric> getMdMetricByMetricType(String metricType) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        List<MdMetric> list = new ArrayList<MdMetric>();
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            String sql = "SELECT ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,METRIC_TYPE,DESCRIPTION FROM MD_METRIC WHERE METRIC_TYPE=?";
            stat = conn.prepareStatement(sql);

            stat.setString(1, metricType);
            rset = stat.executeQuery();
            while (rset.next()) {
                MdMetric info = new MdMetric();
                info.setId(rset.getString("ID"));
                info.setMetricIdentity(rset.getString("METRIC_IDENTITY"));
                info.setMetricName(rset.getString("METRIC_NAME"));
                info.setCycleId(rset.getInt("CYCLE_ID"));
                info.setScript(rset.getString("SCRIPT"));
                info.setScriptParam(rset.getString("SCRIPT_PARAM"));
                info.setScriptReturnType(rset.getInt("SCRIPT_RETURN_TYPE"));
                info.setMetricType(rset.getString("METRIC_TYPE"));
                info.setDescription(rset.getString("DESCRIPTION"));
                list.add(info);
            }
        } catch (SQLException e) {
            LOG.error("select error:", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static MdMetric getMdMetricByMetricId(String metricId) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        MdMetric info = new MdMetric();
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            String sql = "SELECT ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,METRIC_TYPE,DESCRIPTION FROM MD_METRIC WHERE ID=?";
            stat = conn.prepareStatement(sql);
            stat.setString(1, metricId);
            rset = stat.executeQuery();

            while (rset.next()) {
                info.setId(rset.getString("ID"));
                info.setMetricIdentity(rset.getString("METRIC_IDENTITY"));
                info.setMetricName(rset.getString("METRIC_NAME"));
                info.setCycleId(rset.getInt("CYCLE_ID"));
                info.setScript(rset.getString("SCRIPT"));
                info.setScriptParam(rset.getString("SCRIPT_PARAM"));
                info.setScriptReturnType(rset.getInt("SCRIPT_RETURN_TYPE"));
                info.setMetricType(rset.getString("METRIC_TYPE"));
                info.setDescription(rset.getString("DESCRIPTION"));
            }
        } catch (Exception e) {
            LOG.error("get mdMetric by metric id error:", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return info;
    }

}
