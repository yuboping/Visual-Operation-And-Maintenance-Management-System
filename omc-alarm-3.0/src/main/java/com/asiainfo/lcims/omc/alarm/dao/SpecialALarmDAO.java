package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdSpecialALarmInfo;
import com.asiainfo.lcims.util.IDGenerateUtil;
public class SpecialALarmDAO extends BaseDAO {
    private static final Logger logger = LoggerFactory.make();
    
//    public static List<MdSpecialALarmInfo> getAlarmOtherInfos() {
//        Connection conn = null;
//        Statement stat = null;
//        ResultSet rset = null;
//        List<MdSpecialALarmInfo> list = new ArrayList<MdSpecialALarmInfo>();
//        try {
//            conn = ConnPool.getConnection();
//            stat = conn.createStatement();
//            StringBuilder sql = new StringBuilder("SELECT RULE_ID,ALARM_ID,METRIC_ID,ALARM_SEQ,ATTR,ATTR1,ATTR2,ATTR3,ATTR4,ALARM_LEVEL,");
//            sql.append("ALARM_MSG,ALARM_NUM,ALARM_VAL,ALARM_FILENAME,FILE_PATH,MSG_DESC FROM MD_OTHER_ALARM_INFO ");
//            rset = stat.executeQuery(sql.toString());
//            while (rset.next()) {
//                MdSpecialALarmInfo info = new MdSpecialALarmInfo();
//                info.setRule_id(rset.getString("RULE_ID"));
//                info.setAlarm_id(rset.getString("ALARM_ID"));
//                info.setMetric_id(rset.getString("METRIC_ID"));
//                info.setAlarm_seq(rset.getInt("ALARM_SEQ"));
//                info.setAttr(rset.getString("ATTR"));
//                info.setAttr1(rset.getString("ATTR1"));
//                info.setAttr2(rset.getString("ATTR2"));
//                info.setAttr3(rset.getString("ATTR3"));
//                info.setAttr4(rset.getString("ATTR4"));
//                info.setAlarm_level(rset.getString("ALARM_LEVEL"));
//                info.setAlarm_msg(rset.getString("ALARM_MSG"));
//                info.setAlarm_num(rset.getInt("ALARM_NUM"));
//                info.setAlarm_val(rset.getString("ALARM_VAL"));
//                info.setAlarm_filename(rset.getString("ALARM_FILENAME"));
//                info.setFile_path(rset.getString("FILE_PATH"));
//                info.setMsg_desc(rset.getString("MSG_DESC"));
//                list.add(info);
//            }
//        } catch (SQLException e) {
//            logger.error("database getAlarmOtherInfos operate error. {}", e);
//        } finally {
//            ConnPool.close(rset, stat, conn);
//        }
//        return list;
//    }
//    
//    public static int insertAlarmOtherInfo(MdSpecialALarmInfo alarmInfo) {
//        int ret = 0;
//        Connection connection = ConnPool.getConnection();
//        PreparedStatement prst = null;
//        try {
//            String sql = "INSERT INTO MD_OTHER_ALARM_INFO (RULE_ID,ALARM_ID,METRIC_ID,"
//                    + "ALARM_SEQ,ATTR1,ATTR2,ATTR3,ATTR4,ALARM_LEVEL,"
//                    + "ALARM_MSG,ALARM_NUM,ALARM_VAL,ALARM_FILENAME,FILE_PATH,ALARM_TIME,"
//                    + "CLEAR_TIME,MSG_DESC,ATTR) "
//                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            prst = connection.prepareStatement(sql);
//            prst.setString(1, alarmInfo.getRule_id());
//            prst.setString(2, alarmInfo.getAlarm_id());
//            prst.setString(3, alarmInfo.getMetric_id());
//            prst.setInt(4, alarmInfo.getAlarm_seq_new());
//            prst.setString(5, alarmInfo.getAttr1());
//            prst.setString(6, alarmInfo.getAttr2());
//            prst.setString(7, alarmInfo.getAttr3());
//            prst.setString(8, alarmInfo.getAttr4());
//            prst.setString(9, alarmInfo.getAlarm_level());
//            prst.setString(10, alarmInfo.getAlarm_msg());
//            prst.setInt(11, alarmInfo.getAlarm_num());
//            prst.setString(12, alarmInfo.getAlarm_val());
//            prst.setString(13, alarmInfo.getAlarm_filename());
//            prst.setString(14, alarmInfo.getFile_path());
//            prst.setTimestamp(15, alarmInfo.getAlarm_time());
//            prst.setTimestamp(16, alarmInfo.getClear_time());
//            prst.setString(17, alarmInfo.getMsg_desc());
//            prst.setString(18, alarmInfo.getAttr());
//            ret = prst.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("insertAlarmOtherInfo:database operate error.", e);
//            ret = -1;
//        } finally {
//            close(prst, connection);
//        }
//        return ret;
//    }
//    
//    
//    public static int updateAlarmOtherInfo(MdSpecialALarmInfo alarmInfo) {
//        int ret = 0;
//        Connection connection = ConnPool.getConnection();
//        PreparedStatement prst = null;
//        try {
//            String sql = "UPDATE MD_OTHER_ALARM_INFO SET ALARM_SEQ=?,ALARM_LEVEL=?,ALARM_MSG=?"
//                    + ",ALARM_NUM=?,ALARM_VAL=?,ALARM_FILENAME=?,FILE_PATH=?,ALARM_TIME=?"
//                    + ",CLEAR_TIME=?,MSG_DESC=? WHERE ALARM_ID=? AND ALARM_SEQ=?";
//            prst = connection.prepareStatement(sql);
//            prst.setInt(1, alarmInfo.getAlarm_seq_new());
//            prst.setString(2, alarmInfo.getAlarm_level());
//            prst.setString(3, alarmInfo.getAlarm_msg());
//            prst.setInt(4, alarmInfo.getAlarm_num());
//            prst.setString(5, alarmInfo.getAlarm_val());
//            prst.setString(6, alarmInfo.getAlarm_filename());
//            prst.setString(7, alarmInfo.getFile_path());
//            prst.setTimestamp(8, alarmInfo.getAlarm_time());
//            prst.setTimestamp(9, alarmInfo.getClear_time());
//            prst.setString(10, alarmInfo.getMsg_desc());
//            prst.setString(11, alarmInfo.getAlarm_id());
//            prst.setInt(12, alarmInfo.getAlarm_seq());
//            ret = prst.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("updateAlarmOtherInfo:database operate error.", e);
//            ret = -1;
//        } finally {
//            close(prst, connection);
//        }
//        return ret;
//    }
//    
//    
//    public static int insertAlarmOtherInfoHis(MdSpecialALarmInfo alarmInfo) {
//        int ret = 0;
//        Connection connection = ConnPool.getConnection();
//        PreparedStatement prst = null;
//        try {
//            String sql = "INSERT INTO MD_OTHER_ALARM_INFO_HIS (HIS_ID,RULE_ID,ALARM_ID,METRIC_ID,"
//                    + "ALARM_SEQ,ATTR1,ATTR2,ATTR3,ATTR4,ALARM_LEVEL,"
//                    + "ALARM_MSG,ALARM_NUM,ALARM_VAL,ALARM_FILENAME,FILE_PATH,ALARM_TIME,"
//                    + "CLEAR_TIME,MSG_DESC,ATTR) "
//                    + "VALUES('"+IDGenerateUtil.getUuid()+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            prst = connection.prepareStatement(sql);
//            prst.setString(1, alarmInfo.getRule_id());
//            prst.setString(2, alarmInfo.getAlarm_id());
//            prst.setString(3, alarmInfo.getMetric_id());
//            prst.setInt(4, alarmInfo.getAlarm_seq_new());
//            prst.setString(5, alarmInfo.getAttr1());
//            prst.setString(6, alarmInfo.getAttr2());
//            prst.setString(7, alarmInfo.getAttr3());
//            prst.setString(8, alarmInfo.getAttr4());
//            prst.setString(9, alarmInfo.getAlarm_level());
//            prst.setString(10, alarmInfo.getAlarm_msg());
//            prst.setInt(11, alarmInfo.getAlarm_num());
//            prst.setString(12, alarmInfo.getAlarm_val());
//            prst.setString(13, alarmInfo.getAlarm_filename());
//            prst.setString(14, alarmInfo.getFile_path());
//            prst.setTimestamp(15, alarmInfo.getAlarm_time());
//            prst.setTimestamp(16, alarmInfo.getClear_time());
//            prst.setString(17, alarmInfo.getMsg_desc());
//            prst.setString(18, alarmInfo.getAttr());
//            ret = prst.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("insertAlarmOtherInfo:database operate error.", e);
//            ret = -1;
//        } finally {
//            close(prst, connection);
//        }
//        return ret;
//    }
    
}
