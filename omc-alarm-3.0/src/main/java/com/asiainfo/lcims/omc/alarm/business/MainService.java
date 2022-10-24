package com.asiainfo.lcims.omc.alarm.business;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdSpecialALarmInfo;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.alarm.param.RuleValue;
import com.asiainfo.lcims.omc.alarm.service.SpecialALarmService;
import com.asiainfo.lcims.util.ToolsUtils;

public class MainService {

    private static final Logger LOG = LoggerFactory.make();
    private static final int POOL_SIZE = 60;

    public static void main(String[] args) throws InterruptedException {
        /**
         * 判断是否存在参数 周期类型 cycle、周期时间 cycle_time 1、存在参数时候启动 查询指标周期为 cycle 的告警规则，用
         * cycle_time 作为时间去查询数据 2、不存在参数启动 查询所有告警规则
         * ，判断告警规则周期时间和当前时间之差，如果小于5分钟，则执行， 大于5分钟，不执行操作
         */

        String cyclename = null;
        String cycleTime = null;
        String cycleid = null;
        InitParam.init();
        if (args != null && args.length == 1) {
            // 参数存在
            cycleTime = args[0];
            LOG.info("this alarm params cycle_time is : {}", cycleTime);
        }
        // 获取每个周期得当前周期时间 cyclename
        List<MdCollCycleTime> cycleTimelist = InitParam.getCollCycleTimes(cyclename, cycleTime);
        LOG.info("cycleTimelist list : {}", cycleTimelist);
        if (ToolsUtils.ListIsNull(cycleTimelist)) {
            LOG.info("采集周期未配置 OR MD_COLL_CYCLE 无对应数据");
            return;
        }
        /**
         * 1、查询采集周期 ，获取采集周期对应的当前周期时间 2、判断当当前时间-前周期时间 < 5分钟 条件成立，执行操作
         */
        // 查询告警规则细表 信息
        List<MdAlarmRuleDetail> list = AlarmService.getAlarmRuleDetail(cycleid);
        // 告警信息查询
        List<MdAlarmInfo> alarmInfos = AlarmService.getAlarmInfos(cycleid);
        
        InitParam.addAlarmInfos(alarmInfos);
        
        //特殊olt告警信息查询
        List<MdSpecialALarmInfo> alarmOtherInfos = SpecialALarmService.getAlarmOtherInfos();
        
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
        
        ExecutorService fixedOtherThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
        
        int thread_id = 1;
        for (MdAlarmRuleDetail rule : list) {
            MdCollCycleTime collCycleTime = getCycleCurrentTime(rule, cycleTimelist);
            // 过滤无用的规则
            if (null != collCycleTime) {
                String currenttime = collCycleTime.getCurrenttime();
                rule.setCyclename(collCycleTime.getCyclename());
                    
                if (rule.getAlarm_rule().contains(RuleValue.ALARM_LASTEST.getType())) {
                    // 特殊处理
                    SpecialALarmProcess alarmOtherProcess = new SpecialALarmProcess(rule, currenttime,
                            collCycleTime.getCyclename(), alarmOtherInfos, thread_id+"");
                    fixedOtherThreadPool.execute(alarmOtherProcess);
                } else {
                    AlarmProcess alarmProcess = new AlarmProcess(rule, currenttime,
                            collCycleTime.getCyclename(), alarmInfos,thread_id+"");
                    fixedThreadPool.execute(alarmProcess);
                }
                
                thread_id++;
                
            } else {
                LOG.info("collCycleTime is out 5 min : {} ; rule is : {}", rule.getEffectiveRule(),
                        rule);
            }
        }
        fixedThreadPool.shutdown();
        fixedOtherThreadPool.shutdown();
        // 当前线程阻塞，直到等所有已提交的任务（包括正在跑的和队列中等待的）执行完
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        
        /**
         * 判断告警信息表中 alarmid 对应的告警规则是否被删除 1、如果被删除，则删除对应的告警信息数据、告警历史信息数据
         */
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(POOL_SIZE);
        
        // InitParam.getAlarmInfos();
        for (MdAlarmInfo mdAlarmInfo : alarmInfos) {
            if (mdAlarmInfo.getAlarm_rule().contains("alarm_lastest")) {
                //不做处理
                continue;
            } else {
                AlarmInfoProcess alarmInfoProcess = new AlarmInfoProcess(mdAlarmInfo,
                        list);
                fixedThreadPool2.execute(alarmInfoProcess);
            }
            
        }
        fixedThreadPool2.shutdown();
        // 当前线程阻塞，直到等所有已提交的任务（包括正在跑的和队列中等待的）执行完
        fixedThreadPool2.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);

        LOG.info("all thread complete");
    }

    /**
     * 获取周期的当前周期时间
     * 
     * @param rule
     * @param cycleTimelist
     * @return
     */
    public static MdCollCycleTime getCycleCurrentTime(MdAlarmRuleDetail rule,
            List<MdCollCycleTime> cycleTimelist) {
        MdCollCycleTime collCycleTime = null;
        for (MdCollCycleTime mdCollCycleTime : cycleTimelist) {
            if (rule.getCycleid().intValue() == mdCollCycleTime.getCycleid().intValue()) {
                collCycleTime = mdCollCycleTime;
                return collCycleTime;
            }
        }
        return collCycleTime;
    }

}
