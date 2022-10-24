package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.MetricTypeManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 配置管理/指标类型管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/metrictypemanage")
public class MetricTypeManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "metricTypeManageService")
    MetricTypeManageService metricTypeManageService;

    /**
     * 指标类型管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String metricTypeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/metrictypemanage";
    }

    /**
     * 
     * @Title: queryMdMetricTypeList
     * @Description: TODO(查询指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryMdMetricTypeList(@ModelAttribute MdMetricType mdMetricType,
            HttpServletRequest request, HttpSession session, Model model) {
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        Page mdMetricTypePage = metricTypeManageService.getMdMetricTypePage(mdMetricType, page);
        return mdMetricTypePage;
    }

    /**
     * 
     * @Title: addMdMetricType
     * @Description: TODO(新增指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdMetricType(@ModelAttribute MdMetricType mdMetricType,
            HttpServletRequest request, HttpSession session, Model model) {
        return metricTypeManageService.addMdMetricType(mdMetricType);
    }

    /**
     * 
     * @Title: modifyMdMetricType
     * @Description: TODO(修改指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdMetricType(@ModelAttribute MdMetricType mdMetricType,
            HttpServletRequest request, HttpSession session, Model model) {
        return metricTypeManageService.modifyMdMetricType(mdMetricType);
    }

    /**
     * 
     * @Title: deleteMdMetricType
     * @Description: TODO(删除指标类型信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdMetricType(HttpServletRequest request, HttpSession session, Model model) {
        String[] metricTypeidArray = request.getParameterValues("metricTypeidArray[]");
        return metricTypeManageService.deleteMdMetricType(metricTypeidArray);
    }

}
