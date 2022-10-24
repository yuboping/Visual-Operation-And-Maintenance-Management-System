package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.*;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.service.configmanage.BasManageService;
import com.asiainfo.lcims.omc.service.configmanage.FirewallService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 防火墙设备管理
 */
@Controller
@RequestMapping(value = "/view/class/system/firewall")
public class FirewallView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "firewallService")
    FirewallService firewallService;

    /**
     * 防火墙设备管理
     *
     * @return
     */
    @RequestMapping(value = "")
    public String firewall(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        MdParam mdParam = new MdParam();
        mdParam.setType(14);
        List<MdParam> mdParamList = firewallService.getParamByType(mdParam);
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdArea> mdAreaList;
        if(user.getRoleid().equals("0")) {
            mdAreaList=CommonInit.getMdAreaList();
        }else {
            mdAreaList=firewallService.getByRoleMdAreaList(user.getRoleid());
        }
        model.addAttribute("mdParamList", mdParamList);
        model.addAttribute("mdAreaList", mdAreaList);
        return "gscm5G/firewall";
    }

    @RequestMapping(value = "/query/equiplist")
    @ResponseBody
    public List<MdEquipmentModel> queryMdEquips(@ModelAttribute MdEquipmentModel mdEquipmentModel,
                                                HttpServletRequest request,
                                                HttpSession session, Model model) {
        return firewallService.getMdEquipList(mdEquipmentModel);
    }

    @RequestMapping(value = "/query/mdparamlist")
    @ResponseBody
    public List<MdParam> queryMdParam(@ModelAttribute MdParam mdParam, HttpServletRequest request,
                                      HttpSession session, Model model) {
        return firewallService.getParamByType(mdParam);
    }

    @RequestMapping(value = "/query/mdfactorylist")
    @ResponseBody
    public List<MdFactory> queryMdParam(@ModelAttribute MdFactory mdFactory,
                                        HttpServletRequest request,
                                        HttpSession session, Model model) {
        return firewallService.getMdFactoryList(mdFactory);
    }

    @RequestMapping(value = "/query/mdarealist")
    @ResponseBody
    public List<MdArea> queryMdParam(@ModelAttribute MdArea mdArea,
                                     HttpServletRequest request, HttpSession session, Model model) {

        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);

        List<MdArea> list;
        if(user.getRoleid().equals("0")) {
            list=CommonInit.getMdAreaList();
        }else {
            list=firewallService.getByRoleMdAreaList(user.getRoleid());
        }
        return list;

    }

    /**
     * 表格查询
     * @param mdFirewall
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdFirewall> queryFirewallList(@ModelAttribute MdFirewall mdFirewall, HttpServletRequest request, Model model) {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdFirewall> mdFirewalls;
        if(user.getRoleid().equals("0")) {
            mdFirewalls=firewallService.getFirewallList(mdFirewall);
        }else {
            mdFirewalls=firewallService.getBdNasByRoleList(user.getRoleid(),mdFirewall);
        }

        SessionUtils.setSession(Constant.FIREWALL_USER, mdFirewalls);
        return mdFirewalls;
    }

    /**
     * 新增
     * @param mdFirewall
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addFirewall(@ModelAttribute MdFirewall mdFirewall, HttpServletRequest request,
                              HttpSession session, Model model) {
        return firewallService.addFirewall(mdFirewall);
    }

    /**
     * 修改
     * @param mdFirewall
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyFirewall(@ModelAttribute MdFirewall mdFirewall, HttpServletRequest request,
                                 HttpSession session, Model model) {
        return firewallService.modifyFirewall(mdFirewall);
    }

    /**
     * 详情
     * @param mdFirewall
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<MdFirewall> queryDetailList(@ModelAttribute MdFirewall mdFirewall, HttpServletRequest request,
                                       Model model) {
        List<MdFirewall> mdFirewallList = firewallService.getFirewallList(mdFirewall);
        return mdFirewallList;
    }

    /**
     * 删除
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteFirewall(HttpServletRequest request, HttpSession session,
                                 Model model) {
        String[] firewallidArray = request.getParameterValues("firewallidArray[]");
        return firewallService.deleteFirewall(firewallidArray);
    }

    /**
     * 导出
     * @param mdFirewall
     * @param request
     * @param response
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @ResponseBody
    public WebResult exportFirewall(@ModelAttribute MdFirewall mdFirewall, HttpServletRequest request,
                                 HttpServletResponse response, HttpSession session, Model model) {
        List<MdFirewall> mdFirewallList = (List<MdFirewall>) SessionUtils.getFromSession(Constant.FIREWALL_USER);
        WebResult webResult = firewallService.exportExcel(mdFirewallList, response, request);
        return webResult;
    }

    /**
     * 设置属地
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/setFirewallArea")
    @ResponseBody
    public WebResult setFirewallArea(HttpServletRequest request, HttpSession session,
                                 Model model) {
        String firewallids = request.getParameter("firewallids");
        String areano = request.getParameter("areano");
        return firewallService.setFirewallArea(firewallids, areano);
    }

    /**
     * 刷新
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/refreshFirewall")
    @ResponseBody
    public WebResult refreshFirewall(HttpServletRequest request, HttpSession session,
                                 Model model) {
        return firewallService.refreshFirewall();
    }
}
