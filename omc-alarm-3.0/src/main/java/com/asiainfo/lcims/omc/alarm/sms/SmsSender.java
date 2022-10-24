package com.asiainfo.lcims.omc.alarm.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.conf.SystemConf;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.AlarmMessageUtill;

public class SmsSender {
    private static final Logger log = LoggerFactory.getLogger(SmsSender.class);

    SystemConf conf = new SystemConf();

    public void sendMsg(List<Object> phoneList, MdAlarmRuleDetail rule) {
        log.info("phoneList" + phoneList.size());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String now_date = format.format(new Date());
        for (Object o : phoneList) {
            this.send(String.valueOf(o), rule, now_date);
        }
    }

    private void send(String phone, MdAlarmRuleDetail rule, String now_date) {
        try {
        	//调用shell发送告警
            String msg = AlarmMessageUtill.makeSmsMessage(rule, now_date);
            String shell = "sh " + conf.getSmsShellPaht() + "/sendsms.sh " + phone + " "
                    + msg;
            log.info("shell:{" + shell + "}");
            Process process = Runtime.getRuntime().exec(shell);
            String result = readReturn(process);
            process.waitFor();
            log.info("shellinfo_result:{" + result + "}");
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
