package com.asiainfo.lcims.omc.persistence;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class SqlAlarmProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAlarmProvider.class);

    private static final int ORACLE_IN_LIMIT = 1000;

    /**
     * 告警列表展示
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoList(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, ");
        if (StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_SHCM, ReadFile.PROVINCE)) {
            strb.append("M.METRIC_THRESHOLD,M.ALARMTEXT,");
        }
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
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE M.ALARM_NUM > 0 AND M.DELETE_STATE = 0 AND "
                        + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
        
        if (!StringUtils.isEmpty(alarmInfo.getMetric_id())) {
            strb.append(" AND M.METRIC_ID = '").append(alarmInfo.getMetric_id()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfo.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getName())) {
            strb.append(" AND M.NAME = ( SELECT NAME FROM MD_MENU_TREE WHERE ID = '").append(alarmInfo.getName()).append("' ) ");
        }
        if (!StringUtils.isEmpty(alarmInfo.getUrl())) {
            strb.append(" AND M.URL LIKE '%").append(alarmInfo.getUrl()).append("%'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getConfirm_state_str())) {
            strb.append(" AND M.CONFIRM_STATE = ").append(alarmInfo.getConfirm_state_str()).append("");
        }
        if (!StringUtils.isEmpty(alarmInfo.getStart_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + ">='" + alarmInfo.getStart_time() + "'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getEnd_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + "<='" + alarmInfo.getEnd_time() + "'");
        }
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getAlarmInfoList sql = {}", sql);
        return sql;
    }

    /**
     * 查询数据总数
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoCount(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(1) ")
                .append(" FROM MD_ALARM_INFO M ")
                .append(" WHERE M.ALARM_NUM > 0 AND M.DELETE_STATE = 0 AND "+getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
//                .append(" WHERE M.ALARM_NUM > 0 AND M.DELETE_STATE = 0 AND M.URL IN (" +urlList+ ") ");
        if (!StringUtils.isEmpty(alarmInfo.getMetric_id())) {
            strb.append(" AND M.METRIC_ID = '").append(alarmInfo.getMetric_id()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfo.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getConfirm_state_str())) {
            strb.append(" AND M.CONFIRM_STATE = ").append(alarmInfo.getConfirm_state_str()).append("");
        }
        if (!StringUtils.isEmpty(alarmInfo.getName())) {
            strb.append(" AND M.NAME = ( SELECT NAME FROM MD_MENU_TREE WHERE ID = '").append(alarmInfo.getName()).append("' ) ");
        }
        if (!StringUtils.isEmpty(alarmInfo.getUrl())) {
            strb.append(" AND M.URL LIKE '%").append(alarmInfo.getUrl()).append("%'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getStart_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + ">='" + alarmInfo.getStart_time() + "'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getEnd_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + "<='" + alarmInfo.getEnd_time() + "'");
        }
        LOG.debug("getAlarmInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 首页告警信息
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoListWithIndex(Map<String, Object> parameters) {
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID, M.NAME, M.URL, M.ALARM_LEVEL, M.LAST_TIME, M.ALARMMSG,M.confirm_state ")
                .append("FROM MD_ALARM_INFO M ")
                .append("WHERE M.ALARM_NUM > 0 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL") + " AND M.DELETE_STATE = 0 ");
        LOG.debug("getAlarmInfoList sql = {}", strb);
        return strb.toString();
    }

    /**
     * 告警历史列表展示
     *
     * @param parameters
     * @return
     */
    public String getAlarmHisInfoList(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT M.ID, M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, ");
        if (StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_SHCM, ReadFile.PROVINCE)) {
            strb.append("M.METRIC_THRESHOLD,M.ALARMTEXT,");
        }
        strb.append("M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, "
                + DbSqlUtil.getTimeSql("M.CONFIRM_TIME") + " AS CONFIRM_TIME, M.CONFIRM_NAME, "
                + DbSqlUtil.getTimeSql("M.CLEAR_TIME") + " AS CLEAR_TIME, M.CONFIRM_STATE, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, " + DbSqlUtil.getTimeSql("M.CYCLE_TIME") + " AS CYCLE_TIME, M.METRIC_ORIGINAL, M.METRIC_ID, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN " + DbSqlUtil.getConcat("M.DIMENSION1_NAME",":", "M.DIMENSION2_NAME") + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append(DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME FROM MD_ALARM_HIS_INFO M ")
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE 1=1 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
        if (!StringUtils.isEmpty(alarmInfo.getId())) {
            strb.append(" AND M.ID = '").append(alarmInfo.getId()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getMetric_id())) {
            strb.append(" AND M.METRIC_ID = '").append(alarmInfo.getMetric_id()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfo.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getName())) {
            strb.append(" AND M.NAME = ( SELECT NAME FROM MD_MENU_TREE WHERE ID = '").append(alarmInfo.getName()).append("' ) ");
        }
        if (!StringUtils.isEmpty(alarmInfo.getUrl())) {
            strb.append(" AND M.URL LIKE '%").append(alarmInfo.getUrl()).append("%'");
        }
        if(!StringUtils.isEmpty(alarmInfo.getQuery_type()) && "1".equals(alarmInfo.getQuery_type())) {
            if (!StringUtils.isEmpty(alarmInfo.getStart_time())) {
                strb.append(" AND " + DbSqlUtil.getTimeSqlWithMonth("M.CYCLE_TIME") + "='" + alarmInfo.getStart_time() + "'");
            }
        }else {
            if (!StringUtils.isEmpty(alarmInfo.getEnd_time())) {
                Map<String, String> map = DbSqlUtil.getWeekDays(alarmInfo.getEnd_time(), 0);
                strb.append(" AND " + DbSqlUtil.getDateSql("M.CYCLE_TIME") + ">='" + map.get("startdate") + "'");
                strb.append(" AND " + DbSqlUtil.getDateSql("M.CYCLE_TIME") + "<='" + map.get("enddate") + "'");
            }
        }
        strb.append(" ORDER BY M.CYCLE_TIME DESC ,M.ID" );
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        if(page != null){
            sql = PagingUtil.getPageSql(sql, page);
        }
        LOG.debug("getAlarmInfoList sql = {}", sql);
        return sql;
    }

    /**
     * 告警历史列表展示
     *
     * @param parameters
     * @return
     */
    public String getAlarmHisInfoWithLevelIndex(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ID, M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, ")
                .append("M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, " + DbSqlUtil.getTimeSql("M.CONFIRM_TIME")+ " AS CONFIRM_TIME, M.CONFIRM_NAME, " + DbSqlUtil.getTimeSql("M.CLEAR_TIME")+ " AS CLEAR_TIME, M.CONFIRM_STATE, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, " + DbSqlUtil.getTimeSql("M.CYCLE_TIME") + " AS CYCLE_TIME, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN " + DbSqlUtil.getConcat("M.DIMENSION1_NAME",":", "M.DIMENSION2_NAME") + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append(DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME FROM MD_ALARM_HIS_INFO M ")
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE 1=1 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = ( SELECT ALARMLEVEL FROM MD_ALARM_LEVEL WHERE ALARMNAME = '").append(alarmInfo.getAlarm_level()).append("')");
        }
        strb.append(" AND M.CYCLE_TIME >= (SELECT "+ DbSqlUtil.getNullFunction() +"(CLEAR_TIME,"+DbSqlUtil.getToDateFormat() +")")
            .append(" ORDER BY M.CYCLE_TIME DESC , M.ID " );
        LOG.debug("getAlarmInfoList getAlarmHisInfoWithLevelIndex = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询告警历史数据总数
     *
     * @param parameters
     * @return
     */
    public String getAlarmHisInfoCount(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(1) ")
                .append(" FROM MD_ALARM_HIS_INFO M ")
                .append(" WHERE 1=1 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
        if (!StringUtils.isEmpty(alarmInfo.getMetric_id())) {
            strb.append(" AND M.METRIC_ID = '").append(alarmInfo.getMetric_id()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfo.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfo.getName())) {
            strb.append(" AND M.NAME = ( SELECT NAME FROM MD_MENU_TREE WHERE ID = '").append(alarmInfo.getName()).append("' ) ");
        }
        if (!StringUtils.isEmpty(alarmInfo.getUrl())) {
            strb.append(" AND M.URL LIKE '%").append(alarmInfo.getUrl()).append("%'");
        }
        if(!StringUtils.isEmpty(alarmInfo.getQuery_type()) && "1".equals(alarmInfo.getQuery_type())) {
            if (!StringUtils.isEmpty(alarmInfo.getStart_time())) {
                strb.append(" AND " + DbSqlUtil.getTimeSqlWithMonth("M.CYCLE_TIME") + "='" + alarmInfo.getStart_time() + "'");
            }
        }else {
            if (!StringUtils.isEmpty(alarmInfo.getEnd_time())) {
                Map<String, String> map = DbSqlUtil.getWeekDays(alarmInfo.getEnd_time(), 0);
                strb.append(" AND " + DbSqlUtil.getDateSql("M.CYCLE_TIME") + ">='" + map.get("startdate") + "'");
                strb.append(" AND " + DbSqlUtil.getDateSql("M.CYCLE_TIME") + "<='" + map.get("enddate") + "'");
            }
        }
        LOG.debug("getAlarmInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    public String getAlarmHisList(Map<String, Object> parameters) {
        String alarm_id =(String) parameters.get("alarm_id");
        StringBuffer strb = new StringBuffer();
        strb.append(" SELECT M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.DIMENSION_TYPE, M.CHART_NAME, ")
                .append("F.METRIC_NAME, M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, " + DbSqlUtil.getTimeSql("M.CONFIRM_TIME")+ " AS CONFIRM_TIME, M.CONFIRM_NAME, " + DbSqlUtil.getTimeSql("M.CLEAR_TIME")+ " AS CLEAR_TIME, M.CONFIRM_STATE, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN " + DbSqlUtil.getConcat("M.DIMENSION1_NAME",":", "M.DIMENSION2_NAME") + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, " + DbSqlUtil.getTimeSql("M.CYCLE_TIME") + " AS CYCLE_TIME, " )
                .append(DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME FROM MD_ALARM_HIS_INFO M " )
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID ")
                .append("WHERE M.ALARM_ID = ")
                .append("'" + alarm_id + "' AND ")
                .append("M.CYCLE_TIME >= (SELECT "+ DbSqlUtil.getNullFunction() +"(CLEAR_TIME,"+DbSqlUtil.getToDateFormat() +") FROM MD_ALARM_INFO WHERE ALARM_ID = ")
                .append("'" + alarm_id + "')")
                .append(" ORDER BY M.CYCLE_TIME DESC" );
        LOG.debug("getAlarmInfoList sql = {}", strb);
        return strb.toString();
    }

    public String confirmAlarmInfoById(Map<String, Object> parameters) {
        String alarmId =(String) parameters.get("alarmId");
        String username =(String) parameters.get("username");
        String clearflag = (String) parameters.get("clearflag");
        StringBuffer strb = new StringBuffer();
        strb.append(" UPDATE MD_ALARM_INFO SET CONFIRM_NAME = ")
                .append("'"+ username + "',CONFIRM_TIME = "+ DbSqlUtil.getDateMethod() +", CONFIRM_STATE = 1 ");
        //告警数量清零
        if(clearflag.equals("true")){
            strb.append(",ALARM_NUM = 0 ");
        }
        strb.append(" WHERE ALARM_ID = '"+ alarmId +"'");
        LOG.debug("confirmAlarmInfoById sql = {}", strb);
        return strb.toString();
    }

    public String confirmAlarmHisInfoById(Map<String, Object> parameters) {
        String username =(String) parameters.get("username");
        String alarmId =(String) parameters.get("alarmId");
        String alarmFirstTime =(String) parameters.get("alarmFirstTime");
        alarmFirstTime = "'" + alarmFirstTime + "'";
        StringBuffer strb = new StringBuffer();
        strb.append(" UPDATE MD_ALARM_HIS_INFO SET CONFIRM_NAME = ")
                .append("'"+ username + "',CONFIRM_TIME = "+ DbSqlUtil.getDateMethod() +", CONFIRM_STATE = 1 ");
        strb.append(" WHERE ALARM_ID = '"+ alarmId +"'");
        strb.append(" AND " + DbSqlUtil.getTimeSql("CYCLE_TIME") + " = "+ alarmFirstTime);
        LOG.debug("confirmAlarmInfoById sql = {}", strb);
        return strb.toString();
    }


    public String getAlarmStatisInfoListWithDay(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT SUBSTR(M.CYCLE_TIME,11,3) || ':00 ~ ' || SUBSTR(M.CYCLE_TIME,11,3) || ':59' AS ALARM_TIME, COUNT(CASE WHEN M.ALARM_LEVEL = '1' THEN 1 ELSE NULL END) AS ALARM_LEVEL_NORMAL , ")
                .append(" COUNT(CASE WHEN M.ALARM_LEVEL = '2' THEN 2 ELSE NULL END) AS ALARM_LEVEL_WARN , COUNT(CASE WHEN M.ALARM_LEVEL = '3' THEN 3 ELSE NULL END) AS ALARM_LEVEL_SERIOUS ")
                .append(" FROM MD_ALARM_HIS_INFO M WHERE SUBSTR(M.CYCLE_TIME,1,10) = '"+ alarmInfo.getEnd_time() +"' GROUP BY SUBSTR(M.CYCLE_TIME,11,3) || ':00 ~ ' || SUBSTR(M.CYCLE_TIME,11,3) || ':59' ");
        LOG.debug("getAlarmStatisInfoList sql = {}", strb);
        return strb.toString();
    }

    public String getAlarmStatisInfoListWithWeek(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
        Map<String, String> map = DbSqlUtil.getWeekDays(alarmInfo.getEnd_time(), 0);
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT SUBSTR(M.CYCLE_TIME,1,10) AS ALARM_TIME, COUNT(CASE WHEN M.ALARM_LEVEL = '1' THEN 1 ELSE NULL END) AS ALARM_LEVEL_NORMAL , ")
                .append(" COUNT(CASE WHEN M.ALARM_LEVEL = '2' THEN 2 ELSE NULL END) AS ALARM_LEVEL_WARN , COUNT(CASE WHEN M.ALARM_LEVEL = '3' THEN 3 ELSE NULL END) AS ALARM_LEVEL_SERIOUS ")
                .append(" FROM MD_ALARM_HIS_INFO M WHERE SUBSTR(M.CYCLE_TIME,1,10) >= '"+ map.get("startdate") +"' ")
                .append(" AND  SUBSTR(M.CYCLE_TIME,1,10) <= '" + map.get("enddate") +"' GROUP BY SUBSTR(M.CYCLE_TIME,1,10) ");
        LOG.debug("getAlarmStatisInfoList sql = {}", strb);
        return strb.toString();
    }

    public String getAlarmStatisInfoListWithMonth(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT SUBSTR(M.CYCLE_TIME,1,10) AS ALARM_TIME, COUNT(CASE WHEN M.ALARM_LEVEL = '1' THEN 1 ELSE NULL END) AS ALARM_LEVEL_NORMAL , ")
                .append(" COUNT(CASE WHEN M.ALARM_LEVEL = '2' THEN 2 ELSE NULL END) AS ALARM_LEVEL_WARN , COUNT(CASE WHEN M.ALARM_LEVEL = '3' THEN 3 ELSE NULL END) AS ALARM_LEVEL_SERIOUS ")
                .append(" FROM MD_ALARM_HIS_INFO M WHERE SUBSTR(M.CYCLE_TIME,1,7) = '"+ alarmInfo.getStart_time() +"' GROUP BY SUBSTR(M.CYCLE_TIME,1,10) ");
        LOG.debug("getAlarmStatisInfoList sql = {}", strb);
        return strb.toString();
    }

    public String getAlarmInfoWithIndexGraph(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT Q.ALARMNAME AS MARK, "+ DbSqlUtil.getNullFunction() +"(SUM(M.ALARM_NUM),0) AS VALUE FROM MD_ALARM_LEVEL Q " +
                "LEFT JOIN MD_ALARM_INFO M ON Q.ALARMLEVEL = M.ALARM_LEVEL AND M.DELETE_STATE = 0 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL") +" GROUP BY Q.ALARMNAME , M.ALARM_LEVEL ORDER BY M.ALARM_LEVEL");
        LOG.debug("getAlarmInfoWithIndexGraph sql = {}", strb);
        return strb.toString();
    }

    /**
     * 首页告警列表展示
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoListWithIndexConfirm(Map<String, Object> parameters) {
        MdAlarmInfo alarmInfo = (MdAlarmInfo) parameters.get("alarmInfo");
//        String urlList =(String) parameters.get("urlList");
        List urlList =(List) parameters.get("urlList");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID, Q.SHOW_NAME AS NAME, M.URL, M.CHART_NAME, F.METRIC_NAME, M.ALARM_LEVEL,")
                .append("M.ATTR, N.ALARMNAME AS ALARM_LEVEL_NAME, M.ALARM_RULE, M.MODES, M.ALARMMSG, ")
                .append("M.DIMENSION1, M.DIMENSION2, M.DIMENSION3, M.ALARM_NUM, " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + " AS FIRST_TIME, ")
                .append(DbSqlUtil.getTimeSql("M.LAST_TIME") + " AS LAST_TIME , " + DbSqlUtil.getTimeSql("M.CONFIRM_TIME") + " AS CONFIRM_TIME, ")
                .append("CASE M.CONFIRM_STATE WHEN 1 THEN '已确认' WHEN 0 THEN '未确认' END AS CONFIRM_STATE_NAME, M.CONFIRM_STATE, ")
                .append("CASE WHEN M.DIMENSION2_NAME IS NOT NULL THEN " + DbSqlUtil.getConcat("M.DIMENSION1_NAME",":", "M.DIMENSION2_NAME") + " ELSE M.DIMENSION1_NAME END AS DIMENSION_NAME, ")
                .append("M.CONFIRM_NAME, " + DbSqlUtil.getTimeSql("M.CLEAR_TIME") + " AS CLEAR_TIME FROM MD_ALARM_INFO M ")
                .append("LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL ")
                .append("LEFT JOIN MD_MENU_TREE Q ON Q.NAME = M.NAME ")
                .append("LEFT JOIN MD_METRIC F ON M.METRIC_ID = F.ID WHERE M.ALARM_NUM > 0 AND " + getSqlStrByList(urlList, ORACLE_IN_LIMIT, "M.URL"));
//        if (!StringUtils.isEmpty(alarmInfo.getConfirm_name())) {
//            strb.append(" AND M.CONFIRM_NAME = '").append(alarmInfo.getConfirm_name()).append("'");
//        }
        if (!StringUtils.isEmpty(alarmInfo.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = ( SELECT ALARMLEVEL FROM MD_ALARM_LEVEL WHERE ALARMNAME = '").append(alarmInfo.getAlarm_level()).append("')");
        }
        strb.append(" AND M.DELETE_STATE = 0 ");
        return strb.toString();
    }
    
    
    
    public String deleteAlarmInfoById(Map<String, Object> parameters) {
        String alarmId =(String) parameters.get("alarmId");
        StringBuffer strb = new StringBuffer();
        strb.append(" UPDATE MD_ALARM_INFO SET DELETE_STATE = 1 ");
        strb.append(" WHERE ALARM_ID = '"+ alarmId +"'");
        LOG.debug("deleteAlarmInfoById sql = {}", strb);
        return strb.toString();
    }

    /**
     * 首页告警信息
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoWithId(Map<String, Object> parameters) {
        String alarmId =(String) parameters.get("alarmId");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID, M.NAME, M.URL, M.DIMENSION_TYPE, " + DbSqlUtil.getTimeSql("M.FIRST_TIME") + " AS FIRST_TIME," +
                " M.CHART_NAME, M.METRIC_ID, M.ATTR, M.ALARM_LEVEL, M.ALARM_RULE, M.MODES, M.ALARMMSG, M.DIMENSION1, M.DIMENSION1_NAME, M.DIMENSION2, " +
                " M.DIMENSION2_NAME, M.DIMENSION3, M.DIMENSION3_NAME, M.ALARM_NUM, M.LAST_TIME, M.CONFIRM_STATE, M.CONFIRM_TIME, M.CONFIRM_NAME, M.CLEAR_TIME ")
                .append("FROM MD_ALARM_INFO M ")
                .append("WHERE M.ALARM_ID = '"+ alarmId +"'");
        LOG.debug("getAlarmInfoList sql = {}", strb);
        return strb.toString();
    }


    public String getSqlStrByList(List sqhList, int splitNum, String columnName) {
        if(splitNum>1000) //因为数据库的列表sql限制，不能超过1000.
            return null;
        StringBuffer sql = new StringBuffer("");
        if (sqhList != null) {
            sql.append(" ( ").append(columnName).append (" IN ( ");
            for (int i = 0; i < sqhList.size(); i++) {
                sql.append("'").append(sqhList.get(i) + "',");
                if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" ) OR ").append(columnName).append (" IN (");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ) ");
        }
        return sql.toString();
    }

}
