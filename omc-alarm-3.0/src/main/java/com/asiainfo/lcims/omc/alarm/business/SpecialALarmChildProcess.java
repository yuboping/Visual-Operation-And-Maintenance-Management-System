package com.asiainfo.lcims.omc.alarm.business;

import java.sql.Timestamp;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.ChartData;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.MdSpecialALarmInfo;
import com.asiainfo.lcims.omc.alarm.param.RuleValue;
import com.asiainfo.lcims.omc.alarm.service.SpecialALarmService;
import com.asiainfo.lcims.util.ToolsUtils;

public class SpecialALarmChildProcess implements Runnable {
    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    private static final Logger logger = LoggerFactory.make();
    private ChartData chartData;
    private List<MdSpecialALarmInfo> alarmOtherInfos = null;
    private String currentTime; // 当前周期
    private MdAlarmRuleDetail rule;
    private List<ChartData> lastest;
    
    public SpecialALarmChildProcess(ChartData chartData, List<MdSpecialALarmInfo> alarmOtherInfos, String currentTime,
            MdAlarmRuleDetail rule, List<ChartData> lastest) {
        super();
        this.chartData = chartData;
        this.alarmOtherInfos = alarmOtherInfos;
        this.currentTime = currentTime;
        this.rule = rule;
        this.lastest = lastest;
    }

    @Override
    public void run() {
        alarm();
    }
    
    private void alarm() {
        boolean needReport = AlarmService.conf.needReport();
        // 查询当前告警信息
        MdSpecialALarmInfo alarmOtherInfo = getAlarmOtherInfo(chartData);
        boolean isExistCurrentAlarm = isExistCurrentAlarm(alarmOtherInfo);
        if(null == alarmOtherInfo){
            alarmOtherInfo = new MdSpecialALarmInfo();
            alarmOtherInfo.setExist(false);
        }else {
            alarmOtherInfo.setExist(true);
        }
        long millSeconds = TimeControl.getMillSeconds(currentTime);
        Timestamp time = new Timestamp(millSeconds);
        if (isAlarm(chartData, alarmOtherInfo, isExistCurrentAlarm)) {
            // 当前告警不生效时，记录告警信息、对应历史信息、上报操作
            alarmOtherInfo.setAlarm_time(time);
            if(!isExistCurrentAlarm){
                alarmOtherInfo.setAlarm_msg(rule.getAlarmmsg());
                System.out.println("alarm ing");
                SpecialALarmService.addAlarmOtherService(rule, alarmOtherInfo);
                if (needReport) {
                    // 特殊告警转成 一般告警
                    alarmOtherInfo.setAlarm_num(0);
                    MdAlarmInfo alarmInfo = makeAlarmInfo(alarmOtherInfo);
                    AlarmService.alarm(rule, currentTime, alarmInfo);
                }
            }
        } else {
            if(isExistCurrentAlarm){
                //当前告警存在、执行清除操作
                System.out.println("clean alarm ing");
                alarmOtherInfo.setClear_time(time);
                alarmOtherInfo.setAlarm_time(time);
                SpecialALarmService.cleanAlarmOtherService(rule, alarmOtherInfo);
                // 特殊告警转成 一般告警
                MdAlarmInfo alarmInfo = makeAlarmInfo(alarmOtherInfo);
                AlarmService.cleanAlarmByAlarmInfo(alarmInfo);
            }
        }
    }
    
    private MdSpecialALarmInfo getAlarmOtherInfo (ChartData chartData) {
        if(ToolsUtils.ListIsNull(alarmOtherInfos))
            return null;
        for (MdSpecialALarmInfo mdAlarmOtherInfo : alarmOtherInfos) {
            if(existAlarmInfo(chartData, mdAlarmOtherInfo))
                return mdAlarmOtherInfo;
        }
        return null;
    }
    
    private boolean existAlarmInfo(ChartData chartData, MdSpecialALarmInfo mdAlarmOtherInfo) {
        if(rule.getAlarm_id().equals(mdAlarmOtherInfo.getAlarm_id())){
            if(null == chartData.getAttr4()){
                if(null == chartData.getAttr3()){
                    if(null == chartData.getAttr2()){
                        if(null == chartData.getAttr1()){
                            return true;
                        }else{
                            if(chartData.getAttr1().equals(mdAlarmOtherInfo.getAttr1()))
                                return true;
                        }
                    }else{
                        if(chartData.getAttr1().equals(mdAlarmOtherInfo.getAttr1())
                                && chartData.getAttr2().equals(mdAlarmOtherInfo.getAttr2())
                           )
                            return true;
                    }
                }else{
                    if(chartData.getAttr1().equals(mdAlarmOtherInfo.getAttr1())
                            && chartData.getAttr2().equals(mdAlarmOtherInfo.getAttr2())
                            && chartData.getAttr3().equals(mdAlarmOtherInfo.getAttr3())
                       )
                        return true;
                }
            }else{
                if(chartData.getAttr1().equals(mdAlarmOtherInfo.getAttr1())
                        && chartData.getAttr2().equals(mdAlarmOtherInfo.getAttr2())
                        && chartData.getAttr3().equals(mdAlarmOtherInfo.getAttr3())
                        && chartData.getAttr4().equals(mdAlarmOtherInfo.getAttr4())
                   )
                    return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是否存在当前告警信息, 告警为null 返回false
     * @param alarmOtherInfo
     * @return
     */
    private boolean isExistCurrentAlarm(MdSpecialALarmInfo alarmOtherInfo){
        if(null==alarmOtherInfo){
            return false;
        }
        if(alarmOtherInfo.getAlarm_num() > 0){
            return true;
        }
        return false;
    }
    
    private boolean isAlarm(ChartData newestData, MdSpecialALarmInfo alarmOtherInfo, boolean isExistCurrentAlarm) {
        String rules = rule.getAlarm_rule();
        boolean isAlarm = false;
        String expr = new String(rules);
        if (expr.indexOf(RuleValue.NEWEST.getType()) >= 0) {
            expr = replaceAllValue(expr, RuleValue.NEWEST, newestData);
        }
        String compValue="0";
        if(isExistCurrentAlarm){
            //第n+1次发生告警
            if (expr.indexOf(RuleValue.ALARM_LASTEST.getType()) >= 0) {
                compValue = alarmOtherInfo.getAlarm_val();
                expr = expr.replaceAll(RuleValue.LASTEST.getType(), alarmOtherInfo.getAlarm_val());
            }
        }else{
            // 第一次发生告警
            if (expr.indexOf(RuleValue.LASTEST.getType()) >= 0) {
                ChartData lastChartData = getChartDataByList(lastest, newestData);
                if(lastChartData==null){
                    return isAlarm;
                }
                compValue = lastChartData.getValue();
                expr = replaceAllValue(expr, RuleValue.LASTEST, lastChartData);
                alarmOtherInfo.setAlarm_val(lastChartData.getValue());
                if(!ToolsUtils.StringIsNull(newestData.getAttr1()))
                    alarmOtherInfo.setAttr1(newestData.getAttr1());
                if(!ToolsUtils.StringIsNull(newestData.getAttr2()))
                    alarmOtherInfo.setAttr2(newestData.getAttr2());
                if(!ToolsUtils.StringIsNull(newestData.getAttr3()))
                    alarmOtherInfo.setAttr3(newestData.getAttr3());
                if(!ToolsUtils.StringIsNull(newestData.getAttr4()))
                    alarmOtherInfo.setAttr4(newestData.getAttr4());
            }
        }
        expr = expr.replaceAll(RuleValue.ALARM_LASTEST.getType(), "1");
        logger.info("[expr: "+expr+"]");
        try {
            isAlarm = Boolean.valueOf(jse.eval(expr).toString());
            expr = newestData.getValue()+"-"+compValue;
            double v = Double.valueOf(jse.eval(expr).toString());
            if(v<0)
                v = v*-1;
            alarmOtherInfo.setMsg_desc(rule.getAlarmmsg()+" "+v);
        } catch (ScriptException e) {
            logger.error(e.getMessage());
        }
        return isAlarm;
    }
    
    private MdAlarmInfo makeAlarmInfo(MdSpecialALarmInfo alarmOtherInfo) {
        MdAlarmInfo alarmInfo = new MdAlarmInfo();
        alarmInfo.setAlarm_seq(alarmOtherInfo.getAlarm_seq_new());
        alarmInfo.setAlarm_id(alarmOtherInfo.getAlarm_id());
        alarmInfo.setMetric_id(alarmOtherInfo.getMetric_id());
        alarmInfo.setAlarm_level(alarmOtherInfo.getAlarm_level());
        alarmInfo.setAlarm_num(alarmOtherInfo.getAlarm_num());
        alarmInfo.setAlarm_rule(alarmOtherInfo.getAlarm_rule());
        alarmInfo.setAlarmmsg(alarmOtherInfo.getAlarm_msg());
        alarmInfo.setMsg_desc(alarmOtherInfo.getMsg_desc());
        alarmInfo.setModes(rule.getModes());
        alarmInfo.setDimension1(alarmOtherInfo.getAttr1());
        alarmInfo.setDimension1_name(alarmOtherInfo.getAttr1());
        alarmInfo.setDimension2(alarmOtherInfo.getAttr2());
        alarmInfo.setDimension2_name(alarmOtherInfo.getAttr2());
        alarmInfo.setAttr(alarmOtherInfo.getAttr());
        return alarmInfo;
    }
    
    private String replaceAllValue(String expr, RuleValue rv, ChartData chartData) {
        String express = expr.replaceAll(rv.getType(), chartData.getValue());
        return express;
    }
    
    private ChartData getChartDataByList(List<ChartData> list, ChartData chartData) {
        if (ToolsUtils.ListIsNull(list))
            return null;
        ChartData returnData = null;
        for (ChartData chartData2 : list) {
            if(null == chartData.getAttr4()){
                if(null == chartData.getAttr3()){
                    if(null == chartData.getAttr2()){
                        if(null == chartData.getAttr1()){
                            returnData = list.get(0);
                            break;
                        }else{
                            if(chartData.getAttr1().equals(chartData2.getAttr1())){
                                returnData = chartData2;
                                break;
                            }
                        }
                    }else{
                        if(chartData.getAttr1().equals(chartData2.getAttr1()) 
                                &&chartData.getAttr2().equals(chartData2.getAttr2())){
                            returnData = chartData2;
                            break;
                        }
                    }
                }else{
                    if(chartData.getAttr1().equals(chartData2.getAttr1()) 
                            &&chartData.getAttr2().equals(chartData2.getAttr2())
                            &&chartData.getAttr3().equals(chartData2.getAttr3())){
                        returnData = chartData2;
                        break;
                    }
                }
            }else{
                if(chartData.getAttr1().equals(chartData2.getAttr1()) 
                        &&chartData.getAttr2().equals(chartData2.getAttr2())
                        &&chartData.getAttr3().equals(chartData2.getAttr3())
                        &&chartData.getAttr4().equals(chartData2.getAttr4())){
                    returnData = chartData2;
                    break;
                }
            }
        }
        return returnData;
    }
}
