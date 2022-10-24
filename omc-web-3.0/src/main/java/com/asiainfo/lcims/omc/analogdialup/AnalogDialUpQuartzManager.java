package com.asiainfo.lcims.omc.analogdialup;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.persistence.analogdialup.AnalogDialUpDAO;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;

/**
 * 甘肃移动 模拟拨测
 *
 */
public class AnalogDialUpQuartzManager implements InitializingBean {
    private static final Logger logger = LoggerFactory.make();
    private static final SchedulerFactory schedulerFatory = new StdSchedulerFactory();

    private static AnalogDialUpDAO analogDialUpDAOInject = null;

    private static Boolean isFirst = true;

    @Inject
    AnalogDialUpDAO analogDialUpDAO;

    private static class SingletonClassInstance {
        private static final AnalogDialUpQuartzManager instance = new AnalogDialUpQuartzManager();
    }

    private AnalogDialUpQuartzManager() {
    }

    // 静态内部类实现模式（线程安全，调用效率高，可以延时加载）
    public static AnalogDialUpQuartzManager getInstance() {
        return SingletonClassInstance.instance;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_GSCM, ReadFile.PROVINCE)) {
            return;
        }
        List<AnalogDialUp> analogDialUpJobs = analogDialUpDAO.findAllAnalogDialUp();
        startTask(analogDialUpJobs);
    }

    public void startTask(List<AnalogDialUp> analogDialUpJobs) throws Exception {
        shutdownAllTask();
        for (AnalogDialUp analogDialUp : analogDialUpJobs) {
            addJob(analogDialUp);
        }
    }

    public void shutdownAllTask() throws SchedulerException {
        Scheduler scheduler = schedulerFatory.getScheduler();
        if (!scheduler.isShutdown()) {
            logger.info("scheduler name : {}", scheduler.getSchedulerName());
            scheduler.shutdown();
        }
    }

    public void addJob(AnalogDialUp analogDialUp) throws SchedulerException {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_GSCM, ReadFile.PROVINCE)) {
            return;
        }
        Scheduler scheduler = schedulerFatory.getScheduler();
        // trigger key 定时任务唯一键
        String analogDialUpId = analogDialUp.getId();
        TriggerKey triggerKey = TriggerKey.triggerKey(analogDialUpId);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 定时任务周期 Cron 表达式
        CronScheduleBuilder cronBuilder = null;
        cronBuilder = CronScheduleBuilder.cronSchedule(analogDialUp.getCron_erp());
        if (null == trigger) {// 不存在，创建一个
            // 创建执行类
            JobDetail jobDetail = JobBuilder.newJob(AnalogDialUpJob.class)
                    .withIdentity(analogDialUpId).build();
            if (isFirst) {
                isFirst = false;
                analogDialUpDAOInject = analogDialUpDAO;
                jobDetail.getJobDataMap().put("analogDialUpDAO", analogDialUpDAO);
            } else {
                jobDetail.getJobDataMap().put("analogDialUpDAO", analogDialUpDAOInject);
            }
            jobDetail.getJobDataMap().put("analogDialUp", analogDialUp);
            // 创建定时触发器
            trigger = TriggerBuilder.newTrigger().withIdentity(analogDialUpId)
                    .withSchedule(cronBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {// trigger已存在，则更新相应的定时设置
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronBuilder)
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
        scheduler.start();
    }

    public void deleteJob(AnalogDialUp analogDialUp) throws SchedulerException {
        if (!StringUtils.equalsIgnoreCase(ProviceUtill.PROVINCE_GSCM, ReadFile.PROVINCE)) {
            return;
        }
        Scheduler scheduler = schedulerFatory.getScheduler();
        // trigger key 定时任务唯一键
        String analogDialUpId = analogDialUp.getId();
        scheduler.deleteJob(JobKey.jobKey(analogDialUpId));
    }

    public void modifyJob(AnalogDialUp analogDialUp) throws SchedulerException {
        deleteJob(analogDialUp);
        addJob(analogDialUp);
    }
}
