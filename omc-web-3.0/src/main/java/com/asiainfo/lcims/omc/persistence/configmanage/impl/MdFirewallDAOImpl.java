package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdFirewall;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MdFirewallDAOImpl {
    private static final Logger LOG = LoggerFactory.getLogger(MdFirewallDAOImpl.class);

    /**
     * 表格数据
     * @param parameters
     * @return
     */
    public String getFirewall(Map<String, Object> parameters) {
        MdFirewall mdFirewall = (MdFirewall) parameters.get("mdFirewall");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT B.*,PEQ.FACTORY_ID,PN.DESCRIPTION AS IPTYPE,PEQ.MODEL_NAME AS MODELNAME,PFA.FACTORY_NAME AS FACTORYNAME,PAR.NAME AS AREANAME FROM " +
                        "MD_FIREWALL B LEFT JOIN MD_PARAM PN ON (B.IP_TYPE=PN.CODE AND PN.TYPE=14) LEFT JOIN MD_EQUIPMENT_MODEL PEQ ON (B.EQUIP_ID=PEQ.ID) " +
                        "LEFT JOIN MD_FACTORY PFA ON (B.EQUIP_ID=PEQ.ID AND PEQ.FACTORY_ID=PFA.ID) LEFT JOIN MD_AREA PAR ON (B.AREA_NO=PAR.AREANO) WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdFirewall.getFirewall_name())) {
            strb.append(" AND B.FIREWALL_NAME LIKE '%")
                    .append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(mdFirewall.getFirewall_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdFirewall.getFirewall_ip())) {
            strb.append(" AND B.FIREWALL_IP='").append(StringUtil.SqlFilter(mdFirewall.getFirewall_ip()))
                    .append("'");
        }
        if (null != mdFirewall.getIp_type()) {
            strb.append(" AND B.IP_TYPE=").append(mdFirewall.getIp_type()).append("");
        }
        if (!StringUtils.isEmpty(mdFirewall.getId())) {
            strb.append(" AND B.ID='").append(mdFirewall.getId()).append("'");
        }
        if (!StringUtils.isEmpty(mdFirewall.getArea_no())) {
            strb.append(" AND B.AREA_NO='").append(mdFirewall.getArea_no()).append("'");
        }
        LOG.debug("getBdNas sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 修改更新
     * @param parameters
     * @return
     */
    public String updateFirewall(Map<String, Object> parameters) {
        MdFirewall mdFirewall = (MdFirewall) parameters.get("mdFirewall");
        StringBuilder buffer = new StringBuilder("UPDATE MD_FIREWALL SET ID=ID ");
        if (null != mdFirewall.getFirewall_name()) {
            buffer.append(",FIREWALL_NAME='").append(StringUtil.SqlFilter(mdFirewall.getFirewall_name())).append("'");
        }
        if (null != mdFirewall.getFirewall_ip()) {
            buffer.append(",FIREWALL_IP='").append(StringUtil.SqlFilter(mdFirewall.getFirewall_ip())).append("'");
        }
        if (null != mdFirewall.getEquip_id()) {
            buffer.append(",EQUIP_ID='").append(mdFirewall.getEquip_id()).append("' ");
        }
        if (null != mdFirewall.getArea_no()) {
            buffer.append(",AREA_NO='").append(StringUtil.SqlFilter(mdFirewall.getArea_no())).append("' ");
        }
        if (null != mdFirewall.getIp_type()) {
            buffer.append(",IP_TYPE='").append(mdFirewall.getIp_type()).append("' ");
        }
        if (null != mdFirewall.getPort()) {
            buffer.append(",PORT='").append(StringUtil.SqlFilter(mdFirewall.getPort())).append("' ");
        }
        if (null != mdFirewall.getUser_name()) {
            buffer.append(",USER_NAME='").append(StringUtil.SqlFilter(mdFirewall.getUser_name())).append("' ");
        }
        if (null != mdFirewall.getPassword()) {
            buffer.append(",PASSWORD='").append(StringUtil.SqlFilter(mdFirewall.getPassword())).append("' ");
        }
        if (null != mdFirewall.getFile_path()) {
            buffer.append(",FILE_PATH='").append(StringUtil.SqlFilter(mdFirewall.getFile_path())).append("' ");
        }
        if (null != mdFirewall.getResult_path()) {
            buffer.append(",RESULT_PATH='").append(StringUtil.SqlFilter(mdFirewall.getResult_path())).append("' ");
        }
        if (null != mdFirewall.getDescription()) {
            buffer.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdFirewall.getDescription())).append("' ");
        }
        buffer.append("WHERE ID='").append(mdFirewall.getId()).append("'");
        LOG.debug("updateBdNas sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String queryNotMatchIp(Map<String, Object> parameters) {
        String metricid = (String) parameters.get("metricid");
        StringBuilder sql = new StringBuilder("SELECT T.ATTR1 AS FIREWALL_NAME,T.ATTR1 AS FIREWALL_IP FROM ");
        String queryDate = TimeTools.getCurrentTime("yyyy-MM-dd");
        String tableName = "METRIC_DATA_MULTI_" + getMonthDay(queryDate);
        sql.append("( SELECT ATTR1 FROM "+tableName+" WHERE METRIC_ID = '"+metricid+"' GROUP BY ATTR1 ) T ")
                .append(" LEFT JOIN MD_FIREWALL firewall ON T.ATTR1 = firewall.FIREWALL_IP")
                .append(" WHERE FIREWALL_IP IS NULL");
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
}
