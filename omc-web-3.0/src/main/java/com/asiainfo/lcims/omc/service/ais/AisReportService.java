package com.asiainfo.lcims.omc.service.ais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.db.Conn;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisTable;
import com.asiainfo.lcims.omc.model.ais.WdInsSheetHeaderModel;
import com.asiainfo.lcims.omc.model.ais.WdInsSheetModel;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.excelReport.ExcelCell;
import com.asiainfo.lcims.omc.model.excelReport.ExcelReport;
import com.asiainfo.lcims.omc.model.excelReport.ExcelRow;
import com.asiainfo.lcims.omc.model.excelReport.ExcelSheet;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.report.ReportDataSource;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.ais.AisInit;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupDAO;
import com.asiainfo.lcims.omc.persistence.ais.INSReportDAO;
import com.asiainfo.lcims.omc.persistence.ais.INSScheduleDAO;
import com.asiainfo.lcims.omc.persistence.gdcu.GdcuAisReportDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.po.ais.INSReport;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.persistence.report.SpecialDAO;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.gdcu.GdcuAisReportService;
import com.asiainfo.lcims.omc.service.monitor.MetricDataService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.asiainfo.lcims.omc.util.TimeControl;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.poi.ExcelDocument;

@Service(value = "aisReportService")
public class AisReportService {
    private static final Logger log = LoggerFactory.make();
    private static final DateTools DATE_TOOLS = new DateTools("yyyy-MM-dd HH:mm");
    private static final String CHINESE_TIME = "yyyy年MM月dd日";
    private static final DateTools TABLE_DATE_TOOLS = new DateTools("MM_dd");
    private static final String REPORT_SUFFIX = "_巡检报告";
    private final String AIS_MAP_KEY_RULEFAIL = "ais_map_key_rulefail";
    private final String AIS_MAP_KEY_SOLVE = "ais_map_key_sovel";
    private static final DateTools REPORT_POSTFIX = new DateTools("yyyyMMddHHmmsss");// 巡检报告名称后缀时间格式
    private String curTime = null;
    private static final String DATA_SOURCE_ID = "report_ais_id";
    private static final String ISUSESQL = "1";
    private static final String CPU_PEAK_UTILIZATION_INTERVAL = "<50%"; // CPU峰值利用率区间
    private static final String MEMORY_PEAK_UTILIZATION_INTERVAL = "<80%"; // Memory峰值利用率区间
    private static final String DISK_PEAK_UTILIZATION_INTERVAL = "<75%"; // Disk峰值利用率区间
    private static final int CLOCK_JUDGMENT_INTERVAL = 3000; // 时钟判断

    @Autowired
    private GdcuAisReportDAO gdcuAisReportDAO;

    @Inject
    private AisGroupDAO groupDao;

    @Inject
    private INSScheduleDAO scheduleDao;

    @Inject
    AisGroupMetricManageService groupMetricManageService;

    @Inject
    ParamService paramService;

    @Inject
    AisInit aisInit;

    @Inject
    INSReportDAO reportDao;

    @Inject
    MdParamDAO mdParamDAO;

    @Inject
    MetricDataService metricDataService;

    @Autowired
    private GdcuAisReportService gdcuAisReportService;

    /**
     * 生成定时巡检报告
     * 
     * @param scheduleid
     */
    public boolean exportRegularReport(String scheduleid) {
        log.info("start exportRegularReport with scheduleid : {}", scheduleid);
        boolean result = true;
        INSSchedule schedule = scheduleDao.getScheduleDetail(scheduleid);
        if (schedule == null) {
            return false;
        }
        // 分省份生成不同模板的定时巡检
        switch (ReadFile.PROVINCE) {
        case ProviceUtill.PROVINCE_GDCU:
            radiusOperateReport(schedule.getTitle(), schedule.getGroup_ids());
            result = operateReport(schedule);
            break;
        case ProviceUtill.PROVINCE_QHCM:
        case ProviceUtill.PROVINCE_DEV:
            result = defaultReport(schedule);
            break;
        default:
            result = operateReport(schedule);
        }
        log.info("end exportRegularReport with scheduleid : {}", scheduleid);
        return result;
    }

    public Map<String, Object> exportRealTimeReport(String groupids, String title) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        try {
            List<AisGroupModel> grouplist = getGroups(groupids);
            List<WdInsSheetModel> wdInsSheetModelList = new ArrayList<WdInsSheetModel>();
            INSSchedule schedule = new INSSchedule();
            for (AisGroupModel aisGroupModel : grouplist) {
                String group_id = aisGroupModel.getGroup_id();
                log.info("exportRealTimeReport group_id: {}", group_id);
                List<WdInsSheetModel> wdInsSheetModelListByGroupId = groupDao
                        .getSheetByGroupId(group_id);
                for (WdInsSheetModel wdInsSheetModel : wdInsSheetModelListByGroupId) {
                    String sheetid = wdInsSheetModel.getId();
                    wdInsSheetModel.setSheetheaderlist(groupDao.getSheetHeaderBySheetId(sheetid));
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("group_id", group_id);
                    List<AisGroupMetricModel> groupMetrics = groupMetricManageService
                            .getAisGroupMetricDetailList(map);
                    wdInsSheetModel.setGroupMetrics(groupMetrics);
                }
                wdInsSheetModelList.addAll(wdInsSheetModelListByGroupId);
            }
            schedule.setSheetlist(wdInsSheetModelList);
            HSSFWorkbook wb = null;
            String reportName = title + REPORT_SUFFIX;
            wb = makeHSSFWorkbook(schedule);
            reportName = reportName + "_" + REPORT_POSTFIX.getCurrentDate() + ".xls";
            mapResult.put("wb", wb);
            mapResult.put("reportName", reportName);
        } catch (Exception e) {
            log.error("AisReportService exportRealTimeReport error : {}", e.getMessage());
        }
        return mapResult;
    }

    /**
     * 
     * @Title: defaultReport @Description: TODO(通用默认类型的定时巡检模板) @param @param
     *         schedule @param @return 参数 @return boolean 返回类型 @throws
     */
    private boolean defaultReport(INSSchedule schedule) {
        List<AisGroupModel> grouplist = getGroups(schedule.getGroup_ids());
        List<WdInsSheetModel> wdInsSheetModelList = new ArrayList<WdInsSheetModel>();
        for (AisGroupModel aisGroupModel : grouplist) {
            String group_id = aisGroupModel.getGroup_id();
            List<WdInsSheetModel> wdInsSheetModelListByGroupId = groupDao
                    .getSheetByGroupId(group_id);
            for (WdInsSheetModel wdInsSheetModel : wdInsSheetModelList) {
                String sheetid = wdInsSheetModel.getId();
                wdInsSheetModel.setSheetheaderlist(groupDao.getSheetHeaderBySheetId(sheetid));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("group_id", group_id);
                List<AisGroupMetricModel> groupMetrics = groupMetricManageService
                        .getAisGroupMetricDetailList(map);
                wdInsSheetModel.setGroupMetrics(groupMetrics);
            }
            wdInsSheetModelList.addAll(wdInsSheetModelListByGroupId);
        }
        schedule.setSheetlist(wdInsSheetModelList);
        HSSFWorkbook wb = null;
        FileOutputStream fos = null;
        String reportName = schedule.getTitle() + REPORT_SUFFIX;
        try {
            wb = makeHSSFWorkbook(schedule);
            String fileName = reportName + "_" + REPORT_POSTFIX.getCurrentDate() + ".xls";
            String filePath = createDir("aisreport/regular") + File.separator + fileName;
            fos = new FileOutputStream(filePath);
            wb.write(fos);
            fos.flush();
            close(wb, fos);
            reportDao.insert(makeReport(reportName, fileName));
            AisSendInfo.sendEmailSms(schedule, new ExcelReport(), filePath,
                    reportName + "_" + REPORT_POSTFIX.getCurrentDate());
            return true;
        } catch (Exception e) {
            log.error("AisReportService defaultReport error : {}", e.getMessage());
            return false;
        } finally {
            close(wb, fos);
        }
    }

    /**
     * 
     * @Title: makeHSSFWorkbook @Description: TODO(生成HSSFWorkbook) @param @param
     *         schedule @param @return 参数 @return HSSFWorkbook 返回类型 @throws
     */
    private HSSFWorkbook makeHSSFWorkbook(INSSchedule schedule) {
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            log.info("makeHSSFWorkbook get dataSource");
            ReportDataSource dataSource = gdcuAisReportDAO.getDataSourceById(DATA_SOURCE_ID);
            Connection connection = Conn.getConn(dataSource);
            log.info("makeHSSFWorkbook schedule sheetlist size: {}",
                    schedule.getSheetlist().size());
            for (WdInsSheetModel wdInsSheetModel : schedule.getSheetlist()) {
                log.info("makeHSSFWorkbook wdInsSheetModel Sheetname: {}",
                        wdInsSheetModel.getSheetname());
                HSSFSheet sheet = wb.createSheet(wdInsSheetModel.getSheetname());
                Integer porRow = 0;
                List<WdInsSheetHeaderModel> sheetheaderlist = wdInsSheetModel.getSheetheaderlist();
                Map<Integer, Integer> colLengthMap = new HashMap<Integer, Integer>();
                log.info("start makeHSSFWorkbook Construction header WdInsSheetModel sheetname: {}",
                        wdInsSheetModel.getSheetname());
                // 构造表头
                Integer curRow = porRow;
                porRow = mkSheetHeader(wb, sheet, sheetheaderlist,
                        ConstantUtill.WDINSSHEETHEADER_HEADERTYPE, colLengthMap, curRow, porRow,
                        connection);
                // 构造内容
                log.info(
                        "start makeHSSFWorkbook Construction content WdInsSheetModel sheetname: {}",
                        wdInsSheetModel.getSheetname());
                curRow = porRow;
                List<MdParam> mdParamList = paramService
                        .getMdParamList(ConstantUtill.PARAM_HOSTTYPE);
                BusinessConf conf = CommonInit.BUSCONF;
                List<MdParam> mMdParamList = mdParamDAO
                        .getParamByType(ConstantUtill.MD_PARAM_TYPE_HOST);
                switch (wdInsSheetModel.getSheettype()) {
                case ConstantUtill.WDINSSHEET_SHEETTYPE_HOST:
                    // 主机巡检
                    aisInit.init();
                    for (MdParam mdParam : mMdParamList) {
                        porRow = mkHostSheet(wb, sheet, wdInsSheetModel.getGroupMetrics(),
                                colLengthMap, mdParam.getCode(), mdParamList, curRow, porRow, conf);
                        curRow = porRow;
                    }
                    break;
                case ConstantUtill.WDINSSHEET_SHEETTYPE_APPLICATION:
                    // 应用巡检
                    aisInit.init();
                    Integer serialNo = 1;
                    for (MdParam mdParam : mMdParamList) {
                        Map<String, Integer> mapApplication = mkApplicationSheet(wb, sheet,
                                wdInsSheetModel.getGroupMetrics(), colLengthMap, mdParam.getCode(),
                                mdParamList, curRow, porRow, serialNo, conf);
                        porRow = mapApplication.get("porRow");
                        serialNo = mapApplication.get("serialNo");
                        curRow = porRow;
                    }
                    break;
                default:
                }
                // 构造页脚
                log.info("start makeHSSFWorkbook Construction footer WdInsSheetModel sheetname: {}",
                        wdInsSheetModel.getSheetname());
                curRow = porRow;
                porRow = mkSheetHeader(wb, sheet, sheetheaderlist,
                        ConstantUtill.WDINSSHEETHEADER_FOOTERTYPE, colLengthMap, curRow, porRow,
                        connection);
                ExcelDocument.autoSheet(sheet, colLengthMap);
            }
            Conn.close(connection);
        } catch (Exception e) {
            log.error("AisReportService makeHSSFWorkbook error: {}", e.getMessage());
        }
        return wb;
    }

    private Integer mkSheetHeader(HSSFWorkbook wb, HSSFSheet sheet,
            List<WdInsSheetHeaderModel> sheetheaderlist, String wdInsSheetHeaderType,
            Map<Integer, Integer> colLengthMap, Integer curRow, Integer porRow,
            Connection connection) {
        for (WdInsSheetHeaderModel wdInsSheetHeaderModel : sheetheaderlist) {
            try {
                if (wdInsSheetHeaderType.equals(wdInsSheetHeaderModel.getHeadertype())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    String isusesql = wdInsSheetHeaderModel.getIsusesql();
                    String content = "";
                    if (ISUSESQL.equals(isusesql)) {
                        String sql = wdInsSheetHeaderModel.getSelectsql();
                        log.info("mkSheetHeader isusesql transfer before sql : {}", sql);
                        String currentDate = DATE_TOOLS.getCurrentDate();
                        String tableDate = TABLE_DATE_TOOLS.getCurrentDate();
                        String cycleTime = gdcuAisReportService.getCycleTime(currentDate);
                        String alarmLastTime = gdcuAisReportService.getAlarmLastTime(currentDate);
                        sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", cycleTime);
                        sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}",
                                alarmLastTime);
                        sql = StringUtils.replace(sql, "#{TIME.MM_dd}", tableDate);
                        log.info("mkSheetHeader isusesql transfer after sql : {}", sql);
                        content = SpecialDAO.selectReportHeaderContent(connection, sql);
                    } else {
                        content = wdInsSheetHeaderModel.getContent();
                    }
                    Integer firstcol = Integer.parseInt(wdInsSheetHeaderModel.getFirstcol());
                    Integer lastcol = Integer.parseInt(wdInsSheetHeaderModel.getLastcol());
                    Integer lastrow = Integer.parseInt(wdInsSheetHeaderModel.getLastrow());
                    if ((lastrow + 1) > porRow) {
                        porRow = lastrow + 1;
                    }
                    map.put("content", content);
                    map.put("firstrow",
                            Integer.parseInt(wdInsSheetHeaderModel.getFirstrow()) + curRow);
                    map.put("lastrow", lastrow + curRow);
                    map.put("firstcol", firstcol);
                    map.put("lastcol", lastcol);
                    ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, map);
                    ExcelDocument.calcColLengthMap(colLengthMap, content, firstcol, lastcol);
                }
            } catch (Exception e) {
                log.error("AisReportService mkSheetHeader error: {}", e.getMessage());
            }
        }
        return porRow;
    }

    private String getMetricValue(String metricId, List<AisGroupMetricModel> groupMetrics,
            MonHost monHost) {
        log.info("getMetricValue Start");
        String metricValue = "";
        try {
            for (AisGroupMetricModel groupMetric : groupMetrics) {
                if (null == groupMetric.getMetric_id()) {
                    groupMetric.setMetric_id("");
                }
                if (null == groupMetric.getDimension1()) {
                    groupMetric.setDimension1("");
                }
                if (null == groupMetric.getDimension2()) {
                    groupMetric.setDimension2("");
                }
                if (null == groupMetric.getDimension3()) {
                    groupMetric.setDimension3("");
                }
                if (null != groupMetric.getMetric_id() && null != groupMetric.getDimension1()
                        && null != groupMetric.getDimension2()
                        && null != groupMetric.getDimension3() && null != monHost.getHostid()) {
                    log.info(
                            "getMetricValue groupMetric.getMetric_id: {}, groupMetric.getDimension1: {}, groupMetric.getDimension2: {}, groupMetric.getDimension3: {}, monHost.getHostid: {}",
                            metricId, groupMetric.getDimension1(), groupMetric.getDimension2(),
                            groupMetric.getDimension3(), monHost.getHostid());
                    if (groupMetric.getMetric_id().equals(metricId)
                            && (groupMetric.getDimension1().equals(monHost.getHostid())
                                    || groupMetric.getDimension2().equals(monHost.getHostid())
                                    || groupMetric.getDimension3().equals(monHost.getHostid()))) {
                        log.info(
                                "getMetricValue makeMonitorTargetInfo Start metricId: {}, hostid: {}",
                                metricId, monHost.getHostid());
                        // 根据指标id获取周期时间值
                        Map<String, Object> params = new HashMap<String, Object>();
                        makeMonitorTargetInfo(groupMetric, params);
                        log.info("getMetricValue makeMonitorTargetInfo End");
                        // 查询当前指标值
                        log.info("getMetricValue getDataRecentByChartNameMetricIdAttr Start");
                        List<ChartData> datalist = metricDataService
                                .getDataRecentByChartNameMetricIdAttr(groupMetric, params);
                        log.info("getMetricValue getDataRecentByChartNameMetricIdAttr End");
                        if (datalist == null || datalist.isEmpty() || datalist.size() > 1) {
                            log.info("getMetricValue datalist isEmpty");
                            metricValue = "";
                        } else if (datalist.get(0) == null) {
                            log.info("getMetricValue datalist get(0) isEmpty");
                            metricValue = "";
                        } else {
                            log.info("getMetricValue datalist.get(0).getValue()");
                            metricValue = datalist.get(0).getValue();
                            log.info("getMetricValue getValue metricValue : {}", metricValue);
                        }
                        log.info("getMetricValue assignment metricValue end");
                    }
                }
            }
        } catch (Exception e) {
            log.error("AisReportService getMetricValue error: {}", e.getMessage());
        }
        log.info("getMetricValue End");
        log.info("metricValue : {}", metricValue);
        return metricValue;
    }

    private Integer mkHostSheet(HSSFWorkbook wb, HSSFSheet sheet,
            List<AisGroupMetricModel> groupMetrics, Map<Integer, Integer> colLengthMap,
            String hostType, List<MdParam> mdParamList, Integer curRow, Integer porRow,
            BusinessConf conf) {
        List<MonHost> monHostList = AisInit.getHostByHostType(hostType);
        if (monHostList.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            String hostTypeName = "";
            for (MdParam mdParam : mdParamList) {
                if (mdParam.getCode().equals(hostType)) {
                    hostTypeName = mdParam.getDescription();
                }
            }
            String content = hostTypeName;
            Integer firstcol = 0;
            Integer lastcol = 0;
            Integer firstrow = 0 + curRow;
            Integer lastrow = monHostList.size() - 1 + curRow;
            if ((lastrow + 1) > porRow) {
                porRow = lastrow + 1;
            }
            map.put("content", content);
            map.put("firstrow", firstrow);
            map.put("lastrow", lastrow);
            map.put("firstcol", firstcol);
            map.put("lastcol", lastcol);
            ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, map);
            ExcelDocument.calcColLengthMap(colLengthMap, content, firstcol, lastcol);
            for (int i = 0; i < monHostList.size(); i++) {
                MonHost monHost = monHostList.get(i);
                Map<String, Object> maphost = new HashMap<String, Object>();
                // 设备名称
                String contenthost = monHost.getHostname();
                Integer firstcolhost = 1;
                Integer lastcolhost = 1;
                Integer firstrowhost = i + curRow;
                Integer lastrowhost = i + curRow;
                if ((lastrowhost + 1) > porRow) {
                    porRow = lastrowhost + 1;
                }
                maphost.put("content", contenthost);
                maphost.put("firstrow", firstrowhost);
                maphost.put("lastrow", lastrowhost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // CPU峰值利用率
                log.info("mkHostSheet CpuPeakVal getMetricValue Start");
                log.info("mkHostSheet CpuPeakVal getMetricValue groupMetrics: {}",
                        groupMetrics.size());
                log.info("mkHostSheet CpuPeakVal getMetricValue monHost: {}", monHost.getAddr());
                String metricValue = getMetricValue(conf.getCpuPeakValMetricId(), groupMetrics,
                        monHost);
                log.info("mkHostSheet CpuPeakVal metricValue : {}", metricValue);
                contenthost = ("".equals(metricValue)) ? ConstantUtill.NO_DATA
                        : metricValue + ConstantUtill.PERCENT_SIGN;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // CPU峰值利用率区间
                contenthost = CPU_PEAK_UTILIZATION_INTERVAL;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // Memory峰值利用率
                metricValue = getMetricValue(conf.getMemPeakValMetricId(), groupMetrics, monHost);
                contenthost = ("".equals(metricValue)) ? ConstantUtill.NO_DATA
                        : metricValue + ConstantUtill.PERCENT_SIGN;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // Memory峰值利用率区间
                contenthost = MEMORY_PEAK_UTILIZATION_INTERVAL;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // Disk峰值利用率
                metricValue = getMetricValue(conf.getDiskBusyRatePeakValMetricId(), groupMetrics,
                        monHost);
                contenthost = ("".equals(metricValue)) ? ConstantUtill.NO_DATA
                        : metricValue + ConstantUtill.PERCENT_SIGN;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // Disk峰值利用率区间
                contenthost = DISK_PEAK_UTILIZATION_INTERVAL;
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
                // 主机时间是否准确
                metricValue = getMetricValue(conf.getClockJudgmentMetricId(), groupMetrics,
                        monHost);
                contenthost = ConstantUtill.YES;
                if (metricValue == null || "".equals(metricValue)) {
                    contenthost = ConstantUtill.NO;
                } else {
                    double metricValueDouble = Double.parseDouble(metricValue);
                    if (metricValueDouble > CLOCK_JUDGMENT_INTERVAL) {
                        contenthost = ConstantUtill.NO;
                    }
                }
                firstcolhost = firstcolhost + 1;
                lastcolhost = lastcolhost + 1;
                maphost.put("content", contenthost);
                maphost.put("firstcol", firstcolhost);
                maphost.put("lastcol", lastcolhost);
                ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                ExcelDocument.calcColLengthMap(colLengthMap, contenthost, firstcolhost,
                        lastcolhost);
            }
        }
        return porRow;
    }

    private Map<String, Integer> mkApplicationSheet(HSSFWorkbook wb, HSSFSheet sheet,
            List<AisGroupMetricModel> groupMetrics, Map<Integer, Integer> colLengthMap,
            String hostType, List<MdParam> mdParamList, Integer curRow, Integer porRow,
            Integer serialNo, BusinessConf conf) {
        List<MonHost> monHostList = AisInit.getHostByHostType(hostType);
        if (monHostList.size() > 0) {
            String hostTypeName = "";
            for (MdParam mdParam : mdParamList) {
                if (mdParam.getCode().equals(hostType)) {
                    hostTypeName = mdParam.getDescription();
                }
            }
            for (int i = 0; i < monHostList.size(); i++) {
                MonHost monHost = monHostList.get(i);
                Map<String, Object> maphost = new HashMap<String, Object>();
                Integer firstcolhost;
                Integer lastcolhost;
                Integer firstrowhost;
                Integer lastrowhost;
                int j = 0;
                for (AisGroupMetricModel aisGroupMetricModel : groupMetrics) {
                    if (!aisGroupMetricModel.getMetric_id().equals(conf.getCpuPeakValMetricId())
                            && !aisGroupMetricModel.getMetric_id()
                                    .equals(conf.getMemPeakValMetricId())
                            && !aisGroupMetricModel.getMetric_id()
                                    .equals(conf.getDiskBusyRatePeakValMetricId())
                            && !aisGroupMetricModel.getMetric_id()
                                    .equals(conf.getClockJudgmentMetricId())
                            && (aisGroupMetricModel.getDimension1().equals(monHost.getHostid())
                                    || aisGroupMetricModel.getDimension2()
                                            .equals(monHost.getHostid())
                            // || aisGroupMetricModel.getDimension3()
                            // .equals(monHost.getHostid())
                            )) {
                        // 序号
                        String content = String.valueOf(serialNo);
                        firstcolhost = 0;
                        lastcolhost = 0;
                        firstrowhost = curRow + j;
                        lastrowhost = curRow + j;
                        maphost.put("content", content);
                        maphost.put("firstrow", firstrowhost);
                        maphost.put("lastrow", lastrowhost);
                        maphost.put("firstcol", firstcolhost);
                        maphost.put("lastcol", lastcolhost);
                        ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet,
                                maphost);
                        ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                                lastcolhost);
                        // 巡检内容
                        content = aisGroupMetricModel.getMetric_name();
                        firstcolhost = firstcolhost + 4;
                        lastcolhost = lastcolhost + 4;
                        maphost.put("content", content);
                        maphost.put("firstcol", firstcolhost);
                        maphost.put("lastcol", lastcolhost);
                        ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet,
                                maphost);
                        ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                                lastcolhost);
                        // 巡检值
                        // 根据指标id获取周期时间值
                        Map<String, Object> params = new HashMap<String, Object>();
                        makeMonitorTargetInfo(aisGroupMetricModel, params);
                        // 查询当前指标值
                        List<ChartData> datalist = metricDataService
                                .getDataRecentByChartNameMetricIdAttr(aisGroupMetricModel, params);
                        // 指标值
                        String metricValue = "";
                        if (datalist == null || datalist.isEmpty() || datalist.size() > 1) {
                            metricValue = "";
                        } else if (datalist.get(0) == null) {
                            metricValue = "";
                        } else {
                            metricValue = datalist.get(0).getValue();
                        }
                        content = metricValue;
                        firstcolhost = firstcolhost + 1;
                        lastcolhost = lastcolhost + 1;
                        maphost.put("content", content);
                        maphost.put("firstcol", firstcolhost);
                        maphost.put("lastcol", lastcolhost);
                        ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet,
                                maphost);
                        ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                                lastcolhost);
                        // 是否正常
                        content = "是";
                        // 判断是否正常
                        String msg = AisInit.getAlarmMsg(aisGroupMetricModel);
                        if (!ToolsUtils.StringIsNull(msg)) {
                            content = "否";
                        }
                        firstcolhost = firstcolhost + 1;
                        lastcolhost = lastcolhost + 1;
                        maphost.put("content", content);
                        maphost.put("firstcol", firstcolhost);
                        maphost.put("lastcol", lastcolhost);
                        ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet,
                                maphost);
                        ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                                lastcolhost);
                        // 巡检日期
                        content = DateUtil.parseStr(new Date(), CHINESE_TIME);
                        firstcolhost = firstcolhost + 1;
                        lastcolhost = lastcolhost + 1;
                        maphost.put("content", content);
                        maphost.put("firstcol", firstcolhost);
                        maphost.put("lastcol", lastcolhost);
                        ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet,
                                maphost);
                        ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                                lastcolhost);
                        j++;
                    }
                }
                if (j > 0) {
                    // 添加序号
                    serialNo = serialNo + 1;
                    // 主机内网地址
                    String content = monHost.getAddr();
                    firstcolhost = 1;
                    lastcolhost = 1;
                    firstrowhost = curRow;
                    lastrowhost = curRow + j - 1;
                    curRow = lastrowhost + 1;
                    if ((lastrowhost + 1) > porRow) {
                        porRow = lastrowhost + 1;
                    }
                    maphost.put("content", content);
                    maphost.put("firstrow", firstrowhost);
                    maphost.put("lastrow", lastrowhost);
                    maphost.put("firstcol", firstcolhost);
                    maphost.put("lastcol", lastcolhost);
                    ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                    ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                            lastcolhost);
                    // 主机名
                    content = monHost.getHostname();
                    firstcolhost = firstcolhost + 1;
                    lastcolhost = lastcolhost + 1;
                    maphost.put("content", content);
                    maphost.put("firstcol", firstcolhost);
                    maphost.put("lastcol", lastcolhost);
                    ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                    ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                            lastcolhost);
                    // 主机用途
                    content = hostTypeName;
                    firstcolhost = firstcolhost + 1;
                    lastcolhost = lastcolhost + 1;
                    maphost.put("content", content);
                    maphost.put("firstcol", firstcolhost);
                    maphost.put("lastcol", lastcolhost);
                    ExcelDocument.makeSheetContent(ExcelDocument.getSheetStyle(wb), sheet, maphost);
                    ExcelDocument.calcColLengthMap(colLengthMap, content, firstcolhost,
                            lastcolhost);
                }
            }
        }
        Map<String, Integer> mapInteger = new HashMap<String, Integer>();
        mapInteger.put("porRow", porRow);
        mapInteger.put("serialNo", serialNo);
        return mapInteger;
    }

    // 默认类型的定时巡检模板
    private boolean operateReport(INSSchedule schedule) {
        ExcelReport report = getActualData(schedule.getGroup_ids());
        log.info("end getActualData");
        String reportName = schedule.getTitle() + REPORT_SUFFIX;
        HSSFWorkbook wb = null;
        FileOutputStream fos = null;
        try {
            log.info("start mkExcel");
            wb = ExcelDocument.mkExcel(report);
            String fileName = reportName + "_" + REPORT_POSTFIX.getCurrentDate() + ".xls";
            log.info("fileName:" + fileName);
            String filePath = createDir("aisreport/regular") + File.separator + fileName;
            fos = new FileOutputStream(filePath);
            wb.write(fos);
            fos.flush();
            close(wb, fos);
            reportDao.insert(makeReport(reportName, fileName));
            AisSendInfo.sendEmailSms(schedule, report, filePath,
                    reportName + "_" + REPORT_POSTFIX.getCurrentDate());
            log.info("ais send end");
            return true;
        } catch (Exception e) {
            log.error("生成巡检报告失败", e);
            return false;
        } finally {
            close(wb, fos);
        }
    }

    // 广东联通特殊化模版
    private void radiusOperateReport(String title, String groupIds) {
        FileOutputStream fos = null;
        try {
            String reportName = title + "_radius维护作业计划";
            HSSFWorkbook wb = gdcuAisReportService.getRadiusData(groupIds);
            String fileName = reportName + "_" + REPORT_POSTFIX.getCurrentDate() + ".xls";
            log.info("radius operate fileName : {}", fileName);
            String filePath = createDir("aisreport/regular") + File.separator + fileName;
            log.info("radius operate relative filePath : {}", filePath);
            fos = new FileOutputStream(filePath);
            wb.write(fos);
            fos.flush();
            close(wb, fos);
            reportDao.insert(makeReport(reportName, fileName));
            log.info("radius operate ais send end");
        } catch (Exception e) {
            log.error("生成RADIUS维护作业失败", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * 定时巡检报告记录入库
     * 
     * @param
     * @param path
     * @return
     */
    private INSReport makeReport(String title, String path) {
        INSReport report = new INSReport();
        report.setTitle(title);
        report.setReportlink(path);
        report.setId(IDGenerateUtil.getUuid());
        report.setCreate_time(new Timestamp(System.currentTimeMillis()));
        return report;
    }

    public String createDir(String dirName) {
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取实时巡检所有数据
     * 
     * @param
     * @return
     */
    public ExcelReport getActualData(String groupids) {
        aisInit.init();
        curTime = new DateTools("yyyy-MM-dd HH:mm").getCurrentDate();
        List<ExcelSheet> sheetList = new ArrayList<ExcelSheet>();
        List<AisGroupModel> grouplist = getGroups(groupids);
        for (AisGroupModel group : grouplist) {
            ExcelSheet sheet = new ExcelSheet(group.getGroup_name());

            Map<String, StringBuilder> aisMsgMap = new HashMap<String, StringBuilder>();
            aisMsgMap.put(AIS_MAP_KEY_RULEFAIL, new StringBuilder(""));
            aisMsgMap.put(AIS_MAP_KEY_SOLVE, new StringBuilder(""));

            mkSheetHeaderList(sheet);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("group_id", group.getGroup_id());
            List<AisGroupMetricModel> groupMetrics = groupMetricManageService
                    .getAisGroupMetricDetailList(map);
            mkSheetInfoList(sheet, groupMetrics, aisMsgMap);
            sheetList.add(sheet);
            // mkSheetFooterList(sheet, aisMsgMap);
        }
        ExcelReport report = new ExcelReport();
        report.setSheetList(sheetList);
        return report;
    }

    /**
     * 页面展示表格数据
     * 
     * @param groupids
     * @return
     */
    public List<AisTable> getActualDataTable(String groupids, String selectData) {
        aisInit.init();
        curTime = new DateTools("yyyy-MM-dd HH:mm").getCurrentDate();
        List<AisTable> aisTableList = new ArrayList<>();
        List<AisGroupModel> grouplist = getGroups(groupids);
        for (AisGroupModel group : grouplist) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("group_id", group.getGroup_id());
            List<AisGroupMetricModel> groupMetrics = groupMetricManageService
                    .getAisGroupMetricDetailList(map);
            aisTableList.addAll(mkSheetInfoListTable(groupMetrics, selectData));
        }
        return aisTableList;
    }

    @SuppressWarnings("unused")
    private void mkSheetFooterList(ExcelSheet sheet, Map<String, StringBuilder> aisMsgMap) {
        List<ExcelRow> rowList = new ArrayList<ExcelRow>();
        List<ExcelCell> cellList1 = new ArrayList<ExcelCell>();
        ExcelCell text1 = new ExcelCell("异常情况");
        cellList1.add(text1);
        ExcelCell text2 = new ExcelCell();
        text2.setValue(aisMsgMap.get(AIS_MAP_KEY_RULEFAIL).toString());
        text2.setColspan(2);
        cellList1.add(text2);
        ExcelRow row1 = new ExcelRow();
        row1.setCellList(cellList1);
        rowList.add(row1);

        List<ExcelCell> cellList2 = new ArrayList<ExcelCell>();
        ExcelCell text3 = new ExcelCell("处理建议");
        cellList2.add(text3);
        ExcelCell text4 = new ExcelCell();
        text4.setValue(aisMsgMap.get(AIS_MAP_KEY_SOLVE).toString());
        text4.setColspan(2);
        cellList2.add(text4);
        ExcelRow row2 = new ExcelRow();
        row2.setCellList(cellList2);
        rowList.add(row2);
        sheet.setFooterList(rowList);
    }

    private void mkSheetHeaderList(ExcelSheet sheet) {
        List<ExcelCell> cellList = new ArrayList<ExcelCell>();
        ExcelCell text1 = new ExcelCell("作业内容");
        cellList.add(text1);
        ExcelCell text2 = new ExcelCell("执行时间");
        cellList.add(text2);
        ExcelCell text3 = new ExcelCell("检查结果");
        cellList.add(text3);
        ExcelCell valueCell = new ExcelCell("指标值");
        cellList.add(valueCell);
        ExcelCell text4 = new ExcelCell("检查项");
        cellList.add(text4);
        ExcelCell text5 = new ExcelCell("备注");
        cellList.add(text5);
        ExcelRow row = new ExcelRow();
        row.setCellList(cellList);
        List<ExcelRow> rowList = new ArrayList<ExcelRow>();
        rowList.add(row);
        sheet.setHeaderList(rowList);
    }

    private void mkSheetInfoList(ExcelSheet sheet, List<AisGroupMetricModel> groupMetrics,
            Map<String, StringBuilder> aisMsgMap) {
        List<ExcelRow> infoList = new ArrayList<ExcelRow>();
        for (AisGroupMetricModel groupMetric : groupMetrics) {
            infoList.add(makeGroupMetricDetail(groupMetric, aisMsgMap));
        }
        sheet.setInfoList(infoList);
    }

    /**
     * 页面表格展示
     * 
     * @param groupMetrics
     * @param selectData
     * @return
     */
    private List<AisTable> mkSheetInfoListTable(List<AisGroupMetricModel> groupMetrics,
            String selectData) {
        List<AisTable> tableList = new ArrayList<>();
        AisTable aisTable = new AisTable();
        for (AisGroupMetricModel groupMetric : groupMetrics) {
            aisTable = makeGroupMetricDetailTable(groupMetric, selectData);
            if (aisTable.getResult().equals("正常") && selectData.equals("1")) {
                tableList.add(makeGroupMetricDetailTable(groupMetric, selectData));
            } else if (aisTable.getResult().equals("异常") && selectData.equals("2")) {
                tableList.add(makeGroupMetricDetailTable(groupMetric, selectData));
            } else if (selectData.equals("0")) {
                tableList.add(makeGroupMetricDetailTable(groupMetric, selectData));
            }
            // tableList.add(makeGroupMetricDetailTable(groupMetric,selectData));
        }
        return tableList;
    }

    /**
     * 页面表格展示
     * 
     * @param groupMetric
     * @param selectData
     * @return
     */
    private AisTable makeGroupMetricDetailTable(AisGroupMetricModel groupMetric,
            String selectData) {
        try {
            log.info("makeGroupMetricDetail:{group_metric_id:" + groupMetric.getGroup_metric_id()
                    + "}");
            AisTable tableList = new AisTable();
            // 根据指标id获取周期时间值
            Map<String, Object> params = new HashMap<String, Object>();
            makeMonitorTargetInfo(groupMetric, params);
            // 查询当前指标值
            List<ChartData> datalist = metricDataService
                    .getDataRecentByChartNameMetricIdAttr(groupMetric, params);
            log.info("current metric value is {}", datalist);
            String name = CommonInit.getMetricNameByMetricId(groupMetric.getMetric_id());
            String result = "正常";
            String value = null;
            if (null != groupMetric.getDimension1_name()
                    && null != groupMetric.getDimension2_name()) {
                value = groupMetric.getDimension1_name() + ":" + groupMetric.getDimension2_name();
            } else {
                value = groupMetric.getDimension1_name();
            }
            // 判断是否正常
            String msg = AisInit.getAlarmMsg(groupMetric);
            if (!ToolsUtils.StringIsNull(msg)) {
                result = "异常";
                // value = value + " " + msg;
            }
            log.info("remarks value is: {}", value);
            // 指标名称
            tableList.setContent(name);
            // 执行时间
            tableList.setTime(curTime);
            // 检查结果
            tableList.setResult(result);
            // 指标值
            String metricValue = "";
            if (datalist == null || datalist.isEmpty() || datalist.size() > 1) {
                metricValue = "";
            } else if (datalist.get(0) == null) {
                metricValue = "";
            } else {
                metricValue = datalist.get(0).getValue();
            }
            tableList.setValue(metricValue);
            // 检查项
            tableList.setIp(value);
            // 备注
            tableList.setChecking(msg);

            return tableList;
        } catch (Exception e) {
            log.info("生成数据异常:" + e.getMessage(), e);
        }
        return null;
    }

    private ExcelRow makeGroupMetricDetail(AisGroupMetricModel groupMetric,
            Map<String, StringBuilder> aisMsgMap) {
        try {
            log.info("makeGroupMetricDetail:{group_metric_id:" + groupMetric.getGroup_metric_id()
                    + "}");
            List<ExcelCell> cellList = new ArrayList<ExcelCell>();
            AisTable tableList = new AisTable();
            // 根据指标id获取周期时间值
            Map<String, Object> params = new HashMap<String, Object>();
            makeMonitorTargetInfo(groupMetric, params);
            // 查询当前指标值
            List<ChartData> datalist = metricDataService
                    .getDataRecentByChartNameMetricIdAttr(groupMetric, params);
            log.info("current metric value is {}", datalist);
            String name = CommonInit.getMetricNameByMetricId(groupMetric.getMetric_id());
            String result = "正常";
            String value = null;
            if (null != groupMetric.getDimension1_name()
                    && null != groupMetric.getDimension2_name()) {
                value = groupMetric.getDimension1_name() + ":" + groupMetric.getDimension2_name();
            } else {
                value = groupMetric.getDimension1_name();
            }
            // 判断是否正常
            String msg = AisInit.getAlarmMsg(groupMetric);
            if (!ToolsUtils.StringIsNull(msg)) {
                result = "异常";
                // value = value + " " + msg;
            }
            log.info("remarks value is: {}", value);
            // 指标名称
            cellList.add(new ExcelCell(name));
            tableList.setContent(name);
            // 执行时间
            cellList.add(new ExcelCell(curTime));
            tableList.setTime(curTime);
            // 检查结果
            cellList.add(new ExcelCell(result));
            tableList.setResult(result);
            // 指标值
            String metricValue = "";
            if (datalist == null || datalist.isEmpty() || datalist.size() > 1) {
                metricValue = "";
            } else if (datalist.get(0) == null) {
                metricValue = "";
            } else {
                metricValue = datalist.get(0).getValue();
            }
            cellList.add(new ExcelCell(metricValue));
            tableList.setValue(metricValue);
            // IP
            cellList.add(new ExcelCell(value));
            tableList.setIp(value);
            // 检查项
            cellList.add(new ExcelCell(msg));
            tableList.setChecking(msg);

            return new ExcelRow(cellList);
        } catch (Exception e) {
            log.info("生成数据异常:" + e.getMessage(), e);
        }
        return null;
    }

    private String mkValueUnit(String value, List<ChartData> datalist) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (ToolsUtils.ListIsNull(datalist)) {
            return null;
        }
        value = "";
        for (ChartData chartData : datalist) {

        }
        return value;
    }

    private String getStimeByCycleid(int cycleid, int num) {
        String stime = TimeControl.getLasttime();
        if (cycleid == 2) {
            stime = TimeControl.getFiveMinTime(num * 5);
        } else if (cycleid == 4) {
            stime = TimeControl.getOneHourTime(num);
        } else if (cycleid == 5) {
            stime = TimeControl.getOneDayTime(num - 1);
        }
        return stime;
    }

    private List<AisGroupModel> getGroups(String groups) {
        List<AisGroupModel> itemgroups = new ArrayList<AisGroupModel>();
        if (groups == null || groups.equals("")) {
            return itemgroups;
        }
        String[] groupid = groups.split(",");
        for (int i = 0; i < groupid.length; i++) {
            AisGroupModel group = groupDao.getAisGroupById(groupid[i]);
            if (group != null) {
                itemgroups.add(group);
            }
        }
        return itemgroups;
    }

    public void close(HSSFWorkbook wb, FileOutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 根据url 赋值 维度1 维度2 值 node/host 节点 --> 主机 ： 维度1：节点名称， 维度2：主机IP node/area 节点
     * --> 地市 ： 维度1：节点名称， 维度2：地市名称 area/bras 地市 --> bras: 维度1：地市 ， 维度2：brasip
     * node 节点 ： 维度1：节点名称， 维度2：空 area 地市 ： 维度1：地市名称， 维度2：空 node/summary： 维度1
     * 节点总览 area/summary: 维度1 地市总览
     * 
     * 新增： area/node: 地市 --> 节点 ：维度1：地市名称， 维度2：节点名称
     */
    private void makeMonitorTargetInfo(AisGroupMetricModel groupMetricModel,
            Map<String, Object> map) {
        String url = groupMetricModel.getUrl();
        String[] strings = url.split("--");
        String url_1 = url;
        if (strings.length == 2) {
            url_1 = strings[0];
        }
        if (url_1.endsWith("node")) {
            if (url_1.endsWith("area/node")) {
                MdArea area = AisInit.getAreaByAreano(groupMetricModel.getDimension1());
                map.put("parentId", groupMetricModel.getDimension1());
                map.put("paramid", groupMetricModel.getDimension2());
                if (area != null)
                    groupMetricModel.setDimension1_name(area.getName());
                MdNode node = AisInit.getNodeById(groupMetricModel.getDimension2());
                if (node != null)
                    groupMetricModel.setDimension2_name(node.getNode_name());
            } else {// 节点
                map.put("paramid", groupMetricModel.getDimension1());
                MdNode node = AisInit.getNodeById(groupMetricModel.getDimension1());
                if (node != null)
                    groupMetricModel.setDimension1_name(node.getNode_name());
            }
        } else if (url_1.endsWith("host")) {
            if (url_1.endsWith("node/host")) {// 节点下主机
                map.put("parentId", groupMetricModel.getDimension1());
                map.put("paramid", groupMetricModel.getDimension2());
                MdNode node = AisInit.getNodeById(groupMetricModel.getDimension1());
                if (node != null)
                    groupMetricModel.setDimension1_name(node.getNode_name());
                MonHost host = AisInit.getHostByHostId(groupMetricModel.getDimension2());
                groupMetricModel.setDimension2_name(host.getAddr());
            } else {// 主机
                map.put("paramid", groupMetricModel.getDimension1());
                MonHost host = AisInit.getHostByHostId(groupMetricModel.getDimension1());
                groupMetricModel.setDimension1_name(host.getAddr());
            }
        } else if (url_1.endsWith("area")) {
            if (url_1.endsWith("node/area")) {// 节点下地市
                map.put("parentId", groupMetricModel.getDimension1());
                map.put("paramid", groupMetricModel.getDimension2());
                MdNode node = AisInit.getNodeById(groupMetricModel.getDimension1());
                if (node != null)
                    groupMetricModel.setDimension1_name(node.getNode_name());
                MdArea area = AisInit.getAreaByAreano(groupMetricModel.getDimension2());
                if (area != null)
                    groupMetricModel.setDimension2_name(area.getName());
            } else {// 属地
                map.put("paramid", groupMetricModel.getDimension1());
                MdArea area = AisInit.getAreaByAreano(groupMetricModel.getDimension1());
                if (area != null)
                    groupMetricModel.setDimension1_name(area.getName());
            }
        } else if (url_1.endsWith("area/bras")) {// 属地下bras
            map.put("parentId", groupMetricModel.getDimension1());
            map.put("paramid", groupMetricModel.getDimension2());
            MdArea area = AisInit.getAreaByAreano(groupMetricModel.getDimension1());
            if (area != null)
                groupMetricModel.setDimension1_name(area.getName());
            BdNas nas = AisInit.getBdNasById(groupMetricModel.getDimension2());
            groupMetricModel.setDimension2_name(nas.getNas_ip());
        } else if (url_1.endsWith("node/summary")) {// 节点总览
            groupMetricModel.setDimension1_name("节点总览");
        } else if (url_1.endsWith("area/summary")) {// 属地总览
            groupMetricModel.setDimension1_name("属地总览");
        } else if (url_1.endsWith("host/summary")) {// 服务器总览
            groupMetricModel.setDimension1_name("服务器总览");
        }
    }
}
