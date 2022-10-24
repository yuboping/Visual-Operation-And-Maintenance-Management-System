package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmHisInfo;
import com.asiainfo.lcims.util.DbSqlUtil;
import com.asiainfo.lcims.util.IDGenerateUtil;

public class HisAlarmDAO extends BaseDAO {
    private static final Logger logger = LoggerFactory.make();

    public static int insertHistoryAlarm(MdAlarmHisInfo alarmHisInfo, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String date = "NOW()";
            if (DbSqlUtil.isMysql()) {
                date = "NOW()";
            } else if (DbSqlUtil.isOracle()) {
                date = "SYSDATE";
            }
            String sql = "INSERT INTO MD_ALARM_HIS_INFO (ID,ALARM_ID,NAME,URL,"
                    + "DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,"
                    + "ALARM_RULE,MODES,ALARMMSG,DIMENSION1,DIMENSION2,"
                    + "DIMENSION3,CYCLE_TIME,CREATE_TIME,DIMENSION1_NAME,DIMENSION2_NAME,ALARM_TYPE,METRIC_ORIGINAL,METRIC_THRESHOLD,ALARMTEXT,REPORT_RULE) "
                    + "VALUES('" + IDGenerateUtil.getUuid() + "',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + date + ",?,?,?,?,?,?,?)";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmHisInfo.getAlarm_id());
            prst.setString(2, alarmHisInfo.getName());
            prst.setString(3, alarmHisInfo.getUrl());
            prst.setInt(4, alarmHisInfo.getDimension_type());
            prst.setString(5, alarmHisInfo.getChart_name());
            prst.setString(6, alarmHisInfo.getMetric_id());
            prst.setString(7, alarmHisInfo.getAttr());
            prst.setString(8, alarmHisInfo.getAlarm_level());
            prst.setString(9, alarmHisInfo.getAlarm_rule());
            prst.setString(10, alarmHisInfo.getModes());
            prst.setString(11, alarmHisInfo.getAlarmmsg());
            prst.setString(12, alarmHisInfo.getDimension1());
            prst.setString(13, alarmHisInfo.getDimension2());
            prst.setString(14, alarmHisInfo.getDimension3());
            prst.setTimestamp(15, time);
            prst.setString(16, alarmHisInfo.getDimension1_name());
            prst.setString(17, alarmHisInfo.getDimension2_name());
            prst.setString(18, alarmHisInfo.getAlarm_type());
            prst.setString(19, alarmHisInfo.getMetric_original());
            prst.setString(20, alarmHisInfo.getMetric_threshold());
            prst.setString(21, alarmHisInfo.getAlarmText());
            prst.setString(22, alarmHisInfo.getReport_rule());
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("insertHistoryAlarm:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int deleteHisAlarmInfo(String alamrId) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String date = "NOW()";
            if (DbSqlUtil.isMysql()) {
                date = "NOW()";
            } else if (DbSqlUtil.isOracle()) {
                date = "SYSDATE";
            }
            String sql = "DELETE FROM MD_ALARM_HIS_INFO WHERE ALARM_ID = ?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alamrId);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("deleteHisAlarmInfo :database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static String getAlarmHisLastId(String alarm_id) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        String hisId = null;
        try {
            String sql = "SELECT ID FROM MD_ALARM_HIS_INFO WHERE ALARM_ID = ? ORDER BY CYCLE_TIME DESC LIMIT 0,1";
            if (DbSqlUtil.isOracle()) {
                sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT ID FROM MD_ALARM_HIS_INFO WHERE ALARM_ID = ? ORDER BY CYCLE_TIME DESC ) A WHERE ROWNUM <= 1 ) WHERE RN >= 0";
            }
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarm_id);
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                hisId = resultSet.getString("ID");
            }
        } catch (SQLException e) {
            logger.error("deleteHisAlarmInfo :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return hisId;
    }

    public static int updateAlarmHisClearTime(String hisId, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "UPDATE MD_ALARM_HIS_INFO SET CLEAR_TIME = ? WHERE ID = ?";
            prst = connection.prepareStatement(sql);
            prst.setTimestamp(1, time);
            prst.setString(2, hisId);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("updateAlarmHisClearTime :database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

}
