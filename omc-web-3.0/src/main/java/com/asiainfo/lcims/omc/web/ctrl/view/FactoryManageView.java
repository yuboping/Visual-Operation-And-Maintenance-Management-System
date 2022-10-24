package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.FactoryManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 工厂管理Controller类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 上午10:07:11
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/factorymanage")
public class FactoryManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Autowired
    private FactoryManageService factoryManageService;

    /**
     * 工厂管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String nodeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/factorymanage";
    }

    /**
     * 
     * @param mdFactory
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdFactory> queryMdFactoryList(@ModelAttribute MdFactory mdFactory,
            HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdFactory> factoryList = factoryManageService.getMdFactoryList(mdFactory);
        return factoryList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdFactory(@ModelAttribute MdFactory mdFactory,
            HttpServletRequest request,
            HttpSession session, Model model) {
        return factoryManageService.addMdFactory(mdFactory);
    }

    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdNode(@ModelAttribute MdFactory mdFactory,
            HttpServletRequest request, HttpSession session, Model model) {
        return factoryManageService.modifyMdFactory(mdFactory);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdNode(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] factoryidArray = request.getParameterValues("factoryidArray[]");
        return factoryManageService.deleteMdFactory(factoryidArray);
    }

}
