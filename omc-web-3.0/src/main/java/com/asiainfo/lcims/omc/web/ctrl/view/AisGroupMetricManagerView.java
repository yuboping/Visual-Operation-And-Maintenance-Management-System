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
import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.ais.AisGroupMetricManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 巡检组指标关系配置类
 */
@Controller
@RequestMapping(value = "/view/class/ais/aisgroupmetricmanage")
public class AisGroupMetricManagerView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AisGroupMetricManagerView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "aisGroupMetricManageService")
    AisGroupMetricManageService aisGroupMetricManageService;

    /**
     * 巡检组指标关系配置初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String aisGroupMetricManageManage(HttpServletRequest request, Model model,
            HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "ais/aisgroupmetricmanage";
    }

    /**
     * 
     * @Title: queryAisGroupMetricList
     * @Description: TODO(查询巡检组指标信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdAlarmRule> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<AisGroupMetricModel> queryAisGroupMetricList(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<AisGroupMetricModel> aisGroupMetricModelList = null;
        try {
            aisGroupMetricModelList = aisGroupMetricManageService.getAisGroupMetricList(map);
        } catch (Exception e) {
            LOG.error("AisGroupMetricManagerView queryAisGroupMetricList Exception:{}", e);
        }
        return aisGroupMetricModelList;
    }

    /**
     * 
     * @Title: queryAisGroupList
     * @Description: TODO(获取巡检组列表)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/aisgrouplist")
    @ResponseBody
    public List<AisGroupModel> queryAisGroupList(HttpServletRequest request, HttpSession session,
            Model model) {
        List<AisGroupModel> aisGroupModelList = null;
        try {
            aisGroupModelList = aisGroupMetricManageService.getAisGroupList();
        } catch (Exception e) {
            LOG.error("AisGroupMetricManagerView queryAisGroupList Exception:{}", e);
        }
        return aisGroupModelList;
    }

    /**
     * 
     * @Title: addAisGroupMetric
     * @Description: TODO(新增巡检组指标)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdAlarmRule> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addAisGroupMetric(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = aisGroupMetricManageService.addAisGroupMetric(map);
        } catch (Exception e) {
            LOG.error("AisGroupMetricManagerView addAisGroupMetric Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: modifyAisGroupMetric
     * @Description: TODO(修改巡检组指标)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdAlarmRule> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyAisGroupMetric(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = aisGroupMetricManageService.modifyAisGroupMetric(map);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView modifyAisGroupMetric Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: deleteAisGroupMetric
     * @Description: TODO(删除巡检组指标)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdAlarmRule> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteAisGroupMetric(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] groupmetricidArray = request.getParameterValues("groupmetricidArray[]");
        WebResult webResult = null;
        try {
            webResult = aisGroupMetricManageService.deleteAisGroupMetric(groupmetricidArray);
        } catch (Exception e) {
            LOG.error("AlarmRuleManageView deleteAisGroupMetric Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: queryAisGroupMetricDetailList
     * @Description: TODO(查询巡检组指标明细)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<AisGroupMetricModel> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/aisgroupmetricdetaillist")
    @ResponseBody
    public List<AisGroupMetricModel> queryAisGroupMetricDetailList(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        List<AisGroupMetricModel> aisGroupMetricModelList = null;
        try {
            aisGroupMetricModelList = aisGroupMetricManageService.getAisGroupMetricDetailList(map);
        } catch (Exception e) {
            LOG.error("AisGroupMetricManagerView queryAisGroupMetricDetailList Exception:{}", e);
        }
        return aisGroupMetricModelList;
    }
}
