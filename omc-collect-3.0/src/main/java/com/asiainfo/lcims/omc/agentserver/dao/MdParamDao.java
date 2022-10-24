package com.asiainfo.lcims.omc.agentserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.enity.MdParam;

public class MdParamDao {
    private static final Logger log = LoggerFactory.make();

    public List<MdParam> getMdParamByType(int type) {
        DruidPooledConnection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdParam> list = new ArrayList<MdParam>();
        String sql = null;
        try {
            DruidConnPool dbp = DruidConnPool.getInstance();
            conn = dbp.getConnection();
            stat = conn.createStatement();
            sql = "SELECT TYPE,CODE,DESCRIPTION FROM MD_PARAM";

            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdParam info = new MdParam();
                info.setType(rset.getInt("TYPE"));
                info.setCode(rset.getString("CODE"));
                info.setDescription(rset.getString("DESCRIPTION"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("select error:" + e);
        } finally {
            DruidConnPool.close(rset, stat, conn);
        }
        return list;
    }
}
