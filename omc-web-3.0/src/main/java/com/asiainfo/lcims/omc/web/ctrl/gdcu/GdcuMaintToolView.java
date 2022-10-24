package com.asiainfo.lcims.omc.web.ctrl.gdcu;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
/**
 * 运维工具
 */
@Controller
@RequestMapping(value = "/view/class/mainttool")
public class GdcuMaintToolView {
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;    
    /**
     * 查询在线用户数
     * 
     * @return
     */
    @RequestMapping(value = "/queryOnlineUser")
    public String queryOnlineUser(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        MdMenu menu = mdMenuDataListener.getMdMenuByUrl(uri);
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("menuId", menu.getId());
        return "gdcu/queryOnlineUser";
    }
    
    /**
     * 在线用户统计
     * 
     * @return
     */
    @RequestMapping(value = "/onlineUserStats")
    public String onlineUserStats(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        MdMenu menu = mdMenuDataListener.getMdMenuByUrl(uri);
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("menuId", menu.getId());
        return "gdcu/onlineUserStats";
    }
    
    /**
     * 查询认证日志
     * 
     * @return
     */
    @RequestMapping(value = "/qryAccessLog")
    public String qryAccessLog(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        MdMenu menu = mdMenuDataListener.getMdMenuByUrl(uri);
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("province", MainServer.conf.getProvince());
        model.addAttribute("menuId", menu.getId());
        return "gdcu/qryAccessLog";
    }
    
    /**
     * 批量操作日志查询
     */
    @RequestMapping(value = "/offlineLog")
    public String offlineLog(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getParentMdMenuByUrl(uri).getName();
        model.addAttribute("classtype", classtype);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        return "gdcu/offlineLog";
    }

    /**
     * 批量踢用户下线
     */
    @RequestMapping(value = "/offlineBatchuser")
    public String offlineBatchuser(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getParentMdMenuByUrl(uri).getName();
        model.addAttribute("classtype", classtype);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "gdcu/offlineBatchuser";
    }

    /**
     * 批量删除在线用户
     */
    @RequestMapping(value = "/offlineKickLMUser")
    public String offlineKickLMUser(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getParentMdMenuByUrl(uri).getName();
        model.addAttribute("classtype", classtype);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "gdcu/offlineKickLMUser";
    }
}
