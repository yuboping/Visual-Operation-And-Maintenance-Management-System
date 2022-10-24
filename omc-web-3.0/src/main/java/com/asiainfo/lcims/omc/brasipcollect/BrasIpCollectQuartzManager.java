package com.asiainfo.lcims.omc.brasipcollect;

import javax.annotation.Resource;
import javax.inject.Inject;

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
import org.springframework.beans.factory.InitializingBean;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.BdNasDAO;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.QuartzConstant;
import com.asiainfo.lcims.omc.util.ReadFile;
/**
 * 上海移动 bras定时入库
 * @author zhul
 *
 */
public class BrasIpCollectQuartzManager implements InitializingBean {
    private static final Logger logger = LoggerFactory.make();
    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();

    @Inject
    BdNasDAO nasDao;
    @Resource(name = "commoninit")
    CommonInit commoninit;
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_SHCM, ReadFile.PROVINCE)) {
            return;
        }
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFatory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzConstant.TIME_BRASIP_TRIGGER_KEY);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //定时任务周期 Cron 表达式
            CronScheduleBuilder cronBuilder = null;
            cronBuilder = CronScheduleBuilder.cronSchedule(QuartzConstant.TIME_HOUR_CRON);

            if (null == trigger) {// 不存在，创建一个
                // 创建执行类
                JobDetail jobDetail = JobBuilder.newJob(BrasIpCollectJob.class)
                        .withIdentity(QuartzConstant.TIME_BRASIP_TRIGGER_KEY).build();

                jobDetail.getJobDataMap().put("nasDao", nasDao);
                jobDetail.getJobDataMap().put("commoninit", commoninit);
                // 创建定时触发器
                trigger = TriggerBuilder.newTrigger().withIdentity(QuartzConstant.TIME_BRASIP_TRIGGER_KEY)
                        .withSchedule(cronBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {// trigger已存在，则更新相应的定时设置
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronBuilder)
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }

            scheduler.start();
            logger.info("--------------{} shcm brasip add running.--------------");
        } catch (Exception e) {
            logger.error("shcm brasip add init error:", e);
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
