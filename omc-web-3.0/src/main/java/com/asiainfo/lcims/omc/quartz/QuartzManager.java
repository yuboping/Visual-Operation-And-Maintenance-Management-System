package com.asiainfo.lcims.omc.quartz;

import java.util.List;

import javax.inject.Inject;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.ais.INSScheduleDAO;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.service.ais.AisReportService;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 实时巡检报告生成管理
 * 
 * @author luohuawuyin
 *
 */
@Component
public class QuartzManager implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.make();
    private static final String PRE_NAME = "task_";
    private static final String PRE_GROUP = "group_";
    @Inject
    private INSScheduleDAO scheduleDao;

    private ApplicationContext context;

    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();

    private static final boolean TASKOPEN = CommonInit.conf.openTimedTask();

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (TASKOPEN) {
            Scheduler scheduler = null;
            try {
                context = ctx;
                scheduler = schedulerFatory.getScheduler();
                List<INSSchedule> schedules = scheduleDao.getAllSchedule();
                for (INSSchedule task : schedules) {
                    ScheduleJob info = makeTaskInfo(task);
                    addJob(scheduler, info);
                }
                scheduler.start();
            } catch (SchedulerException e) {
                LOG.error("生成实时巡检报告初始化失败", e);
                shutdown(scheduler);
            }
        }
    }

    private void shutdown(Scheduler scheduler) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                LOG.error("", e);
            }
        }
    }

    private ScheduleJob makeTaskInfo(INSSchedule task) {
        LOG.info("start makeTaskInfo");
        ScheduleJob info=new ScheduleJob();
        info.setJobId(task.getId());
        info.setCronExpression("0 "+task.getTimer());
        info.setJobName(PRE_NAME + task.getId());
        info.setJobGroup(PRE_GROUP + task.getId());
        info.setDesc(task.getTitle());
        info.setService((AisReportService) context.getBean("aisReportService"));
        LOG.info("end makeTaskInfo with CronExpression : {}", info.getCronExpression());
        return info;
    }

    private void addJob(Scheduler scheduler, ScheduleJob task) throws SchedulerException {
        LOG.info("start addJob, task : {}", task);
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder builder = null;
        try {
            builder = CronScheduleBuilder.cronSchedule(covCronExpression(task.getCronExpression()));
        } catch (Exception e) {
            LOG.error("任务调度时间格式错误", e);
            return;
        }
        if (null == trigger) {// 不存在，创建一个
            JobDetail jobDetail = JobBuilder.newJob(TimerReport.class)
                    .withIdentity(task.getJobName(), task.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", task);
            trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())
                    .withSchedule(builder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {// trigger已存在，则更新相应的定时设置
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(builder)
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
        LOG.info("end addJob");
    }
    
    /*
     * 定时任务，按周执行的时候，1-7不会执行，需要转换为英文。
     */
    private String covCronExpression(String str) {
        if(str == null){
            return str;
        }
        String s[] = str.split(" ");
        if(s.length != 6){
            return str;
        }
        String temp = s[5];
        if(ToolsUtils.isNumeric(temp)){
            switch (Integer.parseInt(s[5])) {
                case 1: temp = "Mon"; break;
                case 2: temp = "Tue"; break;
                case 3: temp = "Wed"; break;
                case 4: temp = "Thu"; break;
                case 5: temp = "Fri"; break;
                case 6: temp = "Sat"; break;
                case 7: temp = "Sun"; break;
            }
        }
        String temp_1 = " ";
        temp = s[0] + temp_1 + s[1] + temp_1 + s[2] + temp_1 + s[3] + temp_1 + s[4] + temp_1 + temp;
        return temp;
    }

    public void shutdownJobs() {
        try {
            Scheduler sched = schedulerFatory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addSchedule(INSSchedule task) {
        LOG.info("TaskOpen flag is : {} ", TASKOPEN);
        if (TASKOPEN) {
            Scheduler scheduler = null;
            try {
                scheduler = schedulerFatory.getScheduler();
                ScheduleJob info = makeTaskInfo(task);
                addJob(scheduler, info);
                if (scheduler.isShutdown()) {
                    scheduler.start();
                }
            } catch (SchedulerException e) {
                LOG.error("新增/修改巡检计划任务失败", e);
            }
        }
    }

    public void updateSchedule(INSSchedule task) {
        addSchedule(task);
    }

    public void deleteSchedule(String scheduleid) {
        if (TASKOPEN) {
            Scheduler scheduler;
            try {
                scheduler = schedulerFatory.getScheduler();
                TriggerKey triggerKey = TriggerKey.triggerKey(PRE_NAME + scheduleid,
                        PRE_GROUP + scheduleid);
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                if (scheduler != null && trigger != null) {
                    scheduler.pauseTrigger(triggerKey);
                    scheduler.unscheduleJob(triggerKey);
                }
            } catch (SchedulerException e) {
                LOG.error("删除巡检计划任务失败", e);
            }
        }
    }
}