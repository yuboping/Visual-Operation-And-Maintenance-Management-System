package com.asiainfo.lcims.omc.service.ais;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.asiainfo.lcims.omc.persistence.ais.INSScheduleDAO;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.quartz.QuartzManager;
import com.asiainfo.lcims.omc.util.TimerConvert;

/**
 * 巡检定时任务记录表：MON_INS_SCHEDULE
 * 
 * @author luohuawuyin
 *
 */
@Service("scheduleService")
public class ScheduleService {

    @Inject
    private INSScheduleDAO insScheduleDAO;

    @Inject
    private QuartzManager quartz;

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public INSSchedule getScheduleDetail(String id) {
        INSSchedule schedule = insScheduleDAO.getScheduleDetail(id);
        // 根据items获取groupids
        if (schedule != null) {
//            String items = schedule.getItems().trim();
//            if (items.endsWith(",")) {
//                items = items.substring(0, items.length() - 1);
//            }
//            List<String> groupids = insItemDAO.getGroupidsByitems(items);
//            String groups = jointoString(groupids);
//            schedule.setGroup_ids(groups);
//            if (!StringUtils.isEmpty(schedule.getTimer())) {
//                TimerConvert convert = new TimerConvert();
//                schedule.setTimer(convert.calPerIdentify(schedule.getTimer()));
//            }
        }
        return schedule;
    }

    /**
     * 新增
     *
     * @param schedule
     * @return
     */
    public int addSchedule(INSSchedule schedule) {

        String id = IDGenerateUtil.getUuid();

        schedule.setId(id);
        schedule.setSchedule_name(getScheduleName(schedule.getSchedule_type()));
        schedule.setCreate_time(DateTools.getCurrentFormatDate());
        schedule.setUpdate_time(DateTools.getCurrentFormatDate());
        this.initProcess(schedule);
        int result = insScheduleDAO.addSchedule(schedule);
        quartz.addSchedule(schedule);
        return result;
    }

    /**
     * 新增
     *
     * @param schedule
     * @return
     */
    public WebResult addVaildTitle(INSSchedule schedule) {

        this.initProcess(schedule);
        int titleCount = insScheduleDAO.getScheduleCount(schedule.getTitle());
        if(titleCount > 0){
            return new WebResult(false, "标题不能重复","repeat");
        }
        return new WebResult(true, "校验成功");
    }

    public int editSchedule(INSSchedule schedule) {

        schedule.setSchedule_name(getScheduleName(schedule.getSchedule_type()));
        schedule.setUpdate_time(DateTools.getCurrentFormatDate());
        this.initProcess(schedule);
        int result = insScheduleDAO.updateSchedule(schedule);
        quartz.updateSchedule(schedule);
        return result;
    }

    public int delSchedule(String id) {
        int result = insScheduleDAO.deleteSchedule(id);
        quartz.deleteSchedule(id);
        return result;
    }

    /**
     * 获取巡检计划列表
     * 
     * @return
     */
    public List<INSSchedule> getScheduleList() {
        List<INSSchedule> schedules = insScheduleDAO.getAllSchedule();
        if (schedules == null || schedules.isEmpty()) {
            return null;
        }
        return schedules;
    }

    /**
     * 获取下一个执行时间
     * 
     * @return
     * @throws Exception
     */
    public String getNextExecTime() throws Exception {
        List<INSSchedule> schedules = insScheduleDAO.getAllSchedule();
        if (schedules == null || schedules.isEmpty()) {
            return null;
        }
        List<String> nexttimes = new ArrayList<String>();
        for (INSSchedule schedule : schedules) {
            TimerConvert convert = new TimerConvert();
            String nexttime = convert.getNextExecTime(schedule.getTimer());
            if (!StringUtils.isEmpty(nexttime)) {
                nexttimes.add(nexttime);
            }
        }
        if (!nexttimes.isEmpty()) {
            String[] timesarray = (String[]) nexttimes.toArray(new String[nexttimes.size()]);
            Arrays.sort(timesarray);
            return timesarray[0];
        }
        return null;

    }

    /**
     * 初始化处理timer和items
     *
     * @param schedule
     */
    private void initProcess(INSSchedule schedule) {

        String timer = schedule.getTimer();
        String perIdentify = schedule.getSchedule_name();
        TimerConvert convert = new TimerConvert();
        if (!StringUtils.isEmpty(timer) && !StringUtils.isEmpty(perIdentify)) {
            Map<String, String> timermap = this.getTimerMap(perIdentify, timer);
            schedule.setTimer(convert.calTimerFromShowInfo(perIdentify, timermap));
        }
    }

    private String jointoString(List<String> groupids) {
        String groups = "";
        if (groupids != null && !groupids.isEmpty()) {
            for (String groupid : groupids) {
                groups += groupid + ",";
            }
        }
        if (groups.endsWith(",")) {
            groups = groups.substring(0, groups.length() - 1);
        }
        return groups;
    }

    /**
     * @param perIdentify
     * @param seltimer
     * @return
     */
    private Map<String, String> getTimerMap(String perIdentify, String seltimer) {
        String[] timerarray = seltimer.split(" ");
        Map<String, String> timermap = new HashMap<String, String>();
        if (timerarray.length == 5) {

            timermap.put("selmin", timerarray[0]);
            timermap.put("selhour", timerarray[1]);
            if (perIdentify.equals("week")) {
                timermap.put("selweek", timerarray[4]);
            } else if (perIdentify.equals("month")) {
                timermap.put("selday", timerarray[2]);
            } else if (perIdentify.equals("year")) {
                timermap.put("selmonth", timerarray[3]);
                timermap.put("selday", timerarray[2]);
            }

        }

        return timermap;
    }

    private String getScheduleName(int scheduleType){
        String scheduleName = "";
        if(scheduleType == 1){
            scheduleName = "year";
        }else if(scheduleType == 2){
            scheduleName = "month";
        }else if(scheduleType == 3){
            scheduleName = "week";
        }else if(scheduleType == 4){
            scheduleName = "day";
        }
        return scheduleName;
    }
}
