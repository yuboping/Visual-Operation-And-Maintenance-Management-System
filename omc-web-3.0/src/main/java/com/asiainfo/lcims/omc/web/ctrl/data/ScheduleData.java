package com.asiainfo.lcims.omc.web.ctrl.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.service.ais.ScheduleService;
import com.asiainfo.lcims.omc.util.TimerConvert;

/**
 * 巡检定时任务记录表：MON_INS_SCHEDULE
 * 
 * @author luohuawuyin
 *
 */
@Controller
@RequestMapping("/data/class/ais")
public class ScheduleData {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleData.class);
    @Resource(name = "scheduleService")
    private ScheduleService service;

    @RequestMapping(value = "/schedules")
    public @ResponseBody WebResult getSchedules() {
        try {
            List<INSSchedule> schedules = service.getScheduleList();
            if (schedules == null || schedules.isEmpty()) {
                return new WebResult(false, "无巡检计划");
            } else {
                String nextExeTime = service.getNextExecTime();
                Map<String, Object> optiondata = new HashMap<String, Object>();
                optiondata.put("schedule", schedules);
                optiondata.put("nexttime", (new TimerConvert()).formatTimeForShow(nextExeTime));
                return new WebResult(true, "", optiondata);
            }

        } catch (Exception e) {
            LOG.error("获取数据失败", e);
        }
        return new WebResult(false, "获取数据失败");
    }

    @RequestMapping(value = "/schedule/add")
    public String addSchedule(@ModelAttribute("form") INSSchedule form) {

        service.addSchedule(form);
        return "redirect:/view/class/ais/schedule/savefineinit";
    }

    @RequestMapping(value = "/schedule/query/{id}")
    public @ResponseBody INSSchedule querySchedule(HttpServletRequest request, Model model,
            @PathVariable("id") String id) {
        INSSchedule schedule = service.getScheduleDetail(id);
        return schedule;
    }

    @RequestMapping(value = "/schedule/edit")
    public String editSchedule(@ModelAttribute("form") INSSchedule form) {
        service.editSchedule(form);
        return "redirect:/view/class/ais/schedule/savefineinit";
    }

    @RequestMapping(value = "/schedule/del/item/{id}")
    public String delSchedule(HttpServletRequest request, Model model,
            @PathVariable("id") String id) {
        service.delSchedule(id);
        return "redirect:/view/class/ais";
    }
}
