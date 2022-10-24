package com.asiainfo.lcims.omc.web.ctrl.shcm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.asiainfo.lcims.omc.model.alarm.MdAlarmHisInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmLevel;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.monitor.ChartWithGraph;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.service.alarm.AlarmService;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.page.Page;

@Controller
@RequestMapping(value = "/view/class/system/shcmalarmquery")
public class SHCMAlarmQueryView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SHCMAlarmQueryView.class);

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Autowired
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

        return "alarmmanage/alarmshcmhistory";
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
        Page alarmInfoList = alarmService.getAlarmInfoList(alarmInfo, params, pageNumber);
        return alarmInfoList;
    }

    /**
     * 查询指标信息
     */
    @RequestMapping(value = "/getAlarm")
    @ResponseBody
    public List<Object> getAlarmQueryInfo(HttpServletRequest request) {
        List<Object> list = new ArrayList<Object>();
        List<MdMetric> metriclist = CommonInit.getMetricList();
        List<MdAlarmLevel> alarmlevel = mdAlarmInfoDao.getAllAlarmLevelList();
        list.add(metriclist);
        list.add(alarmlevel);
        return list;
    }

    /**
     * 确认告警信息
     * 
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmAlarm")
    @ResponseBody
    public WebResult confirmAlarmWithUser(HttpServletRequest request, HttpSession session) {
        // 获取当前登录用户名
        String username = (String) session.getAttribute("username");
        String[] alarmArray = request.getParameterValues("alarmArray[]");
        WebResult result = alarmService.confirmAlarmInfo(username, alarmArray);
        return result;
    }

    /**
     * 获取告警历史信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/hisalarmlist")
    @ResponseBody
    public List<MdAlarmHisInfo> getAlarmHistoryList(HttpServletRequest request) {
        String alarm_id = request.getParameter("alarm_id");
        List<MdAlarmHisInfo> mdAlarmHisInfoList = alarmService.getAlarmHisList(alarm_id);
        return mdAlarmHisInfoList;
    }

    /**
     * 查询告警信息
     */
    @RequestMapping(value = "/querywithindex")
    @ResponseBody
    public List<MdAlarmInfo> queryInfoWithIndex(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        alarmInfo.setConfirm_name(user.getAdmin());
        List<MdAlarmInfo> alarmInfoList = alarmService.getAlarmListWithIndexConfirm(alarmInfo);
        return alarmInfoList;
    }

    /**
     * 查询告警信息
     */
    @RequestMapping(value = "/querywithgraphlist")
    @ResponseBody
    public List<MdAlarmInfo> queryInfoWithGraphList(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
        List<MdAlarmInfo> alarmInfoList = alarmService.getAlarmListWithIndexConfirm(alarmInfo);
        return alarmInfoList;
    }

    /**
     * 查询告警统计图
     */
    @RequestMapping(value = "/query/alarmgraph")
    @ResponseBody
    public ChartWithGraph queryInfoWithIndexGraph(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdAlarmInfo alarmInfo = BeanToMapUtils.toBean(MdAlarmInfo.class, params);
        ChartWithGraph chartWithGraph = alarmService.getAlarmInfoWithIndexGraph(alarmInfo);
        return chartWithGraph;
    }

    /**
     * 删除告警信息
     * 
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/deleteAlarm")
    @ResponseBody
    public WebResult deleteAlarmWithUser(HttpServletRequest request, HttpSession session) {
        // 获取当前登录用户名
        String[] alarmArray = request.getParameterValues("alarmArray[]");
        WebResult result = alarmService.deleteAlarmInfo(alarmArray);
        return result;
    }

    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<MdAlarmInfo> queryDetailAlarmInfo(HttpServletRequest request) {
        String alarmId = request.getParameter("alarm_id");
        System.out.println(alarmId);
        List<MdAlarmInfo> alarmInfoList = alarmService.getDetailAlarmInfo(alarmId);
        return alarmInfoList;
    }

}
