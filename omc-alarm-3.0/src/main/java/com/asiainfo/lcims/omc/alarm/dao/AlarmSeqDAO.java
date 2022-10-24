package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.db.ConnPool;

//用户操作
public class AlarmSeqDAO extends BaseDAO{

    private static final Logger LOG = LoggerFactory.getLogger(AlarmSeqDAO.class);
    
    /**
     * 获取序列号
     * @return
     */
    public static Integer getAlarmSeq(){
        Integer alarmSeq = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT DESCRIPTION FROM MD_PARAM WHERE TYPE=16 AND CODE ='alarm_seq'";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                alarmSeq = Integer.parseInt(rset.getString("DESCRIPTION"));
            }
        } catch (SQLException e) {
            LOG.error("database getAlarmSeq operate error. {}", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return alarmSeq;
    }
    
    /**
     * 更新序列号
     * @param alarmSql
     * @return
     */
    public static int updateAlarmSeq(String alarmSql){
        int ret = 0;
        Connection conn = null;
        PreparedStatement prst = null;
        try {
            conn = ConnPool.getConnection();
            String sql = "UPDATE MD_PARAM SET DESCRIPTION=? WHERE TYPE=16 AND CODE ='alarm_seq'";
            prst = conn.prepareStatement(sql);
            prst.setString(1, alarmSql);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("updateAlarmSeq operate error.", e);
            ret = -1;
        } finally {
            close(prst, conn);
        }
        return ret;
    }
    
    /**
     * 获取递增编码
     * 
     * @return
     */
    public static String getWidSeq() {
        String alarmSeq = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT DESCRIPTION FROM MD_PARAM WHERE TYPE='31' AND CODE ='daily_limit'";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                alarmSeq = rset.getString("DESCRIPTION");
            }
        } catch (SQLException e) {
            LOG.error("database getWidSeq operate error. {}", e);
            alarmSeq = "1";
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return alarmSeq;
    }

    /**
     * 更新递增编码
     * 
     * @param alarmSql
     * @return
     */
    public static int updateWidSeq(String widSeqIncr) {
        int ret = 0;
        Connection conn = null;
        PreparedStatement prst = null;
        try {
            conn = ConnPool.getConnection();
            String sql = "UPDATE MD_PARAM SET DESCRIPTION=? WHERE TYPE='31' AND CODE ='daily_limit'";
            prst = conn.prepareStatement(sql);
            prst.setString(1, widSeqIncr);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("updateWidSeq operate error.", e);
            ret = -1;
        } finally {
            close(prst, conn);
        }
        return ret;
    }

}
