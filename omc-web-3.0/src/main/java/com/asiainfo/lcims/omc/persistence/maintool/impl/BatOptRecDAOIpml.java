package com.asiainfo.lcims.omc.persistence.maintool.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.util.DbSqlUtil;

public class BatOptRecDAOIpml {

    private static final Logger LOG = LoggerFactory.getLogger(BatOptRecDAOIpml.class);

    public String getBatOptLog(Map<String, Object> parameters) {
        String startdate = (String) parameters.get("startdate");
        String enddate = (String) parameters.get("enddate");
        String admin = (String) parameters.get("admin");
        String opttype = (String) parameters.get("opttype");
        String brasip = (String) parameters.get("brasip");
        String date = (String) parameters.get("date");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT BAT.OPTTYPE,BAT.NASIP,BAT.IPADDRESS,BAT.OPTREASON,BAT.ADMIN,ROLE.`NAME` AS ADMINNAME,AREA.`NAME` AS AREANAME,"
                        + DbSqlUtil.getTimeSql("BAT.OPTTIME") + "AS OPTTIME");
        strb.append(" FROM BAT_OPT_REC BAT LEFT JOIN M_ADMIN A ON BAT.ADMIN=A.ADMIN");
        strb.append(" LEFT JOIN M_ADMIN_ROLE ADMIN ON BAT.ADMIN= ADMIN.ADMIN");
        strb.append(" LEFT JOIN MD_ROLE ROLE ON ADMIN.ROLEID = ROLE.ROLEID");
        strb.append(
                " LEFT JOIN BD_NAS NAS ON BAT.NASIP = NAS.NAS_IP");
        strb.append(
                " LEFT JOIN MD_AREA AREA ON NAS.AREA_NO = AREA.AREANO WHERE 1=1 ");

        if (!StringUtils.isEmpty(startdate)) {
            strb.append(" AND " + DbSqlUtil.getDateSql("BAT.OPTTIME") + ">='" + startdate + "'");
        }
        if (!StringUtils.isEmpty(enddate)) {
            strb.append(" AND " + DbSqlUtil.getDateSql("BAT.OPTTIME") + "<='" + enddate + "'");
        }
        if (!StringUtils.isEmpty(date)) {
            strb.append(" AND " + DbSqlUtil.getDateSql("BAT.OPTTIME") + "='" + date + "'");
        }
        if (!StringUtils.isEmpty(admin)) {
            strb.append(" AND BAT.ADMIN LIKE '%" + admin + "%'");
        }
        if (!StringUtils.isEmpty(opttype)) {
            strb.append(" AND BAT.OPTTYPE='" + opttype + "'");
        }
        if (!StringUtils.isEmpty(brasip)) {
            strb.append(" AND BAT.NASIP='" + brasip + "'");
        }
        strb.append(" ORDER BY BAT.OPTTIME DESC");
        LOG.debug("getBatOptLog sql = {}", strb);
        return strb.toString();
    }

}
