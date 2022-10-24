package com.asiainfo.lcims.omc.web.ctrl.shcm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.shcm.AuthenSituationVo;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.service.shcm.AuthenSituationAnalyseService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Controller
@RequestMapping(value = "/view/class/shcmreport/authensituationanalyse")
public class AuthenSituationAnalyseView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenSituationAnalyseView.class);

    @Autowired
    private AuthenSituationAnalyseService authenSituationAnalyseService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        String dayDate = DateUtil.getFormatTime(30, "yyyy-MM-dd") + " - "
                + DateUtil.getFormatTime(0, "yyyy-MM-dd");
        String monthDate = DateUtil.getFormatTime(30, "yyyy-MM") + " - "
                + DateUtil.getFormatTime(0, "yyyy-MM");
        model.addAttribute("dayDate", dayDate);
        model.addAttribute("monthDate", monthDate);
        List<BdNas> naslist = CommonInit.getBdNasList();
        model.addAttribute("naslist", naslist);

        return "shcm/authensituation";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public WebResult queryAuthenSituationInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        WebResult result = authenSituationAnalyseService.getAuthenSituationList(params);
        return result;
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportAuthenSituationInfo(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        List<AuthenSituationVo> tableData = authenSituationAnalyseService
                .getAuthenSituationReport(params);
        String[] field = { "cycleTime&统计时间", "authen_success&认证成功数", "authen_fail&失败认证数",
                "authen_total&总认证数" };
        String brasIp = (String) params.get("brasIp");
        String fileName = "认证情况分析报表";
        if (!ToolsUtils.StringIsNull(brasIp)) {
            fileName = fileName + "-" + brasIp;
        }
        ExcelUtil.downloadExcelToFile(fileName, DateUtil.nowDateString(), field, request, response,
                tableData);
    }
}
