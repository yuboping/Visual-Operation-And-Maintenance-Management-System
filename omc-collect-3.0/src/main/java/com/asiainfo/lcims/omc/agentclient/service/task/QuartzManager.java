package com.asiainfo.lcims.omc.agentclient.service.task;

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
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.agentserver.model.MetricServer2Client;

public class QuartzManager {
    private static final Logger LOG = LoggerFactory.getLogger(QuartzManager.class);

    private static SchedulerFactory SCHEDULER_FATORY = new StdSchedulerFactory();

    public static void startTask(List<MetricServer2Client> metricJobs, String localIp)
            throws Exception {
        Scheduler scheduler = null;
        scheduler = SCHEDULER_FATORY.getScheduler();
        shutdownAllTask(scheduler);
        scheduler = SCHEDULER_FATORY.getScheduler();
        for (MetricServer2Client metricJob : metricJobs) {
            addJob(scheduler, metricJob, localIp);
        }
        scheduler.start();
    }

    public static void shutdownAllTask(Scheduler scheduler) throws SchedulerException {
        if (!scheduler.isShutdown()) {
            LOG.info("scheduler name : {}", scheduler.getSchedulerName());
            scheduler.shutdown();
        }
    }

    private static void addJob(Scheduler scheduler, MetricServer2Client metricJob, String localIp)
            throws SchedulerException {
        String shell = metricJob.getShell();
        String params = metricJob.getParams() == null ? "" : metricJob.getParams();
        String resulttype = metricJob.getResulttype();
        String expr = metricJob.getExpr();
        String name = shell + "_" + params + "_" + resulttype + "_" + expr;
        String group = localIp;
        LOG.info("name : {}, group : {}", name, group);

        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder builder = null;
        builder = CronScheduleBuilder.cronSchedule(expr);
        JobDetail jobDetail = JobBuilder.newJob(TimerJob.class).withIdentity(name, group).build();
        jobDetail.getJobDataMap().put("scheduleJob", metricJob);
        jobDetail.getJobDataMap().put("localIp", localIp);
        trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(builder)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
