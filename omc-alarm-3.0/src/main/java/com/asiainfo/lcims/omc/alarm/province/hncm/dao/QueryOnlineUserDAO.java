package com.asiainfo.lcims.omc.alarm.province.hncm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.db.ConnPoolOutSystem;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.dao.BaseDAO;
import com.asiainfo.lcims.omc.alarm.province.hncm.model.MdOnlineUser;
/**
 * 河南移动
 * 查询在线用户数
 * @author zhul
 *
 */
public class QueryOnlineUserDAO extends BaseDAO{
    private static final Logger logger = LoggerFactory.make();
    
    public static List<MdOnlineUser> getOnlineUsers(String oltIp){
        List<MdOnlineUser> list = new ArrayList<MdOnlineUser>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        try {
            conn = ConnPoolOutSystem.getConnection();
            stat = conn.createStatement();
            StringBuilder sql = new StringBuilder("select User_list from olt_CustCare where Olt_ip = '"+oltIp+"' and Recover_Alarm_time is null");
            rset = stat.executeQuery(sql.toString());
            while (rset.next()) {
                MdOnlineUser info = new MdOnlineUser();
                info.setUserlist(rset.getString("User_list"));
                list.add(info);
            }
        } catch (SQLException e) {
            logger.error("database getAlarmOtherInfos operate error. {}", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }
}
