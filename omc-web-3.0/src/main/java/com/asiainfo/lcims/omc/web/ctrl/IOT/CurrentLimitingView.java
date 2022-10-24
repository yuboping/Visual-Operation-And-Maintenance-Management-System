package com.asiainfo.lcims.omc.web.ctrl.IOT;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.apn.ApnOperate;
import com.asiainfo.lcims.omc.model.apn.ApnResult;
import com.asiainfo.lcims.omc.model.apn.LimitResp;
import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.IOT.CurrentLimitingService;
import com.asiainfo.lcims.omc.service.apn.ApnOperateService;
import com.asiainfo.lcims.omc.service.apn.LimitStrategyService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.web.ctrl.apn.LimitStrategyView;

/**
 * 物联网限流
 */
@Controller
@RequestMapping(value = "/view/class/system/currentlimiting")
public class CurrentLimitingView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LimitStrategyView.class);

    private final static String MANAGETYPE_CANCEL = "cancelApn";
    private final static String MANAGETYPE_MODIFY = "modifyApn";
    private final static String MANAGETYPE_ADD = "addApn";

    @Autowired
    private LimitStrategyService limitStrategyService;

    @Autowired
    private ApnOperateService apnOperateService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "currentLimitingService")
    CurrentLimitingService currentLimitingService;

    @RequestMapping(value = "")
    public String currentLimiting(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "IOT/currentLimiting";
    }

    /**
     *
     * @Title: queryAPNList
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdApnLimitRule> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdApnLimitRule> queryAPNList(@ModelAttribute MdApnLimitRule mdApnLimitRule) {
        List<MdApnLimitRule> mdApnLimitRules = currentLimitingService.getApnList(mdApnLimitRule);
        return mdApnLimitRules;
    }

    /**
     *
     * @Title: addApn @Description: (新增) @param @param request @param @param
     * session @param @param model @param @return 参数 @return List<MdMetric>
     * 返回类型 @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addApn(@ModelAttribute MdApnLimitRule mdApnLimitRule) {
        return currentLimitingService.addApn(mdApnLimitRule);
    }

    /**
     *
     * @Title: getApnRecord @Description: (查询apn记录表) @param @param
     * request @param @param session @param @param model @param @return
     * 参数 @return List<MdMetric> 返回类型 @throws
     */
    @RequestMapping(value = "/queryApnRecord")
    @ResponseBody
    public List<MdApnRecord> getApnRecord(@ModelAttribute MdApnRecord mdApnRecord) {
        return currentLimitingService.getApnRecord();
    }

    /**
     *
     * @Title: modifyApn @Description: (修改指标信息) @param @param
     * request @param @param session @param @param model @param @return
     * 参数 @return List<MdMetric> 返回类型 @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyApn(@ModelAttribute MdApnLimitRule mdApnLimitRule) {
        return currentLimitingService.modifyApn(mdApnLimitRule);
    }

    /**
     *
     * @Title: deleteApn @Description: (删除) @param @param request @param @param
     * session @param @param model @param @return 参数 @return List<MdMetric>
     * 返回类型 @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteApn(HttpServletRequest request) {
        String[] apnIdArray = request.getParameterValues("apnIdArray[]");
        return currentLimitingService.deleteApn(apnIdArray);
    }

    /**
     * 限流策略同步接口
     *
     * @param managetype
     *            操作类型，分增加，修改，取消
     * @param mdApnLimitRule
     *            APN限流规则
     * @return
     */
    @RequestMapping(value = "/{managetype}")
    @ResponseBody
    public LimitResp limitStrategy(@PathVariable String managetype,
            @ModelAttribute MdApnLimitRule mdApnLimitRule, HttpServletRequest request) {
        LimitResp manageResp = null;
        if (StringUtils.equalsIgnoreCase(MANAGETYPE_ADD, managetype)
                || StringUtils.equalsIgnoreCase(MANAGETYPE_MODIFY, managetype)) {
            manageResp = limitStrategyService.getLimitStrategy(mdApnLimitRule,
                    Constant.LIMIT_VALID_OPEN);
        } else if (StringUtils.equalsIgnoreCase(MANAGETYPE_CANCEL, managetype)) {
            String[] apnArray = request.getParameterValues("apnArray[]");
            manageResp = limitStrategyService.deleteLimitStrategy(apnArray,
                    Constant.LIMIT_VALID_UNOPEN);
        }
        LOG.info("limit strategy response : {}", manageResp);
        return manageResp;
    }

    /**
     * APN增删操作接口
     *
     * @param apnOperate
     *            APN增删操作
     * @return
     */
    @RequestMapping(value = "/operate")
    @ResponseBody
    public ApnResult operateApn(@RequestBody ApnOperate apnOperate,
            HttpServletRequest request, HttpSession session, Model model) {
        ApnResult apnResult = apnOperateService.operateApn(apnOperate);
        return apnResult;
    }

}
