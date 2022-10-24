package com.asiainfo.lcims.util;

import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.param.InitParam;

public class AlarmMessageUtill {
    /**
     * 组装短信告警信息
     * @param rule
     * @return
     */
    public static String makeSmsMessage(MdAlarmRuleDetail rule, String now_date){
        String message = "";
        String level=InitParam.getAlarmLevelName(rule.getAlarm_level());
        message = "管理员，您好！"+getElementName(rule)+"于"+now_date+"时产生告警级别为["+level+"]的告警信息"+"["+rule.getAlarmmsg()+"]，请注意查收";
        return message;
    }
    
    public static String getElementName(MdAlarmRuleDetail rule){
        String elementname = "";
        if(ToolsUtils.StringIsNull(rule.getDimension2())){
            elementname = rule.getDimension1_name();
        }else{
            elementname = "["+rule.getDimension1_name() + " : " + rule.getDimension2_name()+"]";
        }
        return elementname;
    }
    
    
}
