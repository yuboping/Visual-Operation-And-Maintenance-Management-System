package com.asiainfo.lcims.omc.timecleaning;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.persistence.timecleaning.MdTimeCleaningDao;
import com.asiainfo.lcims.omc.util.QuartzConstant;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;

public class TimeCleaningQuartzManager implements InitializingBean {
    private static final Logger logger = LoggerFactory.make();
    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();

    @Inject
    MdTimeCleaningDao mdTimeCleaningDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFatory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzConstant.TIME_CLEANING_TRIGGER_KEY);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //定时任务周期 Cron 表达式
            CronScheduleBuilder cronBuilder = null;
            cronBuilder = CronScheduleBuilder.cronSchedule(QuartzConstant.TIME_CLEANING_CRON);

            if (null == trigger) {// 不存在，创建一个
                // 创建执行类
                JobDetail jobDetail = JobBuilder.newJob(TimeCleaningJob.class)
                        .withIdentity(QuartzConstant.TIME_CLEANING_TRIGGER_KEY).build();

                jobDetail.getJobDataMap().put("mdTimeCleaningDao", mdTimeCleaningDao);
                // 创建定时触发器
                trigger = TriggerBuilder.newTrigger().withIdentity(QuartzConstant.TIME_CLEANING_TRIGGER_KEY)
                        .withSchedule(cronBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {// trigger已存在，则更新相应的定时设置
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronBuilder)
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }

            scheduler.start();
            logger.info("--------------{} TimeCleaning running.--------------");
        } catch (Exception e) {
            logger.error("TimeCleaning init error:", e);
            shutdown(scheduler);
        }
    }

    private void shutdown(Scheduler scheduler) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                logger.error("定时任务中止失败", e);
            }
        }
    }

}
