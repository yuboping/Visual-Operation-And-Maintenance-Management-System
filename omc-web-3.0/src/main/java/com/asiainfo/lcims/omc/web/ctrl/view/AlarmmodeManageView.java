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
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.AlarmmodeManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 告警方式管理Controller类
 * 
 * @author zhujiansheng
 * @date 2018年8月30日 上午11:08:56
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/alarmmodemanage")
public class AlarmmodeManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Autowired
    private AlarmmodeManageService alarmmodeManageService;

    @RequestMapping(value = "")
    public String alarmmodeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());

        // 初始化告警方式类型
        List<MdParam> params = alarmmodeManageService.getAlarmModeType();
        model.addAttribute("params", params);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/alarmmodemanage";
    }

    @RequestMapping(value = "/querytype")
    @ResponseBody
    public List<MdParam> queryMdParamList() {
        List<MdParam> params = alarmmodeManageService.getAlarmModeType();
        return params;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdAlarmMode> queryMdAlarmModeList(@ModelAttribute MdAlarmMode mdAlarmMode,
            HttpServletRequest request, HttpSession session, Model model) {
        List<MdAlarmMode> alarmModeList = alarmmodeManageService.getMdAlarmModeList(mdAlarmMode);
        return alarmModeList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdAlarmMode(@ModelAttribute MdAlarmMode mdAlarmMode,
            HttpServletRequest request, HttpSession session, Model model) {
        WebResult result = alarmmodeManageService.addMdAlarmMode(mdAlarmMode);
        return result;
    }

    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdAlarmMode(@ModelAttribute MdAlarmMode mdAlarmMode,
            HttpServletRequest request, HttpSession session, Model model) {
        WebResult result = alarmmodeManageService.modifyMdAlarmMode(mdAlarmMode);
        return result;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdAlarmMode(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] alarmmodeArray = request.getParameterValues("alarmmodeArray[]");
        WebResult result = alarmmodeManageService.deleteMdAlarmMode(alarmmodeArray);
        return result;
    }

}
