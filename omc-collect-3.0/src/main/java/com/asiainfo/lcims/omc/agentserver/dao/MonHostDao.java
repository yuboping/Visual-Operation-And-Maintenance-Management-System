package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.MonHost;

public class MonHostDao {
    private static final Logger log = LoggerFactory.make();

    public List<MonHost> getAllMonHost() {
        DruidPooledConnection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MonHost> list = new ArrayList<MonHost>();
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            stat = conn.createStatement();
            sql = "SELECT HOSTID,HOSTNAME,ADDR,NODEID,STATUS,HOSTTYPE FROM MON_HOST";

            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MonHost info = new MonHost();
                info.setHostId(rset.getString("HOSTID"));
                info.setHostName(rset.getString("HOSTNAME"));
                info.setNodeId(rset.getString("NODEID"));
                info.setStatus(rset.getInt("STATUS"));
                info.setHostType(rset.getInt("HOSTTYPE"));
                info.setAddr(rset.getString("ADDR"));
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
