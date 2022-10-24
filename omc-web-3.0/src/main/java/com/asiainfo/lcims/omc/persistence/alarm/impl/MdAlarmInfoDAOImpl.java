package com.asiainfo.lcims.omc.persistence.alarm.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.util.DbSqlUtil;

public class MdAlarmInfoDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MdAlarmInfoDAOImpl.class);

    private static final int ORACLE_IN_LIMIT = 1000;

    public String getAlarmHisInfoById(Map<String, Object> parameters) {
        String id = (String) parameters.get("id");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT M.ID, M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, ");
        strb.append("M.METRIC_THRESHOLD, M.ALARMTEXT, ");
        strb.append("M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, "
                + DbSqlUtil.getTimeSql("M.CONFIRM_TIME") + " AS CONFIRM_TIME, M.CONFIRM_NAME, "
                + DbSqlUtil.getTimeSql("M.CLEAR_TIME") + " AS CLEAR_TIME, M.CONFIRM_STATE, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, "
                        + DbSqlUtil.getTimeSql("M.CYCLE_TIME") + " AS CYCLE_TIME, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN "
                        + DbSqlUtil.getConcat("M.DIMENSION1_NAME", ":", "M.DIMENSION2_NAME")
                        + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append(DbSqlUtil.getTimeSql("M.CREATE_TIME")
                        + " AS CREATE_TIME FROM MD_ALARM_HIS_INFO M ")
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE 1=1 ");
        strb.append(" AND M.ID = '").append(id).append("'");
        String sql = strb.toString();
        LOG.debug("getAlarmHisInfoById sql = {}", sql);
        return sql;
    }

    public String getSqlStrByList(List sqhList, int splitNum, String columnName) {
        if (splitNum > 1000) {// 因为数据库的列表sql限制，不能超过1000.
            return null;
        }
        StringBuffer sql = new StringBuffer("");
        if (sqhList != null) {
            sql.append(" ( ").append(columnName).append(" IN ( ");
            for (int i = 0; i < sqhList.size(); i++) {
                sql.append("'").append(sqhList.get(i) + "',");
                if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" ) OR ").append(columnName).append(" IN (");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ) ");
        }
        return sql.toString();
    }

    public String getAlarmInfoById(Map<String, Object> parameters) {
        String alarmId = (String) parameters.get("alarmId");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, ");
        strb.append("M.METRIC_THRESHOLD, M.ALARMTEXT, ");
        strb.append("M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, M.ALARM_NUM, "
                        + DbSqlUtil.getTimeSql("M.FIRST_TIME") + " AS FIRST_TIME, ")
                .append(DbSqlUtil.getTimeSql("M.LAST_TIME") + " AS LAST_TIME , "
                        + DbSqlUtil.getTimeSql("M.CONFIRM_TIME") + " AS CONFIRM_TIME, ")
                .append("CASE M.CONFIRM_STATE WHEN 1 THEN '已确认' WHEN 0 THEN '未确认' END AS CONFIRM_STATE_NAME, M.CONFIRM_STATE, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN "
                        + DbSqlUtil.getConcat("M.DIMENSION1_NAME", ":", "M.DIMENSION2_NAME")
                        + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append("M.CONFIRM_NAME, " + DbSqlUtil.getTimeSql("M.CLEAR_TIME")
                        + " AS CLEAR_TIME FROM MD_ALARM_INFO M ")
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE M.ALARM_NUM > 0 AND M.DELETE_STATE = 0");
        strb.append(" AND M.ALARM_ID = '").append(alarmId).append("'");
        String sql = strb.toString();
        LOG.debug("getAlarmInfoById sql = {}", sql);
        return sql;
    }

}
