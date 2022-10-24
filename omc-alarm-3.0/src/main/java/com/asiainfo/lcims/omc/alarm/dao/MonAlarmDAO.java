package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;

public class MonAlarmDAO extends BaseDAO {

    private static final Logger logger = LoggerFactory.make();

    public static int insertAlarm(MdAlarmInfo alarmInfo, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "INSERT INTO MD_ALARM_INFO (ALARM_ID,NAME,URL,"
                    + "DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,ALARM_RULE,"
                    + "MODES,ALARMMSG,DIMENSION1,DIMENSION2,DIMENSION3,ALARM_NUM,"
                    + "FIRST_TIME,LAST_TIME,CONFIRM_STATE,DIMENSION1_NAME,DIMENSION2_NAME,DELETE_STATE,ALARM_TYPE,METRIC_ORIGINAL,METRIC_THRESHOLD,ALARMTEXT,REPORT_RULE) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmInfo.getAlarm_id());
            prst.setString(2, alarmInfo.getName());
            prst.setString(3, alarmInfo.getUrl());
            prst.setInt(4, alarmInfo.getDimension_type());
            prst.setString(5, alarmInfo.getChart_name());
            prst.setString(6, alarmInfo.getMetric_id());
            prst.setString(7, alarmInfo.getAttr());
            prst.setString(8, alarmInfo.getAlarm_level());
            prst.setString(9, alarmInfo.getAlarm_rule());
            prst.setString(10, alarmInfo.getModes());
            prst.setString(11, alarmInfo.getAlarmmsg());
            prst.setString(12, alarmInfo.getDimension1());
            prst.setString(13, alarmInfo.getDimension2());
            prst.setString(14, alarmInfo.getDimension3());
            prst.setInt(15, alarmInfo.getAlarm_num());
            prst.setTimestamp(16, time);
            prst.setTimestamp(17, time);
            prst.setInt(18, alarmInfo.getConfirm_state());
            prst.setString(19, alarmInfo.getDimension1_name());
            prst.setString(20, alarmInfo.getDimension2_name());
            prst.setInt(21, alarmInfo.getDelete_state());
            prst.setString(22, alarmInfo.getAlarm_type());
            prst.setString(23, alarmInfo.getMetric_original());
            prst.setString(24, alarmInfo.getMetric_threshold());
            prst.setString(25, alarmInfo.getAlarmText());
            prst.setString(26, alarmInfo.getReport_rule());
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("insertAlarm:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int updateAlarm(MdAlarmInfo alarmInfo, Timestamp time) {
        int ret = 0;
        int num = alarmInfo.getAlarm_num();
        if (num == 0) {
            // 告警数量为0
            ret = updateAlarmAlarmNumIsZero(alarmInfo, time);
        } else {
            ret = updateAlarmAlarmNumIsNotZero(alarmInfo, time);
        }
        return ret;
    }

    public static int updateAlarmAlarmNumIsZero(MdAlarmInfo alarmInfo, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "UPDATE MD_ALARM_INFO SET ATTR=?,ALARM_LEVEL=?,ALARM_RULE=?,"
                    + " MODES=?,ALARMMSG=?,ALARM_NUM=?,LAST_TIME=?,DIMENSION1_NAME=?,DIMENSION2_NAME=?,FIRST_TIME=?,ALARM_TYPE=?,METRIC_ORIGINAL=?,METRIC_THRESHOLD=?,ALARMTEXT=?,CLEAR_TIME=?"
                    + " WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmInfo.getAttr());
            prst.setString(2, alarmInfo.getAlarm_level());
            prst.setString(3, alarmInfo.getAlarm_rule());
            prst.setString(4, alarmInfo.getModes());
            prst.setString(5, alarmInfo.getAlarmmsg());
            prst.setInt(6, alarmInfo.getAlarm_num() + 1);
            prst.setTimestamp(7, time);
            prst.setString(8, alarmInfo.getDimension1_name());
            prst.setString(9, alarmInfo.getDimension2_name());
            prst.setTimestamp(10, time);
            prst.setString(11, alarmInfo.getAlarm_type());
            prst.setString(12, alarmInfo.getMetric_original());
            prst.setString(13, alarmInfo.getMetric_threshold());
            prst.setString(14, alarmInfo.getAlarmText());
            prst.setTimestamp(15, null);
            prst.setString(16, alarmInfo.getAlarm_id());
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("updateAlarmAlarmNumIsZero:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int updateAlarmAlarmNumIsNotZero(MdAlarmInfo alarmInfo, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "UPDATE MD_ALARM_INFO SET ATTR=?,ALARM_LEVEL=?,ALARM_RULE=?,"
                    + " MODES=?,ALARMMSG=?,ALARM_NUM=?,LAST_TIME=?,DIMENSION1_NAME=?,DIMENSION2_NAME=?,ALARM_TYPE=?,METRIC_ORIGINAL=?,METRIC_THRESHOLD=?,ALARMTEXT=?,REPORT_RULE=?,CLEAR_TIME=?"
                    + " WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmInfo.getAttr());
            prst.setString(2, alarmInfo.getAlarm_level());
            prst.setString(3, alarmInfo.getAlarm_rule());
            prst.setString(4, alarmInfo.getModes());
            prst.setString(5, alarmInfo.getAlarmmsg());
            prst.setInt(6, alarmInfo.getAlarm_num() + 1);
            prst.setTimestamp(7, time);
            prst.setString(8, alarmInfo.getDimension1_name());
            prst.setString(9, alarmInfo.getDimension2_name());
            prst.setString(10, alarmInfo.getAlarm_type());
            prst.setString(11, alarmInfo.getMetric_original());
            prst.setString(12, alarmInfo.getMetric_threshold());
            prst.setString(13, alarmInfo.getAlarmText());
            prst.setString(14, alarmInfo.getReport_rule());
            prst.setTimestamp(15, null);
            prst.setString(16, alarmInfo.getAlarm_id());
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("updateAlarmAlarmNumIsNotZero:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int modifyAlarm(MdAlarmRuleDetail rule, Timestamp time) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "UPDATE MD_ALARM_INFO SET ALARM_NUM=0,CONFIRM_STATE=0,CONFIRM_TIME=NULL,"
                    + "CONFIRM_NAME=NULL,CLEAR_TIME=?,DELETE_STATE=? WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setTimestamp(1, time);
            prst.setInt(2, 0);
            prst.setString(3, rule.getAlarm_id());
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("modifyAlarm:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int deleteAlarmInfo(String alarmId) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "DELETE FROM MD_ALARM_INFO WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmId);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("deleteAlarmInfo:database operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static int modifyReportFlag(String alarmId, String reportFlag) {
        int ret = 0;
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        try {
            String sql = "UPDATE MD_ALARM_INFO SET REPORT_FLAG=? WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, reportFlag);
            prst.setString(2, alarmId);
            ret = prst.executeUpdate();
        } catch (SQLException e) {
            logger.error("modify report flag operate error.", e);
            ret = -1;
        } finally {
            close(prst, connection);
        }
        return ret;
    }

    public static String getReportFlag(String alarmId) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        String reportFlag = null;
        try {
            String sql = "SELECT REPORT_FLAG FROM MD_ALARM_INFO WHERE ALARM_ID=?";
            prst = connection.prepareStatement(sql);
            prst.setString(1, alarmId);
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                reportFlag = resultSet.getString("REPORT_FLAG");
            }
        } catch (SQLException e) {
            logger.error("query report flag operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return reportFlag;
    }

}
