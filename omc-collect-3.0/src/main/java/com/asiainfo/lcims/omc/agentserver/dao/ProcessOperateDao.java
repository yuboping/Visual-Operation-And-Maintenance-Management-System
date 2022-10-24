package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.ProcessOperate;

public class ProcessOperateDao {
    private static final Logger log = LoggerFactory.make();

    public List<ProcessOperate> getMdHostMetricById(List<String> idList) {
        List<ProcessOperate> list = new ArrayList<ProcessOperate>();

        if (idList == null || idList.isEmpty()) {
            return list;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT ID,HOST_IP,PROCESS_SCRIPT FROM PROCESS_OPERATE WHERE ID IN(?");
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
                ProcessOperate info = new ProcessOperate();
                info.setId(rset.getString("ID"));
                info.setHostIp(rset.getString("HOST_IP"));
                info.setProcessScript(rset.getString("PROCESS_SCRIPT"));
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
    public boolean updateInfoById(List<ProcessOperate> infoList) {
        String sql = "UPDATE PROCESS_OPERATE SET UPDATE_TIME =?, OPERATE_RESULT=?,OPERATE_STATE=? WHERE ID=?";
        boolean flag = true;
        DruidPooledConnection conn = null;
        PreparedStatement prep = null;
        try {
            conn = DruidConnPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            prep = conn.prepareStatement(sql);
            for (ProcessOperate info : infoList) {
                prep.setTimestamp(1, info.getUpdateTime());
                prep.setString(2, info.getOperateResult());
                prep.setInt(3, info.getOperateState());
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
}
