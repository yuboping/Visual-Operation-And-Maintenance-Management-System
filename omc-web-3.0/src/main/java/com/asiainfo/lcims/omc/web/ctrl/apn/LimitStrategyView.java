package com.asiainfo.lcims.omc.web.ctrl.apn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asiainfo.lcims.omc.model.apn.LimitResp;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.apn.LimitResp;
import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.service.apn.LimitStrategyService;
import com.asiainfo.lcims.omc.util.Constant;

@Controller
@RequestMapping(value = "/view/class/system/limitstrategy")
public class LimitStrategyView {

    private static final Logger LOG = LoggerFactory.getLogger(LimitStrategyView.class);

    private final static String MANAGETYPE_CANCEL = "cancel";
    private final static String MANAGETYPE_MODIFY = "modify";
    private final static String MANAGETYPE_ADD = "add";

    @Autowired
    private LimitStrategyService limitStrategyService;

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
            @ModelAttribute MdApnLimitRule mdApnLimitRule, HttpServletRequest request,
            HttpServletResponse response) {
        LimitResp manageResp = null;
        if (StringUtils.equalsIgnoreCase(MANAGETYPE_ADD, managetype)
                || StringUtils.equalsIgnoreCase(MANAGETYPE_MODIFY, managetype)) {
            manageResp = limitStrategyService.getLimitStrategy(mdApnLimitRule,
                    Constant.LIMIT_VALID_OPEN);
        } else if (StringUtils.equalsIgnoreCase(MANAGETYPE_CANCEL, managetype)) {
            manageResp = limitStrategyService.getLimitStrategy(mdApnLimitRule,
                    Constant.LIMIT_VALID_UNOPEN);
        }
        LOG.info("limit strategy response : {}", manageResp);
        return manageResp;
    }

}
