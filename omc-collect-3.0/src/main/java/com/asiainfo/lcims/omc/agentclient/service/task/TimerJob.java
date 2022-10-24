package com.asiainfo.lcims.omc.agentclient.service.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.agentserver.model.MetricServer2Client;
import com.asiainfo.lcims.omc.utils.IDGenerateUtil;

public class TimerJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(TimerJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            String uuid = IDGenerateUtil.getUuid();
            LOG.info("===TimerJob exec start==={}", uuid);
            MetricServer2Client metricJob = (MetricServer2Client) context.getMergedJobDataMap()
                    .get("scheduleJob");
            String localIp = context.getMergedJobDataMap().getString("localIp");
            CollectTask task = new CollectTask(metricJob, localIp, uuid);
            task.collect(uuid);
            LOG.info("===TimerJob exec stop==={}", uuid);
        } catch (Exception e) {
            LOG.error("TimerJob exec Exception:", e);
        }
    }
}
