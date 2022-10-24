package com.asiainfo.lcims.omc.apn;

import org.apache.commons.lang.StringUtils;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.apn.MdApnRecordDAO;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.QuartzConstant;
import com.asiainfo.lcims.omc.util.ReadFile;

public class ApnQuartzManager implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ApnQuartzManager.class);

    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();

    @Autowired
    private MdApnRecordDAO mdApnRecordDAO;

    @Autowired
    private CommonInit commoninit;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_DEV, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_DEV_REAL, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_CQCMTEST, ReadFile.PROVINCE)
                && !StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_CQCM, ReadFile.PROVINCE)) {
            return;
        }
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFatory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzConstant.TIME_APN_TRIGGER_KEY);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 定时任务周期 Cron 表达式
            CronScheduleBuilder cronBuilder = null;
            cronBuilder = CronScheduleBuilder.cronSchedule(QuartzConstant.TIME_MINUTE_CRON);
            if (null == trigger) {// 不存在，创建一个
                // 创建执行类
                JobDetail jobDetail = JobBuilder.newJob(ApnJob.class)
                        .withIdentity(QuartzConstant.TIME_APN_TRIGGER_KEY).build();
                jobDetail.getJobDataMap().put("mdApnRecordDAO", mdApnRecordDAO);
                jobDetail.getJobDataMap().put("commoninit", commoninit);
                // 创建定时触发器
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(QuartzConstant.TIME_APN_TRIGGER_KEY).withSchedule(cronBuilder)
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {// trigger已存在，则更新相应的定时设置
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(cronBuilder).build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }
            scheduler.start();
            LOG.info("-------------- apn add is running.--------------");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            shutdown(scheduler);
        }
    }

    private void shutdown(Scheduler scheduler) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                LOG.error("APN定时任务中止失败", e);
            }
        }
    }

}
