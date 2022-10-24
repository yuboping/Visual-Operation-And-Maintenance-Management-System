package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 运维工具
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class")
public class MdMenuView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 首页
     * 
     * @return
     */
    @RequestMapping(value = "/home")
    public String moduleMonitor(Model model) {

        model.addAttribute("classtype", "home");
        model.addAttribute("moduleid", "home");
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
            return MainServer.conf.getProvince() + "/home";
        } else {
            return "home";
        }
    }

    /**
     * 
     * @Title: monitorModule
     * @Description: TODO(module菜单)
     * @param @param classtype
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/{classtype}/module")
    public String monitorModule(@PathVariable String classtype, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdMenu> menuList = mdMenuDataListener.getChildrenMdMenuByName(classtype);
        List<MdMenu> mdMenuList = mdMenuDataListener.getAlarmCount(menuList);
        model.addAttribute("mdMenuList", mdMenuList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "monitor/busmodule";
    }

    /**
     * 
     * @Title: getLeftMenuListOther
     * @Description: TODO(显示左侧菜单)
     * @param @param classtype
     * @param @param session
     * @param @param request
     * @param @param model
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/{classtype}")
    public String getLeftMenuListOther(@PathVariable String classtype, HttpSession session,
            HttpServletRequest request, Model model) {
        String roleid = String.valueOf(session.getAttribute(Constant.ADMIN_ROLEID_STRING));
        String username = String.valueOf(session.getAttribute("username"));
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByName(classtype);
        if (mdMenuList != null) {
            List<MdMenu> mdMenus = mdMenuDataListener.getMenuWithRolePermission(username,
                    mdMenuList, roleid);
            return "redirect:" + mdMenus.get(0).getUrl();
        } else
            return "redirect:/resources/unauthorized.html";
    }

    /**
     * 
     * @Title: querychildrenmdmenu
     * @Description: TODO(查询子菜单)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMenu> 返回类型
     * @throws
     */
    @RequestMapping(value = "/querychildrenmdmenu")
    @ResponseBody
    public List<MdMenu> querychildrenmdmenu(HttpServletRequest request, HttpSession session,
            Model model) {
        String pageUrl = request.getParameter("pageUrl");
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByUrl(pageUrl);
        return mdMenuList;
    }

    /**
     * 
     * @Title: refreshMenuList
     * @Description: TODO(刷新菜单)
     * @param @param request
     * @param @param session
     * @param @param model 参数
     * @return void 返回类型
     * @throws
     */
    @RequestMapping(value = "/refreshMenuList")
    public void refreshMenuList(HttpServletRequest request, HttpSession session, Model model) {
        // 加载菜单
        mdMenuDataListener.loadMenuList();
    }
}
