package com.asiainfo.ais.omcstatistic.statistic;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.DateUtil;
import com.asiainfo.ais.omcstatistic.common.TimeControl;
import com.asiainfo.ais.omcstatistic.mapper.MdCollCycleMapper;
import com.asiainfo.ais.omcstatistic.mapper.MdStatisticRuleMapper;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;

@Configuration
@EnableScheduling
public class ScheduledConfigService implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledConfigService.class);

    @Autowired
    ExcuteStatisticSql ExcuteStatisticSql;

    @Autowired
    MdStatisticRuleMapper mdStatisticRuleMapper;

    @Autowired
    MdCollCycleMapper mdCollCycleMapper;

    @Autowired
    ShellExcute shellExcute;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加5分钟执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledMinute(Constant.EXCUTE_SPACE_MINUTE);
                    } catch (InterruptedException e) {
                        LOG.error(
                                "execute scheduledMinute 5min error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_FIVE_MIN)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 5min e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MIN_5;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加10分钟执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledMinute(Constant.EXCUTE_SPACE_MINUTE_10);
                    } catch (InterruptedException e) {
                        LOG.error(
                                "execute scheduledMinute 10min error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_MIN_10)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 10min e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MIN_10;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加15分钟执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledMinute(Constant.EXCUTE_SPACE_MINUTE_15);
                    } catch (InterruptedException e) {
                        LOG.error(
                                "execute scheduledMinute 15min error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_MIN_15)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 15min e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MIN_15;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加20分钟执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledMinute(Constant.EXCUTE_SPACE_MINUTE_20);
                    } catch (InterruptedException e) {
                        LOG.error(
                                "execute scheduledMinute 20min error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_MIN_20)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 20min e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MIN_20;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加30分钟执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledMinute(Constant.EXCUTE_SPACE_MINUTE_30);
                    } catch (InterruptedException e) {
                        LOG.error(
                                "execute scheduledMinute 30min error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_MIN_30)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 30min e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MIN_30;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加1小时执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledHour();
                    } catch (InterruptedException e) {
                        LOG.error("execute scheduledHour error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_HOUR)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 1hour e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_HOUR;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加1天执行任务内容(Runnable)
                () -> {
                    try {
                        scheduledDay();
                    } catch (InterruptedException e) {
                        LOG.error("execute scheduledDay error with InterruptedException e -> {}",
                                e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_DAY)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 1day e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_DAY;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });

        scheduledTaskRegistrar.addTriggerTask(
                // 1.添加1月执行任务内容(Runnable)
                () -> {
                    scheduledMonth();
                },
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = null;
                    try {
                        cron = mdCollCycleMapper.selectByPrimaryKey(Constant.COLL_CYCLE_MONTH)
                                .getCron();
                    } catch (Exception e) {
                        LOG.error("execute scheduledMinute 1month e -> {}", e.getMessage());
                    }
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        cron = Constant.CRON_DEFAULT_MONTH;
                    }
                    // 执行时间推迟2分钟
                    cron = getCronDelay2Min(cron);
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }

    public void scheduledMinute(String excute_space) throws InterruptedException {
        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getMinuteRuleList(excute_space);
        long currentTimeMillis = System.currentTimeMillis();
        String formatTime = TimeControl.getCycleTime(excute_space, 0);
        // 创建闭锁 大小为规则的条数
        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
            // lambda表达式
            Runnable run = () -> {
                try {
                    mdStatisticRule.setStime(DateUtil.getFormatTime(formatTime, Constant.NOW_FORMAT,
                            Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()));
                    mdStatisticRule.setCycleTime(formatTime);
                    ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
                } finally {
                    countDownLatch.countDown();
                }
            };
            pool.execute(run);
        }
        pool.shutdown();
        // 等待所有线程执行完毕
        countDownLatch.await();
        LOG.info("使用线程池一共执行：" + String.valueOf(System.currentTimeMillis() - currentTimeMillis)
                + "ms");
        String shellFormatTime = DateUtil.getFormatTime(formatTime, null,
                Constant.REPLACE_TIME_FORMAT, null);
        // 5分钟的周期执行告警shell脚本
        if (Constant.EXCUTE_SPACE_MINUTE.equals(excute_space)) {
            shellExcute.runAlarmShell(shellFormatTime, Constant.EXCUTE_SPACE_MINUTE_SHELL);
        }
    }

    public void scheduledHour() throws InterruptedException {
        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getHourRuleList();
        long currentTimeMillis = System.currentTimeMillis();
        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
        // 创建闭锁 大小为规则的条数
        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
            // lambda表达式
            Runnable run = () -> {
                try {
                    mdStatisticRule.setStime(DateUtil.getCronFormatDate(
                            DateUtil.getFormatTime(formatTime, Constant.NOW_FORMAT,
                                    Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()),
                            Constant.COLL_CYCLE_HOUR)

                    );
                    mdStatisticRule.setCycleTime(formatTime);
                    ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
                } finally {
                    countDownLatch.countDown();
                }
            };
            pool.execute(run);
        }
        pool.shutdown();
        // 等待所有线程执行完毕
        countDownLatch.await();
        LOG.info("使用线程池一共执行：" + String.valueOf(System.currentTimeMillis() - currentTimeMillis)
                + "ms");

    }

    public void scheduledDay() throws InterruptedException {
        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getDayRuleList();
        long currentTimeMillis = System.currentTimeMillis();
        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
        // 创建闭锁 大小为规则的条数
        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
        ExecutorService pool = Executors.newFixedThreadPool(5);

        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
            // lambda表达式
            Runnable run = () -> {
                try {
                    mdStatisticRule.setStime(DateUtil.getCronFormatDate(
                            DateUtil.getFormatTime(formatTime, Constant.YESTERDAY_FORMAT,
                                    Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()),
                            Constant.COLL_CYCLE_DAY));
                    mdStatisticRule.setCycleTime(formatTime);
                    ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
                } finally {
                    countDownLatch.countDown();
                }
            };

            pool.execute(run);
        }
        pool.shutdown();

        // 等待所有线程执行完毕
        countDownLatch.await();
        LOG.info("使用线程池一共执行：" + String.valueOf(System.currentTimeMillis() - currentTimeMillis)
                + "ms");

    }

    public void scheduledMonth() {
        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getMonthRuleList();

        long currentTimeMillis = System.currentTimeMillis();

        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);

        ExecutorService pool = Executors.newFixedThreadPool(5);

        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
            // lambda表达式
            Runnable run = () -> {
                mdStatisticRule
                        .setStime(DateUtil.getFormatTime(formatTime, Constant.YESTERDAY_FORMAT,
                                Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()));
                mdStatisticRule.setCycleTime(formatTime);
                ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
            };

            pool.execute(run);
        }
        pool.shutdown();

        LOG.debug("使用线程池一共执行：" + String.valueOf(System.currentTimeMillis() - currentTimeMillis)
                + "ms");

    }

    /**
     * 将执行推后两分钟
     * 
     * @param cron
     * @return
     */
    private static String getCronDelay2Min(String cron) {
        String[] cronArray = cron.split(" ");
        StringBuilder cronBuilder = new StringBuilder();
        if (cronArray.length > 2) {
            String cronMin = cronArray[1];
            if (cronMin.contains("/")) {
                String[] cronMinArray = cronMin.split("/");
                if (cronMinArray.length > 1) {
                    String cronMinTemp = String.valueOf(Integer.valueOf(cronMinArray[0]) + 2);
                    cronMin = cronMinTemp + "/" + cronMinArray[1];
                }
            } else {
                if (!cronMin.equals("*")) {
                    cronMin = String.valueOf(Integer.valueOf(cronMin) + 2);
                }
            }
            for (int i = 0; i < cronArray.length; i++) {
                if (i != 1) {
                    cronBuilder.append(cronArray[i]);
                } else {
                    cronBuilder.append(cronMin);
                }
                cronBuilder.append(" ");
            }
            cron = cronBuilder.substring(0, cronBuilder.length() - 1);
        }
        return cron;
    }

}