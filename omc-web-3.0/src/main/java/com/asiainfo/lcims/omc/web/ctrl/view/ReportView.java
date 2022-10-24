package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.report.ReportService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 报表统计页面
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/report")
public class ReportView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "reportService")
    ReportService reportService;

    /**
     * 报表页面
     * 
     * @return
     */
    @RequestMapping(value = "{menuTreeName}")
    public String hostResourcesStatistics(@PathVariable String menuTreeName,
            HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "report/reportpage";
    }

    /**
     * 
     * @Title: queryReportType
     * @Description: TODO(查询报表类型)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/reporttype")
    @ResponseBody
    public List<Map<String, Object>> queryReportType(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> result = null;
        try {
            result = reportService.getReportType(map);
        } catch (Exception e) {
            LOG.error("ReportView queryReportType Exception:{}", e);
        }
        return result;
    }
}
