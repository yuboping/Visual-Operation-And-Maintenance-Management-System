package com.asiainfo.lcims.omc.web.ctrl.gscm;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
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
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.util.BaseController;

@Controller
@RequestMapping(value = "/view/class/mainttool/obsauthlog")
public class ObsAuthLogView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ObsAuthLogView.class);

    @Autowired
    private MdMenuDataListener mdMenuDataListener;

    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getParentMdMenuByUrl(uri).getName();
        model.addAttribute("classtype", classtype);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "gscm/obsauthlog";
    }

    @RequestMapping(value = "/obsloginurl")
    @ResponseBody
    public WebResult obsloginurl() {
        return new WebResult(true, new BusinessConf().getObsLoginUrl());
    }

    @RequestMapping(value = "/obsreporturl")
    @ResponseBody
    public WebResult obsreporturl() {
        return new WebResult(true, new BusinessConf().getObsReportUrl());
    }
}
