package com.asiainfo.lcims.omc.web.ctrl.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.service.ais.InsReportService;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 巡检报告记录表:WD_INS_REPORT
 *
 */
@Controller
public class InsReportData {
    private static final Logger LOG = LoggerFactory.make();
//    private static final int PAGESIZE = 6;
    @Resource(name = "insReportService")
    private InsReportService service;

    @RequestMapping({ "/data/class/ais/report" })
    public @ResponseBody WebResult aisReport(HttpServletRequest request, Model model) {

        String begintime = request.getParameter("begintime");
        String endtime = request.getParameter("endtime");
        String searchkey = request.getParameter("searchkey");
        if(!ToolsUtils.StringIsNull(searchkey)){
            searchkey = searchkey.trim();
        }
        String currentpage = request.getParameter("currentpage");
        String pagesize = request.getParameter("pagesize");

        if (StringUtils.isEmpty(begintime) || StringUtils.isEmpty(endtime)) {
            return new WebResult(false, "参数有误");
        }

        if (StringUtils.isEmpty(currentpage)) {
            currentpage = "1";
        }
        begintime += " 00:00:00";
        endtime += " 23:59:59";
        if (StringUtils.isEmpty(searchkey)) {
            searchkey = "";
        }
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("begintime", begintime);
        parameters.put("endtime", endtime);
        parameters.put("currentpage", currentpage);
        parameters.put("pagesize", pagesize);
        parameters.put("searchkey", searchkey);
        return new WebResult(true, "", service.getReportListWithPage(parameters));
    }

    /**
     * 实时巡检报告
     * 
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/export/ais/actualReport")
    public void actualAis(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();
        String reportName = (String) session.getAttribute("reportName");
        exportFile(response, "aisreport/actual/", reportName,
                ToolsUtils.getDownFileName(request, reportName));
    }

    /**
     * 定时巡检报告
     * 
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/export/ais/regularReport")
    public void regularAis(HttpServletRequest request, HttpServletResponse response, Model model) {
        String reportId = (String) request.getParameter("reportId");
        String reportName = service.getReportNameById(reportId);
        exportFile(response, "aisreport/regular/", reportName,
                ToolsUtils.getDownFileName(request, reportName));
    }

    private void exportFile(HttpServletResponse response, String path, String reportName,
            String filename) {
        FileInputStream in = null;
        ServletOutputStream os = null;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(path + reportName);
            wb = new HSSFWorkbook(in);
            response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", filename));  
            response.setContentType("multipart/form-data");   
            response.setCharacterEncoding("UTF-8");
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            LOG.error("", e);
            try {
                response.sendRedirect("/error");
            } catch (IOException e1) {
                LOG.error("", e1);
            }
        } finally {
            close(in, os, wb);
        }
    }

    private void close(FileInputStream in, ServletOutputStream os, HSSFWorkbook wb) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
    }
}
