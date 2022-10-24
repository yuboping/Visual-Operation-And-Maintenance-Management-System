package com.asiainfo.lcims.omc.web.ctrl.hncu;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfoHn;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmLevel;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmInfo;
import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmResult;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.persistence.hncu.RealTimeAlarmDAO;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.service.hncu.RealTimeAlarmService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.page.Page;

@Controller
@RequestMapping(value = "/view/class/mainttool/realtime")
public class RealTimeAlarmView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RealTimeAlarmView.class);

    @Autowired
    private RealTimeAlarmService realTimeAlarmService;

    @Inject
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Autowired
    private RealTimeAlarmDAO realTimeAlarmDAO;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 告警查询页面--河南
     * @param request
     * @param session
     * @param model
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

        return "alarmmanage/alarmHistoryHn";
    }

    /**
     * 查询告警级别信息
     */
    @RequestMapping(value = "/getAlarm")
    @ResponseBody
    public List<MdAlarmLevel> getAlarmQueryInfo(HttpServletRequest request) {
        List<MdAlarmLevel> alarmlevel = mdAlarmInfoDao.getAllAlarmLevelList();
        return alarmlevel;
    }

    /**
     * 查询告警类型信息
     * @return
     */
    @RequestMapping(value = "/getAlarmTypeList")
    @ResponseBody
    public List<Map<String, Object>> queryMdAlarmLevelList() {
        return realTimeAlarmDAO.getAlarmTypeList();
    }

    /**
     * 查询告警信息
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfoHn alarmInfohn = BeanToMapUtils.toBean(MdAlarmInfoHn.class, params);

        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page alarmInfoHnList = realTimeAlarmService.getAlarmInfoHnList(alarmInfohn, params, pageNumber);
        return alarmInfoHnList;
    }

    /**
     * 确认告警信息
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmAlarm")
    @ResponseBody
    public WebResult confirmAlarmWithUser(HttpServletRequest request, HttpSession session){
        String[] alarmArray = request.getParameterValues("alarmArray[]");
        WebResult result = realTimeAlarmService.confirmAlarmInfo(alarmArray);
        return result;
    }

    @RequestMapping(value = "/handler")
    @ResponseBody
    public RealTimeAlarmResult sendRealTimeAlarm(@RequestBody RealTimeAlarmInfo realTimeAlarmInfo,
            HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        RealTimeAlarmResult result = realTimeAlarmService.handlerRealTimeAlarm(realTimeAlarmInfo);
        LOG.info("real time alarm result is : {}", result);
        return result;
    }

}
