package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.service.ais.AisReportService;

/**
 * 自动巡检模块视图
 * 
 * @author luohuawuyin
 *
 */
@Controller
public class AisView {
    @Resource(name = "aisReportService")
    private AisReportService aisReportService;
    
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @RequestMapping(value = "/view/class/ais/inspection")
    public String aisInspection(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("moduleid", "ais");
        model.addAttribute("now", new Date());
        model.addAttribute("province", MainServer.conf.getProvince());
        return "ais/inspection_new";
    }

    @RequestMapping(value = "/view/class/ais/schedule/addinit")
    public String addScheduleInit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("sendEmail", CommonInit.BUSCONF.getAISEmailNotice());
        session.setAttribute("sendSms", CommonInit.BUSCONF.getAISSmsNotice());
        return "ais/plan";
    }

    @RequestMapping(value = "/view/class/ais/schedule/savefineinit")
    public String savefineScheduleInit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("sendEmail", CommonInit.BUSCONF.getAISEmailNotice());
        session.setAttribute("sendSms", CommonInit.BUSCONF.getAISSmsNotice());
        return "ais/plan";
    }

//    @RequestMapping(value = "/view/class/ais/schedule/editinit/{id}")
    @RequestMapping(value = "/view/class/ais/schedule/{querytype}/{id}")
    public String editScheduleInit(HttpServletRequest request, Model model,@PathVariable("querytype") String querytype,
            @PathVariable("id") String id) {
        HttpSession session = request.getSession();
        session.setAttribute("sendEmail", CommonInit.BUSCONF.getAISEmailNotice());
        session.setAttribute("sendSms", CommonInit.BUSCONF.getAISSmsNotice());
        return "ais/plan";
    }
}
