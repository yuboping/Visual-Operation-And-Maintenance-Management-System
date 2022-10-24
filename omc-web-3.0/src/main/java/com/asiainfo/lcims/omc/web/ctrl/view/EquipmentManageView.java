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
import com.asiainfo.lcims.omc.model.configmanage.MdEquipmentModel;
import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.EquipmentManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 设置型号管理Controller类
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 下午5:14:32
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/equipmentmanage")
public class EquipmentManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Autowired
    private EquipmentManageService equipmentManageService;

    @RequestMapping(value = "")
    public String nodeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        // 初始化厂家名称
        List<MdFactory> factoryList = equipmentManageService.getFactoryList();
        model.addAttribute("factoryList", factoryList);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/equipmentmanage";
    }

    @RequestMapping(value = "/queryNames")
    @ResponseBody
    public List<MdFactory> getFactoryNames() {
        List<MdFactory> factoryList = equipmentManageService.getFactoryList();
        return factoryList;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdEquipmentModel> queryMdEquipList(@ModelAttribute MdEquipmentModel mdEquipmentModel,
            HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdEquipmentModel> equipmentList = equipmentManageService
                .getEquipmentList(mdEquipmentModel);
        return equipmentList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdEquip(@ModelAttribute MdEquipmentModel mdEquipmentModel,
            HttpServletRequest request, HttpSession session, Model model) {
        return equipmentManageService.addMdEquipment(mdEquipmentModel);
    }

    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdEquip(@ModelAttribute MdEquipmentModel mdEquipmentModel,
            HttpServletRequest request, HttpSession session, Model model) {
        return equipmentManageService.modifyMdEquipment(mdEquipmentModel);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdEquip(HttpServletRequest request, HttpSession session, Model model) {
        String[] equipidArray = request.getParameterValues("equipmentidArray[]");
        return equipmentManageService.deleteMdEquipment(equipidArray);
    }
}
