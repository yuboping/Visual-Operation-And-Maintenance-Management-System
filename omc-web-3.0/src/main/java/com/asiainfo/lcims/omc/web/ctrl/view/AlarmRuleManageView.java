package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmLevel;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRule;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 配置管理/告警规则管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/alarmrulemanage")
public class AlarmRuleManageView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmRuleManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Resource(name = "paramService")
    ParamService paramService;

    /**
     * 告警规则管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String alarmruleManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
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
        return "servermanage/alarmrulemanage";
    }

    /**
     * 
     * @Title: queryMdAlarmRuleList @Description: TODO(查询告警规则信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdAlarmRule> 返回类型 @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdAlarmRule> queryMdAlarmRuleList(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = getParams(request);
        List<MdAlarmRule> mdAlarmRuleList = null;
        try {
            mdAlarmRuleList = alarmRuleManageService.getMdAlarmRuleList(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMdAlarmRuleList Exception:{}", e);
        }
        return mdAlarmRuleList;
    }

    /**
     * 
     * @Title: queryInitialization @Description: TODO(初始化数据) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/initializationlist")
    @ResponseBody
    public List<Map<String, Object>> queryInitialization(HttpServletRequest request,
            HttpSession session, Model model) {
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getInitializationList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryInitialization Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryModuleList @Description: TODO(获取模块列表) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/modulelist")
    @ResponseBody
    public List<MdMenu> queryModuleList(HttpServletRequest request, HttpSession session,
            Model model) {
        List<MdMenu> mdMenuList = null;
        try {
            mdMenuList = alarmRuleManageService.getModuleList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryModuleList Exception:{}", e);
        }
        return mdMenuList;
    }

    /**
     * 
     * @Title: queryMonitorTargetOneLevelList @Description:
     *         TODO(获取一级监控目标) @param @param request @param @param
     *         session @param @param model @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/monitortargetonelevellist")
    @ResponseBody
    public List<Map<String, Object>> queryMonitorTargetOneLevelList(HttpServletRequest request,
            HttpSession session, Model model) {
        String id = request.getParameter("id");
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getMonitorTargetOneLevelList(id);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMonitorTargetOneLevelList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryMonitorTargetOneLevelList @Description:
     *         TODO(获取二级监控目标) @param @param request @param @param
     *         session @param @param model @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/monitortargettwolevellist")
    @ResponseBody
    public List<Map<String, Object>> queryMonitorTargetTwoLevelList(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getMonitorTargetTwoLevelList(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMonitorTargetTwoLevelList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryMonitorTargetOneLevelList @Description:
     *         TODO(获取三级监控目标) @param @param request @param @param
     *         session @param @param model @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/monitortargetthreelevellist")
    @ResponseBody
    public List<Map<String, Object>> queryMonitorTargetThreeLevelList(HttpServletRequest request,
            HttpSession session, Model model) {
        String id = request.getParameter("id");
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getMonitorTargetThreeLevelList(id);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMonitorTargetThreeLevelList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryMonitorTargetOneLevelList @Description:
     *         TODO(获取四级监控目标) @param @param request @param @param
     *         session @param @param model @param @return 参数 @return
     *         List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/monitortargetfourlevellist")
    @ResponseBody
    public List<Map<String, Object>> queryMonitorTargetFourLevelList(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getMonitorTargetFourLevelList(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMonitorTargetFourLevelList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryMetriclist @Description: TODO(获取指标列表) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/metriclist")
    @ResponseBody
    public List<MdMetric> queryMetricList(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = getParams(request);
        List<MdMetric> mdMetricList = null;
        try {
            mdMetricList = alarmRuleManageService.getMetriclist(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMetricList Exception:{}", e);
        }
        return mdMetricList;
    }

    /**
     * 
     * @Title: queryMetriclist @Description: TODO(获取图表列表) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/chartslist")
    @ResponseBody
    public List<Map<String, Object>> queryChartsList(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getChartsList(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryChartsList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryAllMetricList @Description: TODO(获取所有指标列表) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdMetric> 返回类型 @throws
     */
    @RequestMapping(value = "/query/allmetriclist")
    @ResponseBody
    public List<MdMetric> queryAllMetricList(HttpServletRequest request, HttpSession session,
            Model model) {
        return CommonInit.getMetricList();
    }

    @RequestMapping(value = "/query/alarmurl")
    @ResponseBody
    public String queryAlarmUrl(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        String wholeUrl = null;
        try {
            wholeUrl = alarmRuleManageService.getWholeUrl(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryAlarmUrl Exception:{}", e);
        }
        return wholeUrl;
    }

    /**
     * 
     * @Title: queryAlarmModesList @Description: TODO(查询告警方式) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdAlarmLevel> 返回类型 @throws
     */
    @RequestMapping(value = "/query/alarmmodeslist")
    @ResponseBody
    public List<Map<String, Object>> queryAlarmModesList(HttpServletRequest request,
            HttpSession session, Model model) {
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getAlarmModesList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryAlarmModesList Exception:{}", e);
        }
        return mapList;
    }

    /**
     * 
     * @Title: queryAlarmTypeList @Description: TODO(查询告警类型) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<Map<String,Object>> 返回类型 @throws
     */
    @RequestMapping(value = "/query/alarmtypelist")
    @ResponseBody
    public List<Map<String, Object>> queryAlarmTypeList(HttpServletRequest request,
            HttpSession session, Model model) {
        List<Map<String, Object>> mapList = null;
        try {
            mapList = alarmRuleManageService.getAlarmTypeList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryAlarmTypeList Exception:{}", e);
        }
        return mapList;
    }

    @RequestMapping(value = "/query/mdalarmlevellist")
    @ResponseBody
    public List<MdAlarmLevel> queryMdAlarmLevelList(HttpServletRequest request, HttpSession session,
            Model model) {
        List<MdAlarmLevel> mdAlarmLevelList = null;
        try {
            mdAlarmLevelList = alarmRuleManageService.getMdAlarmLevelList();
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView queryMdAlarmLevelList Exception:{}", e);
        }
        return mdAlarmLevelList;
    }

    /**
     * 
     * @Title: addMdAlarmRule @Description: TODO(新增告警规则信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdAlarmRule> 返回类型 @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdAlarmRule(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = alarmRuleManageService.addMdAlarmRule(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView addMdAlarmRule Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: modifyMdAlarmRule @Description: TODO(修改告警规则信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdAlarmRule> 返回类型 @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdAlarmRule(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = alarmRuleManageService.modifyMdAlarmRule(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView modifyMdAlarmRule Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: deleteMdAlarmRule @Description: TODO(删除告警规则信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdAlarmRule> 返回类型 @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdAlarmRule(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] ruleidArray = request.getParameterValues("ruleidArray[]");
        WebResult webResult = null;
        try {
            webResult = alarmRuleManageService.deleteMdAlarmRule(ruleidArray);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView deleteMdAlarmRule Exception:{}", e);
        }
        return webResult;
    }

    /**
     *
     * 
     * @Title: refreshMdAlarmRule @Description: TODO(刷新告警规则信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return WebResult 返回类型 @throws
     */
    @RequestMapping(value = "/refresh")
    @ResponseBody
    public WebResult refreshMdAlarmRule(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] ruleidArray = request.getParameterValues("ruleidArray[]");
        WebResult webResult = null;
        try {
            webResult = alarmRuleManageService.refreshMdAlarmRule(ruleidArray);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView refreshMdAlarmRule Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: dimensionModify @Description: TODO(维度更新) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return WebResult 返回类型 @throws
     */
    @RequestMapping(value = "/dimensionmodify")
    @ResponseBody
    public String dimensionModify(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        String result = null;
        try {
            result = alarmRuleManageService.dimensionModify(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView refreshMdAlarmRule Exception:{}", e);
        }
        return result;
    }
}
