package com.asiainfo.lcims.omc.persistence.analogdialup.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class AnalogDialUpDAOIpml {

    private static final Logger LOG = LoggerFactory.getLogger(AnalogDialUpDAOIpml.class);

    public String getAnalogDialUpList(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT adu.ID, adu.HOST_ID, adu.CRON_ERP, adu.USERNAME, adu.NAS_PORT, adu.CALL_FROM_ID, adu.CALL_TO_ID, adu.EXT, h.ADDR AS HOST_IP, h.HOSTNAME AS HOST_NAME FROM ANALOG_DIAL_UP adu LEFT JOIN MON_HOST h ON (h.HOSTID = adu.HOST_ID) WHERE 1=1 ");
        if (!StringUtils.isEmpty(analogDialUp.getHost_name())) {
            strb.append(" AND h.HOSTNAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(analogDialUp.getHost_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getHost_ip())) {
            strb.append(" AND h.ADDR='").append(analogDialUp.getHost_ip()).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getUsername())) {
            strb.append(" AND adu.USERNAME='")
                    .append(StringUtil.SqlFilter(analogDialUp.getUsername())).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getId())) {
            strb.append(" AND adu.ID='").append(analogDialUp.getId()).append("'");
        }
        LOG.debug("getAnalogDialUpList sql = {}", strb.toString());
        return strb.toString();
    }

    public String getAnalogDialUpResultList(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT adur.ID, adur.ANALOG_DIAL_UP_ID, adur.CRON_ERP, adur.USERNAME, adur.NAS_PORT, adur.CALL_FROM_ID, adur.CALL_TO_ID, adur.EXT, adur.HOST_IP,")
                .append(DbSqlUtil.getDateFormatSql("adur.DIAL_UP_TIME"))
                .append(" AS DIAL_UP_TIME, adur.COMMAND_NAME, adur.SERIALNO, adur.RETURNCODE, adur.ERRORDESC, adur.CALLTIME, h.HOSTNAME AS HOST_NAME, p.DESCRIPTION AS RETURNCODE_NAME FROM ANALOG_DIAL_UP_RESULT adur LEFT JOIN MON_HOST h ON (h.ADDR = adur.HOST_IP) LEFT JOIN MD_PARAM p ON (p.TYPE = '")
                .append(ConstantUtill.ANALOGDIALUPRESULT_TYPE)
                .append("' AND p.CODE = adur.RETURNCODE) WHERE 1=1 ");
        if (!StringUtils.isEmpty(analogDialUp.getHost_name())) {
            strb.append(" AND h.HOSTNAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(analogDialUp.getHost_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getHost_ip())) {
            strb.append(" AND h.ADDR='").append(analogDialUp.getHost_ip()).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getUsername())) {
            strb.append(" AND adur.USERNAME='")
                    .append(StringUtil.SqlFilter(analogDialUp.getUsername())).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getReturncode())) {
            strb.append(" AND adur.RETURNCODE='")
                    .append(StringUtil.SqlFilter(analogDialUp.getReturncode())).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getStartdate())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("adur.DIAL_UP_TIME") + ">='"
                    + analogDialUp.getStartdate() + "'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getEnddate())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("adur.DIAL_UP_TIME") + "<='"
                    + analogDialUp.getEnddate() + "'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getId())) {
            strb.append(" AND adur.ID='").append(analogDialUp.getId()).append("'");
        }
        strb.append(" ORDER BY adur.DIAL_UP_TIME DESC");
        LOG.debug("getAnalogDialUpResultList sql = {}", strb.toString());
        return strb.toString();
    }

    public String updateAnalogDialUp(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        StringBuilder strb = new StringBuilder("UPDATE ANALOG_DIAL_UP SET ID = ID ");
        if (null != analogDialUp.getHost_id()) {
            strb.append(",HOST_ID='").append(StringUtil.SqlFilter(analogDialUp.getHost_id()))
                    .append("' ");
        }
        if (null != analogDialUp.getCron_erp()) {
            strb.append(",CRON_ERP='").append(StringUtil.SqlFilter(analogDialUp.getCron_erp()))
                    .append("' ");
        }
        if (null != analogDialUp.getUsername()) {
            strb.append(",USERNAME='").append(analogDialUp.getUsername()).append("' ");
        }
        if (null != analogDialUp.getPassword()) {
            strb.append(",PASSWORD='").append(StringUtil.SqlFilter(analogDialUp.getPassword()))
                    .append("' ");
        }
        if (null != analogDialUp.getNas_port()) {
            strb.append(",NAS_PORT='").append(StringUtil.SqlFilter(analogDialUp.getNas_port()))
                    .append("' ");
        }
        if (null != analogDialUp.getCall_from_id()) {
            strb.append(",CALL_FROM_ID='").append(analogDialUp.getCall_from_id()).append("' ");
        }
        if (null != analogDialUp.getCall_to_id()) {
            strb.append(",CALL_TO_ID='").append(analogDialUp.getCall_to_id()).append("' ");
        }
        if (null != analogDialUp.getExt()) {
            strb.append(",EXT='").append(analogDialUp.getExt()).append("' ");
        }
        strb.append("WHERE ID='").append(analogDialUp.getId()).append("'");
        LOG.debug("updateAnalogDialUp sql = {}", strb.toString());
        return strb.toString();
    }

    public String insertAnalogDialUpResult(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO ANALOG_DIAL_UP_RESULT (ID,ANALOG_DIAL_UP_ID,CRON_ERP,USERNAME,PASSWORD,NAS_PORT,CALL_FROM_ID,CALL_TO_ID,EXT,HOST_IP,DIAL_UP_TIME,COMMAND_NAME,SERIALNO,RETURNCODE,ERRORDESC,CALLTIME) VALUES('")
                .append(analogDialUp.getId()).append("','")
                .append(analogDialUp.getAnalog_dial_up_id()).append("','")
                .append(analogDialUp.getCron_erp()).append("','").append(analogDialUp.getUsername())
                .append("','").append(analogDialUp.getPassword()).append("','")
                .append(analogDialUp.getNas_port()).append("','")
                .append(analogDialUp.getCall_from_id()).append("','")
                .append(analogDialUp.getCall_to_id()).append("','").append(analogDialUp.getExt())
                .append("','").append(analogDialUp.getHost_ip()).append("',")
                .append(DbSqlUtil.getDateFormatSql("'" + analogDialUp.getDial_up_time() + "'"))
                .append(",'").append(analogDialUp.getCommand_name()).append("','")
                .append(analogDialUp.getSerialno()).append("','")
                .append(analogDialUp.getReturncode()).append("','")
                .append(analogDialUp.getErrordesc()).append("','")
                .append(analogDialUp.getCalltime()).append("'").append(")");
        return strb.toString();
    }

    public String insertMetricDataMulti(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        String dial_up_mm_dd = DateUtil.getFormatTimeByDate(
                DateUtil.parseDate(analogDialUp.getDial_up_time(), DateUtil.C_TIME_PATTON_DEFAULT),
                "MM_dd");
        String tableName = Constant.METRIC_DATA_MULTI + dial_up_mm_dd;
        StringBuilder strb = new StringBuilder();
        strb.append("INSERT INTO ").append(tableName).append(
                " (HOST_ID,METRIC_ID,MVALUE,ATTR1,ATTR2,ATTR3,ATTR4,STIME,CREATE_TIME,UPDATE_TIME) VALUES('")
                .append(analogDialUp.getHost_id()).append("','").append(analogDialUp.getMetric_id())
                .append("','").append(analogDialUp.getId()).append("','','','','',")
                .append(DbSqlUtil.getDateFormatSql("'" + analogDialUp.getDial_up_time() + "'"))
                .append(",").append(DbSqlUtil.getDateMethod()).append(",")
                .append(DbSqlUtil.getDateMethod()).append(")");
        return strb.toString();
    }

    public String getAnalogDialUpResultTotalCount(Map<String, Object> parameters) {
        AnalogDialUp analogDialUp = (AnalogDialUp) parameters.get("analogDialUp");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT COUNT(*) FROM ANALOG_DIAL_UP_RESULT adur LEFT JOIN MON_HOST h ON (h.ADDR = adur.HOST_IP) LEFT JOIN MD_PARAM p ON (p.TYPE = '")
                .append(ConstantUtill.ANALOGDIALUPRESULT_TYPE)
                .append("' AND p.CODE = adur.RETURNCODE) WHERE 1=1 ");
        if (!StringUtils.isEmpty(analogDialUp.getHost_name())) {
            strb.append(" AND h.HOSTNAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(analogDialUp.getHost_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getHost_ip())) {
            strb.append(" AND h.ADDR='").append(analogDialUp.getHost_ip()).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getUsername())) {
            strb.append(" AND adur.USERNAME='")
                    .append(StringUtil.SqlFilter(analogDialUp.getUsername())).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getReturncode())) {
            strb.append(" AND adur.RETURNCODE='")
                    .append(StringUtil.SqlFilter(analogDialUp.getReturncode())).append("'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getStartdate())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("adur.DIAL_UP_TIME") + ">='"
                    + analogDialUp.getStartdate() + "'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getEnddate())) {
            strb.append(" AND " + DbSqlUtil.getTimeSql("adur.DIAL_UP_TIME") + "<='"
                    + analogDialUp.getEnddate() + "'");
        }
        if (!StringUtils.isEmpty(analogDialUp.getId())) {
            strb.append(" AND adur.ID='").append(analogDialUp.getId()).append("'");
        }
        LOG.debug("getAnalogDialUpResultTotalCount sql = {}", strb.toString());
        return strb.toString();
    }

    public String getAnalogDialUpResultPage(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        String sql = getAnalogDialUpResultList(parameters);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getAnalogDialUpResultPage sql = {}", sql);
        return sql;
    }
}
