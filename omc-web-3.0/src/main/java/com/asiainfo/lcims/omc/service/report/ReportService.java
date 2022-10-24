package com.asiainfo.lcims.omc.service.report;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.db.Conn;
import com.asiainfo.lcims.omc.model.report.ReportDataSource;
import com.asiainfo.lcims.omc.model.report.ReportFieldInfo;
import com.asiainfo.lcims.omc.model.report.ReportInfo;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.report.ReportDataSourceDAO;
import com.asiainfo.lcims.omc.persistence.report.ReportFieldDAO;
import com.asiainfo.lcims.omc.persistence.report.ReportInfoDAO;
import com.asiainfo.lcims.omc.persistence.report.SpecialDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DbDateFormatReplaceUtil;
import com.asiainfo.lcims.omc.util.EnumUtil;
import com.asiainfo.lcims.omc.util.poi.ExcelDocument;

@Service(value = "reportService")
public class ReportService {
    @Inject
    ReportInfoDAO reportInfoDAO;
    @Inject
    ReportFieldDAO reportFieldDAO;
    @Inject
    ReportDataSourceDAO reportDataSourceDAO;
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    // 获取所有的报表
    public List<ReportInfo> getAllReport() {
        List<ReportInfo> reportList = reportInfoDAO.getAllReport();
        return reportList;
    }

    public ReportInfo getReportByMenuName(String menuName) {
        ReportInfo reportInfo = reportInfoDAO.getReportByMenuName(menuName);
        if (reportInfo == null) {
            logger.error("menuName:{" + menuName + "}reportInfo is null.");
            return null;
        }
        return reportInfo;
    }

    // 导出EXCEL报表
    public HSSFWorkbook exportReport(String reportId, String reportType, String date) {
        ReportInfo reportInfo = reportInfoDAO.getReportByReportId(reportId);
        if (reportInfo == null) {
            logger.error("reportId:{" + reportId + "}reportInfo is null.");
            return null;
        }
        List<ReportFieldInfo> fieldInfoList = reportFieldDAO.getReportFieldByReportId(reportId);
        if (fieldInfoList.isEmpty()) {
            logger.error("fieldInfoList of reportId:{" + reportId + "} is null.");
            return null;
        }
        ReportDataSource dataSource = reportDataSourceDAO
                .getDataSourceById(reportInfo.getDatasourceId());
        if (dataSource == null) {
            logger.error("dataSourceId :{" + reportInfo.getDatasourceId() + "}  is null.");
            return null;
        }
        String sql = mkSelectInfoSql(reportInfo, reportType, date);
        logger.info("exportReport sql : " + sql);
        Connection conn = Conn.getConn(dataSource);
        if (sql == null) {
            return null;
        }
        String[][] data = SpecialDAO.SelectReportInfoSql(conn, sql, fieldInfoList);
        Conn.close(conn);

        Map<String, Object> exportMap = new HashMap<String, Object>();
        exportMap.put("data", data);
        exportMap.put("tableName", reportInfo.getReportName());
        exportMap.put("fieldInfoList", fieldInfoList);
        HSSFWorkbook wb = ExcelDocument.mkExcel(exportMap);

        return wb;
    }

    // 根据相关信息分页查询对应的报表数据
    public Map<String, Object> selectReportInfo(String menuTreeName, String reportType, String time,
            String startIndex, String endIndex) {
        Map<String, Object> result = new HashMap<String, Object>();

        ReportInfo reportInfo = reportInfoDAO.getReportByMenuName(menuTreeName);
        if (reportInfo == null) {
            logger.error("menuTreeName:{" + menuTreeName + "}reportInfo is null.");
            return null;
        }
        String reportId = reportInfo.getReportId();
        List<ReportFieldInfo> fieldInfoList = reportFieldDAO.getReportFieldByReportId(reportId);
        if (fieldInfoList.isEmpty()) {
            logger.error("fieldInfoList of reportId:{" + reportId + "} is null.");
            return null;
        }
        ReportDataSource dataSource = reportDataSourceDAO
                .getDataSourceById(reportInfo.getDatasourceId());
        if (dataSource == null) {
            logger.error("dataSourceId :{" + reportInfo.getDatasourceId() + "}  is null.");
            return null;
        }

        Connection conn = Conn.getConn(dataSource);
        String sql = mkSelectInfoSql(reportInfo, dataSource, reportType, time, startIndex,
                endIndex);
        if (sql == null) {
            return result;
        }
        String[][] data = SpecialDAO.SelectReportInfoSql(conn, sql, fieldInfoList);
        String totalCountSql = this.mkSelectCountSql(reportInfo, reportType, time);
        int totalCount = SpecialDAO.SelectReportCount(conn, totalCountSql);
        Conn.close(conn);

        result.put("header", fieldInfoList);
        result.put("data", data);
        result.put("totalcount", totalCount);

        return result;
    }

    // 获取报表的名称
    public String getReportName(String reportId) {
        ReportInfo reportInfo = reportInfoDAO.getReportByReportId(reportId);
        if (reportInfo == null) {
            logger.info("reportInfo is null");
            return null;
        }
        return reportInfo.getReportName();
    }

    // 根据时间拼接查询报表内容的sql
    private String mkSelectInfoSql(ReportInfo reportInfo, String reportType, String date) {
        return this.mkInitSql(reportInfo, reportType, date);
    }

    // 根据时间拼接查询报表内容的sql
    private String mkSelectInfoSql(ReportInfo reportInfo, ReportDataSource dataSource,
            String reportType, String date, String startIndex, String endIndex) {
        String sql = this.mkInitSql(reportInfo, reportType, date);
        if (startIndex == null || startIndex.isEmpty() || endIndex == null || endIndex.isEmpty()) {
            return sql;
        }
        StringBuilder strb = new StringBuilder();
        if (EnumUtil.DB_ORACLE.equalsIgnoreCase(dataSource.getType())) {
            strb.append("select * from (select t.*,rownum as t_new_rownum from(").append(sql)
                    .append(")t) where t_new_rownum<=").append(endIndex)
                    .append(" and t_new_rownum>").append(startIndex);
        } else if (EnumUtil.DB_MYSQL.equalsIgnoreCase(dataSource.getType())) {
            strb.append("select * from (").append(sql).append(")as tmp_t limit ").append(startIndex)
                    .append(",").append(endIndex);
        }
        return strb.toString();
    }

    // 根据时间拼接查询报表总数量的sql
    private String mkSelectCountSql(ReportInfo reportInfo, String reportType, String date) {
        StringBuilder strb = new StringBuilder();
        String sql = this.mkInitSql(reportInfo, reportType, date);
        strb.append("select count(*) from (").append(sql).append(") tmp_count ");
        return strb.toString();
    }

    private String mkInitSql(ReportInfo reportInfo, String reportType, String date) {
        if (reportInfo == null) {
            logger.error("reportInfo is null");
            return null;
        }
        StringBuilder strb = new StringBuilder();
        if (reportType.equals(EnumUtil.REPORT_DAY)) {
            strb.append(reportInfo.getDayReportSql());
        } else if (reportType.equals(EnumUtil.REPORT_WEEK)) {
            strb.append(reportInfo.getWeekReportSql());
        } else {
            strb.append(reportInfo.getMonthReportSql());
        }
        if (strb.length() == 0) {
            return null;
        }
        String result = mkNeedReplaceStr(strb.toString(), date);
        logger.info("report_sql is:" + result);
        return result;
    }

    private String mkNeedReplaceStr(String sql, String date) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            logger.error("date:{" + date + "} format error, date format should be yyyy-mm-dd.", e);
            return null;
        }
        return DbDateFormatReplaceUtil.mkReplaceSqlStr(sql, localDate);
    }

    /**
     * 
     * @Title: getReportType
     * @Description: TODO(获取报表类型)
     * @param @param
     *            map
     * @param
     * @return 参数
     * @return List<String> 返回类型
     */
    public List<Map<String, Object>> getReportType(Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String pageUrl = map.get("pageUrl").toString();
        MdMenu mdMenu = mdMenuDataListener.getMdMenuByUrl(pageUrl);
        ReportInfo reportInfo = getReportByMenuName(mdMenu.getName());
        if (null != reportInfo.getDayReportSql() && !"".equals(reportInfo.getDayReportSql())) {
            Map<String, Object> daymap = new HashMap<String, Object>();
            daymap.put("type", EnumUtil.REPORT_DAY);
            daymap.put("name", Constant.DAY_REPORT_NAME);
            result.add(daymap);
        }
        if (null != reportInfo.getWeekReportSql() && !"".equals(reportInfo.getWeekReportSql())) {
            Map<String, Object> weekmap = new HashMap<String, Object>();
            weekmap.put("type", EnumUtil.REPORT_WEEK);
            weekmap.put("name", Constant.WEEK_REPORT_NAME);
            result.add(weekmap);
        }
        if (null != reportInfo.getMonthReportSql() && !"".equals(reportInfo.getMonthReportSql())) {
            Map<String, Object> monthmap = new HashMap<String, Object>();
            monthmap.put("type", EnumUtil.REPORT_MON);
            monthmap.put("name", Constant.MONTH_REPORT_NAME);
            result.add(monthmap);
        }
        return result;
    }
}
