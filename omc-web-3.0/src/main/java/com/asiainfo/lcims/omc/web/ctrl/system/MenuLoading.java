package com.asiainfo.lcims.omc.web.ctrl.system;

import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.util.BaseController;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/system/menu")
public class MenuLoading extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(MenuLoading.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 
     * @Title: getTopMenuList
     * @Description: 返回头部菜单
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/top")
    @ResponseBody
    public List<MdMenu> getTopMenuList(HttpServletRequest request, HttpSession session, Model model) {
        String pageUrl = request.getParameter("pageUrl");
        mdMenuDataListener.setMdMenuActive(pageUrl);
        List<MdMenu> mdMenuList = mdMenuDataListener.getTopMenuList();
        List<MdMenu> moveMdMenuChildrenList = mdMenuDataListener.moveMdMenuChildren(mdMenuList);
        return moveMdMenuChildrenList;
    }

    /**
     * 
     * @Title: getLeftMenuList
     * @Description: 返回左侧菜单
     * @param @param classtype
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/{classtype}")
    @ResponseBody
    public List<MdMenu> getLeftMenuList(@PathVariable String classtype, HttpServletRequest request,
            HttpSession session, Model model) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String lastClick = request.getParameter("lastClick");
        String username = String.valueOf(session.getAttribute("username"));
        String pageUrl = request.getParameter("pageUrl");
        mdMenuDataListener.setMdMenuActive(pageUrl);
        LOG.debug("username is [{}], classtype is [{}]", username, classtype);
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByName(classtype);
        List<MdMenu> mdMenuListFinal = new ArrayList<>();

        for (MdMenu mdMenu : mdMenuList) {
            MdMenu tempMenu = new MdMenu();
            PropertyUtils.copyProperties(tempMenu, mdMenu);
            if (!mdMenu.getName().equals(lastClick)) {
                tempMenu.setChildren(null);
            }
            mdMenuListFinal.add(tempMenu);
        }
        // 修改MdMenu中alarmcount的值，进行左侧菜单告警
        List<MdMenu> leftMenus = mdMenuDataListener.getLeftAlarm(mdMenuListFinal);
        return leftMenus;
    }

    /**
     * 
     * @Title: querymenutreename
     * @Description: TODO(获取菜单名称)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/query/menutreename")
    @ResponseBody
    public MdMenu querymenutreename(HttpServletRequest request, HttpSession session, Model model) {
        String pageUrl = request.getParameter("pageUrl");
        MdMenu menuTree = mdMenuDataListener.getMenuTreeName(pageUrl);
        return menuTree;
    }
}
