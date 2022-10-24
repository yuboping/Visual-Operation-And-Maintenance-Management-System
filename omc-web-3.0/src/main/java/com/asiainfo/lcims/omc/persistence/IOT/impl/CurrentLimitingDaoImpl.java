package com.asiainfo.lcims.omc.persistence.IOT.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

public class CurrentLimitingDaoImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentLimitingDaoImpl.class);

    /**
     * 查询apn信息
     *
     * @param parameters
     * @return
     */
    public String getApnList(Map<String, Object> parameters) {
        MdApnLimitRule mdApnLimitRule = (MdApnLimitRule) parameters.get("mdApnLimitRule");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT A.RULEID, A.APNID, A.LIMIT_CYCLE, A.AUTH_VALUE, A.LOG_VALUE, A.RECORD_VALUE, A.DAY_VALUE, B.APN "
                        + "FROM MD_APN_LIMITRULE A JOIN MD_APN_RECORD B ON A.APNID = B.APNID WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdApnLimitRule.getApnId())) {
            strb.append(" AND A.APNID= '")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdApnLimitRule.getApnId())))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdApnLimitRule.getRuleId())) {
            strb.append(" AND A.RULEID='").append(mdApnLimitRule.getRuleId()).append("'");
        }
        if (!StringUtils.isEmpty(mdApnLimitRule.getApn())) {
            strb.append(" AND B.APN LIKE '%").append(mdApnLimitRule.getApn()).append("%'");
        }
        LOG.debug("getApnList sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 新增
     * @param parameters
     * @return
     */
    public String addApn(Map<String, Object> parameters) {
        MdApnLimitRule mdApnLimitRule = (MdApnLimitRule) parameters.get("mdApnLimitRule");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MD_APN_LIMITRULE (RULEID,APNID,LIMIT_CYCLE,AUTH_VALUE,LOG_VALUE,RECORD_VALUE,DAY_VALUE,UPDATE_TIME) VALUES('")
                .append(mdApnLimitRule.getRuleId()).append("','").append(mdApnLimitRule.getApnId())
                .append("','").append(mdApnLimitRule.getLimit_cycle()).append("','")
                .append(mdApnLimitRule.getAuth_value()).append("','")
                .append(mdApnLimitRule.getLog_value()).append("','")
                .append(mdApnLimitRule.getRecord_value()).append("','")
                .append(mdApnLimitRule.getDay_value()).append("',")
                .append(DbSqlUtil.getDateMethod()).append(")");
        return strb.toString();
    }

    /**
     * 修改
     * @param parameters
     * @return
     */
    public String updateMdApn(Map<String, Object> parameters) {
        MdApnLimitRule mdApnLimitRule = (MdApnLimitRule) parameters.get("mdApnLimitRule");
        StringBuilder strb = new StringBuilder("UPDATE MD_APN_LIMITRULE SET RULEID = RULEID ");
        if (null != mdApnLimitRule.getApnId()) {
            strb.append(",APNID='").append(StringUtil.SqlFilter(mdApnLimitRule.getApnId()))
                    .append("' ");
        }
        if (null != mdApnLimitRule.getLimit_cycle()) {
            strb.append(",LIMIT_CYCLE='")
                    .append(StringUtil.SqlFilter(mdApnLimitRule.getLimit_cycle())).append("' ");
        }
        if (null != mdApnLimitRule.getAuth_value()) {
            strb.append(",AUTH_VALUE='").append(mdApnLimitRule.getAuth_value()).append("' ");
        }
        if (null != mdApnLimitRule.getLog_value()) {
            strb.append(",LOG_VALUE='").append(StringUtil.SqlFilter(mdApnLimitRule.getLog_value()))
                    .append("' ");
        }
        if (null != mdApnLimitRule.getRecord_value()) {
            strb.append(",RECORD_VALUE='")
                    .append(StringUtil.SqlFilter(mdApnLimitRule.getRecord_value())).append("' ");
        }
        if (null != mdApnLimitRule.getDay_value()) {
            strb.append(",DAY_VALUE='").append(mdApnLimitRule.getDay_value()).append("' ");
        }
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE APNID='").append(mdApnLimitRule.getApnId()).append("'");
        LOG.debug("updateMdMetric sql = {}", strb.toString());
        return strb.toString();
    }

}
