package com.asiainfo.lcims.omc.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;

/**
 * 定时生成巡检报告
 * 
 * @author luohuawuyin
 *
 */
@Component
public class TimerReport implements Job {

    private static final Logger LOG = LoggerFactory.make();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            LOG.info("生成实时巡检报告_start");
            ScheduleJob task = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
            task.getService().exportRegularReport(task.getJobId());
            LOG.info("生成实时巡检报告_end");
        } catch (Exception e) {
            LOG.error("生成实时巡检报告失败", e);
        }
    }

}
