package com.asiainfo.lcims.omc.persistence.sql;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.asiainfo.lcims.omc.util.TimeControl;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 复杂sql
 *
 */
public class SqlMetricDataProvider {

    private static final Logger LOG = LoggerFactory.make();

    private static final String PROVINCE_SHCM = "shcm";
    private static final String PROVINCE_GSCM = "gscm";
    private static final String PROVINCE_GSCU = "gscu";

    /**
     * 获取基本折线图数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getLineData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        String beginDate = (String) parameters.get("beginDate");
        String endDate = (String) parameters.get("endDate");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        Integer cycleId = (Integer) params.get("cycleId");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder();
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())
                || "24hour".equals(chartDataSet.getScope())) {
            strb.append("SELECT " + DbSqlUtil.getTimeDHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        } else {
            strb.append("SELECT " + DbSqlUtil.getTimeHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        }
        strb.append(chartDataSet.getTable_name()).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())) {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + queryDate + "' ");
        } else {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getDateSql("DT.STIME") + "='" + queryDate + "' ");
        }
        if (!ToolsUtils.StringIsNull(beginDate)) {
            strb.append(
                    " AND " + DbSqlUtil.getDateFormatSql("'" + beginDate + "'") + " < DT.STIME");
        }
        if (!ToolsUtils.StringIsNull(endDate)) {
            strb.append(" AND DT.STIME <= " + DbSqlUtil.getDateFormatSql("'" + endDate + "'"));
        }
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
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getLineData sql : {}", sql);
        return sql;
    }

    /**
     * 查询多天数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getLineMultipleDayData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        List<String> days = (List<String>) parameters.get("days");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        // 组合sql数据
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        // 汇总sql
        StringBuilder sql_all = new StringBuilder(" ");
        // 公共sql
        StringBuilder sql_pulic_1 = new StringBuilder(" ");
        StringBuilder sql_pulic_2 = new StringBuilder("");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            sql_pulic_1.append(chartDataSet.getLeft_join());
        }

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            sql_pulic_2.append(" AND " + chartDataSet.getConditions());
        }

        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            sql_pulic_2.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String table_name = null;
        for (int i = 0; i < days.size(); i++) {
            table_name = chartDataSet.getTable_name() + "_" + getMonthDay(days.get(i));
            sql_all.append("SELECT " + DbSqlUtil.getTimeDSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ").append(table_name).append(" DT ")
                    .append(sql_pulic_1.toString())
                    .append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                            + DbSqlUtil.getDateSql("DT.STIME") + "='" + days.get(i) + "' ")
                    .append(sql_pulic_2.toString());
            if (i < days.size() - 1) {
                sql_all.append(" UNION ALL ");
            }
        }

        String sql = sql_all.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = sql.replaceAll("param_paramId", "'" + paramid + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getLineMultipleDayData: " + sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String getLineDataFromStatisDay(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        String queryDate = (String) params.get("queryDate");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder("SELECT " + DbSqlUtil.getTimeHMSql("DT.STIME")
                + " AS MARK," + mvalue + " AS VALUE FROM ");
        strb.append(chartDataSet.getTable_name()).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND " + DbSqlUtil.getDateSql("DT.STIME")
                + "='" + queryDate + "' ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }

        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = sql.replaceAll("param_paramId", "'" + paramid + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.debug("getLineDataFromStatisDay: " + sql);
        return sql;
    }

    /**
     * 获取基本最新数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getRecentData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        String table_name = (String) params.get("table_name");
        String mvalue = "DT.MVALUE";
        String markname = getMarkName(chartDataSet.getMarktype());
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder(
                "SELECT " + markname + " AS MARK," + mvalue + " AS VALUE FROM ").append(table_name)
                        .append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
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

        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getRecentData: " + sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String getRecentDataFromStatisDay(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        String mvalue = "DT.MVALUE";
        String markname = getMarkName(chartDataSet.getMarktype());
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder(
                "SELECT " + markname + " AS MARK," + mvalue + " AS VALUE FROM ").append(table_name)
                        .append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + " = '" + queryDate + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = sql.replaceAll("param_paramId", "'" + paramid + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getRecentDataFromStatisDay: " + sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String getRecentDataTable(Map<String, Object> parameters) {
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
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder(
                "SELECT DT.ATTR1,DT.ATTR2," + mvalue + " as MVALUE FROM ").append(table_name)
                        .append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
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
        LOG.info("getRecentDataTable: " + sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String getRcentTableProcessData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");
        StringBuilder strb = new StringBuilder("SELECT " + DbSqlUtil.getNullFunction()
                + "(P.PROCESS_NAME,DT.ITEM) AS ATTR1,DT.ITEM AS ATTR2,DT.MVALUE FROM ")
                        .append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
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
        LOG.info("getRcentTableProcessData: " + sql);
        return sql;
    }

    private String getMarkName(String markType) {
        String markname = DbSqlUtil.getTimeDHMSql("DT.STIME");
        if (ToolsUtils.StringIsNull(markType))
            return markname;
        switch (markType) {
        case "node":
            markname = "N.NODE_NAME";
            break;
        case "host":
            markname = "H.HOSTNAME";
            break;
        case "MdParam":
            markname = "P.DESCRIPTION";
            break;
        case "area":
            markname = "A.NAME";
            break;
        case "item":
            markname = "DT.ITEM";
            break;
        default:
            break;
        }
        return markname;
    }

    public String getHostPerformance(Map<String, Object> parameters) {
        String cpu_metricid = (String) parameters.get("cpu_metricid");
        String momery_metricid = (String) parameters.get("momery_metricid");
        String connectable_metricid = (String) parameters.get("connectable_metricid");
        String queryDate = (String) parameters.get("queryDate");
        String nodeids = (String) parameters.get("nodeids");
        String menuName = (String) parameters.get("menuName");
        StringBuilder strb = new StringBuilder(
                "SELECT H.HOSTID,H.HOSTNAME,PM.DESCRIPTION AS HOSTTYPENAME,H.ADDR,N.NODE_NAME AS NODENAME, T1.MVALUE AS CPU_VALUE, T2.MVALUE AS MEMORY_VALUE ,T3.MVALUE AS CONNECTABLE_VALUE");
        strb.append(", '" + cpu_metricid + "' AS CPU_METRICID");
        strb.append(", '" + momery_metricid + "' AS MEMORY_METRICID");
        strb.append(", '" + connectable_metricid + "' AS CONNECTABLE_METRICID ");
        strb.append(" FROM (SELECT * FROM MON_HOST WHERE 1=1 ");
        if (!ToolsUtils.StringIsNull(nodeids)) {
            strb.append("AND NODEID IN (" + nodeids + ")");
        }
        String tbsuffixes = TimeControl.getTbsuffixes(queryDate);
        strb.append(
                " ) H  LEFT JOIN ( SELECT * FROM MD_PARAM WHERE TYPE = 3) PM ON H.HOSTTYPE = PM.CODE ")
                .append(" LEFT JOIN MD_NODE N ON H.NODEID = N.ID");
        strb.append(" INNER JOIN MD_BUSINESS_HOST BH ON BH.HOSTID = H.HOSTID AND BH.NAME = '"
                + menuName + "'");
        strb.append(" LEFT JOIN METRIC_DATA_SINGLE_" + tbsuffixes
                + " T1 ON T1.HOST_ID = H.HOSTID AND T1.METRIC_ID ='" + cpu_metricid + "'");
        strb.append(" AND " + DbSqlUtil.getTimeDHMSql("T1.STIME") + " = '" + queryDate + "'");
        strb.append(" LEFT JOIN METRIC_DATA_SINGLE_" + tbsuffixes
                + " T2 ON T2.HOST_ID = H.HOSTID AND  T2.METRIC_ID = '" + momery_metricid + "'");
        strb.append(" AND " + DbSqlUtil.getTimeDHMSql("T2.STIME") + " = '" + queryDate + "'");
        strb.append(" LEFT JOIN METRIC_DATA_MULTI_" + tbsuffixes
                + " T3 ON T3.ATTR1 = H.ADDR  AND T3.METRIC_ID = '" + connectable_metricid + "'");
        strb.append(" AND " + DbSqlUtil.getTimeDHMSql("T3.STIME") + " = '" + queryDate + "'")
                .append(" ORDER BY H.NODEID, H.HOSTTYPE, HOSTNAME ASC");
        LOG.debug("getHostPerformance:" + strb.toString());
        return strb.toString();
    }

    public String getHostPerformanceALarm(Map<String, Object> parameters) {
        String metricids = (String) parameters.get("metricids");
        StringBuilder strb = new StringBuilder(
                "SELECT H.HOSTID,alarm.METRIC_ID,alarm.ATTR,alarm.ALARM_LEVEL FROM MON_HOST H");
        strb.append(
                " LEFT JOIN MD_ALARM_INFO alarm ON (H.HOSTID = alarm.DIMENSION2 OR H.HOSTID = alarm.DIMENSION1) ");
        strb.append(" WHERE alarm.METRIC_ID IN (" + metricids + ")");
        strb.append(" AND alarm.ALARM_NUM > 0 AND alarm.DELETE_STATE = 0");
        LOG.debug("getHostPerformanceALarm:" + strb.toString());
        return strb.toString();
    }

    public String getHostMetricInfos(Map<String, Object> parameters) {
        String metricid = (String) parameters.get("metricid");
        String queryDate = (String) parameters.get("queryDate");
        String tbsuffixes = TimeControl.getTbsuffixes(queryDate);
        StringBuilder strb = new StringBuilder(
                "SELECT H.HOSTID,T.METRIC_ID,T.MVALUE,T.ITEM," + DbSqlUtil.getNullFunction()
                        + "(P.PROCESS_NAME,T.ITEM) AS ITEMNAME FROM MON_HOST H");
        strb.append(" INNER JOIN METRIC_DATA_SINGLE_" + tbsuffixes + " T ON H.HOSTID = T.HOST_ID");
        strb.append(" LEFT JOIN MD_PROCESS P ON T.ITEM = P.PROCESS_KEY");
        strb.append(" AND T.METRIC_ID ='" + metricid + "'");
        strb.append(" WHERE " + DbSqlUtil.getTimeDHMSql("T.STIME") + " = '" + queryDate + "'");
        LOG.debug("getHostMetricInfos:" + strb.toString());
        return strb.toString();
    }

    @SuppressWarnings("unchecked")
    public String recentTableMultiDataByDataTableSingle(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");
        String attr1 = "DT.ITEM";
        attr1 = getMarkName(chartDataSet.getMarktype());
        if (attr1.equals(DbSqlUtil.getTimeDHMSql("DT.STIME"))) {
            attr1 = "DT.ITEM";
        }
        StringBuilder strb = new StringBuilder(
                "SELECT " + attr1 + " AS ATTR1," + DbSqlUtil.getNullFunction()
                        + "(P.DESCRIPTION,DT.MVALUE) AS MVALUE , DT.MVALUE AS ITEMVALUE FROM ")
                                .append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
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
        LOG.info("recentTableMultiDataByDataTableSingle: " + sql);
        return sql;
    }

    @SuppressWarnings("unchecked")
    public String recentTableMultiDataByDataTableMulti(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String table_name = (String) params.get("table_name");
        String paramid = (String) params.get("paramid");

        String attr1 = getMarkName(chartDataSet.getMarktype());
        StringBuilder strb = new StringBuilder("SELECT DT.ATTR1,DT.ATTR2,DT.ATTR3,DT.ATTR4,");
        if (StringUtils.equalsIgnoreCase(PROVINCE_SHCM, ReadFile.PROVINCE)
                || StringUtils.equalsIgnoreCase(PROVINCE_GSCM, ReadFile.PROVINCE)
                || StringUtils.equalsIgnoreCase(PROVINCE_GSCU, ReadFile.PROVINCE)) {
            strb.append(DbSqlUtil.getNullFunction() + "(P.DESCRIPTION,DT.MVALUE) AS MVALUE,");
        } else {
            strb.append("DT.MVALUE,");
        }
        strb.append(attr1).append(" AS ITEMVALUE FROM ").append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
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
        LOG.info("recentTableMultiDataByDataTableMulti sql : {}", sql);
        return sql;
    }

    /**
     * 获取基本最新数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getStaticSqlData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String paramid = (String) params.get("paramid");
        StringBuilder strb = new StringBuilder(chartDataSet.getLeft_join());
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = strb.toString().replaceAll("param_paramId", "'" + paramid + "'");
        }
        LOG.info("getStaticSqlData: " + sql);
        return sql;
    }

    /**
     * 获取表格数据表头
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getTableNameData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        String table_name = (String) params.get("table_name");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder("SELECT DT.ATTR1 MARK,DT.ATTR2 VALUE FROM ")
                .append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND " + DbSqlUtil.getDateSql("DT.STIME")
                + "='" + queryDate + "' ");
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

        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getTableNameData: " + sql);
        return sql;
    }

    /**
     * 获取表格基本折线图数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getTableLineData(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        String attr1 = (String) parameters.get("attr1");
        String attr2 = (String) parameters.get("attr2");
        String beginDate = (String) parameters.get("beginDate");
        String endDate = (String) parameters.get("endDate");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        Integer cycleId = (Integer) params.get("cycleId");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder();
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())
                || "24hour".equals(chartDataSet.getScope())) {
            strb.append("SELECT " + DbSqlUtil.getTimeDHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        } else {
            strb.append("SELECT " + DbSqlUtil.getTimeHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        }
        strb.append(chartDataSet.getTable_name()).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())) {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + queryDate + "' ");
        } else {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getDateSql("DT.STIME") + "='" + queryDate + "' ");
        }
        if (!ToolsUtils.StringIsNull(attr1)) {
            strb.append(" AND DT.ATTR1 ='" + attr1 + "'");
        }
        if (!ToolsUtils.StringIsNull(attr2)) {
            strb.append(" AND DT.ATTR2 ='" + attr2 + "'");
        }
        if (!ToolsUtils.StringIsNull(beginDate)) {
            strb.append(
                    " AND " + DbSqlUtil.getDateFormatSql("'" + beginDate + "'") + " < DT.STIME");
        }
        if (!ToolsUtils.StringIsNull(endDate)) {
            strb.append(" AND DT.STIME <= " + DbSqlUtil.getDateFormatSql("'" + endDate + "'"));
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = strb.toString().replaceAll("param_paramId", "'" + paramid + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getTableLineData sql : {}", sql);
        return sql;
    }

    /**
     * 获取表格数据表头
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getTableNameDataSingle(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        String table_name = (String) params.get("table_name");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder("SELECT DT.ITEM MARK,DT.ITEM VALUE FROM ")
                .append(table_name).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND " + DbSqlUtil.getDateSql("DT.STIME")
                + "='" + queryDate + "' ");
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

        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getTableNameDataSingle: " + sql);
        return sql;
    }

    /**
     * 获取表格基本折线图数据
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getTableLineDataSingle(Map<String, Object> parameters) {
        MdChartDataSet chartDataSet = (MdChartDataSet) parameters.get("chartDataSet");
        String metricId = (String) parameters.get("metricId");
        String attr1 = (String) parameters.get("attr1");
        String beginDate = (String) parameters.get("beginDate");
        String endDate = (String) parameters.get("endDate");
        Map<String, Object> params = (Map<String, Object>) parameters.get("paramsMap");
        String queryDate = (String) params.get("queryDate");
        String paramid = (String) params.get("paramid");
        String parentId = (String) params.get("parentId");
        Integer cycleId = (Integer) params.get("cycleId");
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder();
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())
                || "24hour".equals(chartDataSet.getScope())) {
            strb.append("SELECT " + DbSqlUtil.getTimeDHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        } else {
            strb.append("SELECT " + DbSqlUtil.getTimeHMSql("DT.STIME") + " AS MARK," + mvalue
                    + " AS VALUE FROM ");
        }
        strb.append(chartDataSet.getTable_name()).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        if ((cycleId != null && cycleId == 6) || "1month".equals(chartDataSet.getScope())) {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + queryDate + "' ");
        } else {
            strb.append(" WHERE DT.METRIC_ID='" + metricId + "' AND "
                    + DbSqlUtil.getDateSql("DT.STIME") + "='" + queryDate + "' ");
        }
        if (!ToolsUtils.StringIsNull(attr1)) {
            strb.append(" AND DT.ITEM ='" + attr1 + "'");
        }
        if (!ToolsUtils.StringIsNull(beginDate)) {
            strb.append(
                    " AND " + DbSqlUtil.getDateFormatSql("'" + beginDate + "'") + " < DT.STIME");
        }
        if (!ToolsUtils.StringIsNull(endDate)) {
            strb.append(" AND DT.STIME <= " + DbSqlUtil.getDateFormatSql("'" + endDate + "'"));
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        String sql = strb.toString();
        if (!ToolsUtils.StringIsNull(paramid)) {
            sql = strb.toString().replaceAll("param_paramId", "'" + paramid + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        LOG.info("getTableLineDataSingle sql : {}", sql);
        return sql;
    }

    private String getMonthDay(String date) {
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return month + "_" + day;
    }
}
