package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.configmanage.MdFirewall;
import com.asiainfo.lcims.omc.model.configmanage.MdThirdparty;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MdThirdpartyDaoImpl {
    private static final Logger LOG = LoggerFactory.getLogger(MdThirdpartyDaoImpl.class);
    /**
     * 表格数据
     * @param parameters
     * @return
     */
    public String getThirdparty(Map<String, Object> parameters) {
        MdThirdparty mdThirdparty = (MdThirdparty) parameters.get("mdThirdparty");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT B.*,PEQ.FACTORY_ID,PN.DESCRIPTION AS IPTYPE,PEQ.MODEL_NAME AS MODELNAME,PFA.FACTORY_NAME AS FACTORYNAME,PAR.NAME AS AREANAME FROM " +
                        "MD_THIRDPARTY B LEFT JOIN MD_PARAM PN ON (B.IP_TYPE=PN.CODE AND PN.TYPE=14) LEFT JOIN MD_EQUIPMENT_MODEL PEQ ON (B.EQUIP_ID=PEQ.ID) " +
                        "LEFT JOIN MD_FACTORY PFA ON (B.EQUIP_ID=PEQ.ID AND PEQ.FACTORY_ID=PFA.ID) LEFT JOIN MD_AREA PAR ON (B.AREA_NO=PAR.AREANO) WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdThirdparty.getThirdparty_name())) {
            strb.append(" AND B.THIRDPARTY_NAME LIKE '%")
                    .append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(mdThirdparty.getThirdparty_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdThirdparty.getThirdparty_ip())) {
            strb.append(" AND B.THIRDPARTY_IP='").append(StringUtil.SqlFilter(mdThirdparty.getThirdparty_ip()))
                    .append("'");
        }
        if (null != mdThirdparty.getIp_type()) {
            strb.append(" AND B.IP_TYPE=").append(mdThirdparty.getIp_type())
                    .append("");
        }
        if (!StringUtils.isEmpty(mdThirdparty.getId())) {
            strb.append(" AND B.ID='").append(mdThirdparty.getId()).append("'");
        }
        if (!StringUtils.isEmpty(mdThirdparty.getArea_no())) {
            strb.append(" AND B.AREA_NO='").append(mdThirdparty.getArea_no()).append("'");
        }
        LOG.debug("getThirdparty sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 修改更新
     * @param parameters
     * @return
     */
    public String updateThirdparty(Map<String, Object> parameters) {
        MdThirdparty mdThirdparty = (MdThirdparty) parameters.get("mdThirdparty");
        StringBuilder buffer = new StringBuilder("UPDATE MD_THIRDPARTY SET ID=ID ");
        if (null != mdThirdparty.getThirdparty_name()) {
            buffer.append(",THIRDPARTY_NAME='").append(StringUtil.SqlFilter(mdThirdparty.getThirdparty_name())).append("'");
        }
        if (null != mdThirdparty.getThirdparty_ip()) {
            buffer.append(",THIRDPARTY_IP='").append(StringUtil.SqlFilter(mdThirdparty.getThirdparty_ip())).append("'");
        }
        if (null != mdThirdparty.getEquip_id()) {
            buffer.append(",EQUIP_ID='").append(mdThirdparty.getEquip_id()).append("' ");
        }
        if (null != mdThirdparty.getArea_no()) {
            buffer.append(",AREA_NO='").append(StringUtil.SqlFilter(mdThirdparty.getArea_no())).append("' ");
        }
        if (null != mdThirdparty.getIp_type()) {
            buffer.append(",IP_TYPE='").append(mdThirdparty.getIp_type()).append("' ");
        }
        if (null != mdThirdparty.getPort()) {
            buffer.append(",PORT='").append(StringUtil.SqlFilter(mdThirdparty.getPort())).append("' ");
        }
        if (null != mdThirdparty.getUser_name()) {
            buffer.append(",USER_NAME='").append(StringUtil.SqlFilter(mdThirdparty.getUser_name())).append("' ");
        }
        if (null != mdThirdparty.getPassword()) {
            buffer.append(",PASSWORD='").append(StringUtil.SqlFilter(mdThirdparty.getPassword())).append("' ");
        }
        if (null != mdThirdparty.getFile_path()) {
            buffer.append(",FILE_PATH='").append(StringUtil.SqlFilter(mdThirdparty.getFile_path())).append("' ");
        }
        if (null != mdThirdparty.getDescription()) {
            buffer.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdThirdparty.getDescription())).append("' ");
        }
        buffer.append("WHERE ID='").append(mdThirdparty.getId()).append("'");
        LOG.debug("updateThirdparty sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String queryNotMatchIp(Map<String, Object> parameters) {
        String metricid = (String) parameters.get("metricid");
        StringBuilder sql = new StringBuilder("SELECT T.ATTR1 AS THIRDPARTY_NAME,T.ATTR1 AS THIRDPARTY_IP FROM ");
        String queryDate = TimeTools.getCurrentTime("yyyy-MM-dd");
        String tableName = "METRIC_DATA_MULTI_" + getMonthDay(queryDate);
        sql.append("( SELECT ATTR1 FROM "+tableName+" WHERE METRIC_ID = '"+metricid+"' GROUP BY ATTR1 ) T ")
                .append(" LEFT JOIN MD_THIRDPARTY thirdparty ON T.ATTR1 = thirdparty.THIRDPARTY_IP")
                .append(" WHERE THIRDPARTY_IP IS NULL");
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
