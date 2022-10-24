package com.asiainfo.lcims.omc.persistence.monitor.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class MetricDataDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MetricDataDAOImpl.class);

    @SuppressWarnings("unchecked")
    public String recentMetricDataByTableMulti(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT " + mvalue + " AS MVALUE FROM ");
        strb.append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID IN ('" + metricId + "') AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + queryDate + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }

        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = strb.toString().replaceAll("param_paramId", "'" + paramid + "'");
        }
        sql = sql.replaceAll("param_metricId", "'" + metricId + "'");
        LOG.debug("recentMetricDataByTableMulti sql : {} ", sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String recentMetricTableByTableStatis(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");
        String left_join = chartDataSet.getLeft_join();
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(left_join) && left_join.indexOf("DESCRIPTION") != -1) {
            mvalue = "P.DESCRIPTION";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder(
                "SELECT " + mvalue + " AS MVALUE FROM ").append(table_name)
                        .append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID IN ('" + metricId + "') AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + queryDate + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }

        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = strb.toString().replaceAll("param_paramId", "'" + paramid + "'");
        }
        LOG.debug("recentMetricTableByTableStatis sql : {}", sql);
        return sql;
    }

}
