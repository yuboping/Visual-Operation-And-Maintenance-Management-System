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
import com.asiainfo.lcims.omc.agentserver.enity.RadiusReq;

public class RadiusBusinessDao {
    private static final Logger log = LoggerFactory.make();
    
    public int updateStateByUuid(String uuid, int operateState) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        int result = 0;
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "UPDATE MAINT_OPERATE_LOG SET OPERATE_STATE=? WHERE UUID=?";
            stat = conn.prepareStatement(sql);
            stat.setInt(1, operateState);
            stat.setString(2, uuid);
            result = stat.executeUpdate();
        } catch (SQLException e) {
            log.error("sql{" + sql + "} update error.", e);
        } finally {
            DruidConnPool.close(stat, conn);
        }
        return result;
    }
    
    public int updateStateByUuidAndHostip(String uuid, String hostIp, int operateState) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        int result = 0;
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "UPDATE MAINT_OPERATE_LOG SET OPERATE_STATE=? WHERE UUID=? AND HOST_IP=?";
            stat = conn.prepareStatement(sql);
            stat.setInt(1, operateState);
            stat.setString(2, uuid);
            stat.setString(3, hostIp);
            result = stat.executeUpdate();
        } catch (SQLException e) {
            log.error("sql{" + sql + "} update error.", e);
        } finally {
            DruidConnPool.close(stat, conn);
        }
        return result;
    }
}
