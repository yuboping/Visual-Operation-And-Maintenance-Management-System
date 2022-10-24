package com.asiainfo.lcims.omc.alarm.model;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.lcims.util.ToolsUtils;

/**
 * 
 * @author zhul 对应 MD_ALARM_RULE_DETAIL 表
 */
public class MdAlarmRuleDetail {
    private String alarm_id;
    private String rule_id;
    private String name;
    private String url;
    private int dimension_type;
    private String chart_name;
    private String metric_id;
    private String attr;
    private String alarm_level;
    private String alarm_rule;
    private String modes; // 告警方式
    private String alarmmsg;
    private String dimension1;
    private String dimension1_name;
    private String dimension2;
    private String dimension2_name;
    private String dimension3;
    private String dimension3_name;
    private String create_time;
    private String update_time;

    private Integer cycleid;
    private String cyclename;
    private String effectiveRule;
    private List<String> newestvalues = new ArrayList<String>();
    private List<String> marks = new ArrayList<String>();
    /** 告警链路，如 节点1->主机1 */
    private String alarm_linkname;

    private String collectvalue;

    private String alarm_mvalue;

    private String report_rule;

    private String alarm_type;

    private String neName;

    private String neIp;

    private String metric_original;

    private String metric_threshold;

    private String alarmText;

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChart_name() {
        return chart_name;
    }

    public void setChart_name(String chart_name) {
        this.chart_name = chart_name;
    }

    public String getMetric_id() {
        return metric_id;
    }

    public void setMetric_id(String metric_id) {
        this.metric_id = metric_id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getAlarm_level() {
        return alarm_level;
    }

    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }

    public String getAlarm_rule() {
        return alarm_rule;
    }

    public void setAlarm_rule(String alarm_rule) {
        this.alarm_rule = alarm_rule;
    }

    public String getModes() {
        return modes;
    }

    public void setModes(String modes) {
        this.modes = modes;
    }

    public String getAlarmmsg() {
        return alarmmsg;
    }

    public void setAlarmmsg(String alarmmsg) {
        this.alarmmsg = alarmmsg;
    }

    public String getDimension1() {
        return dimension1;
    }

    public void setDimension1(String dimension1) {
        this.dimension1 = dimension1;
    }

    public String getDimension2() {
        return dimension2;
    }

    public void setDimension2(String dimension2) {
        this.dimension2 = dimension2;
    }

    public String getDimension3() {
        return dimension3;
    }

    public void setDimension3(String dimension3) {
        this.dimension3 = dimension3;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getDimension_type() {
        return dimension_type;
    }

    public void setDimension_type(int dimension_type) {
        this.dimension_type = dimension_type;
    }

    public String getCyclename() {
        return cyclename;
    }

    public void setCyclename(String cyclename) {
        this.cyclename = cyclename;
    }

    public Integer getCycleid() {
        return cycleid;
    }

    public void setCycleid(Integer cycleid) {
        this.cycleid = cycleid;
    }

    public List<String> getNewestvalues() {
        return newestvalues;
    }

    public void setNewestvalues(List<String> newestvalues) {
        this.newestvalues = newestvalues;
    }

    public void addNewestvalues(String value) {
        newestvalues.add(value);
    }

    public void addMarks(String value) {
        marks.add(value);
    }

    public List<String> getMarks() {
        return marks;
    }

    public void setMarks(List<String> marks) {
        this.marks = marks;
    }

    public String getEffectiveRule() {
        return effectiveRule;
    }

    public void setEffectiveRule(String effectiveRule) {
        this.effectiveRule = effectiveRule;
    }

    public String getAlarm_linkname() {
        return alarm_linkname;
    }

    public void setAlarm_linkname(String alarm_linkname) {
        this.alarm_linkname = alarm_linkname;
    }

    public String getDimension1_name() {
        return dimension1_name;
    }

    public void setDimension1_name(String dimension1_name) {
        this.dimension1_name = dimension1_name;
    }

    public String getDimension2_name() {
        return dimension2_name;
    }

    public void setDimension2_name(String dimension2_name) {
        this.dimension2_name = dimension2_name;
    }

    public String getDimension3_name() {
        return dimension3_name;
    }

    public void setDimension3_name(String dimension3_name) {
        this.dimension3_name = dimension3_name;
    }

    public String getCollectvalue() {
        return collectvalue;
    }

    public void setCollectvalue(String collectvalue) {
        this.collectvalue = collectvalue;
    }

    public String getReport_rule() {
        return report_rule;
    }

    public void setReport_rule(String report_rule) {
        this.report_rule = report_rule;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public String getNeIp() {
        return neIp;
    }

    public void setNeIp(String neIp) {
        this.neIp = neIp;
    }

    public String getMetric_original() {
        return metric_original;
    }

    public void setMetric_original(String metric_original) {
        this.metric_original = metric_original;
    }

    public String getMetric_threshold() {
        return metric_threshold;
    }

    public void setMetric_threshold(String metric_threshold) {
        this.metric_threshold = metric_threshold;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }

    public void makeCollectValue() {
        if (ToolsUtils.ListIsNull(newestvalues)) {
            this.collectvalue = "null";
        } else {
            this.collectvalue = "";
            if (this.marks.size() > 1) {
                for (int i = 0; i < this.marks.size(); i++) {
                    if (i == 0)
                        this.collectvalue = this.marks.get(i) + ":" + this.newestvalues.get(i);
                    else
                        this.collectvalue = this.collectvalue + "," + this.marks.get(i) + ":"
                                + this.newestvalues.get(i);
                }
            } else {
                this.collectvalue = this.newestvalues.get(0);
            }
        }
    }

    @Override
    public String toString() {
        return "MdAlarmRuleDetail [ alarm_rule=" + alarm_rule + ", dimension1=" + dimension1
                + ",dimension2=" + dimension2 + ",dimension1_name=" + dimension1_name
                + ",dimension2_name=" + dimension2_name + ",url=" + url + ",chart_name="
                + chart_name + ",metric_id=" + metric_id + ", cyclename=" + cyclename + ", cycleid="
                + cycleid + " ]";
    }

    public String getAlarm_mvalue() {
        return alarm_mvalue;
    }

    public void setAlarm_mvalue(String alarm_mvalue) {
        this.alarm_mvalue = alarm_mvalue;
    }

}