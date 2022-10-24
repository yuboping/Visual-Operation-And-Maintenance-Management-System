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
import com.asiainfo.lcims.omc.model.shcm.LineChartData;
import com.asiainfo.lcims.omc.model.shcm.OnlineUserStatisticVo;
import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.shcm.OnlineUserStatisticService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;

/**
 * 在线用户情况分析
 * @author ZP
 *
 */
@Controller
@RequestMapping(value = "/view/class/shcmreport/onlineuserstatictic")
public class OnlineUserStatisQueryView extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(OnlineUserStatisQueryView.class);

    @Resource(name = "onlineUserStatisticService")
    OnlineUserStatisticService onlineUserStatisticService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 在线用户情况分析查询
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
                + " - " + DateUtil.getFormatTime(0, "yyyy-MM-dd");
        String monthDate = DateUtil.getFormatTime(30, "yyyy-MM")
                + " - " + DateUtil.getFormatTime(0, "yyyy-MM");
        model.addAttribute("dayDate", dayDate);
        model.addAttribute("monthDate", monthDate);

        return "shcm/onlineUserStatistic";
    }
    
    /**
     * 查询在线用户情况分析
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<StatisData> queryOnlineUserStatisticInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        OnlineUserStatisticVo onlineUserStatisticVo = BeanToMapUtils.toBean(OnlineUserStatisticVo.class, params);

        List<StatisData> statisInfoList = onlineUserStatisticService.getOnlineUserStatisticList(onlineUserStatisticVo);
        return statisInfoList;
    }

    /**
     * 查询在线用户情况分析
     */
    @RequestMapping(value = "/queryChart")
    @ResponseBody
    public LineChartData queryOnlineUserStatisticChart(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        OnlineUserStatisticVo onlineUserStatisticVo = BeanToMapUtils.toBean(OnlineUserStatisticVo.class, params);

        LineChartData onlineUserStatisticWithGraph = onlineUserStatisticService.getOnlineUserStatisticWithGraph(onlineUserStatisticVo);
        return onlineUserStatisticWithGraph;
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportOnlineUserStatisticData( HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        OnlineUserStatisticVo onlineUserStatisticVo = BeanToMapUtils.toBean(OnlineUserStatisticVo.class, params);

        List<StatisData> statisInfoList = onlineUserStatisticService.getOnlineUserStatisticList(onlineUserStatisticVo);
        String [] field = {"stime&统计时间", "mvalue&在线用户数"};
        String fileName = "onlineUserStatistic";
//        String fileName = "在线用户情况分析报表";
        ExcelUtil.downloadExcelToFile(fileName,
                DateUtil.nowDateString(),
                field,
                request,
                response,
                statisInfoList);
    }

}
