package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdCollectAgreement;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.CollectagreementService;
import com.asiainfo.lcims.omc.service.configmanage.MetricManageService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 采集协议插件管理
 */
@Controller
@RequestMapping(value = "/view/class/system/collectagreement")
public class CollectagreementView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "collectagreementService")
    CollectagreementService collectagreementService;

    @Resource(name = "paramService")
    ParamService paramService;

    /**
     * 指标管理
     *
     * @return
     */
    @RequestMapping(value = "")
    public String collectAgreement(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        List<MdMetricType> mdMetricTypeList = collectagreementService.getMdMetricTypeList();
        model.addAttribute("mdMetricTypeList", mdMetricTypeList);
        return "gscm5G/collectagreement";
    }

    /**
     *
     * @Title: querycollectAgreementList
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
    public List<MdCollectAgreement> querycollectAgreementList(@ModelAttribute MdCollectAgreement mdCollectAgreement,
                                            HttpServletRequest request, HttpSession session, Model model) {
        List<MdCollectAgreement> mdCollectAgreementList = collectagreementService.getCollectAgreementList(mdCollectAgreement);
        return mdCollectAgreementList;
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
        return collectagreementService.getMdCollCycleList();
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
        return collectagreementService.getParamByType(mdParam);
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
        return collectagreementService.getMdMetricTypeList();
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
    public WebResult addMdCollectAgreement(@ModelAttribute MdCollectAgreement mdCollectAgreement, HttpServletRequest request,
                                 HttpSession session, Model model) {
        return collectagreementService.addMdCollectAgreement(mdCollectAgreement);
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
    public WebResult modifyMdCollectAgreement(@ModelAttribute MdCollectAgreement mdCollectAgreement, HttpServletRequest request,
                                    HttpSession session, Model model) {
        return collectagreementService.modifyMdCollectAgreement(mdCollectAgreement);
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
    public WebResult deleteMdCollectAgreement(HttpServletRequest request, HttpSession session, Model model) {
        String[] protocolidArray = request.getParameterValues("protocolidArray[]");
        return collectagreementService.deleteMdCollectAgreement(protocolidArray);
    }

}
