package com.asiainfo.lcims.omc.alarm.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;

public class GdcuSms {
    private static final Logger log = LoggerFactory.getLogger(GdcuSms.class);
    public static final SystemConf sysConf = new SystemConf();
    public static final SmsDelivery delivery = new SmsDelivery(sysConf.getUserName(),
            sysConf.getPasswd());
    public static void smsAlarm(MdAlarmRuleDetail rule, AlarmMode am){
        log.info("调用短信接口");
        delivery.sendMessage(rule, am);
    }
}
