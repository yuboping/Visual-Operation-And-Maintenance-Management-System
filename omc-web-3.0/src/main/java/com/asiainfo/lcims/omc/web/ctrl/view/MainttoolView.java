package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowResultDao;
import com.asiainfo.lcims.omc.service.business.HostPerformaceService;
import com.asiainfo.lcims.omc.util.BaseController;

@Controller
@RequestMapping(value = "/view/class/mainttool")
public class MainttoolView extends BaseController {
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;
    @Autowired
    MdFlowResultDao flowResult;

    @Resource(name = "hostPerformaceService")
    HostPerformaceService hostPerformaceService;

    /**
     * 业务全流程监控
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/flowmonitor")
    public String adminManage(HttpServletRequest request, Model model, HttpSession session) {
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(request.getRequestURI()).getName());
        model.addAttribute("flowlist", flowResult.getAllFlowResult());
        String username = String.valueOf(session.getAttribute("username"));
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByUrl(request.getRequestURI());
        model.addAttribute("buttons", mdMenuList);
        return "mainttool/flowmonitor";
    }

    @RequestMapping(value = "/hostinfoshow")
    public String deviceManage(HttpServletRequest request, Model model, HttpSession session) {
        /**
         * 山西联通个性化页面
         */
        String uri = request.getRequestURI();
        MdMenu menu = mdMenuDataListener.getMdMenuByUrl(uri);
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("menuId", menu.getId());
        return "hostperformance/host_performance";
    }
    
    /**
     * 一键
     * @param request
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/keyoperate")
    public String keyMmergency(HttpServletRequest request, Model model, HttpSession session) {
        /**
         * 一键应急页面
         */
        String uri = request.getRequestURI();
        MdMenu menu = mdMenuDataListener.getMdMenuByUrl(uri);
        String province = MainServer.conf.getProvince();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("menuId", menu.getId());
        model.addAttribute("nodelist", CommonInit.getNodeList());
        if("dev".equals(province) || "develop".equals(province)) {
            province = "";
        }
        return "mainttool/keyoperate";
    }
    
}
