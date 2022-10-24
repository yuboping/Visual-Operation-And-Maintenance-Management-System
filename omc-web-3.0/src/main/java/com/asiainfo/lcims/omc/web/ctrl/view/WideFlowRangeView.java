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
import com.asiainfo.lcims.omc.model.shcm.ChartListVo;
import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeData;
import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeVo;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.shcm.WideFlowRangeService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;

@Controller
@RequestMapping(value = "/view/class/shcmreport/wideFlowRange")
public class WideFlowRangeView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(WideFlowRangeView.class);

    @Resource(name = "wideFlowRangeService")
    WideFlowRangeService wideFlowRangeService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 宽带业务流量分析报表
     *
     * @return
     */
    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        String dayDate = DateUtil.getFormatTime(30, "yyyy-MM-dd")
                + " - " + DateUtil.getFormatTime(1, "yyyy-MM-dd");
        String monthDate = DateUtil.getFormatTime(30, "yyyy-MM")
                + " - " + DateUtil.getFormatTime(0, "yyyy-MM");
        model.addAttribute("dayDate", dayDate);
        model.addAttribute("monthDate", monthDate);

        return "shcm/wideFlowRange";
    }

    /**
     * 宽带业务流量分析报表——表格
     * @param request
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<WideFlowRangeVo> getWideFlowRangeTable(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        WideFlowRangeData wideFlowRangeData = BeanToMapUtils.toBean(WideFlowRangeData.class, params);

        List<WideFlowRangeVo> wideFlowRangeVoList = wideFlowRangeService.getWideFlowRangeList(wideFlowRangeData);
        return wideFlowRangeVoList;
    }

    /**
     * 宽带业务流量分析报表——折线图
     */
    @RequestMapping(value = "/queryChart")
    @ResponseBody
    public ChartListVo queryWideFlowRangeChart(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        WideFlowRangeData wideFlowRangeData = BeanToMapUtils.toBean(WideFlowRangeData.class, params);

        return wideFlowRangeService.getWideFlowRangeChart(wideFlowRangeData);
    }


    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportWideFlowRange( HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        WideFlowRangeData wideFlowRangeData = BeanToMapUtils.toBean(WideFlowRangeData.class, params);

        List<WideFlowRangeVo> wideFlowRangeList = wideFlowRangeService.getWideFlowRangeList(wideFlowRangeData);
        String [] field = {"stime&统计时间", "input_v4&input_v4上行流量", "output_v4&output_v4下行流量", "total_v4&total_v4总流量", "input_v6&input_v6上行流量", "output_v6&output_v6下行流量", "total_v6&total_v6总流量"};
        String fileName = "WideFlowRange";
        ExcelUtil.downloadExcelToFile(fileName,
                DateUtil.nowDateString(),
                field,
                request,
                response,
                wideFlowRangeList);
    }
}
