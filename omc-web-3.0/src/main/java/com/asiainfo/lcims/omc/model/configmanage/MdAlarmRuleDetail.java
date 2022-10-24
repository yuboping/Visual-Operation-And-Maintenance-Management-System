package com.asiainfo.lcims.omc.model.configmanage;

import java.util.Date;

public class MdAlarmRuleDetail {
    private String alarm_id;

    private String rule_id;

    private String name;

    private String url;

    private Integer dimension_type;

    private String chart_name;

    private String metric_id;

    private String attr;

    private String alarm_level;

    private String alarm_rule;

    private String modes;

    private String alarmmsg;

    private String dimension1;

    private String dimension2;

    private String dimension3;

    private Date create_time;

    private Date update_time;

    private String businesslink;

    private String businesslinkurl;

    private String report_rule;

    private String alarm_type;

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

    public Integer getDimension_type() {
        return dimension_type;
    }

    public void setDimension_type(Integer dimension_type) {
        this.dimension_type = dimension_type;
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getBusinesslink() {
        return businesslink;
    }

    public void setBusinesslink(String businesslink) {
        this.businesslink = businesslink;
    }

    public String getBusinesslinkurl() {
        return businesslinkurl;
    }

    public void setBusinesslinkurl(String businesslinkurl) {
        this.businesslinkurl = businesslinkurl;
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
}