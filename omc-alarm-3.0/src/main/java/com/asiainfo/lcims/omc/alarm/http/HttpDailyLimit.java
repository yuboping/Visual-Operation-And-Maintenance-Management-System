package com.asiainfo.lcims.omc.alarm.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.asiainfo.lcims.omc.alarm.dao.AlarmSeqDAO;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.DailyLimitData;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmInfo;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.DateUtil;
import com.asiainfo.lcims.util.HttpJsonUtils;

public class HttpDailyLimit {

    private static final Logger LOG = LoggerFactory.getLogger(HttpDailyLimit.class);

    private final static String WID_SEQ_MIN = "00001";

    private final static int WID_SEQ_MAX = 99999;

    public static void orderNotice(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        LOG.info("HTTP daily limit order notice start");
        String stype = "05";
        String otype = "11";
        String wid = stype + otype + getCurrentDate() + getWidSeq();
        String apn = ruleDetail.getDimension1_name();
        DailyLimitData orderDailyLimit = new DailyLimitData();
        orderDailyLimit.setStype(stype);
        orderDailyLimit.setOtype(otype);
        orderDailyLimit.setWid(wid);
        orderDailyLimit.setApn(apn);
        String orderContent = JSON.toJSONString(orderDailyLimit,
                SerializerFeature.WriteMapNullValue);
        LOG.info("daily limit order content : {}", orderContent);
        String url = am.getModeattr() + "/iot/limitflow/order";
        String httpResp = HttpJsonUtils.post(url, orderContent);
        LOG.info("HTTP daily limit order notice end, {}", httpResp);
    }

    public static void unorderNotice(MdAlarmRuleDetail ruleDetail, AlarmMode am, String currentDate,
            MdAlarmInfo alarmInfo) {
        LOG.info("HTTP daily limit unorder notice start");
        String stype = "05";
        String otype = "11";
        String wid = stype + otype + getCurrentDate() + getWidSeq();
        String apn = ruleDetail.getDimension1_name();
        DailyLimitData dailyLimit = new DailyLimitData();
        dailyLimit.setStype(stype);
        dailyLimit.setOtype(otype);
        dailyLimit.setWid(wid);
        dailyLimit.setApn(apn);
        String unorderContent = JSON.toJSONString(dailyLimit, SerializerFeature.WriteMapNullValue);
        LOG.info("daily limit unorder content : {}", unorderContent);
        String url = am.getModeattr() + "/iot/limitflow/unorder";
        String httpResp = HttpJsonUtils.post(url, unorderContent);
        LOG.info("HTTP daily limit unorder notice end, {}", httpResp);
    }

    public static String getCurrentDate() {
        DateTools dateTools = new DateTools(DateUtil.WID_TIME_FORMAT);
        String currentDate = dateTools.getCurrentDate();
        return currentDate;
    }

    public static String getWidSeq() {
        String widSeqStr = AlarmSeqDAO.getWidSeq();
        int widSeq = Integer.parseInt(widSeqStr);
        int widSeqIncr = 0;
        if (widSeq == WID_SEQ_MAX) {
            widSeqIncr = 1;
        } else {
            widSeqIncr = widSeq + 1;
        }
        AlarmSeqDAO.updateWidSeq(String.valueOf(widSeqIncr));
        String fixWidSeq = fixWidLen(widSeqStr);
        LOG.debug("widSeqIncr ： {}, fixWidSeq : {}", widSeqIncr, fixWidSeq);
        return fixWidSeq;
    }

    public static String fixWidLen(String widSeq) {
        if (widSeq == null) {
            return WID_SEQ_MIN;
        }
        int length = widSeq.length();
        if (length < 5) {
            String fixWidSeq = StringUtils.substring(WID_SEQ_MIN, 0, 5 - length) + widSeq;
            return fixWidSeq;
        } else {
            return widSeq;
        }
    }

}
