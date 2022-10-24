package com.asiainfo.ais.omcstatistic.statistic;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.DateUtil;
import com.asiainfo.ais.omcstatistic.common.TimeControl;
import com.asiainfo.ais.omcstatistic.pojo.MdStatisticRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ScheduledService {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ScheduledService.class);
//
//    @Autowired
//    ExcuteStatisticSql ExcuteStatisticSql;
//
//    @Autowired
//    ShellExcute shellExcute;
//
////    @Scheduled(cron = "0 2/5 * * * ? ")
//    @Scheduled(cron = "${cron.five.min}")
//    public void scheduledMinute() throws InterruptedException {
//        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getMinuteRuleList();
//        long currentTimeMillis = System.currentTimeMillis();
//        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
//        // 创建闭锁 大小为规则的条数
//        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
//            //lambda表达式
//            Runnable run = () -> {
//                try {
//                    mdStatisticRule.setStime(DateUtil.getFormatTime(formatTime, Constant.NOW_FORMAT, Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()));
//                    mdStatisticRule.setCycleTime(formatTime);
//                    ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            };
//            pool.execute(run);
//        }
//        pool.shutdown();
//        // 等待所有线程执行完毕
//        countDownLatch.await();
//        LOG.info("使用线程池一共执行："+String.valueOf(System.currentTimeMillis()-currentTimeMillis)+"ms");
//        String shellFormatTime = DateUtil.getFormatTime(formatTime, null, Constant.REPLACE_TIME_FORMAT, null);
//        shellExcute.runAlarmShell(shellFormatTime, Constant.EXCUTE_SPACE_MINUTE_SHELL);
//
//    }
//
////    @Scheduled(cron = "0 1 0/1 * * ? ")
//    @Scheduled(cron = "${cron.one.hour}")
//    public void scheduledHour() throws InterruptedException {
//        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getHourRuleList();
//        long currentTimeMillis = System.currentTimeMillis();
//        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
//        // 创建闭锁 大小为规则的条数
//        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
//            //lambda表达式
//            Runnable run = () -> {
//                try {
//                mdStatisticRule.setStime(DateUtil.getCronFormatDate(
//                        DateUtil.getFormatTime(formatTime,
//                                    Constant.PREHOUR_FORMAT,
//                                    Constant.CREATE_TIME_FORMAT,
//                                    mdStatisticRule.getOffset()
//                        ), Constant.COLL_CYCLE_HOUR)
//
//                );
//                mdStatisticRule.setCycleTime(formatTime);
//                ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            };
//            pool.execute(run);
//        }
//        pool.shutdown();
//        // 等待所有线程执行完毕
//        countDownLatch.await();
//        LOG.info("使用线程池一共执行："+String.valueOf(System.currentTimeMillis()-currentTimeMillis)+"ms");
//
//    }
//
////    @Scheduled(cron = "0 1 0 1/1 * ? ")
//    @Scheduled(cron = "${cron.one.day}")
//    public void scheduledDay() throws InterruptedException {
//        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getDayRuleList();
//        long currentTimeMillis = System.currentTimeMillis();
//        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
//        // 创建闭锁 大小为规则的条数
//        CountDownLatch countDownLatch = new CountDownLatch(mdStatisticRuleList.size());
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//
//        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
//            //lambda表达式
//            Runnable run = () -> {
//                try {
//                mdStatisticRule.setStime(DateUtil.getCronFormatDate(
//                        DateUtil.getFormatTime(formatTime,
//                            Constant.YESTERDAY_FORMAT,
//                            Constant.CREATE_TIME_FORMAT,
//                            mdStatisticRule.getOffset()
//                        ), Constant.COLL_CYCLE_DAY)
//                );
//                mdStatisticRule.setCycleTime(formatTime);
//                ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            };
//
//            pool.execute(run);
//        }
//        pool.shutdown();
//
//        // 等待所有线程执行完毕
//        countDownLatch.await();
//        LOG.info("使用线程池一共执行："+String.valueOf(System.currentTimeMillis()-currentTimeMillis)+"ms");
//
//    }
//
//    @Scheduled(cron = "${cron.one.week}")
//    public void scheduledWeek() {
//        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getWeekRuleList();
//        long currentTimeMillis = System.currentTimeMillis();
//        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
//            //lambda表达式
//            Runnable run = () -> {
//                mdStatisticRule.setStime(DateUtil.getFormatTime(formatTime, Constant.YESTERDAY_FORMAT, Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()));
//                mdStatisticRule.setCycleTime(formatTime);
//                ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
//            };
//
//            pool.execute(run);
//        }
//        pool.shutdown();
//
//        LOG.debug("使用线程池一共执行："+String.valueOf(System.currentTimeMillis()-currentTimeMillis)+"ms");
//
//    }
//
//    @Scheduled(cron = "${cron.one.month}")
//    public void scheduledMonth() {
//        List<MdStatisticRule> mdStatisticRuleList = MemoryUtil.getMonthRuleList();
//
//        long currentTimeMillis = System.currentTimeMillis();
//
//        String formatTime = TimeControl.getCycleTime(Constant.EXCUTE_SPACE_MINUTE, 0);
//
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//
//        for (MdStatisticRule mdStatisticRule : mdStatisticRuleList) {
//            //lambda表达式
//            Runnable run = () -> {
//                mdStatisticRule.setStime(DateUtil.getFormatTime(formatTime, Constant.YESTERDAY_FORMAT, Constant.CREATE_TIME_FORMAT, mdStatisticRule.getOffset()));
//                mdStatisticRule.setCycleTime(formatTime);
//                ExcuteStatisticSql.excuteSqlWithExpression(mdStatisticRule);
//            };
//
//            pool.execute(run);
//        }
//        pool.shutdown();
//
//        LOG.debug("使用线程池一共执行："+String.valueOf(System.currentTimeMillis()-currentTimeMillis)+"ms");
//
//    }
}