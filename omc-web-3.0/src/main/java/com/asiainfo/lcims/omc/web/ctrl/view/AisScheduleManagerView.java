package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.service.ais.AisGroupManageService;
import com.asiainfo.lcims.omc.service.ais.AisScheduleManageService;
import com.asiainfo.lcims.omc.service.ais.ScheduleService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 巡检计划管理类
 */
@Controller
@RequestMapping(value = "/view/class/ais/aisschedulemanage")
public class AisScheduleManagerView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(AisScheduleManagerView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "aisScheduleManageService")
    AisScheduleManageService aisScheduleManageService;

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;

    /**
     * 巡检计划管理初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String aisScheduleManageManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        model.addAttribute("sendEmail", CommonInit.BUSCONF.getAISEmailNotice());
        model.addAttribute("sendSms", CommonInit.BUSCONF.getAISSmsNotice());
        return "ais/aisschedulemanage";
    }

    /**
     * 巡检计划查询方法
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<AisScheduleModel> queryAisScheduleList(HttpServletRequest request,
                                                   HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        AisScheduleModel aisScheduleModel = BeanToMapUtils.toBean(AisScheduleModel.class, params);
        String str = DbSqlUtil.replaceSpecialStr(aisScheduleModel.getTitle());
        aisScheduleModel.setTitle(str);
        List<AisScheduleModel> aisScheduleList = aisScheduleManageService.getAisScheduleList(aisScheduleModel);
        return aisScheduleList;
    }
//, method = RequestMethod.POST
    @RequestMapping(value = "/{managetype}")
    public @ResponseBody
    WebResult manageInfo(@PathVariable String managetype,
                         HttpServletRequest request , INSSchedule insSchedule) {

        LOG.info("start {} Schedule.", managetype);

        WebResult result = new WebResult(false, "操作类型错误！");

        try {

            Map<String, Object> params = getParams(request);

            if (EnumUtil.MANAGETYPE_ADD.equals(managetype)) {
                result = scheduleService.addVaildTitle(insSchedule);
                if (result.isOpSucc()) {
                    scheduleService.addSchedule(insSchedule);
                    result = new WebResult(true, "新增成功！");
                }
            } else if (EnumUtil.MANAGETYPE_DELETE.equals(managetype)) {
                String[] aisschedulelist = request.getParameterValues("aisScheduleArray[]");
                result = aisScheduleManageService.deleteInfo(aisschedulelist);
            } else if (EnumUtil.MANAGETYPE_MODIFY.equals(managetype)) {
                scheduleService.editSchedule(insSchedule);
                result = new WebResult(true, "修改成功！");
            }
        } catch (Exception e) {

            LOG.error("Ais Schedule Manage View Error, error : {}", e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/vaild")
    @ResponseBody
    public WebResult queryTitleVaild(HttpServletRequest request, Model model) {
        Map<String, Object> params = getParams(request);
        INSSchedule insSchedule = BeanToMapUtils.toBean(INSSchedule.class, params);
        WebResult result = scheduleService.addVaildTitle(insSchedule);
        return result;
    }

}
