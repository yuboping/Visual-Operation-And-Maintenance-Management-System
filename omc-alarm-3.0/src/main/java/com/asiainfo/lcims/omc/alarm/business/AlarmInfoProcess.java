package com.asiainfo.lcims.omc.alarm.business;

import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.util.ToolsUtils;

public class AlarmInfoProcess implements Runnable {
    private static final Logger logger = LoggerFactory.make();
    private MdAlarmInfo alarmInfo;
    private List<MdAlarmRuleDetail> rulelist;

    public AlarmInfoProcess(MdAlarmInfo alarmInfo, List<MdAlarmRuleDetail> rulelist) {
        this.alarmInfo = alarmInfo;
        this.rulelist = rulelist;
    }

    @Override
    public void run() {
        // 对应的告警规则不存在，删除告警信息
        if (!isExitAlarmRule()) {
            logger.info("delete corresponding alarminfo ：alarmId={}", alarmInfo.getAlarm_id());
            makeMonitorTargetInfo();
            // 删除告警信息
            AlarmService.deleteAlarmInfo(alarmInfo.getAlarm_id());
            // 删除告警历史信息
            AlarmService.deleteHisAlarmInfo(alarmInfo.getAlarm_id());
            // syslog上报的要清除上报信息
            boolean needReport = AlarmService.conf.needReport();
            if (needReport && alarmInfo.getAlarm_num() > 0) {
                AlarmService.cleanAlarmByAlarmInfo(alarmInfo);
            }
        }
    }

    /**
     * 判断告警信息对应的规则是否存在
     * 
     * @return
     */
    private boolean isExitAlarmRule() {
        boolean flag = false;
        if (ToolsUtils.ListIsNull(rulelist)) {
            return false;
        }
        for (MdAlarmRuleDetail rule : rulelist) {
            if (alarmInfo.getAlarm_id().equals(rule.getAlarm_id())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 根据url 赋值 维度1 维度2 值:
     * 
     * node/host 节点 --> 主机 ： 维度1：节点名称， 维度2：主机IP.
     * 
     * node/area 节点 --> 地市 ： 维度1：节点名称， 维度2：地市名称.
     * 
     * area/bras 地市 --> bras: 维度1：地市 ， 维度2：brasip.
     * 
     * node 节点 ： 维度1：节点名称， 维度2：空.
     * 
     * area 地市 ： 维度1：地市名称， 维度2：空.
     * 
     * node/summary： 维度1 节点总览.
     * 
     * area/summary: 维度1 地市总览.
     * 
     * area/node: 地市 --> 节点 ：维度1：地市名称， 维度2：节点名称.
     * 
     * bras --> bras: 维度1：bras.
     */
    private void makeMonitorTargetInfo() {
        String url = alarmInfo.getUrl();
        String[] strings = url.split("--");
        String url_1 = url;
        if (strings.length == 2) {
            url_1 = strings[0];
        }
        if (url_1.endsWith("host")) {
            if (url_1.endsWith("node/host")) {// 节点下主机
                Host host = InitParam.getHost(alarmInfo.getDimension2());
                if (host == null) {
                    alarmInfo.setNeIp("null");
                    alarmInfo.setNeName(alarmInfo.getDimension2_name());
                    logger.info("rule dimension1 is [{}]", alarmInfo.getDimension1());
                } else {
                    alarmInfo.setNeIp(host.getAddr());
                    alarmInfo.setNeName(host.getHostname() + "/" + host.getHosttypename());
                }
                
            } else {// 主机
                Host host = InitParam.getHost(alarmInfo.getDimension1());
                if (host == null) {
                    alarmInfo.setNeIp("null");
                    alarmInfo.setNeName(alarmInfo.getDimension1_name());
                    logger.info("rule dimension1 is [{}]", alarmInfo.getDimension1());
                } else {
                    alarmInfo.setNeIp(host.getAddr());
                    alarmInfo.setNeName(host.getHostname() + "/" + host.getHosttypename());
                }
            }
        }
    }
}
