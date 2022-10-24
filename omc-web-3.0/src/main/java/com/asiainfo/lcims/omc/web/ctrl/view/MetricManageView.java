package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.MetricManageService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 配置管理/指标管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/metricmanage")
public class MetricManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "metricManageService")
    MetricManageService metricManageService;

    @Resource(name = "paramService")
    ParamService paramService;
    
    /**
     * 指标管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String metricManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        List<MdMetricType> mdMetricTypeList = metricManageService.getMdMetricTypeList();
        model.addAttribute("mdMetricTypeList", mdMetricTypeList);
        return "servermanage/metricmanage";
    }

    /**
     * 
     * @Title: queryMdMetricList
     * @Description: TODO(查询指标信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdMetric> queryMdMetricList(@ModelAttribute MdMetric mdMetric,
            HttpServletRequest request, HttpSession session, Model model) {
        List<MdMetric> mdMetricList = metricManageService.getMdMetricList(mdMetric);
        return mdMetricList;
    }

    /**
     * 
     * @Title: queryMdCollCycle
     * @Description: TODO(查询采集周期信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/mdcollcyclelist")
    @ResponseBody
    public List<MdCollCycle> queryMdCollCycle(HttpServletRequest request, HttpSession session,
            Model model) {
        return metricManageService.getMdCollCycleList();
    }

    /**
     * 
     * @Title: queryMdParam
     * @Description: TODO(查询脚本数据返回格式类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/mdparamlist")
    @ResponseBody
    public List<MdParam> queryMdParam(@ModelAttribute MdParam mdParam, HttpServletRequest request,
            HttpSession session, Model model) {
        return metricManageService.getParamByType(mdParam);
    }

    /**
     * 
     * @Title: queryMdCollCyc
     * @Description: TODO(查询指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/mdmetrictypelist")
    @ResponseBody
    public List<MdMetricType> queryMdCollCyc(HttpServletRequest request, HttpSession session,
            Model model) {
        return metricManageService.getMdMetricTypeList();
    }
    
    /**
     * 
     * @Title: queryServerType
     * @Description: TODO(查询指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/servertypelist")
    @ResponseBody
    public List<MdParam> queryServertypelist(HttpServletRequest request, HttpSession session,
            Model model) {
    	List<MdParam> codetype = paramService.getMdParamList("106");
        return codetype;
    }

    /**
     * 
     * @Title: addMdMetric
     * @Description: TODO(新增指标信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdMetric(@ModelAttribute MdMetric mdMetric, HttpServletRequest request,
            HttpSession session, Model model) {
        return metricManageService.addMdMetric(mdMetric);
    }

    /**
     * 
     * @Title: modifyMdMetric
     * @Description: TODO(修改指标信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdMetric(@ModelAttribute MdMetric mdMetric, HttpServletRequest request,
            HttpSession session, Model model) {
        return metricManageService.modifyMdMetric(mdMetric);
    }

    /**
     * 
     * @Title: deleteMdMetric
     * @Description: TODO(删除指标信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdMetric(HttpServletRequest request, HttpSession session, Model model) {
        String[] metricidArray = request.getParameterValues("metricidArray[]");
        return metricManageService.deleteMdMetric(metricidArray);
    }
}
