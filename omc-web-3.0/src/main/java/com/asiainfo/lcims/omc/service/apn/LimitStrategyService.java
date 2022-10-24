package com.asiainfo.lcims.omc.service.apn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.asiainfo.lcims.omc.model.apn.LimitResp;
import com.asiainfo.lcims.omc.model.apn.LimitStrategy;
import com.asiainfo.lcims.omc.model.apn.MdApnLimitRule;
import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.persistence.apn.LimitStrategyDAO;
import com.asiainfo.lcims.omc.persistence.apn.MdApnRecordDAO;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.HttpJsonUtils;

@Service
public class LimitStrategyService {

    private static final Logger LOG = LoggerFactory.getLogger(LimitStrategyService.class);

    private final static int WID_SEQ_MAX = 99999;

    private final static String WID_SEQ_MIN = "00001";

    private final static BusinessConf BUSINESS_CONF = new BusinessConf();

    @Autowired
    private LimitStrategyDAO limitStrategyDAO;

    @Autowired
    private MdApnRecordDAO mdApnRecordDAO;

    public LimitResp getLimitStrategy(MdApnLimitRule mdApnLimitRule, String limitvalid) {
        String stype = "05";
        String otype = "01";
        String wid = stype + otype + getCurrentDate() + getWidSeq();
        String apnId = mdApnLimitRule.getApnId();
        String limitCycle = mdApnLimitRule.getLimit_cycle();
        String authValue = mdApnLimitRule.getAuth_value();
        String logValue = mdApnLimitRule.getLog_value();
        String recordValue = mdApnLimitRule.getRecord_value();
        String dayValue = mdApnLimitRule.getDay_value();
        MdApnRecord mdApnRecord = mdApnRecordDAO.getApnNameById(apnId);
        LimitStrategy limitStrategy = new LimitStrategy();
        limitStrategy.setStype(stype);
        limitStrategy.setOtype(otype);
        limitStrategy.setWid(wid);
        limitStrategy.setApn(mdApnRecord.getApn());
        limitStrategy.setLimitvalid(limitvalid);
        limitStrategy.setLimitcycle(limitCycle);
        limitStrategy.setAuththreshold(authValue);
        limitStrategy.setLogthreshold(logValue);
        limitStrategy.setRecordthreshold(recordValue);
        limitStrategy.setLimitthreshold(dayValue);
        String json = JSON.toJSONString(limitStrategy, SerializerFeature.WriteMapNullValue);
        LOG.info("limit strategy json content : {}", json);
        String url = BUSINESS_CONF.getLimitFlowUrl();
        String response = HttpJsonUtils.post(url, json);
        LimitResp limitResp = JSONObject.parseObject(response, LimitResp.class);
        LOG.info("limit strategy response data : {}", limitResp);
        return limitResp;
    }

    public String getWidSeq() {
        String widSeqStr = limitStrategyDAO.getWidSeq();
        int widSeq = Integer.parseInt(widSeqStr);
        int widSeqIncr = 0;
        if (widSeq == WID_SEQ_MAX) {
            widSeqIncr = 1;
        } else {
            widSeqIncr = widSeq + 1;
        }
        limitStrategyDAO.updateWidSeq(String.valueOf(widSeqIncr));
        String fixWidSeq = fixWidLen(widSeqStr);
        LOG.debug("widSeqIncr ： {}, fixWidSeq : {}", widSeqIncr, fixWidSeq);
        return fixWidSeq;
    }

    public LimitResp deleteLimitStrategy(String[] apnArray, String limitvalid) {
        String stype = "05";
        String otype = "01";
        String wid = stype + otype + getCurrentDate() + getWidSeq();
        List<String> delFailList = new ArrayList<>();
        List<String> delSuccessList = new ArrayList<>();
        String message = "";
        for (String apnId : apnArray) {
            MdApnRecord mdApnRecord = mdApnRecordDAO.getApnNameById(apnId);
            LimitStrategy limitStrategy = new LimitStrategy();
            limitStrategy.setStype(stype);
            limitStrategy.setOtype(otype);
            limitStrategy.setWid(wid);
            String apn = mdApnRecord.getApn();
            limitStrategy.setApn(apn);
            limitStrategy.setLimitvalid(limitvalid);
            String json = JSON.toJSONString(limitStrategy, SerializerFeature.WriteMapNullValue);
            LOG.info("delete limit strategy json content : {}", json);
            String url = BUSINESS_CONF.getLimitFlowUrl();
            String response = HttpJsonUtils.post(url, json);
            LimitResp limitResp = JSONObject.parseObject(response, LimitResp.class);
            LOG.info("delete limit strategy response data : {}", limitResp);
            if ("1".equals(limitResp.getRet())) {
                delFailList.add(apn);
            } else if ("0".equals(limitResp.getRet())) {
                delSuccessList.add(apn);
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "台APN取消成功，APN名：" + delSuccessList + "。";
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "台APN取消失败，主机名：" + delFailList + "。";
        }
        LimitResp response = new LimitResp();
        response.setRet("0");
        response.setDesc(message);
        return response;
    }

    public String getCurrentDate() {
        DateTools dateTools = new DateTools(DateUtil.WID_TIME_FORMAT);
        String currentDate = dateTools.getCurrentDate();
        return currentDate;
    }

    public String fixWidLen(String widSeq) {
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
