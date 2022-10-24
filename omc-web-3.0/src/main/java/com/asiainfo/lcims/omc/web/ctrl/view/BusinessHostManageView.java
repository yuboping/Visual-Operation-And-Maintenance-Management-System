package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.BusinessHostManageService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 配置管理/业务关联主机配置管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/businesshostmanage")
public class BusinessHostManageView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessHostManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "businessHostManageService")
    BusinessHostManageService businessHostManageService;

    @Resource(name = "hostService")
    HostService hostService;

    @Resource(name = "menuService")
    MenuService menuService;

    /**
     * 业务关联主机配置管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String businessHostManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        // 查询所有主机
        List<MonHost> hostlist = hostService.getAllHost();
        model.addAttribute("hostlist", hostlist);
        // 查询主机动态菜单
        List<MdMenu> mdMenuHostList = menuService.getMdMenuHostList();
        model.addAttribute("mdMenuHostList", mdMenuHostList);
        return "servermanage/businesshostmanage";
    }

    /**
     * 
     * @Title: queryMdBusinessHostList
     * @Description: TODO(查询业务关联主机配置信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryMdBusinessHostList(@ModelAttribute MdBusinessHost mdBusinessHost,
            HttpServletRequest request, HttpSession session, Model model) {
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        Page mdBusinessHostPage = businessHostManageService.getMdBusinessHostPage(mdBusinessHost,
                page);
        return mdBusinessHostPage;
    }

    /**
     * 
     * @Title: queryMonHost
     * @Description: TODO(查询主机列表)
     * @param @param mdParam
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MonHost> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/hostlist")
    @ResponseBody
    public List<MonHost> queryMonHost(@ModelAttribute MdParam mdParam, HttpServletRequest request,
            HttpSession session, Model model) {
        return hostService.getAllHost();
    }

    /**
     * 
     * @Title: queryMdMenuHost
     * @Description: TODO(主机动态菜单)
     * @param @param mdParam
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MonHost> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/mdmenuhostlist")
    @ResponseBody
    public List<MdMenu> queryMdMenuHost(@ModelAttribute MdParam mdParam,
            HttpServletRequest request, HttpSession session, Model model) {
        return menuService.getMdMenuHostList();
    }

    /**
     * 
     * @Title: addMdBusinessHost
     * @Description: TODO(新增业务关联主机配置信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdBusinessHost(@ModelAttribute MdBusinessHost mdBusinessHost,
            HttpServletRequest request, HttpSession session, Model model) {
        WebResult webResult = null;
        try {
            webResult = businessHostManageService.addMdBusinessHost(mdBusinessHost,true);
        } catch (Exception e) {
            LOG.error("BusinessHostManageView addMdBusinessHost Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: modifyMdBusinessHost
     * @Description: TODO(修改业务关联主机配置信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdBusinessHost(@ModelAttribute MdBusinessHost mdBusinessHost,
            HttpServletRequest request, HttpSession session, Model model) {
        return businessHostManageService.modifyMdBusinessHost(mdBusinessHost);
    }

    /**
     * 
     * @Title: deleteMdBusinessHost
     * @Description: TODO(删除业务关联主机配置信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdBusinessHost(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] businessHostidArray = request.getParameterValues("businessHostidArray[]");
        return businessHostManageService.deleteMdBusinessHost(businessHostidArray,true);
    }

}
