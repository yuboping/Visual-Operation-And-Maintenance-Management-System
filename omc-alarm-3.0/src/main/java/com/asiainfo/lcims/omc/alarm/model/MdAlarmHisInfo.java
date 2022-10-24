package com.asiainfo.lcims.omc.alarm.model;

/**
 * 
 * @author zhul MD_ALARM_HIS_INFO
 */
public class MdAlarmHisInfo {
    private String id;
    private String alarm_id;
    private String name;
    private String url;
    private Integer dimension_type;
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
    private String cycle_time;
    private String create_time;
    private String alarm_type;
    private String metric_original;
    private String metric_threshold;
    private String alarmText;
    private String report_rule;

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCycle_time() {
        return cycle_time;
    }

    public void setCycle_time(String cycle_time) {
        this.cycle_time = cycle_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getDimension_type() {
        return dimension_type;
    }

    public void setDimension_type(Integer dimension_type) {
        this.dimension_type = dimension_type;
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

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
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

    public String getReport_rule() {
        return report_rule;
    }

    public void setReport_rule(String report_rule) {
        this.report_rule = report_rule;
    }

}
