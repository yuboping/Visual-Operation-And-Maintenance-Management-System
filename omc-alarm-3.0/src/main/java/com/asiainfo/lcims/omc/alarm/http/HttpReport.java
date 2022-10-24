package com.asiainfo.lcims.omc.alarm.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.ConstantUtil;
import com.asiainfo.lcims.util.HttpUtils;

public class HttpReport {

    private static final Logger log = LoggerFactory.getLogger(HttpReport.class);

    /**
     * 上报操作
     * 
     * @param ruleDetail
     * @param alarmMode
     * @param currentTime
     * @param reportflag
     */
    public static void reportAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        log.info("HTTP reportAlarm start");
        HttpALarmInfo httpALarmInfo = new HttpALarmInfo();
        httpALarmInfo.setAlarmSource(ConstantUtil.HTTP_ALARM_SOURCE);
        httpALarmInfo.setAlarmId(ruleDetail.getAlarm_id());
        httpALarmInfo.setAlarmMsg(ruleDetail.getAlarmmsg());
        httpALarmInfo.setAlarmLeve(ruleDetail.getAlarm_level());
        httpALarmInfo.setIsClear(ConstantUtil.HTTP_ALARM_STATUS_REPORT);
        httpALarmInfo.setOccurTime(currentDate);
        httpALarmInfo.setClearTime("");
        httpALarmInfo.setNeName(ruleDetail.getNeName());
        httpALarmInfo.setNeIp(ruleDetail.getNeIp());
        httpALarmInfo.setAlarmType(Integer.parseInt(ruleDetail.getAlarm_type()));
        httpALarmInfo.setEquipmentType("AAA");
        httpALarmInfo.setLocateNeType("主机");
        httpALarmInfo.setAlarmText(ruleDetail.getAlarmText());
        JSONObject httpALarmInfoJson = JSONObject.parseObject(JSON.toJSONString(httpALarmInfo));
        String url = am.getModeattr();
        HttpUtils.post(url, httpALarmInfoJson.toString(), "UTF-8");
        log.info("HTTP reportAlarm end");
    }

    /**
     * 清除告警
     * 
     * @param alarmInfo
     * @param am
     * @param alarmStatus
     */
    public static void cleanAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        log.info("HTTP cleanAlarm start");
        HttpALarmInfo httpALarmInfo = new HttpALarmInfo();
        httpALarmInfo.setAlarmSource(ConstantUtil.HTTP_ALARM_SOURCE);
        httpALarmInfo.setAlarmId(alarmInfo.getAlarm_id());
        httpALarmInfo.setAlarmMsg(alarmInfo.getAlarmmsg());
        httpALarmInfo.setAlarmLeve(alarmInfo.getAlarm_level());
        httpALarmInfo.setIsClear(ConstantUtil.HTTP_ALARM_STATUS_CLEAR);
        httpALarmInfo.setOccurTime("");
        httpALarmInfo.setClearTime(currentDate);
        httpALarmInfo.setNeName(ruleDetail.getNeName());
        httpALarmInfo.setNeIp(ruleDetail.getNeIp());
        httpALarmInfo.setAlarmType(Integer.parseInt(alarmInfo.getAlarm_type()));
        httpALarmInfo.setEquipmentType("AAA");
        httpALarmInfo.setLocateNeType("主机");
        httpALarmInfo.setAlarmText(alarmInfo.getAlarmText());
        JSONObject httpALarmInfoJson = JSONObject.parseObject(JSON.toJSONString(httpALarmInfo));
        String url = am.getModeattr();
        HttpUtils.post(url, httpALarmInfoJson.toString(), "UTF-8");
        log.info("HTTP cleanAlarm end");
    }
}
