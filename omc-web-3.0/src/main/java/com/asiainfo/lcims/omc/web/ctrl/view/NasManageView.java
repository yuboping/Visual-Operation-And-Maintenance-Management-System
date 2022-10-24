package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.service.configmanage.BasManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;

/**
 * 配置管理/ BRAS管理的Controller类
 * 
 * @author zhujiansheng
 * @date 2018年8月8日 下午3:16:17
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/basmanage")
public class NasManageView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(NasManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "basManageService")
    BasManageService basManageService;

    /**
     * bas管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String basManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        MdParam mdParam = new MdParam();
        mdParam.setType(14);
        List<MdParam> mdParamList = basManageService.getParamByType(mdParam);
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdArea> mdAreaList;
        if (user.getRoleid().equals("0")) {
            mdAreaList = CommonInit.getMdAreaList();
        } else {
            mdAreaList = basManageService.getByRoleMdAreaList(user.getRoleid());
        }
        model.addAttribute("mdParamList", mdParamList);
        model.addAttribute("mdAreaList", mdAreaList);
        return "servermanage/basmanage";
    }

    /**
     * 查询事件
     * 
     * @param bdNas
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<BdNas> queryBdNasList(@ModelAttribute BdNas bdNas, HttpServletRequest request,
            Model model) {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<BdNas> bdNasList;
        if (user.getRoleid().equals("0")) {
            bdNasList = basManageService.getBdNasList(bdNas);
        } else {
            bdNasList = basManageService.getBdNasByRoleList(user.getRoleid(), bdNas);
        }

        SessionUtils.setSession(Constant.BRAS_USER, bdNasList);
        return bdNasList;
    }

    @RequestMapping(value = "/query/equiplist")
    @ResponseBody
    public List<MdEquipmentModel> queryMdEquips(@ModelAttribute MdEquipmentModel mdEquipmentModel,
            HttpServletRequest request, HttpSession session, Model model) {
        return basManageService.getMdEquipList(mdEquipmentModel);
    }

    @RequestMapping(value = "/query/mdparamlist")
    @ResponseBody
    public List<MdParam> queryMdParam(@ModelAttribute MdParam mdParam, HttpServletRequest request,
            HttpSession session, Model model) {
        return basManageService.getParamByType(mdParam);
    }

    @RequestMapping(value = "/query/mdfactorylist")
    @ResponseBody
    public List<MdFactory> queryMdParam(@ModelAttribute MdFactory mdFactory,
            HttpServletRequest request, HttpSession session, Model model) {
        return basManageService.getMdFactoryList(mdFactory);
    }

    @RequestMapping(value = "/query/mdarealist")
    @ResponseBody
    public List<MdArea> queryMdParam(@ModelAttribute MdArea mdArea, HttpServletRequest request,
            HttpSession session, Model model) {

        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);

        List<MdArea> list;
        if (user.getRoleid().equals("0")) {
            list = CommonInit.getMdAreaList();
        } else {
            list = basManageService.getByRoleMdAreaList(user.getRoleid());
        }

        // CommonInit.getMdAreaList()
        return list;

    }

    /**
     * 增加事件
     * 
     * @param bdNas
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addBdNas(@ModelAttribute BdNas bdNas, HttpServletRequest request,
            HttpSession session, Model model) {
        return basManageService.addBdNas(bdNas);
    }

    /**
     * 修改事件
     * 
     * @param bdNas
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyBdNas(@ModelAttribute BdNas bdNas, HttpServletRequest request,
            HttpSession session, Model model) {
        return basManageService.modifyBdNas(bdNas);
    }

    /**
     * 删除事件
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteBdNas(HttpServletRequest request, HttpSession session, Model model) {
        String[] basidArray = request.getParameterValues("basidArray[]");
        return basManageService.deleteBdNas(basidArray);
    }

    /**
     * 详情事件
     * 
     * @param bdNas
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<BdNas> queryDetailList(@ModelAttribute BdNas bdNas, HttpServletRequest request,
            Model model) {
        List<BdNas> bdNasList = basManageService.getBdNasList(bdNas);
        return bdNasList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @ResponseBody
    public WebResult exportBdNas(@ModelAttribute BdNas bdNas, HttpServletRequest request,
            HttpServletResponse response, HttpSession session, Model model) {
        List<BdNas> bdBrasList = (List<BdNas>) SessionUtils.getFromSession(Constant.BRAS_USER);
        WebResult webResult = basManageService.exportExcel(bdBrasList, response, request);
        return webResult;
    }

    /**
     * 刷新bras库
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/refreshBras")
    @ResponseBody
    public WebResult refreshBras(HttpServletRequest request, HttpSession session, Model model) {
        return basManageService.refreshBras();
    }

    /**
     * 设置属地
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/setBrasArea")
    @ResponseBody
    public WebResult setBrasArea(HttpServletRequest request, HttpSession session, Model model) {
        String brasids = request.getParameter("brasids");
        String areano = request.getParameter("areano");
        return basManageService.setBrasArea(brasids, areano);
    }

    /**
     * 
     * @Title: syncPWG @Description: TODO(同步PWG) @param @param
     *         bdNas @param @param request @param @param session @param @param
     *         model @param @return 参数 @return Map<String,String> 返回类型 @throws
     */
    @RequestMapping(value = "/syncPWG")
    @ResponseBody
    public Map<String, String> syncPWG(@RequestBody BdNas bdNas, HttpServletRequest request) {
        return basManageService.syncPWG(bdNas);
    }
}
