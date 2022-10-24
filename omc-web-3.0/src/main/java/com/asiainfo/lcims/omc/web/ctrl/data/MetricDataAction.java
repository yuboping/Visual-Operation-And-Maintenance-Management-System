package com.asiainfo.lcims.omc.web.ctrl.data;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.monitor.ChartInfo;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.monitor.MetricDataService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ExcelUtil;

@Controller
public class MetricDataAction extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(MetricDataAction.class);

    @Resource(name = "metricDataService")
    MetricDataService metricDataService;

    @Resource(name = "hostService")
    HostService hostService;

    /**
     * 查询普通折线图
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/data/monitor/getDataLine")
    @ResponseBody
    public List<ChartInfo> getMetricDataLine(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        // 获取chart配置
        List<ChartInfo> data = metricDataService.getMetricDataLine(params);
        return data;
    }

    /**
     * 查询最新一条数据
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/data/monitor/getDataRecent")
    @ResponseBody
    public List<ChartInfo> getDataRecent(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        // 获取chart配置
        List<ChartInfo> data = metricDataService.getDataRecent(params);
        return data;
    }

    /**
     * 在线用户数首页地图
     * 
     * @return
     */
    @RequestMapping(value = "/data/monitor/getOnlineDataHomeMap")
    @ResponseBody
    public WebResult getOnlineDataHomeMap(HttpServletRequest request) {
        try {
            Object result = null;
            Map<String, Object> params = getParams(request);
            result = metricDataService.getOnlineDataHomeMap(params);
            return new WebResult(true, "", result);
        } catch (Exception e) {
            LOG.error("获取数据失败", e);
            return new WebResult(false, "获取数据有误！");
        }
    }

    @RequestMapping(value = "/data/monitor/getRadiusHost")
    @ResponseBody
    public WebResult getRadiusHost() {
        try {
            Object result = null;
            result = metricDataService.getHostDataRadius();
            return new WebResult(true, "", result);
        } catch (Exception e) {
            LOG.error("获取数据失败", e);
            return new WebResult(false, "获取数据有误！");
        }
    }

    /**
     * 查询首页地图数据 配置开发： 查询指标配置、告警配置开关
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/data/monitor/getDataStaticSql")
    @ResponseBody
    public List<ChartInfo> getDataStaticSql(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        // 获取chart配置
        List<ChartInfo> data = metricDataService.getDataStaticSql(params);
        return data;
    }

    /**
     * 主机基本信息图表
     */
    @RequestMapping(value = "/data/monitor/getDataHostInfo")
    @ResponseBody
    public MonHost getDataHostInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        String paramid = (String) params.get("paramid");
        MonHost host = hostService.getHostById(paramid);
        return host;
    }

    /**
     * 
     * @Title: exportRadiusHostAuth @Description: TODO(导出普通折线图) @param @param
     *         session @param @param request @param @param response 参数 @return
     *         void 返回类型 @throws
     */
    @RequestMapping(value = "/data/monitor/exportDataLine")
    public void exportRadiusHostAuth(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        String queryDate = params.get("queryDate").toString();
        String chart_name = params.get("chart_name").toString();
        // 获取chart配置
        List<ChartInfo> data = metricDataService.getMetricDataLine(params);
        String title = queryDate
                + CommonInit.getChartDetailByChartName(chart_name).getChart_title();
        String[][] datas = { { "1", "2" }, { "3", "4" } };
        ExcelUtil.downloadExcelFile(title, queryDate, datas[0], request, response, datas);
    }

    @RequestMapping(value = "/data/monitor/getMajorMetric")
    @ResponseBody
    public List<String> majorMetric(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        List<String> data = metricDataService.getMajorMetric(params);
        LOG.info("major metric is {}", data);
        return data;
    }

    @RequestMapping(value = "/data/monitor/gscmMajorMetric")
    @ResponseBody
    public String gscmMetric(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        String data = metricDataService.getGscmMetric(params);
        LOG.info("major metric is {}", data);
        return data;
    }
}
