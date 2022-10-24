package com.asiainfo.lcims.omc.web.ctrl.data;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.report.ReportInfo;
import com.asiainfo.lcims.omc.service.report.ReportService;
import com.asiainfo.lcims.omc.util.DateTools;

@Controller
@RequestMapping(value = "/data/class/report")
public class ExportReportData {
    @Resource(name = "reportService")
    ReportService reportService;

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    // 根据reportId,以及相关指标信息查询
    @RequestMapping(value = "/select/{menuTreeName}")
    @ResponseBody
    public Map<String, Object> selectReportInfo(@PathVariable String menuTreeName,
            HttpServletRequest request, Model model) {
        String reporttype = String.valueOf(request.getParameter("reportType"));
        String date = String.valueOf(request.getParameter("date"));
        String startIndex = request.getParameter("startIndex");// 为空的时候默认查询全部
        String endIndex = request.getParameter("endIndex");// 为空的时候默认查询全部

        logger.info("menuTreeName:" + menuTreeName);
        logger.info("reporttype:" + reporttype);
        logger.info("startIndex:" + startIndex);
        logger.info("endIndex:" + endIndex);
        if (date == null || date.isEmpty()) {
            date = new DateTools("yyyy-MM-dd").getCurrentDate();
            logger.info("date is empty, given default value of current date:" + date);
        } else {
            logger.info("date:" + date);
        }

        if (date == null || date.isEmpty()) {
            date = new DateTools("yyyy-MM-dd").getCurrentDate();
        }

        Map<String, Object> reportinfo = null;
        try {
            reportinfo = reportService.selectReportInfo(menuTreeName, reporttype, date, startIndex,
                    endIndex);
            logger.info("selectReportInfo end: reportinfo size:" + reportinfo.get("totalcount"));
        } catch (Exception e) {
            logger.error("ExportReportData selectReportInfo Exception:{}", e);
        }
        return reportinfo;
    }

    // 导出报表
    @RequestMapping(value = "/export/{menuTreeName}")
    public void exportReport(@PathVariable String menuTreeName, HttpServletRequest request,
            HttpServletResponse response) {
        String reporttype = request.getParameter("reportType");
        String date = request.getParameter("date");

        logger.info("menuTreeName:" + menuTreeName);
        logger.info("reporttype:" + reporttype);
        if (date == null || date.isEmpty()) {
            date = new DateTools("yyyy-MM-dd").getCurrentDate();
            logger.info("date is empty, given default value of current date:" + date);
        } else {
            logger.info("date:" + date);
        }

        ReportInfo reportInfo = reportService.getReportByMenuName(menuTreeName);
        if (reportInfo == null) {
            logger.info("reportInfo is null");
            return;
        }
        String reportId = reportInfo.getReportId();
        HSSFWorkbook wb = reportService.exportReport(reportId, reporttype, date);
        String reportname = reportService.getReportName(reportId) + reporttype + "_" + date;

        writeInfoInResponse(response, wb, reportname);
        logger.info("exportReport end.");
    }

    private void writeInfoInResponse(HttpServletResponse response, HSSFWorkbook wb,
            String reportname) {
        try {
            reportname = URLEncoder.encode(reportname + ".xls", "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("UnsupportedEncodingException:", e1);
        }
        response.setContentType("octets/stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + reportname);
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            wb.write(output);
        } catch (IOException e) {
            logger.error("IOException:", e);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(wb);
        }
    }
}
