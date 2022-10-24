package com.asiainfo.lcims.omc.web.ctrl.data;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.WebResult;

import com.asiainfo.lcims.omc.service.alarm.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警信息：MON_ALARM
 */
@Controller
public class AlarmData {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmData.class);
    @Inject
    private AlarmService alarmService;

    /**
     * 实时查询监控告警信息
     * @return
     */
    @RequestMapping(value = "/data/alarm/navigation")
    public @ResponseBody
    WebResult getAlarmMessageList(HttpSession session) {
        Map<String, Object> optiondata = new HashMap<String, Object>();

        try {
        	String username = (String)session.getAttribute("username");

            List<MdAlarmInfo> alarmlist = alarmService.getAlarmMessageList(username);
            if (alarmlist != null && !alarmlist.isEmpty()) {
                optiondata.put("total", alarmlist.size());
                optiondata.put("list", alarmlist);
                return new WebResult(true, "", optiondata);
            }
        } catch (Exception e) {
            LOG.error("获取数据失败", e);
        }
        return new WebResult(false, "无告警信息！");
    }
    
}
