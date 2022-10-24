package com.asiainfo.lcims.omc.persistence.monitor;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.business.MdHostPerformance;
import com.asiainfo.lcims.omc.model.business.MdHostPerformanceALarm;
import com.asiainfo.lcims.omc.model.business.MdMetricDataSingle;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;
import com.asiainfo.lcims.omc.model.monitor.MultiData;
import com.asiainfo.lcims.omc.persistence.monitor.impl.MetricDataDAOImpl;
import com.asiainfo.lcims.omc.persistence.sql.SqlMetricDataProvider;

public interface MetricDataDAO {

    @SelectProvider(method = "getLineData", type = SqlMetricDataProvider.class)
    List<ChartData> getLineData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("beginDate") String beginDate, @Param("endDate") String endDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getLineDataFromStatisDay", type = SqlMetricDataProvider.class)
    List<ChartData> getLineDataFromStatisDay(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getRecentData", type = SqlMetricDataProvider.class)
    List<ChartData> getRecentData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getStaticSqlData", type = SqlMetricDataProvider.class)
    List<ChartData> getStaticSqlData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getRecentDataFromStatisDay", type = SqlMetricDataProvider.class)
    List<ChartData> getRecentDataFromStatisDay(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getRecentDataTable", type = SqlMetricDataProvider.class)
    List<MultiData> getRecentDataTable(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getRcentTableProcessData", type = SqlMetricDataProvider.class)
    List<MultiData> getRcentTableProcessData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getHostPerformance", type = SqlMetricDataProvider.class)
    List<MdHostPerformance> getHostPerformance(@Param("cpu_metricid") String cpu_metricid,
            @Param("momery_metricid") String momery_metricid,
            @Param("connectable_metricid") String connectable_metricid,
            @Param("queryDate") String queryDate, @Param("nodeids") String nodeids,
            @Param("menuName") String menuName);

    @SelectProvider(method = "getHostPerformanceALarm", type = SqlMetricDataProvider.class)
    List<MdHostPerformanceALarm> getHostPerformanceALarm(@Param("metricids") String metricids);

    @SelectProvider(method = "getHostMetricInfos", type = SqlMetricDataProvider.class)
    List<MdMetricDataSingle> getHostMetricInfos(@Param("metricid") String metricid,
            @Param("queryDate") String queryDate);

    @SelectProvider(method = "recentTableMultiDataByDataTableSingle", type = SqlMetricDataProvider.class)
    List<MultiData> recentTableMultiDataByDataTableSingle(
            @Param("chartDataSet") MdChartDataSet chartDataSet, @Param("metricId") String metricId,
            @Param("queryDate") String queryDate, @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "recentTableMultiDataByDataTableMulti", type = SqlMetricDataProvider.class)
    List<MultiData> recentTableMultiDataByDataTableMulti(
            @Param("chartDataSet") MdChartDataSet chartDataSet, @Param("metricId") String metricId,
            @Param("queryDate") String queryDate, @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getLineMultipleDayData", type = SqlMetricDataProvider.class)
    List<ChartData> getLineMultipleDayData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("days") List<String> days,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "recentMetricDataByTableMulti", type = MetricDataDAOImpl.class)
    MultiData recentMetricDataByTableMulti(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "recentMetricTableByTableStatis", type = MetricDataDAOImpl.class)
    MultiData recentMetricTableByTableStatis(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getTableNameData", type = SqlMetricDataProvider.class)
    List<ChartData> getTableNameData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getTableLineData", type = SqlMetricDataProvider.class)
    List<ChartData> getTableLineData(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("attr1") String attr1,
            @Param("attr2") String attr2, @Param("queryDate") String queryDate,
            @Param("beginDate") String beginDate, @Param("endDate") String endDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getTableNameDataSingle", type = SqlMetricDataProvider.class)
    List<ChartData> getTableNameDataSingle(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("queryDate") String queryDate,
            @Param("paramsMap") Map<String, Object> params);

    @SelectProvider(method = "getTableLineDataSingle", type = SqlMetricDataProvider.class)
    List<ChartData> getTableLineDataSingle(@Param("chartDataSet") MdChartDataSet chartDataSet,
            @Param("metricId") String metricId, @Param("attr1") String attr1,
            @Param("attr2") String attr2, @Param("queryDate") String queryDate,
            @Param("beginDate") String beginDate, @Param("endDate") String endDate,
            @Param("paramsMap") Map<String, Object> params);
}
