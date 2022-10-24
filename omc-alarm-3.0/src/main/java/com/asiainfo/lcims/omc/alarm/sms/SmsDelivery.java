package com.asiainfo.lcims.omc.alarm.sms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.sms.gdcu.SendSmsReq;
import com.asiainfo.lcims.omc.alarm.sms.gdcu.SendSmsRsp;
import com.asiainfo.lcims.omc.alarm.sms.gdcu.SmsForwardService;
import com.asiainfo.lcims.omc.alarm.sms.gdcu.SmsForwardService_Service;
import com.asiainfo.lcims.util.AlarmMessageUtill;
import com.asiainfo.lcims.util.ToolsUtils;

public class SmsDelivery {

    private static final Logger log = LoggerFactory.getLogger(SmsDelivery.class);

    private static final SmsForwardService_Service factory = new SmsForwardService_Service();

    private static final String PHONE_NUMBER = "^(1[0-9])\\d{9}$";

    private String userName;

    private String passWord;

    public SmsDelivery(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public void sendMessage(MdAlarmRuleDetail rule, AlarmMode am) {
        try {
            SmsForwardService service = factory.getSendSms();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String now_date = format.format(new Date());
            String content = AlarmMessageUtill.makeSmsMessage(rule, now_date);
            List<Object> phoneList = am.getAddrs();
            if(ToolsUtils.ListIsNull(phoneList))
                log.info("phoneList is null,sms is fail!");
            for (Object o : phoneList) {
                String phone = String.valueOf(o);
                boolean idPhoneNum = isPhoneNum(String.valueOf(phone));
                if (!idPhoneNum) {
                    log.debug("{} is not a telephone number, please check.", phone);
                    continue;
                }
                SendSmsReq smsReq = getSmsReq(content, phone, rule);
                SendSmsRsp smsRsp = service.sendSms(smsReq);
                String remark = smsRsp.getRemark();
                String id = smsRsp.getId();
                log.info("sms send remark:{}, rspid:{}, phoneNum:{}, content:{}", remark, id, phone,
                        content);
            }
        } catch (Exception e) {
            log.info("sendMessage", e);
        }
    }

    private SendSmsReq getSmsReq(String content, String phones, MdAlarmRuleDetail rule) {
        String alarmid = rule.getAlarm_id();
        long current = System.currentTimeMillis();
        String smsId = alarmid + String.valueOf(current);
        log.info("sms request id : {}", smsId);
        SendSmsReq sendSmsReq = new SendSmsReq();
        sendSmsReq.setId(smsId);
        sendSmsReq.setSourceID(smsId);
        sendSmsReq.setUserName(userName);
        sendSmsReq.setPassWord(passWord);
        sendSmsReq.setSmsContext(content);
        sendSmsReq.setPhones(phones);
        return sendSmsReq;
    }
    
    /**
     * 判断是否是手机号
     * 
     * @param phone
     * @return
     */
    private boolean isPhoneNum(String phone) {
        if (phone == null || "".equals(phone) || phone.length() != 11) {
            return false;
        }
        Matcher match = Pattern.compile(PHONE_NUMBER).matcher(phone);
        boolean isMatch = match.matches();
        return isMatch;
    }

}
