package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.omc.model.shcm.OnlineUserStatisticVo;
import com.asiainfo.lcims.omc.service.shcm.ShcmReportService;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SqlShcmProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlShcmProvider.class);

    public String getOnlineUserStatisticListWithHour(Map<String, Object> parameters) {
        OnlineUserStatisticVo onlineUserStatisticVo = (OnlineUserStatisticVo) parameters.get("onlineUserStatisticVo");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT " + DbSqlUtil.getTimeSql("STIME") + " as STIME, MVALUE ")
                .append(" from STATIS_DATA_MONTH_" + onlineUserStatisticVo.getTableName())
                .append(" where  " + DbSqlUtil.getDateSql("STIME") + " = '" +onlineUserStatisticVo.getEndDate() + "'" )
                .append(" and metric_id = '3574188989014302b92186c3e413157a' order by STIME asc ");

        LOG.info("getOnlineUserStatisticListWithHour sql = {}", strb.toString());
        return strb.toString();
    }

    public String getOnlineUserStatisticListWithDay(Map<String, Object> parameters) {
        OnlineUserStatisticVo onlineUserStatisticVo = (OnlineUserStatisticVo) parameters.get("onlineUserStatisticVo");
        String tableSuffix = DateUtil.getFormatTimeByFormat(
                onlineUserStatisticVo.getEndDate(),
                "today",
                "MM");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM ( SELECT " + DbSqlUtil.getTimeSql("STIME") + " as STIME, MVALUE ")
                .append(" from STATIS_DATA_MONTH_" + tableSuffix)
                .append(" where  " + DbSqlUtil.getDateSql("STIME") + " >= '" +onlineUserStatisticVo.getStartDate() + "'" )
                .append(" and  " + DbSqlUtil.getDateSql("STIME") + " <= '" +onlineUserStatisticVo.getEndDate() + "'" )
                .append(" and " + DbSqlUtil.getTimeDHMSql("STIME") + " like '%10:00'" )
                .append(" and metric_id = '3574188989014302b92186c3e413157a'");

        for (int i = 0; i < onlineUserStatisticVo.getMonthCount(); i++) {
            StringBuffer strbTemp = new StringBuffer();
            String tableMonth = DateUtil.monthAddNum(onlineUserStatisticVo.getStartDate(), i);
            strbTemp.append(" union SELECT " + DbSqlUtil.getTimeSql("STIME") + " as STIME, MVALUE ")
                    .append(" from STATIS_DATA_MONTH_" + tableMonth)
                    .append(" where  " + DbSqlUtil.getDateSql("STIME") + " >= '" +onlineUserStatisticVo.getStartDate() + "'" )
                    .append(" and  " + DbSqlUtil.getDateSql("STIME") + " <= '" +onlineUserStatisticVo.getEndDate() + "'" )
                    .append(" and " + DbSqlUtil.getTimeDHMSql("STIME") + " like '%10:00'" )
                    .append(" and metric_id = '3574188989014302b92186c3e413157a'");
            strb.append(strbTemp);
        }
        strb.append(" ) m order by STIME asc ");

        LOG.info("getOnlineUserStatisticListWithDay sql = {}", strb.toString());
        return strb.toString();
    }

    public String getOnlineUserStatisticListWithMonth(Map<String, Object> parameters) {
        OnlineUserStatisticVo onlineUserStatisticVo = (OnlineUserStatisticVo) parameters.get("onlineUserStatisticVo");
        String tableSuffix = DateUtil.monthAddNum(onlineUserStatisticVo.getEndDate(), 0);
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM (  SELECT " + DbSqlUtil.getTimeSql("STIME") + " as STIME, MVALUE ")
                .append(" from STATIS_DATA_MONTH_" + tableSuffix)
                .append(" where  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " >= '" +onlineUserStatisticVo.getStartDate() + "'" )
                .append(" and  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " <= '" +onlineUserStatisticVo.getEndDate() + "'" )
                .append(" and " + DbSqlUtil.getTimeDHMSql("STIME") + " like '%15 10:00'" )
                .append(" and metric_id = '3574188989014302b92186c3e413157a'");

        for (int i = 0; i < onlineUserStatisticVo.getMonthCount(); i++) {
            StringBuffer strbTemp = new StringBuffer();
            String tableMonth = DateUtil.monthAddNum(onlineUserStatisticVo.getStartDate(), i);
            strbTemp.append(" union SELECT " + DbSqlUtil.getTimeSql("STIME") + " as STIME, MVALUE ")
                    .append(" from STATIS_DATA_MONTH_" + tableMonth)
                    .append(" where  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " >= '" +onlineUserStatisticVo.getStartDate() + "'" )
                    .append(" and  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " <= '" +onlineUserStatisticVo.getEndDate() + "'" )
                    .append(" and " + DbSqlUtil.getTimeDHMSql("STIME") + " like '%15 10:00'" )
                    .append(" and metric_id = '3574188989014302b92186c3e413157as'");
            strb.append(strbTemp);
        }
        strb.append(" ) m order by STIME asc ");
        LOG.info("getOnlineUserStatisticListWithMonth sql = {}", strb.toString());
        return strb.toString();
    }
    
    
    public String getQueryAuthFailReasonInfos(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer("");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String queryType = (String) params.get("queryType");
        String brasIp = (String) params.get("brasIp");
        String brasipCondition = "";
        String endDateMonth = "" + endDate;
        String startDateMonth = "" + startDate;
        if(ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            startDateMonth = startDateMonth+"-01";
            endDateMonth = endDateMonth + "-15";
        }
        if(!ToolsUtils.StringIsNull(brasIp)) {
            brasipCondition = " AND ATTR1='"+brasIp+"'";
        }
        int monthCount = DateUtil.dayCompare(startDateMonth,endDateMonth);
        String tableSuffix = DateUtil.getFormatTimeByFormat(endDateMonth,"today","MM");
        String stime = getStime(queryType, "STIME");
        String metricId = (String) params.get("metricId");
        strb.append("SELECT "+stime+" AS MARK,SUM(MVALUE) AS VALUE,ATTR2 AS ADSL_REASON")
            .append(" FROM STATIS_DATA_MONTH_"+tableSuffix)
            .append(" WHERE METRIC_ID = '"+metricId+"'")
            .append(" AND " + stime + " >= '" + startDate + "'" )
            .append(" AND " + stime + " <= '" + endDate + "'" )
            .append(brasipCondition)
            .append(" GROUP BY mark,ATTR2");
        
        for (int i = 0; i < monthCount; i++) {
            StringBuffer strbTemp = new StringBuffer();
            String tableMonth = DateUtil.monthAddNum(startDateMonth, i);
            strbTemp.append(" UNION SELECT "+stime+" AS MARK,SUM(MVALUE) AS VALUE,ATTR2 AS ADSL_REASON")
            .append(" FROM STATIS_DATA_MONTH_"+tableMonth)
            .append(" WHERE METRIC_ID = '"+metricId+"'")
            .append(" AND " + stime + " >= '" + startDate + "'" )
            .append(" AND " + stime + " <= '" + endDate + "'" )
            .append(brasipCondition)
            .append(" GROUP BY mark,ATTR2");
            strb.append(strbTemp);
        }
        LOG.info("getQueryAuthFailReasonInfos sql = {}", strb.toString());
        return strb.toString();
    }
    
    private String getStime(String queryType, String columnName) {
        if(ShcmReportService.QUERY_TYPE_DAY.equals(queryType)) {
            return DbSqlUtil.getDateSql(columnName);
        } else if(ShcmReportService.QUERY_TYPE_MONTH.equals(queryType)) {
            return DbSqlUtil.getTimeSqlWithMonth(columnName);
        }
        return DbSqlUtil.getDateSql(columnName);
    }
}
