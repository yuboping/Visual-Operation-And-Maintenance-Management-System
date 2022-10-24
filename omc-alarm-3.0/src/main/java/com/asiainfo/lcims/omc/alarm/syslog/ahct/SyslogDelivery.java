package com.asiainfo.lcims.omc.alarm.syslog.ahct;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.AlarmMsg;
import com.asiainfo.lcims.omc.alarm.model.AlarmMsg.Builder;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.model.SendAddress;
import com.asiainfo.lcims.omc.alarm.syslog.SyslogSender;
import com.asiainfo.lcims.util.ConstantUtil;

/**
 * 组装并发送告警信息
 * 
 * @author zhujiansheng
 * @date 2019年3月26日 下午9:41:54
 * @version V1.0
 */
public class SyslogDelivery {

    private static final Logger LOG = LoggerFactory.getLogger(SyslogDelivery.class);

    private static final ConcurrentHashMap<String, String> CHART_NAME_APPLY_MAP = new ConcurrentHashMap<String, String>();
    
    static {
        CHART_NAME_APPLY_MAP.put("billing_", "billing");
        CHART_NAME_APPLY_MAP.put("inter_", "interface");
        CHART_NAME_APPLY_MAP.put("ocs_", "ocsradius");
        CHART_NAME_APPLY_MAP.put("radius_", "radius");
        CHART_NAME_APPLY_MAP.put("vpdn_", "vpdnradius");
        CHART_NAME_APPLY_MAP.put("zdfw_", "zdfw");
    }
    
    public static void ahctSyslogAlarm(MdAlarmRuleDetail ruleDetail, AlarmMode alarmMode,
            String currentTime, String alarmFlag) {
        AlarmMsg alarmMsg = assembleMsg(ruleDetail, currentTime, alarmFlag);
        List<Object> addrs = alarmMode.getAddrs();
        for (Object tmp : addrs) {
            SendAddress addr = (SendAddress) tmp;
            SyslogSender sender = new SyslogSender(addr.getIp(), addr.getPort());
            try {
                sender.send(alarmMsg.toString().getBytes("UTF-8"));
                LOG.info("ahct send alarmMsg : {}", alarmMsg.toString());
            } catch (Exception e) {
                LOG.error("ahct syslog alarm error, reason : {}", e);
            } finally {
                sender.close();
            }
        }
    }

    /**
     * 组装告警信息AlarmMsg
     * 
     * @param ruleDetail
     * @param currentTime
     * @param alarmFlag
     * @return
     */
    public static AlarmMsg assembleMsg(MdAlarmRuleDetail ruleDetail, String currentTime,
            String alarmFlag) {
        String hostName = null;
        String hostIp = null;
        if (StringUtils.isBlank(ruleDetail.getDimension2())) {
            String dimension1Name = ruleDetail.getDimension1_name();
            LOG.info("dimension1Name : [{}]", dimension1Name);
            int lastIndex = StringUtils.lastIndexOf(dimension1Name, "_");
            hostIp = StringUtils.substring(dimension1Name, lastIndex + 1);
            hostName = StringUtils.substring(dimension1Name, 0, lastIndex);
        } else {
            String dimension2Name = ruleDetail.getDimension2_name();
            LOG.info("dimension2 : [{}], dimension2Name : [{}]", ruleDetail.getDimension2(),
                    dimension2Name);
            int lastIndex = StringUtils.lastIndexOf(dimension2Name, "_");
            hostIp = StringUtils.substring(dimension2Name, lastIndex + 1);
            hostName = StringUtils.substring(dimension2Name, 0, lastIndex);
        }
        String alarmTime = currentTime + ":00";
        String alarmDetail = ruleDetail.getAlarmmsg();
        String alarmLevel = ruleDetail.getAlarm_level();
        String chartName = ruleDetail.getChart_name();
        LOG.debug("chart name : {}", chartName);
        String alarmType = null;
        String alarmPort = null;
        if (StringUtils.containsIgnoreCase(chartName, ConstantUtil.ORACLE_CHART_NAME)
                || StringUtils.equalsIgnoreCase(chartName, ConstantUtil.CPU_CHART_NAME)
                || StringUtils.equalsIgnoreCase(chartName, ConstantUtil.MEMORY_CHART_NAME)
                || StringUtils.equalsIgnoreCase(chartName, ConstantUtil.TIME_CHART_NAME)
                || StringUtils.equalsIgnoreCase(chartName, ConstantUtil.PROCESS_CHART_NAME)
                || StringUtils.equalsIgnoreCase(chartName, ConstantUtil.FS_CHART_NAME)) {
            alarmType = "0";
        } else {
            alarmType = "1";
            alarmPort = transferApply(chartName) == null ? "{NULL}" : transferApply(chartName);
        }
        Builder builder = new AlarmMsg.Builder();
        builder.setHostName(hostName).setHostIp(hostIp).setAlarmType(alarmType)
                .setAlarmDetail(alarmDetail).setAlarmPort(alarmPort)
                .setAlarmLevel(alarmLevel).setAlarmRegion("0");
        if (ConstantUtil.ALARM_FLAG.equals(alarmFlag)) {
            builder.setAlarmCreateTime(alarmTime).setAlarmFlag(alarmFlag);
        } else if (ConstantUtil.CLEAN_FLAG.equals(alarmFlag)) {
            builder.setAlarmCleanTime(alarmTime).setAlarmFlag(alarmFlag);
        }
        AlarmMsg alarmMsg = builder.build();
        return alarmMsg;
    }

    /**
     * 根据chartName转化成应用名称
     * 
     * @param chartName
     * @return
     */
    public static String transferApply(String chartName) {
        Set<Entry<String, String>> chartNameSet = CHART_NAME_APPLY_MAP.entrySet();
        for (Entry<String, String> chartNameEntry : chartNameSet) {
            String chartNamePrefix = chartNameEntry.getKey();
            if (chartName.startsWith(chartNamePrefix)) {
                String value = chartNameEntry.getValue();
                return value;
            }
        }
        return null;
    }

}