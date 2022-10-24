package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.HostResourcesStatisticsService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 运维工具/主机性能资源统计
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/mainttool/hostresourcesstatistics")
public class HostResourcesStatisticsView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(HostResourcesStatisticsView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "hostResourcesStatisticsService")
    HostResourcesStatisticsService hostResourcesStatisticsService;

    /**
     * 主机性能资源统计
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String hostResourcesStatistics(HttpServletRequest request, Model model,
            HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "mainttool/hostresourcesstatistics";
    }

    /**
     * 
     * @Title: queryDayReport
     * @Description: TODO(查询日报表)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/dayreport")
    @ResponseBody
    public List<Map<String, Object>> queryDayReport(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> result = null;
        try {
            result = hostResourcesStatisticsService.getDayReport(map);
        } catch (Exception e) {
            LOG.error("HostResourcesStatisticsView queryDayReport Exception:{}", e);
        }
        return result;
    }

    /**
     * 
     * @Title: queryDayReport
     * @Description: TODO(查询周报表)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/weekreport")
    @ResponseBody
    public List<Map<String, Object>> queryWeekReport(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> result = null;
        try {
            result = hostResourcesStatisticsService.getWeekReport(map);
        } catch (Exception e) {
            LOG.error("HostResourcesStatisticsView queryWeekReport Exception:{}", e);
        }
        return result;
    }

    /**
     * 
     * @Title: queryMonthReport
     * @Description: TODO(查询月报表)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/monthreport")
    @ResponseBody
    public List<Map<String, Object>> queryMonthReport(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> result = null;
        try {
            result = hostResourcesStatisticsService.getMonthReport(map);
        } catch (Exception e) {
            LOG.error("HostResourcesStatisticsView getMonthReport Exception:{}", e);
        }
        return result;
    }

    /**
     * 
     * @Title: exportReport
     * @Description: TODO(报表导出)
     * @param @param request
     * @param @param response
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    @RequestMapping(value = "/export")
    @ResponseBody
    public WebResult exportReport(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = hostResourcesStatisticsService.exportExcel(request, response, map);
        } catch (Exception e) {
            LOG.error("HostResourcesStatisticsView exportReport Exception:{}", e);
        }
        return webResult;
    }
}
