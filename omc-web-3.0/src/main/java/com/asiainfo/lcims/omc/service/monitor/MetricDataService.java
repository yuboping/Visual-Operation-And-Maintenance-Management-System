package com.asiainfo.lcims.omc.service.monitor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;
import com.asiainfo.lcims.omc.model.AlarmMessage;
import com.asiainfo.lcims.omc.model.CompareAlarm;
import com.asiainfo.lcims.omc.model.MessageData;
import com.asiainfo.lcims.omc.model.RaduisHost;
import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.monitor.ChartInfo;
import com.asiainfo.lcims.omc.model.monitor.ChartTableData;
import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;
import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;
import com.asiainfo.lcims.omc.model.monitor.MultiData;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.CycleType;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.monitor.MetricDataDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.alarm.AlarmService;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.TimeControl;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 获取指标数据
 * 
 * @author zhul
 *
 */
@Service(value = "metricDataService")
public class MetricDataService {
    private static final Logger LOG = LoggerFactory.getLogger(MetricDataService.class);

    public static final String CUR_DATE = "yyyy-MM-dd";

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_FORMAT_MIN = "yyyy-MM-dd HH:mm";

    @Inject
    MetricDataDAO metricDataDAO;

    @Inject
    MonHostDAO hostDao;

    @Resource(name = "commoninit")
    CommonInit commoninit;

    @Resource(name = "alarmService")
    AlarmService alarmService;

    public List<ChartInfo> getMetricDataLine(Map<String, Object> params) {
        String chart_name = (String) params.get("chart_name");
        MdChartDataSet chartDataSet = CommonInit.getChartDataSetByChartName(chart_name);
        MdChartDetail chartDetail = CommonInit.getChartDetailByChartName(chart_name);
        // 根据 chartDataSet 配置组装数据
        List<ChartInfo> data = null;
        if (chartDataSet != null && chartDetail != null)
            data = assemblyLineData(chartDataSet, chartDetail, params);
        return data;
    }

    /**
     * 组装数据
     * 
     * @param chartDataSet
     * @return
     */
    private List<ChartInfo> assemblyLineData(MdChartDataSet chartDataSet, MdChartDetail chartDetail,
            Map<String, Object> params) {
        List<ChartInfo> data = null;
        String beginDate = null;
        String endDate = null;
        String queryDate = (String) params.get("queryDate");
        String scope = chartDataSet.getScope();
        switch (scope) {
        case "1day": // 从凌晨到现在
            data = oneDayData(chartDataSet, chartDetail, queryDate, params);
            break;
        case "multipleDays": // 显示多天数据，从queryDate 减 n 天 数据，n可配置，最多显示 7 天数据
            data = multipleDayData(chartDataSet, queryDate, params);
            break;
        case "1month":
            // 周期为一个月的数据页面展示
            data = oneMonthData(chartDataSet, queryDate, params);
            break;
        case "slottime": // 时间段（请求参数需带开始及结束时间）
            data = slottime(chartDataSet, chartDetail.getChart_type(), queryDate, beginDate,
                    endDate, params);
            break;
        case "24hour": // 当前时间24小时内的数据
            data = twentyFourHourData(chartDataSet, queryDate, params);
            break;
        default:
            break;
        }
        return data;
    }

    /**
     * 查询当前时间24小时内的数据
     * 
     * @param chartDataSet
     * @return
     */
    private List<ChartInfo> twentyFourHourData(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        String endDate = DateUtil.getMinusTime(DateUtil.C_TIME_PATTON_DEFAULT, 0, null);
        String beginDate = DateUtil.getMinusTime(DateUtil.C_TIME_PATTON_DEFAULT, 1, null);
        String tablename = chartDataSet.getTable_name();
        switch (tablename) {
        case "STATIS_DATA_DAY":
            // data = oneDayDataStatisDay(chartDataSet, queryDate, params);
            break;
        default:
            data = twentyFourHourDefault(chartDataSet, queryDate, beginDate, endDate, params);
            break;
        }
        return data;
    }

    private List<ChartInfo> twentyFourHourDefault(MdChartDataSet chartDataSet, String queryDate,
            String beginDate, String endDate, Map<String, Object> params) {
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNams = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        if (!ToolsUtils.StringIsNull(metricNams)) {
            metricNameArr = metricNams.split(",");
        }
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        String tablename = chartDataSet.getTable_name();
        for (int i = 0; i < metricIdArr.length; i++) {
            List<ChartData> list = new ArrayList<ChartData>();
            // 获取当天24小时内的数据
            chartDataSet.setTable_name(tablename);
            String curtablename = createTable(chartDataSet, params);
            chartDataSet.setTable_name(curtablename);
            List<ChartData> curlist = metricDataDAO.getLineData(chartDataSet, metricIdArr[i],
                    queryDate, beginDate, endDate, params);
            // 获取前一天24小时内的数据
            String perqueryDate = DateUtil.getMinusTime(CUR_DATE, 1, null);
            params.put("queryDate", perqueryDate);
            chartDataSet.setTable_name(tablename);
            String pertablename = createTable(chartDataSet, params);
            chartDataSet.setTable_name(pertablename);
            List<ChartData> perlist = metricDataDAO.getLineData(chartDataSet, metricIdArr[i],
                    queryDate, beginDate, endDate, params);
            params.put("queryDate", queryDate);
            list.addAll(perlist);
            list.addAll(curlist);
            long time_stamp_difference = 300;
            switch (chartDataSet.getChart_interval()) {
            case "5min":
                time_stamp_difference = (long) 5 * 60;
                break;
            case "10min":
                time_stamp_difference = (long) 10 * 60;
                break;
            case "15min":
                time_stamp_difference = (long) 15 * 60;
                break;
            case "30min":
                time_stamp_difference = (long) 30 * 60;
                break;
            case "1hour":
                time_stamp_difference = (long) 60 * 60;
                break;
            case "day":
                time_stamp_difference = (long) 24 * 60 * 60;
                break;
            default:
                break;
            }
            List<String> days = new ArrayList<String>();
            List<String> timeStamp = TimeControl.getCutTimeStamp(new Date(), 1,
                    time_stamp_difference);
            // 根据指标ID查出采集周期ID
            Integer cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[i]);
            for (int j = timeStamp.size() - 1; j >= 0; j--) {
                String cron = CommonInit.getCollCycleCronById(cycleId);
                String[] cronArr = cron.split(" ");
                switch (chartDataSet.getChart_interval()) {
                case "1hour":
                    days.add(timeStamp.get(j).substring(0, 14) + fillParam(cronArr[1]));
                    break;
                case "day":
                    days.add(timeStamp.get(j).substring(0, 11) + fillParam(cronArr[2]) + ":"
                            + fillParam(cronArr[1]));
                    break;
                default:
                    days.add(timeStamp.get(j).substring(0, 16));
                    break;
                }
            }
            lineDataProduce(data, completionDaysData(list, days), metricIdArr[i], i, metricNameArr);
        }
        return data;
    }

    private static String fillParam(String param) {
        if (param.length() == 1) {
            return "0" + param;
        }
        return param;
    }

    /**
     * 查询从凌晨到现在 折线图
     * 
     * @param chartDataSet
     * @return
     */
    private List<ChartInfo> oneDayData(MdChartDataSet chartDataSet, MdChartDetail mdChartDetail,
            String queryDate, Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        String tablename = chartDataSet.getTable_name();
        switch (tablename) {
        case "STATIS_DATA_DAY": // 从凌晨到现在
            data = oneDayDataStatisDay(chartDataSet, queryDate, params);
            break;
        case "METRIC_DATA_SINGLE":
            data = oneDayDataSingle(chartDataSet, mdChartDetail, queryDate, params);
            break;
        default:
            data = oneDayDataDefault(chartDataSet, mdChartDetail, queryDate, params);
            break;
        }
        return data;
    }

    private List<ChartInfo> oneDayDataDefault(MdChartDataSet chartDataSet,
            MdChartDetail mdChartDetail, String queryDate, Map<String, Object> params) {
        String tablename = createTable(chartDataSet, params);
        chartDataSet.setTable_name(tablename);
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNams = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        switch (mdChartDetail.getChart_type()) {
        case "tableline":
            List<ChartData> tableNameDataList = metricDataDAO.getTableNameData(chartDataSet,
                    metricIdArr[0], queryDate, params);
            if (tableNameDataList.size() == 0) {
                tablelineDataProduce(data, completionData(new ArrayList<ChartData>(), chartDataSet,
                        queryDate, metricIdArr[0]), mdChartDetail.getChart_title());
            } else {
                for (int i = 0; i < tableNameDataList.size(); i++) {
                    List<ChartData> list = metricDataDAO.getTableLineData(chartDataSet,
                            metricIdArr[0], tableNameDataList.get(i).getMark(),
                            tableNameDataList.get(i).getValue(), queryDate, null, null, params);
                    tablelineDataProduce(data,
                            completionData(list, chartDataSet, queryDate, metricIdArr[0]),
                            tableNameDataList.get(i).getMark());
                }
            }
            break;
        default:
            if (!ToolsUtils.StringIsNull(metricNams)) {
                metricNameArr = metricNams.split(",");
            }
            for (int i = 0; i < metricIdArr.length; i++) {
                List<ChartData> list = metricDataDAO.getLineData(chartDataSet, metricIdArr[i],
                        queryDate, null, null, params);
                lineDataProduce(data, completionData(list, chartDataSet, queryDate, metricIdArr[i]),
                        metricIdArr[i], i, metricNameArr);
            }
            break;
        }
        return data;
    }

    private List<ChartInfo> oneDayDataSingle(MdChartDataSet chartDataSet,
            MdChartDetail mdChartDetail, String queryDate, Map<String, Object> params) {
        String tablename = createTable(chartDataSet, params);
        chartDataSet.setTable_name(tablename);
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNams = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        switch (mdChartDetail.getChart_type()) {
        case "tableline":
            List<ChartData> tableNameDataList = metricDataDAO.getTableNameDataSingle(chartDataSet,
                    metricIdArr[0], queryDate, params);
            if (tableNameDataList.size() == 0) {
                tablelineDataProduce(data, completionData(new ArrayList<ChartData>(), chartDataSet,
                        queryDate, metricIdArr[0]), mdChartDetail.getChart_title());
            } else {
                for (int i = 0; i < tableNameDataList.size(); i++) {
                    List<ChartData> list = metricDataDAO.getTableLineDataSingle(chartDataSet,
                            metricIdArr[0], tableNameDataList.get(i).getMark(),
                            tableNameDataList.get(i).getValue(), queryDate, null, null, params);
                    tablelineDataProduce(data,
                            completionData(list, chartDataSet, queryDate, metricIdArr[0]),
                            tableNameDataList.get(i).getMark());
                }
            }
            break;
        default:
            if (!ToolsUtils.StringIsNull(metricNams)) {
                metricNameArr = metricNams.split(",");
            }
            for (int i = 0; i < metricIdArr.length; i++) {
                List<ChartData> list = metricDataDAO.getLineData(chartDataSet, metricIdArr[i],
                        queryDate, null, null, params);
                lineDataProduce(data, completionData(list, chartDataSet, queryDate, metricIdArr[i]),
                        metricIdArr[i], i, metricNameArr);
            }
            break;
        }
        return data;
    }

    private List<ChartInfo> oneDayDataStatisDay(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        String tablename = createTable(chartDataSet, params);
        chartDataSet.setTable_name(tablename);
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNams = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        if (!ToolsUtils.StringIsNull(metricNams)) {
            metricNameArr = metricNams.split(",");
        }
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        for (int i = 0; i < metricIdArr.length; i++) {
            List<ChartData> list = metricDataDAO.getLineDataFromStatisDay(chartDataSet,
                    metricIdArr[i], queryDate, params);
            lineDataProduce(data, completionData(list, chartDataSet, queryDate, metricIdArr[i]),
                    metricIdArr[i], i, metricNameArr);
        }
        return data;
    }

    /**
     * 数据补全
     * 
     * @param list
     * @param chartDataSet
     * @return
     */
    private List<ChartData> completionData(List<ChartData> list, MdChartDataSet chartDataSet,
            String queryDate, String metricId) {
        // 根据指标ID查出采集周期ID
        Integer cycleId = CommonInit.getCycleIdFromMetric(metricId);
        if (cycleId == null) {
            LOG.error("metricId:" + metricId + ",chart_name:" + chartDataSet.getChart_name()
                    + ",cycleId is null");
            return list;
        }
        if (ToolsUtils.ListIsNull(list))
            return list;
        List<ChartData> data = new ArrayList<ChartData>();
        List<String> allTimes = TimeControl.getCycleTimeOneDayHHmm(chartDataSet.getChart_interval(),
                "HH:mm", queryDate, cycleId);
        boolean flag = false;
        for (String mark : allTimes) {
            for (ChartData chartData : list) {
                if (mark.equals(chartData.getMark())) {
                    flag = true;
                    data.add(chartData);
                    break;
                }
            }
            if (!flag) {
                data.add(new ChartData(mark, "-"));
            }
            flag = false;
        }
        return data;
    }

    private void lineDataProduce(List<ChartInfo> data, List<?> list, String metricId, int index,
            String[] metricNameArr) {
        ChartInfo chartInfo = new ChartInfo();
        // 对 List<ChartData> list 补全数据
        chartInfo.setData(list);
        // 指标名称
        if (metricNameArr != null && metricNameArr.length > index) {
            chartInfo.setLegend(metricNameArr[index]);
        } else {
            chartInfo.setLegend(CommonInit.getMetricNameByMetricId(metricId));
        }
        data.add(chartInfo);
    }

    private void tablelineDataProduce(List<ChartInfo> data, List<?> list, String name) {
        ChartInfo chartInfo = new ChartInfo();
        // 对 List<ChartData> list 补全数据
        chartInfo.setData(list);
        chartInfo.setLegend(name);
        data.add(chartInfo);
    }

    /**
     * 查询时间段（请求参数需带开始及结束时间）
     * 
     * @param chartDataSet
     * @param
     * @return
     */
    private List<ChartInfo> slottime(MdChartDataSet chartDataSet, String chartType,
            String queryDate, String beginDate, String endDate, Map<String, Object> params) {
        if ("line".equals(chartType)) {
            // lineData(chartDataSet, queryDate, beginDate, endDate, params);
            LOG.info("create slottime LineData");
        }
        return null;
    }

    /**
     * 生成表名
     * 
     * @param chartDataSet
     * @param
     * @return
     */
    private String createTable(MdChartDataSet chartDataSet, Map<String, Object> params) {
        String table_name = chartDataSet.getTable_name();
        // switch (table_name) {
        // case "METRIC_DATA_SINGLE":
        // table_name = metricTable(table_name, params);
        // break;
        // case "METRIC_DATA_MULTI":
        // table_name = metricTable(table_name, params);
        // break;
        // default:
        // table_name = metricTable(table_name, params);
        // break;
        // }
        table_name = metricTable(table_name, params);
        params.put("table_name", table_name);
        return table_name;
    }

    public String metricTable(String table_name, Map<String, Object> params) {
        String queryDate = (String) params.get("queryDate");
        if (ToolsUtils.StringIsNull(queryDate)) {
            queryDate = TimeTools.getCurrentTime(CUR_DATE);
        }
        params.put("queryDate", queryDate);
        table_name = table_name + "_" + getMonthDay(queryDate);
        return table_name;
    }

    private String getMonthDay(String date) {
        if (date == null || date.length() < 10) {
            date = TimeTools.getCurrentTime(CUR_DATE);
        }
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return month + "_" + day;
    }

    /**
     * 查询最近一条数据 规则:先查询最新周期的数据，如果未空，查询上个周期数据
     * 
     * @param params
     * @return
     */
    public List<ChartInfo> getDataRecent(Map<String, Object> params) {
        List<ChartInfo> data = null;
        String chart_name = (String) params.get("chart_name");
        MdChartDataSet chartDataSet = CommonInit.getChartDataSetByChartName(chart_name);
        MdChartDetail chartDetail = CommonInit.getChartDetailByChartName(chart_name);
        // 根据 chartDataSet 配置组装数据
        if (chartDataSet != null && chartDetail != null)
            data = assemblyRecentData(chartDataSet, chartDetail, params);
        return data;
    }

    /**
     * 查询最近一条数据 规则:先查询最新周期的数据，如果未空，查询上个周期数据
     * 
     * @param params
     * @return
     */
    public List<ChartInfo> getDataStaticSql(Map<String, Object> params) {
        List<ChartInfo> data = null;
        String chart_name = (String) params.get("chart_name");
        MdChartDataSet chartDataSet = CommonInit.getChartDataSetByChartName(chart_name);
        MdChartDetail chartDetail = CommonInit.getChartDetailByChartName(chart_name);
        // 根据 chartDataSet 配置组装数据
        if (chartDataSet != null && chartDetail != null)
            data = assemblyStaticSqlData(chartDataSet, params);
        return data;
    }

    private List<ChartInfo> assemblyStaticSqlData(MdChartDataSet chartDataSet,
            Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        List<ChartData> list = null;
        // 根据指标ID查出采集周期ID
        list = recentStaticSqlData(chartDataSet, params);
        ChartInfo chartInfo = new ChartInfo();
        // 对 List<ChartData> list 补全数据
        chartInfo.setData(list);
        data.add(chartInfo);

        return data;
    }

    public List<String> getMajorMetric(Map<String, Object> params) {
        List<String> dataList = new ArrayList<String>();
        String metric_names = (String) params.get("metric_names");
        String[] metricNameArr = StringUtils.split(metric_names, ",");
        for (String chartName : metricNameArr) {
            MdChartDataSet chartDataSet = CommonInit.getChartDataSetByChartName(chartName);
            // 根据 chartDataSet 配置组装数据
            if (chartDataSet != null) {
                String majorMetric = assemblyMetricData(chartDataSet, params, chartName);
                dataList.add(majorMetric);
            }
        }
        return dataList;
    }

    /**
     * 甘肃移动首页展示
     * 
     * @param params
     * @return
     */
    public String getGscmMetric(Map<String, Object> params) {
        String authenRespRate = null;
        String metric_names = (String) params.get("metric_names");
        String[] metricNameArr = StringUtils.split(metric_names, ",");
        for (String chartName : metricNameArr) {
            MdChartDataSet chartDataSet = CommonInit.getChartDataSetByChartName(chartName);
            // 根据 chartDataSet 配置组装数据
            if (chartDataSet != null) {
                authenRespRate = assemblyMetricData(chartDataSet, params, chartName);
            }
        }
        if (authenRespRate == null) {
            authenRespRate = "0";
        }
        return authenRespRate + "%";
    }

    private String assemblyMetricData(MdChartDataSet chartDataSet, Map<String, Object> params,
            String chartName) {
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricId = StringUtils.join(metricIdArr, "','");
        // 根据指标ID查出采集周期ID
        Integer cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[0]);
        if (cycleId == null) {
            commoninit.refreshMetriclist();
            cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[0]);
        }
        LOG.info("chartName : {} , cycleId is {}", chartDataSet.getChart_name(), cycleId);
        String majorMetric = recentMetricTable(chartDataSet, metricId, params, cycleId, chartName);
        return majorMetric;
    }

    private String recentMetricTable(MdChartDataSet chartDataSet, String metricId,
            Map<String, Object> params, Integer cycleId, String chartName) {
        String interval = chartDataSet.getChart_interval();
        String queryDate = TimeControl.getCycleTime(interval, 0, cycleId, null);
        MultiData multiData = recentMetricMultiData(chartDataSet, metricId, queryDate, params);
        int num = -1;
        while (multiData == null) {
            // 数据为空，依次查询上个周期数据
            queryDate = TimeControl.getCycleTime(interval, num, cycleId, null);
            multiData = recentMetricMultiData(chartDataSet, metricId, queryDate, params);
            num--;
            if (multiData != null) {
                LOG.info("chart name : {}, multiData is not null", chartName);
                break;
            }
        }
        // 组装数据
        String majorMetric = assembleData(chartDataSet, multiData);
        return majorMetric;
    }

    public String assembleData(MdChartDataSet chartDataSet, MultiData multiData) {
        String value = multiData.getMvalue() == null ? "0" : multiData.getMvalue();
        String metricName = chartDataSet.getMetric_names();
        String majorMetric = StringUtils.replace(metricName, "value", value);
        if (StringUtils.contains(metricName, "online_rate")) {
            String onlineRate = onlineRate(value);
            majorMetric = StringUtils.replace(majorMetric, "online_rate", onlineRate);
        }
        LOG.info("chart name : {}, major metric value is : {}", chartDataSet.getChart_name(),
                majorMetric);
        return majorMetric;
    }

    public String onlineRate(String value) {
        String useRate = "";
        try {
            Integer license = CommonInit.BUSCONF.onlineUserLicense();
            Integer userOnline = Integer.parseInt(value);
            DecimalFormat df = new DecimalFormat("0.00");
            useRate = df.format((float) userOnline * 100 / license);
        } catch (Exception e) {
            LOG.error("user online parse error, reason {} error:{}", e.getMessage(), e);
            useRate = "0";
        }
        return useRate + "%";
    }

    private MultiData recentMetricMultiData(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        params.put("queryDate", queryDate);
        createTable(chartDataSet, params);
        MultiData list = new MultiData();
        switch (chartDataSet.getChart_name()) {
        case "host_file_system":
            break;
        default:
            // 用marktype 判断
            list = getMetricDataByMarkType(chartDataSet, metricId, queryDate, params);
            break;
        }
        return list;
    }

    private MultiData getMetricDataByMarkType(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        MultiData list = new MultiData();
        switch (chartDataSet.getMarktype()) {
        case "filetable":
            break;
        default:
            // 用marktype 判断
            list = recentMetricMultiDataByDataTable(chartDataSet, metricId, queryDate, params);
            break;
        }
        return list;
    }

    private MultiData recentMetricMultiDataByDataTable(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        MultiData list = new MultiData();
        switch (chartDataSet.getTable_name()) {
        case "METRIC_DATA_MULTI":
            // 多维度
            list = metricDataDAO.recentMetricDataByTableMulti(chartDataSet, metricId, queryDate,
                    params);
            break;
        case "STATIS_DATA_DAY":
            list = metricDataDAO.recentMetricTableByTableStatis(chartDataSet, metricId, queryDate,
                    params);
            break;
        default:
            break;
        }
        return list;
    }

    private List<ChartData> recentStaticSqlData(MdChartDataSet chartDataSet,
            Map<String, Object> params) {
        List<ChartData> list;
        list = metricDataDAO.getStaticSqlData(chartDataSet, params);
        return list;
    }

    private List<ChartInfo> assemblyRecentData(MdChartDataSet chartDataSet,
            MdChartDetail chartDetail, Map<String, Object> params) {
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricNameArr = null;
        if (!ToolsUtils.StringIsNull(chartDataSet.getMetric_names())) {
            metricNameArr = chartDataSet.getMetric_names().split(",");
        }

        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        List<ChartData> list = null;
        // 根据指标ID查出采集周期ID
        Integer cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[0]);
        if (cycleId == null) {
            commoninit.refreshMetriclist();
            cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[0]);
        }
        if (cycleId == null)
            LOG.error(chartDataSet.getChart_name() + " cycleId is null");
        switch (chartDetail.getChart_type()) {
        case "table":
        case "tablelayer":
            recentDataTable(data, chartDataSet, metricIdArr[0], params, cycleId);
            break;
        default:
            String queryDate = (String) params.get("queryDate");
            Boolean queryDateExist = false;
            if (null != queryDate) {
                queryDateExist = true;
                switch (chartDataSet.getChart_interval()) {
                case "1min":
                    queryDate = DateUtil.getCycleDateTimeToFormatter(
                            DateUtil.strToDate(queryDate, DEFAULT_FORMAT), CycleType.ONE_MINUTE,
                            DEFAULT_FORMAT_MIN);
                    break;
                default:
                    queryDate = DateUtil.getCycleDateTimeToFormatter(
                            DateUtil.strToDate(queryDate, DEFAULT_FORMAT), CycleType.FIVE_MINUTES,
                            DEFAULT_FORMAT_MIN);
                }
            } else {
                queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), 0, cycleId,
                        null);
            }
            list = recentData(chartDataSet, metricIdArr[0], queryDate, params);
            if (ToolsUtils.ListIsNull(list) && !queryDateExist) {
                queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), -1, cycleId,
                        null);
                list = recentData(chartDataSet, metricIdArr[0], queryDate, params);
            }
            lineDataProduce(data, list, metricIdArr[0], 0, metricNameArr);
            break;
        }
        return data;
    }

    private List<ChartData> recentData(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        params.put("queryDate", queryDate);
        String tablename = chartDataSet.getTable_name();
        createTable(chartDataSet, params);
        List<ChartData> list = new ArrayList<ChartData>();
        switch (tablename) {
        case "STATIS_DATA_DAY":
            list = metricDataDAO.getRecentDataFromStatisDay(chartDataSet, metricId, queryDate,
                    params);
            break;
        default:
            list = metricDataDAO.getRecentData(chartDataSet, metricId, queryDate, params);
            break;
        }
        return list;
    }

    private void recentDataTable(List<ChartInfo> data, MdChartDataSet chartDataSet, String metricId,
            Map<String, Object> params, Integer cycleId) {
        String queryDate = (String) params.get("queryDate");
        Boolean queryDateExist = false;
        if (null != queryDate) {
            queryDateExist = true;
            switch (chartDataSet.getChart_interval()) {
            case "1min":
                queryDate = DateUtil.getCycleDateTimeToFormatter(
                        DateUtil.strToDate(queryDate, DEFAULT_FORMAT), CycleType.ONE_MINUTE,
                        DEFAULT_FORMAT_MIN);
                break;
            default:
                queryDate = DateUtil.getCycleDateTimeToFormatter(
                        DateUtil.strToDate(queryDate, DEFAULT_FORMAT), CycleType.FIVE_MINUTES,
                        DEFAULT_FORMAT_MIN);
            }
        } else {
            queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), 0, cycleId,
                    null);
        }
        List<MultiData> multiDatas = recentTableMultiData(chartDataSet, metricId, queryDate,
                params);
        if (ToolsUtils.ListIsNull(multiDatas) && !queryDateExist) {
            // 数据未空，查询上个周期数据并且不是精确查询日期
            queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), -1, cycleId,
                    null);
            multiDatas = recentTableMultiData(chartDataSet, metricId, queryDate, params);
        }
        // 组装表格数据
        produceTableData(data, chartDataSet, multiDatas);
    }

    /**
     * 获取表格数据
     * 
     * @param chartDataSet
     * @param metricId
     * @param queryDate
     * @param params
     * @return
     */
    private List<MultiData> recentTableMultiData(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        params.put("queryDate", queryDate);
        createTable(chartDataSet, params);
        List<MultiData> list = null;
        switch (chartDataSet.getChart_name()) {
        case "host_file_system":
            list = metricDataDAO.getRecentDataTable(chartDataSet, metricId, queryDate, params);
            break;
        case "host_process_table":
            list = metricDataDAO.getRcentTableProcessData(chartDataSet, metricId, queryDate,
                    params);
            break;
        default:
            // 用marktype 判断
            list = getTableDataByMarkType(chartDataSet, metricId, queryDate, params);
            break;
        }
        return list;
    }

    private List<MultiData> getTableDataByMarkType(MdChartDataSet chartDataSet, String metricId,
            String queryDate, Map<String, Object> params) {
        List<MultiData> list = null;
        switch (chartDataSet.getMarktype()) {
        case "filetable":
            list = metricDataDAO.getRecentDataTable(chartDataSet, metricId, queryDate, params);
            break;
        default:
            // 用marktype 判断
            list = recentTableMultiDataByDataTable(chartDataSet, metricId, queryDate, params);
            break;
        }
        return list;
    }

    private List<MultiData> recentTableMultiDataByDataTable(MdChartDataSet chartDataSet,
            String metricId, String queryDate, Map<String, Object> params) {
        List<MultiData> list = null;
        switch (chartDataSet.getTable_name()) {
        case "METRIC_DATA_SINGLE":
            list = metricDataDAO.recentTableMultiDataByDataTableSingle(chartDataSet, metricId,
                    queryDate, params);
            break;
        case "METRIC_DATA_MULTI":
            // 多维度
            list = metricDataDAO.recentTableMultiDataByDataTableMulti(chartDataSet, metricId,
                    queryDate, params);
            break;
        case "STATIS_DATA_DAY":
            list = metricDataDAO.getRecentDataTable(chartDataSet, metricId, queryDate, params);
            break;
        default:
            break;
        }
        return list;
    }

    /**
     * 组装成table数据
     * 
     * @param data
     * @param list
     * @param chartDataSet
     */
    private void produceTableData(List<ChartInfo> data, MdChartDataSet chartDataSet,
            List<MultiData> list) {
        // 获取表头
        String tableFieldnames = chartDataSet.getMetric_names();
        if (!ToolsUtils.StringIsNull(tableFieldnames)) {
            String[] fieldNames = tableFieldnames.split(",");
            ChartInfo chartInfo = new ChartInfo();
            List<ChartTableData> chartTableDatalist = new ArrayList<ChartTableData>();
            for (String string : fieldNames) {
                ChartTableData cTableData = new ChartTableData(string);
                chartTableDatalist.add(cTableData);
            }
            chartInfo.setData(chartTableDatalist);
            data.add(chartInfo);
        }
        // 组装数据
        if (!ToolsUtils.ListIsNull(list)) {
            // 获取字段名称
            String tableField = chartDataSet.getTablefields();
            String[] fieldArr = tableField.split(",");
            produceTableFiledData(chartDataSet, data, list, fieldArr);
        }
    }

    private void produceTableFiledData(MdChartDataSet chartDataSet, List<ChartInfo> data,
            List<MultiData> list, String[] fieldArr) {
        switch (chartDataSet.getChart_name()) {
        case "host_file_system":
            produceTableFiledDataFileSystem(data, list, fieldArr);
            break;
        default:
            produceTableFiledDataByMarkType(chartDataSet, data, list, fieldArr);
            break;
        }
    }

    private void produceTableFiledDataByMarkType(MdChartDataSet chartDataSet, List<ChartInfo> data,
            List<MultiData> list, String[] fieldArr) {
        switch (chartDataSet.getMarktype()) {
        case "filetable":
            produceTableFiledDataFileSystem(data, list, fieldArr);
            break;
        default:
            produceTableFiledData(data, list, fieldArr);
            break;
        }
    }

    private void produceTableFiledData(List<ChartInfo> data, List<MultiData> list,
            String[] fieldArr) {
        for (MultiData multiData : list) {
            ChartInfo chartInfo = new ChartInfo();
            List<ChartTableData> chartTableDatalist = new ArrayList<ChartTableData>();
            for (String filed : fieldArr) {
                switch (filed) {
                case "attr1":
                    chartTableDatalist.add(new ChartTableData(multiData.getAttr1()));
                    break;
                case "attr2":
                    chartTableDatalist.add(new ChartTableData(multiData.getAttr2()));
                    break;
                case "attr3":
                    chartTableDatalist.add(new ChartTableData(multiData.getAttr3()));
                    break;
                case "attr4":
                    chartTableDatalist.add(new ChartTableData(multiData.getAttr4()));
                    break;
                case "mvalue":
                    chartTableDatalist.add(new ChartTableData(multiData.getMvalue()));
                    break;
                case "itemvalue":
                    chartTableDatalist.add(new ChartTableData(multiData.getItemvalue()));
                    break;
                default:
                    break;
                }
            }
            chartInfo.setData(chartTableDatalist);
            data.add(chartInfo);
        }
    }

    private void produceTableFiledDataFileSystem(List<ChartInfo> data, List<MultiData> list,
            String[] fieldArr) {
        // 判断 list 中有几个 ATTR1 值
        List<String> attr1list = new ArrayList<String>();
        for (MultiData multiData : list) {
            if (!attr1list.contains(multiData.getAttr1()))
                attr1list.add(multiData.getAttr1());
        }
        for (String attr1 : attr1list) {
            ChartInfo chartInfo = new ChartInfo();
            List<ChartTableData> chartTableDatalist = new ArrayList<ChartTableData>();
            // ATTR1,ATTR2=totalSize,ATTR2=usedSize,ATTR2=availableSize,ATTR2=useRate,ATTR2=mountOn
            for (String filed : fieldArr) {
                switch (filed) {
                case "attr1":
                    chartTableDatalist.add(new ChartTableData(attr1));
                    break;
                default:
                    // attr1 filed 组合 查询 List<MultiData> list 中的值
                    String value = getMultiValueByAttr1AndAttr2(attr1, filed, list);
                    chartTableDatalist.add(new ChartTableData(value));
                    break;
                }
            }
            chartInfo.setData(chartTableDatalist);
            data.add(chartInfo);
        }
    }

    private String getMultiValueByAttr1AndAttr2(String attr1, String attr2, List<MultiData> list) {
        String value = "-";
        if (ToolsUtils.StringIsNull(attr2)) {
            // 适合进程一类
            for (MultiData multiData : list) {
                if (attr1.equals(multiData.getAttr1()))
                    return multiData.getMvalue();
            }
        }

        if (attr2.contains("=")) {
            String[] arr = attr2.split("=");
            attr2 = arr[1];
        }
        for (MultiData multiData : list) {
            if (attr1.equals(multiData.getAttr1()) && attr2.equals(multiData.getAttr2()))
                return multiData.getMvalue();
        }
        return value;
    }

    public String[][] exportMetricDataLineData(Map<String, Object> params) {
        String[][] result = null;
        return result;
    }

    private String[] getMetricidsByMetricIdentity(String metricIdentitys) {
        String[] metricIdentityArr = metricIdentitys.split(",");
        String[] metricIdArr = new String[metricIdentityArr.length];
        for (int i = 0; i < metricIdentityArr.length; i++) {
            String metricId = CommonInit.getMetricIdByMetricIdentity(metricIdentityArr[i]);
            if (ToolsUtils.StringIsNull(metricId)) {
                metricId = metricIdentityArr[i];
            }
            metricIdArr[i] = metricId;
        }
        return metricIdArr;
    }

    /**
     * AIS server
     * 
     * @param chart_name
     * @param metricid
     * @param attr
     * @return
     */
    public List<ChartData> getDataRecentByChartNameMetricIdAttr(
            AisGroupMetricModel groupMetricModel, Map<String, Object> params) {
        List<ChartData> data = null;
        MdChartDataSet chartDataSet = CommonInit
                .getChartDataSetByChartName(groupMetricModel.getChart_name());
        MdChartDetail chartDetail = CommonInit
                .getChartDetailByChartName(groupMetricModel.getChart_name());
        // 根据 chartDataSet 配置组装数据
        if (chartDataSet != null && chartDetail != null)
            data = assemblyRecentDataForAis(params, chartDataSet, chartDetail,
                    groupMetricModel.getMetric_id(), groupMetricModel.getAttr());
        return data;
    }

    private List<ChartData> assemblyRecentDataForAis(Map<String, Object> params,
            MdChartDataSet chartDataSet, MdChartDetail chartDetail, String metricid, String attr) {
        List<ChartData> list = null;
        // 根据指标ID查出采集周期ID
        Integer cycleId = CommonInit.getCycleIdFromMetric(metricid);
        if (cycleId == null) {
            commoninit.refreshMetriclist();
            cycleId = CommonInit.getCycleIdFromMetric(metricid);
        }
        if (cycleId == null) {
            LOG.error(chartDataSet.getChart_name() + " cycleId is null");
        }

        String queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), 0, cycleId,
                null);
        params.put("queryDate", queryDate);
        params.put("attr", attr);
        list = recentData(chartDataSet, metricid, queryDate, params);
        if (ToolsUtils.ListIsNull(list)) {
            queryDate = TimeControl.getCycleTime(chartDataSet.getChart_interval(), -1, cycleId,
                    null);
            list = recentData(chartDataSet, metricid, queryDate, params);
        }
        return list;
    }

    // 在线用户数首页、地市对应的告警信息
    public List<CompareAlarm> getOnlineDataHomeMap(Map<String, Object> params) {
        String province = "";
        if (params.get("province") != null) {
            province = params.get("province").toString();
        }
        List<ChartInfo> chartInfos = getDataRecent(params);
        List<CompareAlarm> list = new ArrayList<CompareAlarm>();
        CompareAlarm compareAlarm = new CompareAlarm();
        // 查询告警地市类告警
        List<AlarmMessage> data = new ArrayList<AlarmMessage>();
        if (!ToolsUtils.ListIsNull(chartInfos)) {
            MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
            MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
            alarmInfo.setConfirm_name(user.getAdmin());
            List<MdAlarmInfo> alarmInfoList = alarmService.getAlarmListWithIndexConfirm(alarmInfo);
            AlarmMessage alarmMessage = null;
            switch (province) {
            case Constant.PROVINCE_SXCM:
                for (int i = 0; i < chartInfos.size(); i++) {
                    ChartInfo chartInfo = chartInfos.get(i);
                    ChartTableData mark = (ChartTableData) chartInfo.getData().get(0);
                    ChartTableData value = (ChartTableData) chartInfo.getData().get(1);
                    if (i == 0) {
                        compareAlarm.setTitle(value.getValue());
                    } else {
                        alarmMessage = new AlarmMessage(mark.getValue(), value.getValue());
                        addAlarmInfo(alarmMessage, alarmInfoList);
                        data.add(alarmMessage);
                    }
                }
                break;
            default:
                List<ChartData> chartDatas = (List<ChartData>) chartInfos.get(0).getData();
                for (ChartData ChartData : chartDatas) {
                    alarmMessage = new AlarmMessage(ChartData.getMark(), ChartData.getValue());
                    addAlarmInfo(alarmMessage, alarmInfoList);
                    data.add(alarmMessage);
                }
                compareAlarm.setTitle(chartInfos.get(0).getLegend());
                compareAlarm.setGroup(chartInfos.get(0).getGroup());
                break;
            }
            compareAlarm.setData(data);
        }
        list.add(compareAlarm);
        return list;
    }

    private void addAlarmInfo(AlarmMessage alarmMessage, List<MdAlarmInfo> alarmInfoList) {
        if (!ToolsUtils.ListIsNull(alarmInfoList)) {
            int alarmNum = 0;
            List<MessageData> messageData = new ArrayList<MessageData>();
            for (MdAlarmInfo mdAlarmInfo : alarmInfoList) {
                if (mdAlarmInfo.getDimension_name().contains(alarmMessage.getMark())) {
                    alarmNum++;
                    messageData.add(new MessageData(
                            mdAlarmInfo.getDimension_name() + ":" + mdAlarmInfo.getAlarmmsg()));
                }
            }
            if (alarmNum > 0) {
                alarmMessage.setAlarmNum(alarmNum);
                alarmMessage.setData(messageData);
            }
        }
    }

    /**
     * 查询多天数据，包含 queryDate 日期 折线图
     * 
     * @param chartDataSet
     * @return
     */
    private List<ChartInfo> multipleDayData(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        // String tablename = chartDataSet.getTable_name();
        // switch (tablename) {
        // case "STATIS_DATA_DAY": // 从凌晨到现在
        // data = multipleDayDataDefault(chartDataSet, queryDate, params);
        // break;
        // default:
        // data = multipleDayDataDefault(chartDataSet, queryDate, params);
        // break;
        // }
        data = multipleDayDataDefault(chartDataSet, queryDate, params);
        return data;
    }

    private List<ChartInfo> multipleDayDataDefault(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNams = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        if (!ToolsUtils.StringIsNull(metricNams)) {
            metricNameArr = metricNams.split(",");
        }
        List<String> days = multipleDays(queryDate);
        List<ChartInfo> data = new ArrayList<ChartInfo>();
        for (int i = 0; i < metricIdArr.length; i++) {
            List<ChartData> list = metricDataDAO.getLineMultipleDayData(chartDataSet,
                    metricIdArr[i], days, params);
            lineDataProduce(data, completionDaysData(list, days), metricIdArr[i], i, metricNameArr);
        }
        return data;
    }

    /**
     * 天数据数据补全
     * 
     * @param list
     * @param chartDataSet
     * @return
     */
    private List<ChartData> completionDaysData(List<ChartData> list, List<String> days) {
        if (ToolsUtils.ListIsNull(list))
            return list;
        List<ChartData> data = new ArrayList<ChartData>();
        boolean flag = false;
        String mark = null;
        for (int i = days.size() - 1; i >= 0; i--) {
            mark = days.get(i);
            for (ChartData chartData : list) {
                if (mark.equals(chartData.getMark())) {
                    flag = true;
                    chartData.setMark(chartData.getMark());// .substring(5,
                                                           // chartData.getMark().length())
                    data.add(chartData);
                    break;
                }
            }
            if (!flag) {
                // mark=mark.substring(5, mark.length());
                data.add(new ChartData(mark, "-"));
            }
            flag = false;
        }
        return data;
    }

    private List<String> multipleDays(String queryDate) {
        List<String> days = new ArrayList<String>();
        DateTools DATATOOL = new DateTools(CUR_DATE);
        if (ToolsUtils.StringIsNull(queryDate)) {
            queryDate = DATATOOL.getCurrentDate();
        }
        // 日期网球推n天数据
        int n = CommonInit.BUSCONF.getQueryDaysNum();
        for (int i = 0; i < n; i++) {
            days.add(DATATOOL.getOffsetDay(-1 * i, queryDate));
        }
        return days;
    }

    /**
     * 查询从1月到当前月 折线图
     * 
     * @param chartDataSet
     * @param queryDate
     * @param params
     * @return
     */
    private List<ChartInfo> oneMonthData(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<>();
        String tableName = chartDataSet.getTable_name();
        switch (tableName) {
        case "STATIS_DATA_DAY":
            break;
        default:
            data = oneMonthDataDefault(chartDataSet, queryDate, params);
            break;
        }
        return data;
    }

    private List<ChartInfo> oneMonthDataDefault(MdChartDataSet chartDataSet, String queryDate,
            Map<String, Object> params) {
        List<ChartInfo> data = monthChartInfo(chartDataSet, params);
        return data;
    }

    private List<ChartInfo> monthChartInfo(MdChartDataSet chartDataSet,
            Map<String, Object> params) {
        List<ChartInfo> data = new ArrayList<>();
        String tableName = chartDataSet.getTable_name();
        String metricIds = chartDataSet.getMetric_ids();
        String[] metricIdArr = getMetricidsByMetricIdentity(metricIds);
        String metricNames = chartDataSet.getMetric_names();
        String[] metricNameArr = null;
        if (!StringUtils.isBlank(metricNames)) {
            metricNameArr = metricNames.split(",");
        }
        for (int i = 0; i < metricIdArr.length; i++) {
            Integer cycleId = CommonInit.getCycleIdFromMetric(metricIdArr[i]);
            if (cycleId == null)
                LOG.error(chartDataSet.getChart_name() + " cycleId is null");
            params.put("cycleId", cycleId);
            String cronExpress = CommonInit.getCollCycleCronById(cycleId);
            String queryDate = (String) params.get("queryDate");
            String dateTime = TimeControl.cronDateTime(cronExpress, queryDate);
            LOG.info("nearest cron expression date time : {}", dateTime);
            List<ChartData> chartDataList = new ArrayList<>();
            // 根据时间和周期表达式获取该时间之前一年内所有的时间
            List<String> allMonth = TimeControl.getEveryMonth(dateTime, cronExpress);
            for (String everyMonth : allMonth) {
                params.put("queryDate", everyMonth);
                String tableFullName = tableName + "_" + TimeControl.tableSuffixName(everyMonth);
                LOG.info("tableName : {}", tableFullName);
                chartDataSet.setTable_name(tableFullName);
                // 根据相关条件查询得到某个时间点的数据
                List<ChartData> list = metricDataDAO.getLineData(chartDataSet, metricIdArr[i],
                        dateTime, null, null, params);
                chartDataList.addAll(list);
            }
            LOG.info("query chartData list : {}", chartDataList);
            List<ChartData> charData = acquireData(chartDataList, chartDataSet, dateTime,
                    cronExpress);
            lineDataProduce(data, charData, metricIdArr[i], i, metricNameArr);
        }
        return data;
    }

    private List<ChartData> acquireData(List<ChartData> list, MdChartDataSet chartDataSet,
            String queryDate, String cronExpress) {
        List<ChartData> data = new ArrayList<ChartData>();
        List<String> allMonth = TimeControl.getEveryMonth(queryDate, cronExpress);
        // 找到时间集合与ChartData集合中时间属性这两者相匹配的ChartData数据
        boolean retFlag = false;
        for (String month : allMonth) {
            for (ChartData chartData : list) {
                String mark = chartData.getMark();
                if (month.equals(mark)) {
                    String chinaMonth = TimeControl.chineseMonth(mark);
                    chartData.setMark(chinaMonth);
                    retFlag = true;
                    data.add(chartData);
                    break;
                }
            }
            if (!retFlag) {
                String chinaMonth = TimeControl.chineseMonth(month);
                data.add(new ChartData(chinaMonth, "-"));
            }
            retFlag = false;
        }
        return data;
    }

    public RaduisHost getHostDataRadius() {
        RaduisHost raduisHost = new RaduisHost();
        // 获取节点
        List<MdNode> nodelist = CommonInit.getNodeList();
        if (ToolsUtils.ListIsNull(nodelist))
            return raduisHost;
        raduisHost.setNodelist(nodelist);
        // 获取radius主机
        List<MonHost> hostlist = hostDao.getHostByHostType(2);
        if (ToolsUtils.ListIsNull(hostlist))
            raduisHost.setHostnum(0);
        else
            raduisHost.setHostnum(hostlist.size());
        for (MdNode node : nodelist) {
            List<MonHost> nodeHost = new ArrayList<MonHost>();
            for (MonHost host : hostlist) {
                if (node.getId().equals(host.getNodeid())) {
                    nodeHost.add(host);
                }
            }
            node.setHostlist(nodeHost);
        }
        return raduisHost;
    }
}
