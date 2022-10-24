package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

/**
 * 实现BRAS中复杂的sql语句
 * 
 * @author zhujiansheng
 * @date 2018年8月7日 下午5:52:07
 * @version V1.0
 */
public class BdNasDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(BdNasDAOImpl.class);

    
    public String getBdNasByRoleList(Map<String, Object> parameters) {
        BdNas bdNas = (BdNas) parameters.get("bdNas");
        String roleid=(String)parameters.get("roleid");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT B.*,PEQ.FACTORY_ID,PN.DESCRIPTION AS IPTYPE,PEQ.MODEL_NAME "
                + "AS MODELNAME,PFA.FACTORY_NAME AS FACTORYNAME,PAR.NAME AS AREANAME "
                + "FROM BD_NAS B LEFT JOIN MD_PARAM PN ON (B.IP_TYPE=PN.CODE AND PN.TYPE=14) "
                + "LEFT JOIN MD_EQUIPMENT_MODEL PEQ ON (B.EQUIP_ID=PEQ.ID) "
                + "LEFT JOIN MD_FACTORY PFA ON (B.EQUIP_ID=PEQ.ID AND PEQ.FACTORY_ID=PFA.ID) "
                + "LEFT JOIN MD_AREA PAR ON (B.AREA_NO=PAR.AREANO) WHERE 1=1 ");
        strb.append("and B.AREA_NO in (select PERMISSIONID from  MD_ROLE_PERMISSIONS "
        		+ "where  type='area' and \r\n" + 
        		"roleid='"+roleid+"')");
        
        if (!StringUtils.isEmpty(bdNas.getNas_name())) {
            strb.append(" AND B.NAS_NAME LIKE '%")
                    .append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(bdNas.getNas_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(bdNas.getNas_ip())) {
            strb.append(" AND B.NAS_IP='").append(StringUtil.SqlFilter(bdNas.getNas_ip()))
                    .append("'");
        }
        if (null != bdNas.getIp_type()) {
            strb.append(" AND B.IP_TYPE=").append(bdNas.getIp_type()).append("");
        }
        if (!StringUtils.isEmpty(bdNas.getId())) {
            strb.append(" AND B.ID='").append(bdNas.getId()).append("'");
        }
        if (!StringUtils.isEmpty(bdNas.getArea_no())) {
            strb.append(" AND B.AREA_NO='").append(bdNas.getArea_no()).append("'");
        }
        
        
        LOG.debug("getBdNasByRoleList sql = {}", strb.toString());
        return strb.toString();
    }
    
    public String getBdNas(Map<String, Object> parameters) {
        BdNas bdNas = (BdNas) parameters.get("bdNas");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT B.*,PEQ.FACTORY_ID,PN.DESCRIPTION AS IPTYPE,PEQ.MODEL_NAME AS MODELNAME,PFA.FACTORY_NAME AS FACTORYNAME,PAR.NAME AS AREANAME FROM BD_NAS B LEFT JOIN MD_PARAM PN ON (B.IP_TYPE=PN.CODE AND PN.TYPE=14) LEFT JOIN MD_EQUIPMENT_MODEL PEQ ON (B.EQUIP_ID=PEQ.ID) LEFT JOIN MD_FACTORY PFA ON (B.EQUIP_ID=PEQ.ID AND PEQ.FACTORY_ID=PFA.ID) LEFT JOIN MD_AREA PAR ON (B.AREA_NO=PAR.AREANO) WHERE 1=1 ");
        if (!StringUtils.isEmpty(bdNas.getNas_name())) {
            strb.append(" AND B.NAS_NAME LIKE '%")
                    .append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(bdNas.getNas_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(bdNas.getNas_ip())) {
            strb.append(" AND B.NAS_IP='").append(StringUtil.SqlFilter(bdNas.getNas_ip()))
                    .append("'");
        }
        if (null != bdNas.getIp_type()) {
            strb.append(" AND B.IP_TYPE=").append(bdNas.getIp_type()).append("");
        }
        if (!StringUtils.isEmpty(bdNas.getId())) {
            strb.append(" AND B.ID='").append(bdNas.getId()).append("'");
        }
        if (!StringUtils.isEmpty(bdNas.getArea_no())) {
            strb.append(" AND B.AREA_NO='").append(bdNas.getArea_no()).append("'");
        }
        LOG.debug("getBdNas sql = {}", strb.toString());
        return strb.toString();
    }

    public String updateBdNas(Map<String, Object> parameters) {
        BdNas bdNas = (BdNas) parameters.get("bdNas");
        StringBuilder buffer = new StringBuilder("UPDATE BD_NAS SET ID=ID ");
        if (null != bdNas.getNas_name()) {
            buffer.append(",NAS_NAME='").append(StringUtil.SqlFilter(bdNas.getNas_name()))
                    .append("'");
        }
        if (null != bdNas.getNas_ip()) {
            buffer.append(",NAS_IP='").append(StringUtil.SqlFilter(bdNas.getNas_ip())).append("'");
        }
        if (null != bdNas.getEquip_id()) {
            buffer.append(",EQUIP_ID='").append(bdNas.getEquip_id()).append("' ");
        }
        if (null != bdNas.getArea_no()) {
            buffer.append(",AREA_NO='").append(StringUtil.SqlFilter(bdNas.getArea_no()))
                    .append("' ");
        }
        if (null != bdNas.getIp_type()) {
            buffer.append(",IP_TYPE='").append(bdNas.getIp_type()).append("' ");
        }
        if (null != bdNas.getDescription()) {
            buffer.append(",DESCRIPTION='").append(StringUtil.SqlFilter(bdNas.getDescription()))
                    .append("' ");
        }
        buffer.append("WHERE ID='").append(bdNas.getId()).append("'");
        LOG.debug("updateBdNas sql = {}", buffer.toString());
        return buffer.toString();
    }
    
    public String queryNotMatchNasIp(Map<String, Object> parameters) {
        String metricid = (String) parameters.get("metricid");
        StringBuilder sql = new StringBuilder("SELECT T.ATTR1 AS NAS_NAME,T.ATTR1 AS NAS_IP FROM ");
        String queryDate = TimeTools.getCurrentTime("yyyy-MM-dd");
        String tableName = "METRIC_DATA_MULTI_" + getMonthDay(queryDate);
        sql.append("( SELECT ATTR1 FROM "+tableName+" WHERE METRIC_ID = '"+metricid+"' GROUP BY ATTR1 ) T ")
           .append(" LEFT JOIN BD_NAS nas ON T.ATTR1 = nas.NAS_IP")
           .append(" WHERE NAS_IP IS NULL");
        return sql.toString();
    }
    
    private String getMonthDay(String date) {
        if (date == null || date.length() < 10) {
            date = TimeTools.getCurrentTime("yyyy-MM-dd");
        }
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return month + "_" + day;
    }
    
    
    public String queryNotMatchNasIpForShcm(Map<String, Object> parameters) {
        String metricid = (String) parameters.get("metricid");
        StringBuilder sql = new StringBuilder("SELECT T.ATTR1 AS NAS_NAME,T.ATTR1 AS NAS_IP FROM ");
        String queryDate = TimeTools.getCurrentTime("yyyy-MM-dd");
        String tableDate = TimeTools.getCurrentTime("MM");
        String tableName = "STATIS_DATA_MONTH_" + tableDate;
        sql.append("( SELECT ATTR1 FROM "+tableName+" WHERE METRIC_ID = '"+metricid+"' AND '"+queryDate+"' = "+DbSqlUtil.getDateSql("STIME")+" GROUP BY ATTR1 ) T ")
           .append(" LEFT JOIN BD_NAS nas ON T.ATTR1 = nas.NAS_IP")
           .append(" WHERE NAS_IP IS NULL");
        return sql.toString();
    }
}
