package com.asiainfo.lcims.omc.persistence.shcm.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.service.shcm.ShcmReportService;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class AuthenSituationAnalyseDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenSituationAnalyseDAOImpl.class);

    public String getAuthenSituationListWithDay(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer("");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String queryType = (String) params.get("queryType");
        String brasIp = (String) params.get("brasIp");
        String brasipCondition = "";
        String endDateMonth = "" + endDate;
        String startDateMonth = "" + startDate;
        if (ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            startDateMonth = startDateMonth + "-01";
            endDateMonth = endDateMonth + "-15";
        }
        if (!ToolsUtils.StringIsNull(brasIp)) {
            brasipCondition = " AND ATTR1='" + brasIp + "'";
        }
        int monthCount = DateUtil.dayCompare(startDateMonth, endDateMonth);
        String tableSuffix = DateUtil.getFormatTimeByFormat(endDateMonth, "today", "MM");
        String stime = getStime(queryType, "STIME");
        // String metricId = "a88d5251f36748f1887380d7dcbd3f0c";

        Map<String, String> authenType = getAuthenType();
        Set<Entry<String, String>> entrySet = authenType.entrySet();
        for (Entry<String, String> entry : entrySet) {
            String metricId = entry.getKey();
            String metricName = entry.getValue();
            strb.append(" SELECT " + stime + " AS MARK,SUM(MVALUE) AS VALUE,'" + metricName
                    + "' AS AUTHEN_TYPE")
                    .append(" FROM STATIS_DATA_MONTH_" + tableSuffix)
                    .append(" WHERE METRIC_ID = '" + metricId + "'")
                    .append(" AND " + stime + " >= '" + startDate + "'")
                    .append(" AND " + stime + " <= '" + endDate + "'").append(brasipCondition)
                    .append(" GROUP BY MARK UNION");

            for (int i = 0; i < monthCount; i++) {
                StringBuffer strbTemp = new StringBuffer();
                String tableMonth = DateUtil.monthAddNum(startDateMonth, i);
                strbTemp.append(" SELECT " + stime + " AS MARK,SUM(MVALUE) AS VALUE,'"
                        + metricName + "' AS AUTHEN_TYPE")
                        .append(" FROM STATIS_DATA_MONTH_" + tableMonth)
                        .append(" WHERE METRIC_ID = '" + metricId + "'")
                        .append(" AND " + stime + " >= '" + startDate + "'")
                        .append(" AND " + stime + " <= '" + endDate + "'").append(brasipCondition)
                        .append(" GROUP BY MARK UNION");
                strb.append(strbTemp);
            }
        }
        
        String sql = String.valueOf(strb);
        
        if (sql.endsWith("UNION")) {
            sql = sql.substring(0, sql.length() - 5);
        }

        LOG.info("getAuthenSituationListWithDay sql = {}", sql);
        return sql;
    }

    private String getStime(String queryType, String columnName) {
        if (ShcmReportService.QUERY_TYPE_DAY.equals(queryType)) {
            return DbSqlUtil.getDateSql(columnName);
        } else if (ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            return DbSqlUtil.getTimeSqlWithMonth(columnName);
        }
        return DbSqlUtil.getDateSql(columnName);
    }

    public Map<String, String> getAuthenType() {
        Map<String, String> authenTypeMap = new TreeMap<>();
        authenTypeMap.put("028810a343dd440f80b5246668fa1f4c", "authen_success");
        authenTypeMap.put("3ef862c8c8f344dd86b3fc67b25c8fd5", "authen_fail");
        authenTypeMap.put("5482aaccc14b4b27b6abdd825e25f846", "authen_total");
        return authenTypeMap;
    }
}
