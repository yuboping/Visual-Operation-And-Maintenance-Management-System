package com.asiainfo.lcims.omc.persistence.hncu.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfoHn;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmInfo;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class RealTimeAlarmDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(RealTimeAlarmDAOImpl.class);

    public String insertRealTimeAlarm(Map<String, Object> parameters) {
        RealTimeAlarmInfo info = (RealTimeAlarmInfo) parameters.get("realTimeAlarmInfo");
        StringBuffer buffer = new StringBuffer(
                "INSERT INTO MD_REAL_ALARM_INFO(UUID,ALARM_MSG,ALARM_TYPE,CREATE_TIME,HOST_NAME,ALARM_ID,ALARM_LEVEL,CLEAR_STATUS,CLEAR_TIME,UP_DATE,UPDATE_TIME) VALUES('");
        buffer.append(info.getUuid()).append("','");
        buffer.append(info.getAlarmmsg()).append("','");
        buffer.append(info.getAlarmtype()).append("',");
        String createTime = getFormatDate(info.getCreatetime());
        if (StringUtils.isBlank(createTime)) {
            buffer.append("'','");
        } else {
            buffer.append(createTime).append(",'");
        }
        buffer.append(info.getHostname()).append("','");
        buffer.append(info.getAlarmid()).append("','");
        buffer.append(info.getAlarmlevel()).append("','");
        buffer.append(info.getClearstatus()).append("',");
        String clearTime = getFormatDate(info.getCleartime());
        if (StringUtils.isBlank(clearTime)) {
            buffer.append("'','");
        } else {
            buffer.append(clearTime).append(",'");
        }
        buffer.append(info.getUpdate()).append("',");
        buffer.append(getCurrentDate()).append(")");
        String sql = String.valueOf(buffer);
        LOG.info("insert real time alarm sql : {}", sql);
        return sql;
    }

    public String getFormatDate(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return "";
        }
        String dateFormat = DbSqlUtil.formatDateStrSql(dateTime);
        return dateFormat;
    }

    public static String getCurrentDate() {
        DateTools dateTools = new DateTools("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateTools.getCurrentDate();
        String dateFormat = DbSqlUtil.formatDateStrSql(currentDate);
        return dateFormat;
    }

    /**
     * 查询数据总数
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoHnCount(Map<String, Object> parameters) {
        MdAlarmInfoHn alarmInfoHn = (MdAlarmInfoHn) parameters.get("alarmInfoHn");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(1) ")
                .append(" FROM MD_REAL_ALARM_INFO M WHERE 1=1 ");
        if (!StringUtils.isEmpty(alarmInfoHn.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfoHn.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getAlarm_type())) {
            strb.append(" AND M.ALARM_TYPE = '").append(alarmInfoHn.getAlarm_type()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getStart_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + ">='" + alarmInfoHn.getStart_time() + "'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getEnd_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + "<='" + alarmInfoHn.getEnd_time() + "'");
        }
        LOG.debug("getAlarmInfoHnCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 告警列表展示
     *
     * @param parameters
     * @return
     */
    public String getAlarmInfoHnList(Map<String, Object> parameters) {
        MdAlarmInfoHn alarmInfoHn = (MdAlarmInfoHn) parameters.get("alarmInfoHn");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ALARM_ID,N.ALARMNAME AS ALARM_LEVEL,O.DESCRIPTION AS ALARM_TYPE,M.HOST_NAME,M.ALARM_MSG,M.CLEAR_STATUS, ")
                .append(DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME, ")
                .append(DbSqlUtil.getTimeSql("M.CLEAR_TIME") + " AS CLEAR_TIME ")
                .append("FROM MD_REAL_ALARM_INFO M LEFT JOIN MD_ALARM_LEVEL N ON M.ALARM_LEVEL = N.ALARMLEVEL LEFT JOIN MD_PARAM O ON M.ALARM_TYPE = O.CODE WHERE TYPE = '150' ");
        if (!StringUtils.isEmpty(alarmInfoHn.getAlarm_level())) {
            strb.append(" AND M.ALARM_LEVEL = '").append(alarmInfoHn.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getAlarm_type())) {
            strb.append(" AND M.ALARM_TYPE = '").append(alarmInfoHn.getAlarm_type()).append("'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getStart_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + ">='" + alarmInfoHn.getStart_time() + "'");
        }
        if (!StringUtils.isEmpty(alarmInfoHn.getEnd_time())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + "<='" + alarmInfoHn.getEnd_time() + "'");
        }
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getAlarmInfoHnList sql = {}", sql);
        return sql;
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
        strb.append("SELECT M.ALARM_MSG, M.ALARM_TYPE, " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME," +
                " M.HOST_NAME, M.ALARM_ID, M.ALARM_LEVEL, M.CLEAR_STATUS, " + DbSqlUtil.getTimeSql("M.CLEAR_TIME") +
                " ,M.UP_DATE, " + DbSqlUtil.getTimeSql("M.UPDATE_TIME"))
                .append("FROM MD_REAL_ALARM_INFO M ")
                .append("WHERE M.ALARM_ID = '"+ alarmId +"'");
        LOG.debug("getAlarmInfoList sql = {}", strb);
        return strb.toString();
    }

    public String confirmAlarmInfoById(Map<String, Object> parameters) {
        String alarmId =(String) parameters.get("alarmId");
        StringBuffer strb = new StringBuffer();
        strb.append(" UPDATE MD_REAL_ALARM_INFO SET CLEAR_TIME = ")
                .append(""+ DbSqlUtil.getDateMethod() + ", CLEAR_STATUS = 1 ");
        strb.append(" WHERE ALARM_ID = '"+ alarmId +"'");
        LOG.debug("confirmAlarmInfoById sql = {}", strb);
        return strb.toString();
    }
}
