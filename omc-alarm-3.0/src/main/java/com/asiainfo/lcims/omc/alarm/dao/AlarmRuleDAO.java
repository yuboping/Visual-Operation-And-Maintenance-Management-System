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
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.DbSqlUtil;
import com.asiainfo.lcims.util.ToolsUtils;

public class AlarmRuleDAO extends BaseDAO {
    private static final Logger logger = LoggerFactory.make();

    /**
     * 查询告警规则细表
     * 
     * @param tbSuffixes
     * @param lasttime
     * @return
     */
    public static List<MdAlarmRuleDetail> getAlarmRuleDetail(String cycleid) {
        logger.info("cycle is {}", cycleid);
        Connection connection = ConnPool.getConnection();
        ResultSet resultSet = null;
        PreparedStatement prest = null;
        StringBuilder sql = new StringBuilder("SELECT r.ALARM_ID,r.RULE_ID,r.NAME,")
                .append("r.URL,r.DIMENSION_TYPE,r.CHART_NAME,r.METRIC_ID,")
                .append("r.ATTR,r.ALARM_LEVEL,r.ALARM_RULE,r.MODES,r.ALARMMSG,")
                .append("r.REPORT_RULE,")
                .append("r.DIMENSION1,r.DIMENSION2,r.DIMENSION3,m.CYCLE_ID,r.ALARM_TYPE FROM MD_ALARM_RULE_DETAIL r ");
        sql.append(" LEFT JOIN MD_METRIC m ON r.METRIC_ID = m.ID ");
        if (!ToolsUtils.StringIsNull(cycleid)) {
            sql.append(" WHERE m.CYCLE_ID = ").append(cycleid);
        }

        List<MdAlarmRuleDetail> ret = new ArrayList<MdAlarmRuleDetail>();
        try {
            prest = connection.prepareStatement(sql.toString());
            resultSet = prest.executeQuery();
            while (resultSet.next()) {
                try {
                    MdAlarmRuleDetail alarm = new MdAlarmRuleDetail();
                    alarm.setAlarm_id(resultSet.getString("ALARM_ID"));
                    alarm.setRule_id(resultSet.getString("RULE_ID"));
                    alarm.setAlarm_level(resultSet.getString("ALARM_LEVEL"));
                    alarm.setAlarm_rule(resultSet.getString("ALARM_RULE"));
                    alarm.setAlarmmsg(resultSet.getString("ALARMMSG"));
                    alarm.setAttr(resultSet.getString("ATTR"));
                    alarm.setChart_name(resultSet.getString("CHART_NAME"));
                    alarm.setDimension1(resultSet.getString("DIMENSION1"));
                    alarm.setDimension2(resultSet.getString("DIMENSION2"));
                    alarm.setDimension3(resultSet.getString("DIMENSION3"));
                    alarm.setDimension_type(resultSet.getInt("DIMENSION_TYPE"));
                    alarm.setMetric_id(resultSet.getString("METRIC_ID"));
                    alarm.setModes(resultSet.getString("MODES"));
                    alarm.setName(resultSet.getString("NAME"));
                    alarm.setUrl(resultSet.getString("URL"));
                    alarm.setCycleid(resultSet.getInt("CYCLE_ID"));
                    alarm.setReport_rule(resultSet.getString("REPORT_RULE"));
                    alarm.setAlarm_type(resultSet.getString("ALARM_TYPE"));
                    ret.add(alarm);
                } catch (Exception e) {
                    logger.error("alarm table mon_threshold has error data", e);
                }
            }
        } catch (SQLException e) {
            logger.error("database operate error.", e);
        } finally {
            close(resultSet, prest, connection);
        }
        return ret;
    }

    public static List<MdAlarmInfo> getAlarmInfos(String cycleid) {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdAlarmInfo> list = new ArrayList<MdAlarmInfo>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            StringBuilder sql = new StringBuilder(
                    "SELECT alarm.ALARM_ID,alarm.URL,alarm.ALARM_NUM,alarm.ALARM_LEVEL,alarm.MODES,alarm.ALARMMSG,alarm.ALARM_RULE,alarm.DIMENSION1,alarm.DIMENSION1_NAME,alarm.DIMENSION2,");
            sql.append(
                    "alarm.DIMENSION2_NAME,alarm.DIMENSION3,alarm.DIMENSION3_NAME,alarm.CHART_NAME,alarm.ALARM_TYPE,alarm.METRIC_ORIGINAL,alarm.METRIC_THRESHOLD,alarm.ALARMTEXT,alarm.REPORT_RULE,alarm.REPORT_FLAG,")
                    .append(DbSqlUtil.getTimeSql("alarm.FIRST_TIME")
                            + " AS FIRST_TIME,alarm.ALARM_RULE ")
                    .append("FROM MD_ALARM_INFO alarm ");
            if (!ToolsUtils.StringIsNull(cycleid)) {
                sql.append("LEFT JOIN MD_METRIC m ON alarm.METRIC_ID=m.ID")
                        .append("LEFT JOIN MD_COLL_CYCLE c ON m.CYCLE_ID=c.CYCLEID")
                        .append("WHERE c.CYCLEID = ").append(cycleid);
            }
            rset = stat.executeQuery(sql.toString());
            while (rset.next()) {
                MdAlarmInfo info = new MdAlarmInfo();
                info.setAlarm_id(rset.getString("ALARM_ID"));
                info.setUrl(rset.getString("URL"));
                info.setAlarm_num(rset.getInt("ALARM_NUM"));
                info.setAlarm_level(rset.getString("ALARM_LEVEL"));
                info.setModes(rset.getString("MODES"));
                info.setAlarm_rule(rset.getString("ALARM_RULE"));
                info.setAlarmmsg(rset.getString("ALARMMSG"));
                info.setDimension1(rset.getString("DIMENSION1"));
                info.setDimension1_name(rset.getString("DIMENSION1_NAME"));
                info.setDimension2(rset.getString("DIMENSION2"));
                info.setDimension2_name(rset.getString("DIMENSION2_NAME"));
                info.setDimension3(rset.getString("DIMENSION3"));
                info.setDimension3_name(rset.getString("DIMENSION3_NAME"));
                info.setChart_name(rset.getString("CHART_NAME"));
                info.setFirst_time(rset.getString("FIRST_TIME"));
                info.setAlarm_rule(rset.getString("ALARM_RULE"));
                info.setAlarm_type(rset.getString("ALARM_TYPE"));
                info.setMetric_original(rset.getString("METRIC_ORIGINAL"));
                info.setMetric_threshold(rset.getString("METRIC_THRESHOLD"));
                info.setAlarmText(rset.getString("ALARMTEXT"));
                info.setReport_rule(rset.getString("REPORT_RULE"));
                info.setReport_flag(rset.getString("REPORT_FLAG"));
                list.add(info);
            }
        } catch (SQLException e) {
            logger.error("database alarmInfo operate error. {}", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

}
