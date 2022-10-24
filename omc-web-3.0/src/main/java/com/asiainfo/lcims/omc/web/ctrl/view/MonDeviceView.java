package com.asiainfo.lcims.omc.web.ctrl.view;

/**
 * 网络设备管理
 * 交换机、防火墙、路由器
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.util.BaseController;

@Controller
@RequestMapping(value = "/view/class/server/device2")
public class MonDeviceView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 页面初始化
     * 
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "")
    public String deviceManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByUrl(uri);
        if (mdMenuList != null)
            return "redirect:" + mdMenuList.get(0).getUrl();
        else
            return "redirect:/resources/unauthorized.html";
    }

    /**
     * 
     * @Title: deviceSummaryManage
     * @Description: TODO(节点总览)
     * @param @param request
     * @param @param model
     * @param @param session
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/node/summary")
    public String deviceSummaryManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2).getName();
        model.addAttribute("classtype", classtype);
        model.addAttribute("moduleid", "servermanage");
        // 类型
        List<MdParam> typelist = new ArrayList<MdParam>();
        model.addAttribute("typelist", typelist);
        // 网络类型
        List<MdParam> networkTypelist = new ArrayList<MdParam>();
        model.addAttribute("networkTypelist", networkTypelist);
        // 节点
        List<MdNode> nodeList = new ArrayList<MdNode>();
        model.addAttribute("nodelist", nodeList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/devicemanage";
    }

    @RequestMapping(value = "/node--/{nodeId}")
    public String deviceNodeManage(@PathVariable String nodeId, Model model,
            HttpServletRequest request) {
        model.addAttribute("nodeId", nodeId);
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2).getName();
        model.addAttribute("classtype", classtype);
        model.addAttribute("moduleid", "servermanage");
        // 类型
        List<MdParam> typelist = new ArrayList<MdParam>();
        model.addAttribute("typelist", typelist);
        // 网络类型
        List<MdParam> networkTypelist = new ArrayList<MdParam>();
        model.addAttribute("networkTypelist", networkTypelist);
        // 节点
        List<MdNode> nodeList = new ArrayList<MdNode>();
        model.addAttribute("nodelist", nodeList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/devicemanage";
    }

    @RequestMapping(value = "/node/host--/{nodeId}/{hostId}")
    public String deviceNodeHostManage(@PathVariable String nodeId, @PathVariable String hostId,
            Model model, HttpServletRequest request) {
        model.addAttribute("nodeId", nodeId);
        model.addAttribute("hostId", hostId);
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2).getName();
        model.addAttribute("classtype", classtype);
        model.addAttribute("moduleid", "servermanage");
        // 类型
        List<MdParam> typelist = new ArrayList<MdParam>();
        model.addAttribute("typelist", typelist);
        // 网络类型
        List<MdParam> networkTypelist = new ArrayList<MdParam>();
        model.addAttribute("networkTypelist", networkTypelist);
        // 节点
        List<MdNode> nodeList = new ArrayList<MdNode>();
        model.addAttribute("nodelist", nodeList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/devicemanage";
    }

}
