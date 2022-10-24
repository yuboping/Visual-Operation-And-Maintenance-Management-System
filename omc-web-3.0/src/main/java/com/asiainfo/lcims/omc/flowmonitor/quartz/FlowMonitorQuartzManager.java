package com.asiainfo.lcims.omc.flowmonitor.quartz;

import java.util.List;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowTask;
import com.asiainfo.lcims.omc.persistence.flowmonitor.MdFlowTaskDao;

public class FlowMonitorQuartzManager implements InitializingBean {
    private static final Logger logger = LoggerFactory.make();
    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();
    @Autowired
    private MdFlowTaskDao taskDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFatory.getScheduler();
            List<MdFlowTask> taskList = taskDao.selectAll();
            for (MdFlowTask task : taskList) {
                addJob(scheduler, task);
            }
            scheduler.start();
            logger.error("--------------{} flowmonitor running.--------------", taskList.size());
        } catch (SchedulerException e) {
            logger.error("flowmonitor init error:", e);
            shutdown(scheduler);
        }
    }

    private void addJob(Scheduler scheduler, MdFlowTask task) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getTask_id());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronBuilder = null;
        try {
            cronBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
        } catch (Exception e) {
            logger.error("taskId:{},cron:{} format error.error:{}", task.getTask_id(), task.getCron(), e);
            return;
        }
        if (null == trigger) {// 不存在，创建一个
            // 创建执行类
            JobDetail jobDetail = JobBuilder.newJob(TaskMonitorJob.class)
                    .withIdentity(task.getTask_id()).build();

            // 将数据缓存到map中,TaskMonitor.class类中获取对应数据
            jobDetail.getJobDataMap().put("scheduleJob", task);

            // 创建定时触发器
            trigger = TriggerBuilder.newTrigger().withIdentity(task.getTask_id())
                    .withSchedule(cronBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {// trigger已存在，则更新相应的定时设置
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronBuilder)
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    private void shutdown(Scheduler scheduler) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                logger.error("", e);
            }
        }
    }

}
