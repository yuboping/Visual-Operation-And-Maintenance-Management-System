package com.asiainfo.ais.omcstatistic.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 刷新系统缓存数据
 *
 * @author yangyc
 */

@Controller
public class RefreshAction {

    @Autowired
    MemoryUtil memoryUtil;

    @Autowired
    ScheduledConfigService scheduledService;

    @RequestMapping(value = "/refresh")
    @ResponseBody
    public String refreshAll() {
        memoryUtil.setRuleToMemory();
        return "refresh success...";
    }

    @RequestMapping(value = "/refresh/day")
    @ResponseBody
    public String refreshDay() {
        memoryUtil.setRuleToMemory();
        return "refresh day & hour success...";
    }
}
