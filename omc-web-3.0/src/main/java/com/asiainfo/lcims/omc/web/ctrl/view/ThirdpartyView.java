package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.model.configmanage.MdFirewall;
import com.asiainfo.lcims.omc.model.configmanage.MdThirdparty;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.service.configmanage.FirewallService;
import com.asiainfo.lcims.omc.service.configmanage.ThirdpartyService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/view/class/system/thirdparty")
public class ThirdpartyView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "thirdpartyService")
    ThirdpartyService thirdpartyService;

    /**
     * 第三方接口设备管理
     * @param request
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "")
    public String thirdparty(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        MdParam mdParam = new MdParam();
        mdParam.setType(14);
        List<MdParam> mdParamList = thirdpartyService.getParamByType(mdParam);
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdArea> mdAreaList;
        if(user.getRoleid().equals("0")) {
            mdAreaList= CommonInit.getMdAreaList();
        }else {
            mdAreaList=thirdpartyService.getByRoleMdAreaList(user.getRoleid());
        }
        model.addAttribute("mdParamList", mdParamList);
        model.addAttribute("mdAreaList", mdAreaList);
        return "gscm5G/thirdparty";
    }

    @RequestMapping(value = "/query/equiplist")
    @ResponseBody
    public List<MdEquipmentModel> queryMdEquips(@ModelAttribute MdEquipmentModel mdEquipmentModel,
                                                HttpServletRequest request,
                                                HttpSession session, Model model) {
        return thirdpartyService.getMdEquipList(mdEquipmentModel);
    }

    @RequestMapping(value = "/query/mdparamlist")
    @ResponseBody
    public List<MdParam> queryMdParam(@ModelAttribute MdParam mdParam, HttpServletRequest request,
                                      HttpSession session, Model model) {
        return thirdpartyService.getParamByType(mdParam);
    }

    @RequestMapping(value = "/query/mdfactorylist")
    @ResponseBody
    public List<MdFactory> queryMdParam(@ModelAttribute MdFactory mdFactory,
                                        HttpServletRequest request,
                                        HttpSession session, Model model) {
        return thirdpartyService.getMdFactoryList(mdFactory);
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
            list=thirdpartyService.getByRoleMdAreaList(user.getRoleid());
        }
        return list;

    }

    /**
     * 表格数据
     * @param mdThirdparty
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdThirdparty> queryThirdpartyList(@ModelAttribute MdThirdparty mdThirdparty, HttpServletRequest request, Model model) {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdThirdparty> mdThirdparties;
        if(user.getRoleid().equals("0")) {
            mdThirdparties=thirdpartyService.getThirdpartyList(mdThirdparty);
        }else {
            mdThirdparties=thirdpartyService.getThirdpartyByRoleList(user.getRoleid(),mdThirdparty);
        }

        SessionUtils.setSession(Constant.THIRDPARTY_USER, mdThirdparties);
        return mdThirdparties;
    }

    /**
     * 新增
     * @param mdThirdparty
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addThirdparty(@ModelAttribute MdThirdparty mdThirdparty, HttpServletRequest request,
                                 HttpSession session, Model model) {
        return thirdpartyService.addThirdparty(mdThirdparty);
    }

    /**
     * 修改
     * @param mdThirdparty
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyThirdparty(@ModelAttribute MdThirdparty mdThirdparty, HttpServletRequest request,
                                    HttpSession session, Model model) {
        return thirdpartyService.modifyThirdparty(mdThirdparty);
    }

    /**
     * 详情
     * @param mdThirdparty
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<MdThirdparty> queryDetailList(@ModelAttribute MdThirdparty mdThirdparty, HttpServletRequest request,
                                            Model model) {
        List<MdThirdparty> mdThirdpartyList = thirdpartyService.getThirdpartyList(mdThirdparty);
        return mdThirdpartyList;
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
    public WebResult deleteThirdparty(HttpServletRequest request, HttpSession session,
                                    Model model) {
        String[] thirdpartyidArray = request.getParameterValues("thirdpartyidArray[]");
        return thirdpartyService.deleteThirdparty(thirdpartyidArray);
    }

    /**
     * 导出
     * @param mdThirdparty
     * @param request
     * @param response
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @ResponseBody
    public WebResult exportThirdparty(@ModelAttribute MdThirdparty mdThirdparty, HttpServletRequest request,
                                    HttpServletResponse response, HttpSession session, Model model) {
        List<MdThirdparty> mdThirdpartyList = (List<MdThirdparty>) SessionUtils.getFromSession(Constant.THIRDPARTY_USER);
        WebResult webResult = thirdpartyService.exportExcel(mdThirdpartyList, response, request);
        return webResult;
    }

    /**
     * 设置属地
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/setThirdpartyArea")
    @ResponseBody
    public WebResult setThirdpartyArea(HttpServletRequest request, HttpSession session,
                                     Model model) {
        String thirdpartyids = request.getParameter("thirdpartyids");
        String areano = request.getParameter("areano");
        return thirdpartyService.setThirdpartyArea(thirdpartyids, areano);
    }

    /**
     * 刷新
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/refreshThirdparty")
    @ResponseBody
    public WebResult refreshThirdparty(HttpServletRequest request, HttpSession session,
                                     Model model) {
        return thirdpartyService.refreshThirdparty();
    }
}
