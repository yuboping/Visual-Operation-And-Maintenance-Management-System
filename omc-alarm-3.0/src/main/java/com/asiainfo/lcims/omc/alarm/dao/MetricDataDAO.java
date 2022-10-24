package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.model.ChartData;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdChartDataSet;
import com.asiainfo.lcims.util.DbSqlUtil;
import com.asiainfo.lcims.util.ToolsUtils;

public class MetricDataDAO extends BaseDAO {
    private static final Logger logger = LoggerFactory.make();

    /**
     * 查询某时间点的单维度监控数据
     * 
     * @param rule
     * @param chartDataSet
     * @param time
     * @return
     */
    public static List<ChartData> getByTimeSingle(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = new ArrayList<ChartData>();
        String talbe = chartDataSet.getTable_name();
        String mvalue = "DT.MVALUE";
        String attr = rule.getAttr();
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        StringBuilder strb = new StringBuilder("SELECT DT.ITEM AS MARK,DT.ITEM AS ATTR1," + mvalue
                + " AS VALUE FROM ");
        strb.append(talbe + "_" + TimeControl.getTbsuffixesByTime(time)).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + rule.getMetric_id() + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + time + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        if (!ToolsUtils.StringIsNull(attr)) {
            attr = attr.replaceAll("'", "''");
            strb.append(" AND DT.ITEM='" + attr + "'");
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby() + ",DT.ITEM");
        }
        // 指标下维度
        String sql = strb.toString();
        // 获取 paramId
        Map<String, String> params = getParams(rule);
        String paramId = params.get("paramId");
        if (!ToolsUtils.StringIsNull(paramId)) {
            sql = sql.replaceAll("param_paramId", "'" + paramId + "'");
        }

        if (sql.contains("param_parentId")) {
            String parentId = params.get("parentId");
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }

        sql = sql.replaceAll("param_metricId", "'" + rule.getMetric_id() + "'");
        logger.debug("getByTimeSingle sql : {}", sql);
        Connection connection = ConnPool.getConnection();
        ResultSet rs = null;
        PreparedStatement prest = null;
        try {
            prest = connection.prepareStatement(sql);
            rs = prest.executeQuery();
            while (rs.next()) {
                ChartData chartData = new ChartData();
                chartData.setMark(rs.getString("MARK"));
                chartData.setAttr1(rs.getString("ATTR1"));
                chartData.setValue(rs.getString("VALUE"));
                chartDatas.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getByTimeSingle:database operate error.", e);
            logger.error("getByTimeSingle,sql:" + sql);
        } finally {
            close(rs, prest, connection);
        }
        return chartDatas;
    }

    public static Map<String, String> getParams(MdAlarmRuleDetail rule) {
        Map<String, String> params = new HashMap<String, String>();
        if (ToolsUtils.StringIsNull(rule.getDimension2())) {
            params.put("paramId", rule.getDimension1());
        } else {
            params.put("paramId", rule.getDimension2());
            params.put("parentId", rule.getDimension1());
        }
        return params;
    }

    /**
     * 查询某时间点的多维度监控数据
     * 
     * @param rule
     * @param chartDataSet
     * @param time
     * @return
     */
    public static List<ChartData> getByTimeMuti(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = new ArrayList<ChartData>();
        String talbe = chartDataSet.getTable_name();
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        String markname = getMarkName(chartDataSet.getMarktype());
        StringBuilder strb = new StringBuilder("SELECT " + markname + " AS MARK," + mvalue
                + " AS VALUE FROM ");
        strb.append(talbe + "_" + TimeControl.getTbsuffixesByTime(time)).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + rule.getMetric_id() + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + time + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        // 获取 paramId
        Map<String, String> params = getParams(rule);
        String paramId = params.get("paramId");
        if (!ToolsUtils.StringIsNull(paramId)) {
            sql = sql.replaceAll("param_paramId", "'" + paramId + "'");
        }
        if (sql.contains("param_parentId")) {
            String parentId = params.get("parentId");
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        sql = sql.replaceAll("param_metricId", "'" + rule.getMetric_id() + "'");
        logsql(sql);
        Connection connection = ConnPool.getConnection();
        ResultSet rs = null;
        PreparedStatement prest = null;
        try {
            prest = connection.prepareStatement(sql);
            rs = prest.executeQuery();
            while (rs.next()) {
                ChartData chartData = new ChartData();
                chartData.setMark(rs.getString("MARK"));
                chartData.setValue(rs.getString("VALUE"));
                chartDatas.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getByTimeMuti:database operate error.", e);
            logger.error("getByTimeMuti,sql:" + sql);
        } finally {
            close(rs, prest, connection);
        }
        return chartDatas;
    }

    /**
     * 文件系统使用率特殊处理
     * 
     * @param rule
     * @param chartDataSet
     * @param time
     * @return
     */
    public static List<ChartData> getByTimeMutiFileSystemUseRate(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = new ArrayList<ChartData>();
        String talbe = chartDataSet.getTable_name();
        String attr = rule.getAttr();
        StringBuilder strb = new StringBuilder("SELECT DT.ATTR1 AS MARK,DT.MVALUE  AS VALUE FROM ")
                .append(talbe + "_" + TimeControl.getTbsuffixesByTime(time)).append(" DT ");
        ;
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + rule.getMetric_id() + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + time + "' ");
        strb.append(" AND DT.ATTR2='useRate' ");
        if (!ToolsUtils.StringIsNull(attr)) {
            strb.append(" AND DT.ATTR1='" + attr + "'");
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        // 获取 paramId
        Map<String, String> params = getParams(rule);
        String paramId = params.get("paramId");
        if (!ToolsUtils.StringIsNull(paramId)) {
            sql = sql.replaceAll("param_paramId", "'" + paramId + "'");
        }
        logsql(sql);
        Connection connection = ConnPool.getConnection();
        ResultSet rs = null;
        PreparedStatement prest = null;
        try {
            prest = connection.prepareStatement(sql);
            rs = prest.executeQuery();
            while (rs.next()) {
                ChartData chartData = new ChartData();
                chartData.setMark(rs.getString("MARK"));
                chartData.setValue(rs.getString("VALUE"));
                chartDatas.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getByTimeSingle:database operate error.", e);
        } finally {
            close(rs, prest, connection);
        }
        return chartDatas;
    }

    /**
     * 查询某时间点的多维度监控数据
     * 
     * @param rule
     * @param chartDataSet
     * @param time
     * @return
     */
    public static List<ChartData> getByTimeMutigscm5G(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = new ArrayList<ChartData>();
        String talbe = chartDataSet.getTable_name();
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        // String markname = getMarkName(chartDataSet.getMarktype());
        StringBuilder strb = new StringBuilder(
                "SELECT ATTR2 AS MARK," + mvalue + " AS VALUE FROM ");
        strb.append(talbe + "_" + TimeControl.getTbsuffixesByTime(time)).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + rule.getMetric_id() + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + time + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        String sql = strb.toString();
        // 获取 paramId
        Map<String, String> params = getParams(rule);
        String paramId = params.get("paramId");
        if (!ToolsUtils.StringIsNull(paramId)) {
            sql = sql.replaceAll("param_paramId", "'" + paramId + "'");
        }
        if (sql.contains("param_parentId")) {
            String parentId = params.get("parentId");
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        sql = sql.replaceAll("param_metricId", "'" + rule.getMetric_id() + "'");
        logsql(sql);
        Connection connection = ConnPool.getConnection();
        ResultSet rs = null;
        PreparedStatement prest = null;
        try {
            prest = connection.prepareStatement(sql);
            rs = prest.executeQuery();
            while (rs.next()) {
                ChartData chartData = new ChartData();
                chartData.setMark(rs.getString("MARK"));
                chartData.setValue(rs.getString("VALUE"));
                chartDatas.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getByTimeMuti:database operate error.", e);
            logger.error("getByTimeMuti,sql:" + sql);
        } finally {
            close(rs, prest, connection);
        }
        return chartDatas;
    }

    private static String getMarkName(String markType) {
        String markname = "''";
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
        default:
            break;
        }
        return markname;
    }

    public static List<ChartData> getByTimeStatisDay(MdAlarmRuleDetail rule,
            MdChartDataSet chartDataSet, String time) {
        List<ChartData> chartDatas = new ArrayList<ChartData>();
        String talbe = chartDataSet.getTable_name();
        String mvalue = "DT.MVALUE";
        if (!ToolsUtils.StringIsNull(chartDataSet.getMethod())) {
            mvalue = chartDataSet.getMethod() + "(DT.MVALUE)";
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getRound_num())) {
            mvalue = "ROUND(" + mvalue + "," + chartDataSet.getRound_num() + ")";
        }
        String markname = getMarkName(chartDataSet.getMarktype());
        StringBuilder strb = new StringBuilder("SELECT " + markname + " AS MARK," + mvalue
                + " AS VALUE FROM ");
        strb.append(talbe + "_" + TimeControl.getTbsuffixesByTime(time)).append(" DT ");
        if (!ToolsUtils.StringIsNull(chartDataSet.getLeft_join())) {
            strb.append(chartDataSet.getLeft_join());
        }
        strb.append(" WHERE DT.METRIC_ID='" + rule.getMetric_id() + "' AND "
                + DbSqlUtil.getTimeDHMSql("DT.STIME") + "='" + time + "' ");

        if (!ToolsUtils.StringIsNull(chartDataSet.getConditions())) {
            strb.append(" AND " + chartDataSet.getConditions());
        }
        if (!ToolsUtils.StringIsNull(chartDataSet.getGroupby())) {
            strb.append(" GROUP BY " + chartDataSet.getGroupby());
        }
        String sql = strb.toString();
        // 获取 paramId
        Map<String, String> params = getParams(rule);
        String paramId = params.get("paramId");
        String parentId = params.get("parentId");
        if (!ToolsUtils.StringIsNull(paramId)) {
            sql = sql.replaceAll("param_paramId", "'" + paramId + "'");
        }
        if (!ToolsUtils.StringIsNull(parentId)) {
            sql = sql.replaceAll("param_parentId", "'" + parentId + "'");
        }
        logsql(sql);
        Connection connection = ConnPool.getConnection();
        ResultSet rs = null;
        PreparedStatement prest = null;
        try {
            prest = connection.prepareStatement(sql);
            rs = prest.executeQuery();
            while (rs.next()) {
                ChartData chartData = new ChartData();
                chartData.setMark(rs.getString("MARK"));
                chartData.setValue(rs.getString("VALUE"));
                chartDatas.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getByTimeSingle:database operate error.", e);
        } finally {
            close(rs, prest, connection);
        }
        return chartDatas;
    }

}
