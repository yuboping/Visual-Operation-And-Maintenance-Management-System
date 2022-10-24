package com.asiainfo.lcims.omc.alarm.sms.hncm;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.AlarmMessageUtill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HncuSmsSender {
    private static final Logger log = LoggerFactory.getLogger(HncuSmsSender.class);

    SystemConf conf = new SystemConf();

    public void sendMsg(List<Object> phoneList, MdAlarmRuleDetail rule, String currentTime) {
        log.info("phoneList" + phoneList.size());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String now_date = format.format(new Date());
        for (Object o : phoneList) {
            this.send(String.valueOf(o), rule, currentTime);
        }
    }

    private void send(String phone, MdAlarmRuleDetail rule, String currentTime) {
        try {
        	//调用shell发送告警
            String msg = AlarmMessageUtill.makeSmsMessage(rule, currentTime);
            log.info("短信内容为：{}",msg);
            SMSSendClient.send(conf.getSmsIp(), Integer.parseInt(conf.getSmsPort()), 5000, phone, msg);
        } catch (Exception e) {
            log.info("Exception:" + e.getMessage());
        }
    }

    private String readReturn(Process process) throws IOException {
        String ret = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            String line;
            while ((line = input.readLine()) != null) {
                ret += line;
            }
        } finally {
            input.close();
        }
        return ret;
    }
}
