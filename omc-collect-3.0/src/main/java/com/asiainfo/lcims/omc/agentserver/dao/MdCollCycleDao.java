package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.MdCollCycle;

public class MdCollCycleDao {
    private static final Logger log = LoggerFactory.make();

    public List<MdCollCycle> getAllMdCollCycle() {
        DruidPooledConnection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdCollCycle> list = new ArrayList<MdCollCycle>();
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT CYCLEID,CYCLENAME, CYCLE,CRON,RUNDAY,DESCRIPTION FROM MD_COLL_CYCLE";

            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdCollCycle info = new MdCollCycle();
                info.setCycleId(rset.getInt("CYCLEID"));
                info.setCycleName(rset.getString("CYCLENAME"));
                info.setCycle(rset.getInt("CYCLE"));
                info.setCron(rset.getString("CRON"));
                info.setRunday(rset.getInt("RUNDAY"));
                info.setDescription(rset.getString("DESCRIPTION"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("select error:", e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }
}
