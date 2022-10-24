package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmHisInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.service.alarm.AlarmService;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 告警信息查询
 * @author ZP
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/alarmhisquery")
public class AlarmHisQueryView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(AlarmHisQueryView.class);

    @Resource(name = "alarmService")
    AlarmService alarmService;

    @Inject
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 告警信息查询
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("moduleid", "alarmquery");

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        List<Map<String, Object>> initializationList = null;
        try {
            initializationList = alarmRuleManageService.getInitializationList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView alarmruleManage Exception:{}", e);
        }
        model.addAttribute("initializationList", initializationList);
        
        return "alarmmanage/alarmHisReportForms";
    }
    
    /**
     * 查询告警信息
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);

        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page alarmInfoList = alarmService.getAlarmHisInfoList(alarmInfo, params, pageNumber);
        return alarmInfoList;
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public WebResult exportBdNas( HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
        WebResult webResult = alarmService.exportExcel(response, request, alarmInfo, params);
        return webResult;
    }

    @RequestMapping(value = "/exportwithlevel")
    @ResponseBody
    public WebResult exportAlarmInfoWithLevel( HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
        WebResult webResult = alarmService.exportExcelWithLevel(response, request, alarmInfo, params);
        return webResult;
    }
}
