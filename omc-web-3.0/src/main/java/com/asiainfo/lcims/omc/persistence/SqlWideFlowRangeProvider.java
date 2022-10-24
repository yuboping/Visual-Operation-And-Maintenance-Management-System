package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeData;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SqlWideFlowRangeProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlWideFlowRangeProvider.class);

    public String getWideFlowRangeListWithDay(Map<String, Object> parameters) {
        WideFlowRangeData wideFlowRangeData = (WideFlowRangeData) parameters.get("wideFlowRangeData");
        String tableSuffix = DateUtil.getFormatTimeByFormat(
                wideFlowRangeData.getEndDate(),
                "today",
                "MM");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT " + DbSqlUtil.getDateSql("STIME") + " as stime, ")
                .append("MAX(CASE METRIC_ID WHEN '422dddb3f111436f9ee553506d75701f' THEN mvalue ELSE 0 END ) as input_v4, ")
                .append("MAX(CASE METRIC_ID WHEN '08a19aedce424a32a529bd5ff8edd4ca' THEN mvalue ELSE 0 END ) as output_v4, ")
                .append("MAX(CASE METRIC_ID WHEN 'e1592b59aca94f388241e8ce21de2e5e' THEN mvalue ELSE 0 END ) as total_v4, ")
                .append("MAX(CASE METRIC_ID WHEN '6c3d224c26b14fe58e4b6528fe2beb6c' THEN mvalue ELSE 0 END ) as input_v6, ")
                .append("MAX(CASE METRIC_ID WHEN '06825f68e2c24ba68fb165207a8d668b' THEN mvalue ELSE 0 END ) as output_v6, ")
                .append("MAX(CASE METRIC_ID WHEN '6cb45700c67b48178c973bace53f0557' THEN mvalue ELSE 0 END ) as total_v6 ")
                .append("FROM STATIS_DATA_MONTH_"  + tableSuffix )
                .append(" where  " + DbSqlUtil.getDateSql("STIME") + " >= '" +wideFlowRangeData.getStartDate() + "'" )
                .append(" and  " + DbSqlUtil.getDateSql("STIME") + " <= '" +wideFlowRangeData.getEndDate() + "'" )
                .append(" and METRIC_ID IN ('422dddb3f111436f9ee553506d75701f','08a19aedce424a32a529bd5ff8edd4ca','e1592b59aca94f388241e8ce21de2e5e','6c3d224c26b14fe58e4b6528fe2beb6c','06825f68e2c24ba68fb165207a8d668b','6cb45700c67b48178c973bace53f0557') GROUP BY STIME ");


        for (int i = 0; i < wideFlowRangeData.getMonthCount(); i++) {
            StringBuffer strbTemp = new StringBuffer();
            String tableMonth = DateUtil.monthAddNum(wideFlowRangeData.getStartDate(), i);
            strbTemp.append(" union SELECT " + DbSqlUtil.getDateSql("STIME") + " as stime, ")
                    .append("MAX(CASE METRIC_ID WHEN '422dddb3f111436f9ee553506d75701f' THEN mvalue ELSE 0 END ) as input_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN '08a19aedce424a32a529bd5ff8edd4ca' THEN mvalue ELSE 0 END ) as output_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN 'e1592b59aca94f388241e8ce21de2e5e' THEN mvalue ELSE 0 END ) as total_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN '6c3d224c26b14fe58e4b6528fe2beb6c' THEN mvalue ELSE 0 END ) as input_v6, ")
                    .append("MAX(CASE METRIC_ID WHEN '06825f68e2c24ba68fb165207a8d668b' THEN mvalue ELSE 0 END ) as output_v6, ")
                    .append("MAX(CASE METRIC_ID WHEN '6cb45700c67b48178c973bace53f0557' THEN mvalue ELSE 0 END ) as total_v6 ")
                    .append("FROM STATIS_DATA_MONTH_" + tableMonth )
                    .append(" where  " + DbSqlUtil.getDateSql("STIME") + " >= '" +wideFlowRangeData.getStartDate() + "'" )
                    .append(" and  " + DbSqlUtil.getDateSql("STIME") + " <= '" +wideFlowRangeData.getEndDate() + "'" )
                    .append(" and METRIC_ID IN ('422dddb3f111436f9ee553506d75701f','08a19aedce424a32a529bd5ff8edd4ca','e1592b59aca94f388241e8ce21de2e5e','6c3d224c26b14fe58e4b6528fe2beb6c','06825f68e2c24ba68fb165207a8d668b','6cb45700c67b48178c973bace53f0557') GROUP BY STIME ");
            strb.append(strbTemp);
        }

        LOG.info("getOnlineUserStatisticListWithDay sql = {}", strb.toString());
        return strb.toString();
    }

    public String getWideFlowRangeListWithMonth(Map<String, Object> parameters) {
        WideFlowRangeData wideFlowRangeData = (WideFlowRangeData) parameters.get("wideFlowRangeData");
        String tableSuffix = DateUtil.monthAddNum(wideFlowRangeData.getEndDate(), 0);
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " AS stime, sum(input_v4) AS input_v4, sum(output_v4) AS output_v4, sum(total_v4) AS total_v4, sum(input_v6) AS input_v6, sum(output_v6) output_v6, sum(total_v6) AS total_v6 from ")
                .append("(SELECT " + DbSqlUtil.getTimeSql("STIME") + "AS stime, ")
                .append("MAX(CASE METRIC_ID WHEN '422dddb3f111436f9ee553506d75701f' THEN mvalue ELSE 0 END ) as input_v4, ")
                .append("MAX(CASE METRIC_ID WHEN '08a19aedce424a32a529bd5ff8edd4ca' THEN mvalue ELSE 0 END ) as output_v4, ")
                .append("MAX(CASE METRIC_ID WHEN 'e1592b59aca94f388241e8ce21de2e5e' THEN mvalue ELSE 0 END ) as total_v4, ")
                .append("MAX(CASE METRIC_ID WHEN '6c3d224c26b14fe58e4b6528fe2beb6c' THEN mvalue ELSE 0 END ) as input_v6, ")
                .append("MAX(CASE METRIC_ID WHEN '06825f68e2c24ba68fb165207a8d668b' THEN mvalue ELSE 0 END ) as output_v6, ")
                .append("MAX(CASE METRIC_ID WHEN '6cb45700c67b48178c973bace53f0557' THEN mvalue ELSE 0 END ) as total_v6 ")
                .append("FROM STATIS_DATA_MONTH_"  + tableSuffix )
                .append(" where  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " >= '" +wideFlowRangeData.getStartDate() + "'" )
                .append(" and  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " <= '" +wideFlowRangeData.getEndDate() + "'" )
                .append(" and METRIC_ID IN ('422dddb3f111436f9ee553506d75701f','08a19aedce424a32a529bd5ff8edd4ca','e1592b59aca94f388241e8ce21de2e5e','6c3d224c26b14fe58e4b6528fe2beb6c','06825f68e2c24ba68fb165207a8d668b','6cb45700c67b48178c973bace53f0557') GROUP BY STIME ) m ")
                .append("GROUP BY " + DbSqlUtil.getTimeSqlWithMonth("STIME"));

        for (int i = 0; i < wideFlowRangeData.getMonthCount(); i++) {
            StringBuffer strbTemp = new StringBuffer();
            String tableMonth = DateUtil.monthAddNum(wideFlowRangeData.getStartDate(), i);
            strbTemp.append(" union SELECT " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " AS stime, sum(input_v4) AS input_v4, sum(output_v4) AS output_v4, sum(total_v4) AS total_v4, sum(input_v6) AS input_v6, sum(output_v6) output_v6, sum(total_v6) AS total_v6 from ")
                    .append("(SELECT " + DbSqlUtil.getTimeSql("STIME") + "AS stime, ")
                    .append("MAX(CASE METRIC_ID WHEN '422dddb3f111436f9ee553506d75701f' THEN mvalue ELSE 0 END ) as input_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN '08a19aedce424a32a529bd5ff8edd4ca' THEN mvalue ELSE 0 END ) as output_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN 'e1592b59aca94f388241e8ce21de2e5e' THEN mvalue ELSE 0 END ) as total_v4, ")
                    .append("MAX(CASE METRIC_ID WHEN '6c3d224c26b14fe58e4b6528fe2beb6c' THEN mvalue ELSE 0 END ) as input_v6, ")
                    .append("MAX(CASE METRIC_ID WHEN '06825f68e2c24ba68fb165207a8d668b' THEN mvalue ELSE 0 END ) as output_v6, ")
                    .append("MAX(CASE METRIC_ID WHEN '6cb45700c67b48178c973bace53f0557' THEN mvalue ELSE 0 END ) as total_v6 ")
                    .append("FROM STATIS_DATA_MONTH_"  + tableMonth )
                    .append(" where  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " >= '" +wideFlowRangeData.getStartDate() + "'" )
                    .append(" and  " + DbSqlUtil.getTimeSqlWithMonth("STIME") + " <= '" +wideFlowRangeData.getEndDate() + "'" )
                    .append(" and METRIC_ID IN ('422dddb3f111436f9ee553506d75701f','08a19aedce424a32a529bd5ff8edd4ca','e1592b59aca94f388241e8ce21de2e5e','6c3d224c26b14fe58e4b6528fe2beb6c','06825f68e2c24ba68fb165207a8d668b','6cb45700c67b48178c973bace53f0557') GROUP BY STIME ) m ")
                    .append("GROUP BY " + DbSqlUtil.getTimeSqlWithMonth("STIME"));
            strb.append(strbTemp);
        }

        LOG.info("getOnlineUserStatisticListWithMonth sql = {}", strb.toString());
        return strb.toString();
    }
}
