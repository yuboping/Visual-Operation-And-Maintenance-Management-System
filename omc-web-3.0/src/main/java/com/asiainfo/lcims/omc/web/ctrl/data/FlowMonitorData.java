package com.asiainfo.lcims.omc.web.ctrl.data;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.flowmonitor.FlowMonitorServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.flowmonitor.FlowDetail;
import com.asiainfo.lcims.omc.model.flowmonitor.FlowSerialNum;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowTask;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowResultDao;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowTaskDao;

/**
 * 业务全流程监控
 * 
 * @author luohuawuyin
 *
 */
@Controller
public class FlowMonitorData {
    @Autowired
    MdFlowResultDao flowDao;
    @Autowired
    MdFlowTaskDao taskDao;
    @Autowired
    private FlowMonitorServer flowMonitorServer;

    /**
     * 获取业务全流程监控执行细节数据
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/data/mainttool/flowmonitor/detail")
    public @ResponseBody List<FlowDetail> getFlowDetail(@RequestParam("task_id") String task_id,
            @RequestParam("serial_num") String serial_num) {
        return flowDao.getFlowDetail(task_id, serial_num);
    }

    /**
     * 查询业务全流程监控信息
     * 
     * @param task_id
     * @return
     */
    @RequestMapping(value = "/data/mainttool/flowmonitor/query")
    public @ResponseBody MdFlowTask queryFlowTask(@RequestParam("task_id") String task_id) {
        return taskDao.queryTaskById(task_id);
    }

    /**
     * 修改业务全流程监控信息
     * 
     * @param task_id
     * @return
     */
    @RequestMapping(value = "/data/mainttool/flowmonitor/edit")
    public @ResponseBody WebResult editFlowTask(HttpServletRequest request,
            @ModelAttribute("data") MdFlowTask task) {
        task.setUpdate_time(new Date());
        int count = taskDao.editFlowTask(task);
        WebResult result = null;
        if (count == 1) {
            result = new WebResult(true, "配置修改成功");
        } else {
            result = new WebResult(false, "配置修改失败");
        }
        return result;
    }

    /**
     * 校验crontab表达式是否正确
     * 
     * @param cron
     * @return
     */
    @RequestMapping(value = "/data/mainttool/flowmonitor/checkcron")
    public @ResponseBody boolean checkCron(@RequestParam("cron") String cron) {
        return CronExpression.isValidExpression(cron);
    }

    /**
     * 实时执行全流程监控
     * 
     * @param cron
     * @return
     */
    @RequestMapping(value = "/data/mainttool/flowmonitor/excuteFlow")
    public @ResponseBody FlowSerialNum excuteFlow(@RequestParam("task_id") String task_id) {
        return flowMonitorServer.getNewestSerialNumForTaskId(task_id);
    }

}
