package com.asiainfo.lcims.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.alarm.param.RuleValue;

public class ThresholdUtil {
    private static final Logger log = LoggerFactory.getLogger(ThresholdUtil.class);
    private static Pattern thresholdPattern = Pattern.compile("\\d+");
    /**
     * 支持：
     * newest < 80 || newest > 90
     * newest > 80 && newest < 90
     * newest < 80
     * > >= < <=
     * @param metricid
     * @param rule
     * @return
     */
    public static void getMetricThresholdByRule(MdAlarmRuleDetail rule) {
        String effectiveRule = rule.getEffectiveRule();
        log.info("转换数据：metricId["+rule.getMetric_id()+"] effectiveRule["+effectiveRule+"]");
        log.info("转换前:"+effectiveRule);
        if(effectiveRule.indexOf(RuleValue.LASTEST.getType()) >= 0 
                || effectiveRule.indexOf(RuleValue.PRE5MIN.getType()) >=0 
                || effectiveRule.indexOf(RuleValue.YESCURRENT.getType()) >=0 
                ) {
            rule.setMetric_threshold(effectiveRule);
            return ;
        }
        List<String> thresholdList = new ArrayList<String>();
        Matcher match = thresholdPattern.matcher(effectiveRule);
        while (match.find()) {
            thresholdList.add(match.group(0));
        }
        if(effectiveRule.indexOf(RuleValue.LOGIC_AND.getType()) >= 0 || effectiveRule.indexOf(RuleValue.LOGIC_OR.getType()) >= 0) {
            effectiveRule = effectiveRule.replaceAll(RuleValue.NEWEST.getType(), "");
            effectiveRule = effectiveRule.replaceAll(RuleValue.LOGIC_AND.getType(), RuleValue.LOGIC_AND.getName());
            effectiveRule = effectiveRule.replaceAll("\\|\\|", RuleValue.LOGIC_OR.getName());
            effectiveRule = effectiveRule.replaceAll(RuleValue.GREATER_THAN_EAUAL.getType(), RuleValue.GREATER_THAN_EAUAL.getName());
            effectiveRule = effectiveRule.replaceAll(RuleValue.GREATER_THAN.getType(), RuleValue.GREATER_THAN.getName());
            effectiveRule = effectiveRule.replaceAll(RuleValue.LESS_THAN_EAUAL.getType(), RuleValue.LESS_THAN_EAUAL.getName());
            effectiveRule = effectiveRule.replaceAll(RuleValue.LESS_THAN.getType(), RuleValue.LESS_THAN.getName());
            effectiveRule = effectiveRule.replaceAll(" ", "");
        } else {
            // 只有一个值，则发回一个值
            if(!ToolsUtils.ListIsNull(thresholdList)) {
                effectiveRule = thresholdList.get(0);
                //用metricid去参数表中捞数据，若无数据进行下一步
                effectiveRule = InitParam.getMetricValDesc(rule.getMetric_id(), effectiveRule);
            }
        }
        log.info("转换后:"+effectiveRule);
        rule.setMetric_threshold(effectiveRule);
        String originalDesc = InitParam.getMetricValDesc(rule.getMetric_id(), rule.getMetric_original());
        rule.setMetric_original(originalDesc);
    }
    
    public static void main(String[] args) {
        InitParam.init();
        MdAlarmRuleDetail ruleDetail = new MdAlarmRuleDetail();
        ruleDetail.setMetric_id("100120104025");
        ruleDetail.setMetric_original("1");
        String rule = "newest >= 80 && newest <= 90";
        ruleDetail.setEffectiveRule(rule);
        getMetricThresholdByRule(ruleDetail);
        rule = "newest < 80 || newest > 90";
        ruleDetail.setEffectiveRule(rule);
        getMetricThresholdByRule(ruleDetail);
        rule = "newest >= 1";
        ruleDetail.setEffectiveRule(rule);
        getMetricThresholdByRule(ruleDetail);
    }
}
