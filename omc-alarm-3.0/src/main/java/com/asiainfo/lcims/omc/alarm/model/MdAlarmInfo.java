package com.asiainfo.lcims.omc.alarm.model;

/**
 * 
 * @author zhul MD_ALARM_INFO
 */
public class MdAlarmInfo {
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
    private int alarm_num;
    private String first_time;
    private String last_time;
    /** 0:未确认 1：确认 */
    private int confirm_state;
    private String confirm_time;
    private String confirm_name;
    private String clear_time;

    /** 0:未删除 1：已删除 */
    private int delete_state;

    private String alarm_mvalue;

    private String msg_desc;
    private Integer alarm_seq;

    private String alarm_type;

    private String neName;

    private String neIp;

    private String metric_original;

    private String metric_threshold;

    private String alarmText;

    private String report_rule;

    private String report_flag;

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

    public int getAlarm_num() {
        return alarm_num;
    }

    public void setAlarm_num(int alarm_num) {
        this.alarm_num = alarm_num;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public int getConfirm_state() {
        return confirm_state;
    }

    public void setConfirm_state(int confirm_state) {
        this.confirm_state = confirm_state;
    }

    public String getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(String confirm_time) {
        this.confirm_time = confirm_time;
    }

    public String getConfirm_name() {
        return confirm_name;
    }

    public void setConfirm_name(String confirm_name) {
        this.confirm_name = confirm_name;
    }

    public String getClear_time() {
        return clear_time;
    }

    public void setClear_time(String clear_time) {
        this.clear_time = clear_time;
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

    public Integer getDimension_type() {
        return dimension_type;
    }

    public void setDimension_type(Integer dimension_type) {
        this.dimension_type = dimension_type;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }

    public String getAlarm_mvalue() {
        return alarm_mvalue;
    }

    public void setAlarm_mvalue(String alarm_mvalue) {
        this.alarm_mvalue = alarm_mvalue;
    }

    public String getMsg_desc() {
        return msg_desc;
    }

    public void setMsg_desc(String msg_desc) {
        this.msg_desc = msg_desc;
    }

    public Integer getAlarm_seq() {
        return alarm_seq;
    }

    public void setAlarm_seq(Integer alarm_seq) {
        this.alarm_seq = alarm_seq;
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

    public String getReport_rule() {
        return report_rule;
    }

    public void setReport_rule(String report_rule) {
        this.report_rule = report_rule;
    }

    public String getReport_flag() {
        return report_flag;
    }

    public void setReport_flag(String report_flag) {
        this.report_flag = report_flag;
    }

}
