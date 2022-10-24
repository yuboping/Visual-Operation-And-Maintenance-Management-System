package com.asiainfo.lcims.omc.web.ctrl.shcm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.shcm.AuthFailResonVo;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.service.shcm.ShcmReportService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Controller
@RequestMapping(value = "/view/class/shcmreport")
public class ShcmReportView extends BaseController {
    @Resource(name = "shcmReportService")
    ShcmReportService shcmReportService;
    
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 认证失败原因分析报表
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/authFailReasonReport")
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
        List<BdNas> naslist = CommonInit.getBdNasList();
        model.addAttribute("naslist", naslist);
        return "shcm/authFailReasonReport";
    }
    
    /**
     * 认证失败原因分析报表 查询功能
     * @param request
     * @return
     */
    @RequestMapping(value = "/authFailReasonReport/query")
    @ResponseBody
    public WebResult authFailReasonReportQuery(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        WebResult result = shcmReportService.authFailReasonReportQuery(params);
        return result;
    }
    
    
    @RequestMapping(value = "/authFailReasonReport/export")
    @ResponseBody
    public void exportOnlineUserStatisticData( HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        
        List<AuthFailResonVo> tableData = shcmReportService.getAuthFailReasonReport(params);
        
        String [] field = {"cycleTime&统计时间", "adsl_request&宽带认证请求数",
                "adsl_success&宽带认证成功数", "adsl_failcode1&未注册帐号",
                "adsl_failcode2&密码错误","adsl_failcode3&用户加锁",
                "adsl_failcode4&限时上网","adsl_failcode5&vlan错误",
                "adsl_failcode6&唯一性拒绝"};
        String brasIp = (String) params.get("brasIp");
        String fileName = "认证失败原因分析报表";
        if(!ToolsUtils.StringIsNull(brasIp)) {
            fileName = fileName + "-" + brasIp;
        }
//        String fileName = "在线用户情况分析报表";
        ExcelUtil.downloadExcelToFile(fileName,
                DateUtil.nowDateString(),
                field,
                request,
                response,
                tableData);
    }
}
