package com.asiainfo.lcims.omc.web.ctrl.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisTable;
import com.asiainfo.lcims.omc.model.ais.CheckCategaryModel;
import com.asiainfo.lcims.omc.model.excelReport.ExcelReport;
import com.asiainfo.lcims.omc.service.ais.AisReportService;
import com.asiainfo.lcims.omc.service.ais.InsItemService;
import com.asiainfo.lcims.omc.service.gdcu.GdcuAisReportService;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.poi.ExcelDocument;

/**
 * 巡检项定义表 :WD_INS_ITEMS ,WD_INS_ITEM_GROUP
 * 
 * @author luohuawuyin
 *
 */
@Controller
public class AisInsItemData {

    private static final Logger LOG = LoggerFactory.make();

    private static final DateTools DATETOOL = new DateTools("yyyyMMddHHmmsss");

    private static final DateTools GDCU_DATE_TOOL = new DateTools("yyyyMMdd");

    private static final String REPORT_NAME_QHCM = "青海移动家宽Radius认证平台实时巡检工作报告";

    @Resource(name = "insItemService")
    private InsItemService insItemService;

    @Resource(name = "aisReportService")
    private AisReportService aisReportService;

    @Autowired
    private GdcuAisReportService gdcuAisReportService;

    @RequestMapping(value = "/data/class/ais/categarylist")
    public @ResponseBody WebResult getAllCategary() {
        try {
            List<CheckCategaryModel> result = insItemService.getCategaryGroupList();
            if (result != null && !result.isEmpty()) {
                return new WebResult(true, "", result);
            }
        } catch (Exception e) {
            LOG.error("获取数据失败", e);
        }
        return new WebResult(false, "获取数据失败");
    }

    @RequestMapping({ "/data/class/ais/check/group/{groupid}" })
    public @ResponseBody WebResult aisCheckGroups(HttpServletRequest request, Model model,
            @PathVariable("groupid") String groupid) {
        // CheckGroupModel result = insItemService.proGroupModelInfo(groupid);
        // if (result != null) {
        // return new WebResult(true, "", result);
        // }
        return new WebResult(false, "查询信息有误");
    }

    /**
     * 实时巡检
     * 
     * @param request
     * @param response
     */
    @SuppressWarnings("resource")
    @RequestMapping(value = "/data/class/ais/check")
    public void aisCheck(HttpServletRequest request, HttpServletResponse response) {
        String groupids = request.getParameter("groupids");
        if (StringUtils.isEmpty(groupids)) {
            return;
        }
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String reportName = "实时巡检报告_" + username + "_" + DATETOOL.getCurrentDate() + ".xls";
        ExcelReport report = aisReportService.getActualData(groupids);
        HSSFWorkbook wb = new HSSFWorkbook();
        // 分省份生成不同模板的定时巡检
        switch (ReadFile.PROVINCE) {
        case ProviceUtill.PROVINCE_QHCM:
        case ProviceUtill.PROVINCE_DEV:
            Map<String, Object> map = aisReportService.exportRealTimeReport(groupids,
                    REPORT_NAME_QHCM);
            wb = (HSSFWorkbook) map.get("wb");
            reportName = (String) map.get("reportName");
            break;
        default:
            wb = ExcelDocument.mkExcel(report);// 生成巡检报告
        }
        OutputStream output = null;
        try {
            reportName = ToolsUtils.getDownFileName(request, reportName);
            response.setHeader("Content-disposition",
                    String.format("attachment;filename=\"%s\"", reportName));
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (IOException e) {
            LOG.error("aisCheck error, reason : {}", e);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(wb);
        }
    }

    /**
     * 页面表格展示
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/data/class/ais/checkTable")
    @ResponseBody
    public List<AisTable> aisCheckTable(HttpServletRequest request, HttpServletResponse response,
            String selectData) {
        String groupids = request.getParameter("groupids");
        if (StringUtils.isEmpty(groupids)) {
            return null;
        }
        List<AisTable> tableList = aisReportService.getActualDataTable(groupids, selectData);
        return tableList;
    }

    @RequestMapping(value = "/data/class/ais/radiusReport")
    public void radiusAisReport(HttpServletRequest request, HttpServletResponse response) {
        String groupids = request.getParameter("groupids");
        if (StringUtils.isEmpty(groupids)) {
            return;
        }

        if (StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_GDCU, ReadFile.PROVINCE)
                || StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_DEV, ReadFile.PROVINCE)) {
            radiusOperateReport(groupids, request, response);
        }

    }

    public void radiusOperateReport(String groupIds, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String reportName = "radius维护作业计划-" + GDCU_DATE_TOOL.getCurrentDate();
            HSSFWorkbook wb = gdcuAisReportService.getRadiusData(groupIds);
            writeInfoInResponse(request, response, wb, reportName);
        } catch (Exception e) {
            LOG.error("生成radius维护作业计划失败", e);
        }
    }

    private void writeInfoInResponse(HttpServletRequest request, HttpServletResponse response,
            HSSFWorkbook wb, String reportname) {
        String reportName = ToolsUtils.getDownFileName(request, reportname);
        response.setHeader("Content-disposition",
                String.format("attachment;filename=\"%s\"", reportName + ".xls"));
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");

        OutputStream output = null;
        try {
            output = response.getOutputStream();
            wb.write(output);
        } catch (IOException e) {
            LOG.error("IOException:", e);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(wb);
        }
    }

}
