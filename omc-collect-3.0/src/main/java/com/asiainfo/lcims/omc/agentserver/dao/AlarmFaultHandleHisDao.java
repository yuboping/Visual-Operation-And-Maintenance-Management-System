package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.AlarmFaultHandleHis;
import com.asiainfo.lcims.omc.agentserver.model.FaultShellServer2Client;

public class AlarmFaultHandleHisDao {
    private static final Logger log = LoggerFactory.make();

    public List<AlarmFaultHandleHis> getAlarmFaultHandleHisById(List<String> idList) {
        List<AlarmFaultHandleHis> list = new ArrayList<AlarmFaultHandleHis>();

        if (idList == null || idList.isEmpty()) {
            return list;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT ID,ALARM_FAULT_ID,HOST_IP,FAULT_SCRIPT FROM MD_ALARM_FAULT_HANDLE_HIS WHERE ID IN(?");
        for (int i = 0; i < idList.size() - 1; i++) {
            sql.append(",?");
        }
        sql.append(")");

        DruidPooledConnection conn = null;
        PreparedStatement stat = null;
        ResultSet rset = null;

        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            stat = conn.prepareStatement(sql.toString());

            for (int i = 1; i <= idList.size(); i++) {
                stat.setString(i, idList.get(i - 1));
            }
            rset = stat.executeQuery();
            while (rset.next()) {
                AlarmFaultHandleHis info = new AlarmFaultHandleHis();
                info.setId(rset.getString("ID"));
                info.setAlarmFaultId(rset.getString("ALARM_FAULT_ID"));
                info.setHostIp(rset.getString("HOST_IP"));
                info.setFaultScript(rset.getString("FAULT_SCRIPT"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("sql{" + sql.toString() + "} select error.", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }

    // 插入数据库
    public boolean updateInfoById(List<AlarmFaultHandleHis> infoList) {
        String sql = "UPDATE MD_ALARM_FAULT_HANDLE_HIS SET MODIFY_DATE =?, FAULT_RESULT=?,FAULT_STATE=? WHERE ID=?";
        boolean flag = true;
        DruidPooledConnection conn = null;
        PreparedStatement prep = null;
        try {
            conn = DruidConnPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            prep = conn.prepareStatement(sql);
            for (AlarmFaultHandleHis info : infoList) {
                prep.setTimestamp(1, info.getUpdateDate());
                prep.setString(2, info.getFaultResult());
                prep.setInt(3, info.getFaultState());
                prep.setString(4, info.getId());
                prep.addBatch();
            }
            prep.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.error("error message:" + e);
            flag = false;
        } finally {
            DruidConnPool.close(null, prep, conn);
        }
        return flag;
    }
    
    
    // 更新故障处理细表 MD_ALARM_FAULT_DETAIL
    public boolean updateFaultDetailById(List<FaultShellServer2Client> infoList, int state) {
        String sql = "UPDATE MD_ALARM_FAULT_DETAIL SET MODIFY_DATE =?, FAULT_STATE=? WHERE ID=?";
        boolean flag = true;
        DruidPooledConnection conn = null;
        PreparedStatement prep = null;
        try {
            conn = DruidConnPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            prep = conn.prepareStatement(sql);
            for (FaultShellServer2Client info : infoList) {
                prep.setTimestamp(1, new Timestamp(new Date().getTime()));
                prep.setInt(2, state);
                prep.setString(3, info.getId());
                prep.addBatch();
            }
            prep.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.error("error message:" + e);
            flag = false;
        } finally {
            DruidConnPool.close(null, prep, conn);
        }
        return flag;
    }
}
