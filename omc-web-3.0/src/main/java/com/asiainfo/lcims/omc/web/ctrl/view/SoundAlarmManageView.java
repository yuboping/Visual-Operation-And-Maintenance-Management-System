package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdSoundAlarm;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.SoundAlarmManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 声音告警管理
 * 
 * @author zhujiansheng
 * @date 2019年9月2日 下午4:19:51
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/soundalarmmanage")
public class SoundAlarmManageView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SoundAlarmManageView.class);

    @Autowired
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private SoundAlarmManageService soundAlarmManageService;

    @RequestMapping(value = "")
    public String nodeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());

        // 初始化声音持续时间
        MdParam mdParam = new MdParam();
        mdParam.setType(21);
        List<MdParam> mdParamList = soundAlarmManageService.getParamByType(mdParam);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        model.addAttribute("mdParamList", mdParamList);
        return "servermanage/soundalarmmanage";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdSoundAlarm> queryMdSoundAlarmList(@ModelAttribute MdSoundAlarm mdSoundAlarm,
            HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdSoundAlarm> mdSoundAlarmList = soundAlarmManageService.getMdNodeList(mdSoundAlarm);
        return mdSoundAlarmList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdSoundAlarm(@ModelAttribute MdSoundAlarm mdSoundAlarm,
            HttpServletRequest request, HttpSession session, Model model) {
        return soundAlarmManageService.addMdSoundAlarm(mdSoundAlarm);
    }

    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdSoundAlarm(@ModelAttribute MdSoundAlarm mdSoundAlarm,
            HttpServletRequest request, HttpSession session, Model model) {
        return soundAlarmManageService.modifyMdSoundAlarm(mdSoundAlarm);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdSoundAlarm(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] soundAlarmIdArray = request.getParameterValues("soundalarmids[]");
        return soundAlarmManageService.deleteMdSoundAlarm(soundAlarmIdArray);
    }

    @RequestMapping(value = "/query/mdparamlist")
    @ResponseBody
    public List<MdParam> queryMdParam(@ModelAttribute MdParam mdParam, HttpServletRequest request,
            HttpSession session, Model model) {
        return soundAlarmManageService.getParamByType(mdParam);
    }

}
