package com.asiainfo.lcims.omc.alarm.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.http.zywlw.PushUtils;
import com.asiainfo.lcims.omc.alarm.model.AlarmDataContent;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.ConstantUtil;

public class HttpDataPush {

    private static final Logger LOG = LoggerFactory.getLogger(HttpDataPush.class);

    public static final SystemConf SYSTEM_CONF = new SystemConf();

    /**
     * 上报操作
     * 
     * @param ruleDetail
     * @param am
     * @param currentDate
     * @param alarmInfo
     */
    public static void pushAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        LOG.info("HTTP push alarm start");
        String resultJsonStr;
        try {
            AlarmDataContent alarmData = new AlarmDataContent();
            String plat = "RADIUS";
            String product = "RADIUS-EAST";
            String originalMetric = ruleDetail.getMetric_id();
            String message = ruleDetail.getAlarmmsg();
            long time = TimeControl.getMillSeconds(currentDate) / 1000;
            String priorityStr = ruleDetail.getAlarm_level();
            int priority = Integer.parseInt(priorityStr);
            String eventId = ruleDetail.getAlarm_id();
            String alarmTypeStr = ruleDetail.getAlarm_type();
            int alarmType = Integer.parseInt(alarmTypeStr);
            String dimension1Name = ruleDetail.getDimension1_name();
            String dimension2Name = ruleDetail.getDimension2_name();
            String neName = getNeName(dimension1Name, dimension2Name);
            alarmData.setPlat(plat);
            alarmData.setProduct(product);
            alarmData.setOriginalMetric(originalMetric);
            alarmData.setAlarmType(alarmType);
            alarmData.setMessage(neName + " " + currentDate + ":" + message);
            alarmData.setTime(time);
            alarmData.setPriority(priority);
            alarmData.setEventId(eventId);
            alarmData.setState(ConstantUtil.HTTP_ALARM_STATUS_REPORT);
            String pushData = "[" + JSON.toJSONString(alarmData, SerializerFeature.WriteMapNullValue)
                    + "]";
            LOG.info("push data : {}", pushData);
            String loginUrl = SYSTEM_CONF.getPushDataLoginUrl();
            String userName = SYSTEM_CONF.getPushDataUsername();
            String password = SYSTEM_CONF.getPushDataPassword();
            String pushUrl = am.getModeattr();
            LOG.info("loginUrl : {}, userName : {}, password : {}, pushUrl : {}", loginUrl, userName,
                    password, pushUrl);
            resultJsonStr = PushUtils.push(loginUrl, userName, password, pushUrl, pushData);
            LOG.info("HTTP push alarm end, result : {}", resultJsonStr);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 清除告警
     * 
     * @param ruleDetail
     * @param am
     * @param currentDate
     * @param alarmInfo
     */
    public static void cleanAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        LOG.info("HTTP clean alarm start");
        AlarmDataContent alarmData = new AlarmDataContent();
        String plat = "RADIUS";
        String product = "RADIUS-EAST";
        String originalMetric = ruleDetail.getMetric_id();
        String message = ruleDetail.getAlarmmsg();
        long time = TimeControl.getMillSeconds(currentDate) / 1000;
        String priorityStr = ruleDetail.getAlarm_level();
        int priority = Integer.parseInt(priorityStr);
        String eventId = ruleDetail.getAlarm_id();
        String alarmTypeStr = ruleDetail.getAlarm_type();
        int alarmType = Integer.parseInt(alarmTypeStr);
        String dimension1Name = ruleDetail.getDimension1_name();
        String dimension2Name = ruleDetail.getDimension2_name();
        String neName = getNeName(dimension1Name, dimension2Name);
        alarmData.setPlat(plat);
        alarmData.setProduct(product);
        alarmData.setOriginalMetric(originalMetric);
        alarmData.setAlarmType(alarmType);
        alarmData.setMessage(neName + " " + currentDate + ":" + message);
        alarmData.setTime(time);
        alarmData.setPriority(priority);
        alarmData.setEventId(eventId);
        alarmData.setState(ConstantUtil.HTTP_ALARM_STATUS_CLEAR);
        String pushData = "[" + JSON.toJSONString(alarmData, SerializerFeature.WriteMapNullValue)
                + "]";
        LOG.info("push data : {}", pushData);
        String loginUrl = SYSTEM_CONF.getPushDataLoginUrl();
        String userName = SYSTEM_CONF.getPushDataUsername();
        String password = SYSTEM_CONF.getPushDataPassword();
        String pushUrl = am.getModeattr();
        String resultJsonStr = PushUtils.push(loginUrl, userName, password, pushUrl, pushData);
        LOG.info("HTTP clean alarm end, result : {}", resultJsonStr);
    }

    private static String getNeName(String dimension1Name, String dimension2Name) {
        String objectName = dimension1Name;
        if (StringUtils.isNotBlank(dimension2Name)) {
            objectName = dimension2Name;
        }
        return objectName;
    }

}
