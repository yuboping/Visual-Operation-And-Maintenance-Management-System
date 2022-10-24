package com.asiainfo.lcims.omc.alarm.business;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.ChartData;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdSpecialALarmInfo;
import com.asiainfo.lcims.omc.alarm.model.TimeCell;
import com.asiainfo.lcims.omc.alarm.param.RuleValue;

/**
 * 仅支持 newest <= lastest * 0.6 && alarm_lastest == 1 格式告警规则
 * @author zhul
 *
 */
public class SpecialALarmProcess implements Runnable {
    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    private static final Logger logger = LoggerFactory.make();
    private MdAlarmRuleDetail rule;
    private String currentTime; // 当前周期
    private String cyclename;
    private String thred_id;
    private List<ChartData> newest = null;
    private List<ChartData> lastest = null;
    private List<ChartData> yescurrent = null;
    private List<MdSpecialALarmInfo> alarmOtherInfos = null;
    private static final int POOL_SIZE = 60;
    public SpecialALarmProcess(MdAlarmRuleDetail rule, String currentTime, String cyclename,
            List<MdSpecialALarmInfo> alarmInfos, String thred_id) {
        this.rule = rule;
        this.currentTime = currentTime;
        this.cyclename = cyclename;
        this.alarmOtherInfos = alarmInfos;
        this.thred_id = thred_id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(30000); // 延时30秒
            alarm();
        } catch (Exception e) {
            logger.error(thred_id+": " + e.getMessage());
        }
    }
    
    private void alarm() {
        //数据初始
        initData();
        // newest 循环告警
        long millSeconds = TimeControl.getMillSeconds(currentTime);
        Timestamp time = new Timestamp(millSeconds);
        //newest 存在1w多条，采用线程池方法
        ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        for (ChartData chartData : newest) {
            SpecialALarmChildProcess childProcess = new SpecialALarmChildProcess(chartData, alarmOtherInfos,
                    currentTime, rule, lastest);
            threadPool.execute(childProcess);
        }
        
        threadPool.shutdown();
        threadPool.shutdown();
    }
    
    private void initData (){
        String rules = rule.getAlarm_rule();
        newest = AlarmService.getByTime(rule, currentTime);
        if (rules.indexOf(RuleValue.LASTEST.getType()) >= 0) {
            String lastTime = TimeControl.lasttime(currentTime, cyclename, -1);
            lastest = AlarmService.getByTime(rule, lastTime);
        }
        if (rules.indexOf(RuleValue.YESCURRENT.getType()) >= 0) {
            TimeCell cell = TimeControl.getYesterday(currentTime, cyclename);
            yescurrent = AlarmService.getByTime(rule, cell.getStarttime());
        }
    }
}
