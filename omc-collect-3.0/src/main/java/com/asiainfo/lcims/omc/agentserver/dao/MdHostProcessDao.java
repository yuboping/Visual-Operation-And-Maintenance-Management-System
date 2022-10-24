package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.MdHostProcess;

public class MdHostProcessDao {
    private static final Logger log = LoggerFactory.make();

    public List<MdHostProcess> getAllMdHostProcess() {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        List<MdHostProcess> list = new ArrayList<MdHostProcess>();
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "SELECT HOST_ID,PROCESS_ID,PROCESS_KEY,START_SCRIPT,STOP_SCRIPT,DESCRIPTION,CREATE_TIME,UPDATE_TIME FROM MD_HOST_PROCESS";
            stat = conn.prepareStatement(sql);
            rset = stat.executeQuery();
            while (rset.next()) {
                MdHostProcess info = new MdHostProcess();
                info.setId(rset.getString("ID"));
                info.setHostId(rset.getString("HOST_ID"));
                info.setProcessId(rset.getString("PROCESS_ID"));
                info.setProcessKey(rset.getString("PROCESS_KEY"));
                info.setStartScript(rset.getString("START_SCRIPT"));
                info.setStopScript(rset.getString("STOP_SCRIPT"));
                info.setDescription(rset.getString("DESCRIPTION"));
                info.setCreateTime(rset.getTimestamp("CREATE_TIME"));
                info.setUpdateTime(rset.getTimestamp("UPDATE_TIME"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("sql{" + sql + "} error:" + e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public List<MdHostProcess> getMdHostProcessByHostId(String hostId) {
        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;
        List<MdHostProcess> list = new ArrayList<MdHostProcess>();
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            sql = "SELECT ID,HOST_ID,PROCESS_ID,PROCESS_KEY,START_SCRIPT,STOP_SCRIPT,DESCRIPTION,"
                    + "CREATE_TIME,UPDATE_TIME FROM MD_HOST_PROCESS WHERE HOST_ID=?";
            stat = conn.prepareStatement(sql);
            stat.setString(1, hostId);
            rset = stat.executeQuery();
            while (rset.next()) {
                MdHostProcess info = new MdHostProcess();
                info.setId(rset.getString("ID"));
                info.setHostId(rset.getString("HOST_ID"));
                info.setProcessId(rset.getString("PROCESS_ID"));
                info.setProcessKey(rset.getString("PROCESS_KEY"));
                info.setStartScript(rset.getString("START_SCRIPT"));
                info.setStopScript(rset.getString("STOP_SCRIPT"));
                info.setDescription(rset.getString("DESCRIPTION"));
                info.setCreateTime(rset.getTimestamp("CREATE_TIME"));
                info.setUpdateTime(rset.getTimestamp("UPDATE_TIME"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("sql{" + sql + "} error:" + e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }
}
