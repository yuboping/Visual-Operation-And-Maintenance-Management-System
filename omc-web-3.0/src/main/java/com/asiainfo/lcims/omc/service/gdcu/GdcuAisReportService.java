package com.asiainfo.lcims.omc.service.gdcu;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.db.Conn;
import com.asiainfo.lcims.omc.model.gdcu.GdcuReportFieldInfo;
import com.asiainfo.lcims.omc.model.gdcu.GdcuReportHeader;
import com.asiainfo.lcims.omc.model.gdcu.GdcuReportInfo;
import com.asiainfo.lcims.omc.model.report.ReportDataSource;
import com.asiainfo.lcims.omc.persistence.gdcu.GdcuAisReportDAO;
import com.asiainfo.lcims.omc.persistence.report.SpecialDAO;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.poi.AisExcelDocument;

@Service
public class GdcuAisReportService {

    private static final Logger LOG = LoggerFactory.getLogger(GdcuAisReportService.class);

    private static final DateTools HEADER_DATE_TOOLS = new DateTools("yyyy/MM/dd HH:mm");
    private static final DateTools DATE_TOOLS = new DateTools("yyyy-MM-dd HH:mm");
    private static final DateTools TABLE_DATE_TOOLS = new DateTools("MM_dd");

    private static final String DEVICE_SHEET = "1";
    private static final String BUSINESS_SHEET = "2";

    private static final String DATA_SOURCE_ID = "report_ais_id";

    @Autowired
    private GdcuAisReportDAO gdcuAisReportDAO;

    @SuppressWarnings("resource")
    public HSSFWorkbook getRadiusData(String groupids) {
        List<GdcuReportHeader> headerList = gdcuAisReportDAO.getReportHeaderList(groupids,
                DEVICE_SHEET);
        GdcuReportInfo reportInfo = gdcuAisReportDAO.getReportInfoList(groupids, DEVICE_SHEET);
        LOG.info("reportInfo device sheet : {}", reportInfo);
        ReportDataSource dataSource = gdcuAisReportDAO.getDataSourceById(DATA_SOURCE_ID);
        List<GdcuReportFieldInfo> fieldInfoList = gdcuAisReportDAO.getReportFieldInfoList(groupids);
        Connection connection = Conn.getConn(dataSource);
        String[][] data = mkSqlByCondition(connection, reportInfo, fieldInfoList);
        List<Map<String, Object>> exportList = new ArrayList<>();
        Map<String, Object> exportMap = new HashMap<String, Object>();
        exportMap.put("data", data);
        exportMap.put("tableName", reportInfo.getSheet_name());
        exportMap.put("fieldInfoList", fieldInfoList);
        HSSFWorkbook wb = new HSSFWorkbook();
        assembleHeaderSql(headerList, connection);
        exportMap.put("headerList", headerList);
        exportList.add(exportMap);
        exportMap = new HashMap<String, Object>();
        String[][] businessData = { { "", "", "", "", "", "", "", "", "" } };
        List<GdcuReportHeader> businessHeaderList = gdcuAisReportDAO.getReportHeaderList(groupids,
                BUSINESS_SHEET);
        GdcuReportInfo businessReportInfo = gdcuAisReportDAO.getReportInfoList(groupids,
                BUSINESS_SHEET);
        assembleBusinessHeaderSql(businessHeaderList, connection);
        exportMap.put("data", businessData);
        exportMap.put("tableName", businessReportInfo.getSheet_name());
        exportMap.put("fieldInfoList", fieldInfoList);
        exportMap.put("headerList", businessHeaderList);
        exportList.add(exportMap);
        wb = AisExcelDocument.mkComplexExcel(exportList);
        Conn.close(connection);
        return wb;
    }

    private void assembleBusinessHeaderSql(List<GdcuReportHeader> businessHeaderList,
            Connection conn) {
        for (GdcuReportHeader businessHeader : businessHeaderList) {
            if (StringUtils.equals("1", businessHeader.getIsusesql())) {
                String sql = businessHeader.getSelectsql();
                String currentDate = DATE_TOOLS.getCurrentDate();
                String tableDate = TABLE_DATE_TOOLS.getCurrentDate();
                String cycleTime = getCycleTime(currentDate);
                String alarmLastTime = getAlarmLastTime(currentDate);
                sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", cycleTime);
                sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}", alarmLastTime);
                sql = StringUtils.replace(sql, "#{TIME.MM_dd}", tableDate);
                LOG.info("isusetype = 1, sql = {}", sql);
                String headercontent = SpecialDAO.selectReportHeaderContent(conn, sql);
                businessHeader.setHeadercontent(headercontent);
                LOG.info("isusetype = 1, businessHeader = {}", businessHeader);
            } else if (StringUtils.equals("3", businessHeader.getIsusesql())) {
                String sql = businessHeader.getSelectsql();
                String currentDate = DATE_TOOLS.getCurrentDate();
                String tableDate = TABLE_DATE_TOOLS.getCurrentDate();
                String oneHourTime = getOneHourTime();
                String alarmLastTime = getAlarmLastTime(currentDate);
                sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", oneHourTime);
                sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}", alarmLastTime);
                sql = StringUtils.replace(sql, "#{TIME.MM_dd}", tableDate);
                LOG.info("isusetype = 3, sql = {}", sql);
                String headercontent = SpecialDAO.selectReportHeaderContent(conn, sql);
                businessHeader.setHeadercontent(headercontent);
                LOG.info("isusetype = 3, businessHeader = {}", businessHeader);
            } else if (StringUtils.equals("4", businessHeader.getIsusesql())) {
                String sql = businessHeader.getSelectsql();
                String currentDate = DATE_TOOLS.getCurrentDate();
                String tableDate = TABLE_DATE_TOOLS.getCurrentDate();
                String OneDayTime = getOneDayTime();
                String alarmLastTime = getAlarmLastTime(currentDate);
                sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", OneDayTime);
                sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}", alarmLastTime);
                sql = StringUtils.replace(sql, "#{TIME.MM_dd}", tableDate);
                LOG.info("isusetype = 4, sql = {}", sql);
                String headercontent = SpecialDAO.selectReportHeaderContent(conn, sql);
                businessHeader.setHeadercontent(headercontent);
                LOG.info("isusetype = 4, businessHeader = {}", businessHeader);
            }
        }
        for (GdcuReportHeader businessHeader : businessHeaderList) {
            if (StringUtils.equals("2", businessHeader.getIsusesql())) {
                String sql = businessHeader.getSelectsql();
                String currentDate = DATE_TOOLS.getCurrentDate();
                String cycleTime = getCycleTime(currentDate);
                String alarmLastTime = getAlarmLastTime(currentDate);
                sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", cycleTime);
                sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}", alarmLastTime);
                LOG.info("isusetype = 2, sql = {}", sql);
                String[] alarmSql = StringUtils.split(sql, ";");
                String contentPrefix = "全省带业务设备一共";
                String hostNum = SpecialDAO.selectReportHeaderContent(conn, alarmSql[0]);
                LOG.info("alarmSql[0] : {}", alarmSql[0]);
                String alarmMsg = SpecialDAO.selectReportHeaderContent(conn, alarmSql[1]);
                LOG.info("alarmSql[1] : {}", alarmSql[1]);
                LOG.info("alarmMsg : {}", alarmMsg);
                String contentSuffix = "台，运行正常";
                if (alarmMsg != null) {
                    contentSuffix = "台，有设备运行异常\r\n其中：" + alarmMsg;
                }
                String headercontent = contentPrefix + hostNum + contentSuffix;
                businessHeader.setHeadercontent(headercontent);
                LOG.info("isusetype = 2, businessHeader = {}", businessHeader);
            }
        }
    }

    public String[][] mkSqlByCondition(Connection conn, GdcuReportInfo reportInfo,
            List<GdcuReportFieldInfo> fieldInfoList) {
        String sql = reportInfo.getSelectsql();
        String currentDate = DATE_TOOLS.getCurrentDate();
        String tableDate = TABLE_DATE_TOOLS.getCurrentDate();
        String cycleTime = getCycleTime(currentDate);
        String alarmLastTime = getAlarmLastTime(currentDate);
        sql = StringUtils.replace(sql, "#{TIME.yyyy-MM-dd HH:mm}", cycleTime);
        sql = StringUtils.replace(sql, "#{ALARMTIME.yyyy-MM-dd HH:mm}", alarmLastTime);
        sql = StringUtils.replace(sql, "#{TIME.MM_dd}", tableDate);
        LOG.info("transfer sql : {}", sql);
        String[][] data = SpecialDAO.selectGdcuReportInfoSql(conn, sql, fieldInfoList);
        return data;
    }

    public void assembleHeaderSql(List<GdcuReportHeader> headerList, Connection conn) {
        for (GdcuReportHeader gdcuReportHeader : headerList) {
            if (StringUtils.equals("1", gdcuReportHeader.getIsusesql())) {
                String sql = gdcuReportHeader.getSelectsql();
                String currentDate = HEADER_DATE_TOOLS.getCurrentDate();
                sql = sql.replace("#{headtime}", "'" + currentDate + "'");
                String headercontent = SpecialDAO.selectReportHeaderContent(conn, sql);
                gdcuReportHeader.setHeadercontent(headercontent);
            }
        }
    }

    public String getCycleTime(String currentDate) {
        String minSuffix = currentDate.substring(currentDate.length() - 1, currentDate.length());
        String newMinute = Integer.parseInt(minSuffix) >= 5 ? "5" : "0";
        String cycleTime = currentDate.substring(0, currentDate.length() - 1) + newMinute;
        return cycleTime;
    }

    public String getAlarmLastTime(String currentDate) {
        String minSuffixStr = currentDate.substring(currentDate.length() - 1, currentDate.length());
        int minSuffix = Integer.parseInt(minSuffixStr);
        String alarmLastTime = "";
        if (minSuffix < 7 && minSuffix >= 2) {
            alarmLastTime = currentDate.substring(0, currentDate.length() - 1) + "0";
        } else if (minSuffix >= 7) {
            alarmLastTime = currentDate.substring(0, currentDate.length() - 1) + "5";
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = format.parse(currentDate);
                long dateTime = date.getTime();
                long preoid = (minSuffix + 5) * 60000L;
                long lastTime = dateTime - preoid;
                alarmLastTime = format.format(lastTime);
            } catch (ParseException e) {
                LOG.error("alarm last time when minute 0 or 1 error", e);
                alarmLastTime = currentDate;
            }
        }
        return alarmLastTime;
    }

    public String getOneHourTime() {
        // 0 15 * * * ?
        String currentDate = DATE_TOOLS.getCurrentDate();
        String currentMinStr = DATE_TOOLS.getCurrentMin();
        int currentMin = Integer.parseInt(currentMinStr);
        if (currentMin < 15) {
            currentDate = DATE_TOOLS.getOffsetMin(-60);
        }
        String[] timeArr = currentDate.split(":");
        String oneHourTime = timeArr[0] + ":15";
        return oneHourTime;
    }

    public String getOneDayTime() {
        // 0 45 1 * * ?
        String currentDate = DATE_TOOLS.getCurrentDate();
        String currentMinStr = DATE_TOOLS.getCurrentMin();
        String currentHourStr = DATE_TOOLS.getCurrentHour();

        int currentHour = Integer.parseInt(currentHourStr);
        int currentMin = Integer.parseInt(currentMinStr);
        if (currentHour < 1) {
            currentDate = DATE_TOOLS.getOffsetDay(-1);
        } else if (currentHour == 1 && currentMin < 45) {
            currentDate = DATE_TOOLS.getOffsetDay(-1);
        }
        String[] timeArr = currentDate.split(" ");
        String oneDayTime = timeArr[0] + " 01:45";
        return oneDayTime;
    }

}
