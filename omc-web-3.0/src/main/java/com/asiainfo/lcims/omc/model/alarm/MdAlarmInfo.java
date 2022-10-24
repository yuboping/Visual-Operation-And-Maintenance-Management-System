package com.asiainfo.lcims.omc.model.alarm;

import org.apache.commons.lang.StringUtils;

/**
 * 告警信息表MD_ALARM_INFO实体类
 */
public class MdAlarmInfo {

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
    private String modes;
    private String alarmmsg;
    private String dimension1;
    private String dimension2;
    private String dimension3;
    private Integer alarm_num;
    private String first_time;
    private String last_time;
    private Integer confirm_state;
    private String confirm_time;
    private String confirm_name;
    private String clear_time;
    private String dimension1_name;
    private String dimension2_name;
    private String dimension3_name;
    private String confirm_state_str;
    private String query_type;


    //指标名称
    private String metric_name;
    //告警等级名称
    private String alarmname;
    private String alarm_level_name;
    //模块名称
    private String model_name;
    //确认状态 1已确认 0未确认
    private String confirm_state_name;
    //告警目标
    private String dimension_name;

    //查询条件 起始时间
    private String start_time;
    private String end_time;
    
    private String alarm_mvalue;
    
    // 上海移动新增字段（指标阈值）
    private String metric_threshold;
    // 上海移动新增字段（告警正文）
    private String alarmtext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getAlarm_num() {
        return alarm_num;
    }

    public void setAlarm_num(Integer alarm_num) {
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

    public Integer getConfirm_state() {
        return confirm_state;
    }

    public void setConfirm_state(Integer confirm_state) {
        this.confirm_state = confirm_state;
    }

    public String getConfirm_time() {
        if(StringUtils.isEmpty(this.confirm_time)){
            confirm_time = "";
        }
        return confirm_time;
    }

    public void setConfirm_time(String confirm_time) {
        this.confirm_time = confirm_time;
    }

    public String getConfirm_name() {
        if(StringUtils.isEmpty(this.confirm_name)){
            confirm_name = "";
        }
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getModes() {
        return modes;
    }

    public void setModes(String modes) {
        this.modes = modes;
    }

    public Integer getDimension_type() {
        return dimension_type;
    }

    public void setDimension_type(Integer dimension_type) {
        this.dimension_type = dimension_type;
    }

    public String getMetric_name() {
        return metric_name;
    }

    public void setMetric_name(String metric_name) {
        this.metric_name = metric_name;
    }

    public String getAlarmname() {
        return alarmname;
    }

    public void setAlarmname(String alarmname) {
        this.alarmname = alarmname;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
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

    public String getConfirm_state_name() {
        if(StringUtils.isEmpty(this.confirm_state_name)){
            confirm_state_name = "未确认";
        }
        return confirm_state_name;
    }

    public void setConfirm_state_name(String confirm_state_name) {
        this.confirm_state_name = confirm_state_name;
    }

    public String getDimension_name() {
        if(StringUtils.isEmpty(this.dimension_name)){
            dimension_name = "";
        }
        return dimension_name;
    }

    public void setDimension_name(String dimension_name) {
        this.dimension_name = dimension_name;
    }

    public String getConfirm_state_str() {
        return confirm_state_str;
    }

    public void setConfirm_state_str(String confirm_state_str) {
        this.confirm_state_str = confirm_state_str;
    }

    public String getQuery_type() {
        return query_type;
    }

    public void setQuery_type(String query_type) {
        this.query_type = query_type;
    }

    public String getAlarm_level_name() {
        return alarm_level_name;
    }

    public void setAlarm_level_name(String alarm_level_name) {
        this.alarm_level_name = alarm_level_name;
    }

    public String getMetric_threshold() {
        return metric_threshold;
    }

    public void setMetric_threshold(String metric_threshold) {
        this.metric_threshold = metric_threshold;
    }

    public String getAlarmtext() {
        return alarmtext;
    }

    public void setAlarmtext(String alarmtext) {
        this.alarmtext = alarmtext;
    }

    public String getAlarm_mvalue() {
        return alarm_mvalue;
    }

    public void setAlarm_mvalue(String alarm_mvalue) {
        this.alarm_mvalue = alarm_mvalue;
    }

    @Override
    public String toString() {
        return "MdAlarmInfo{" + "alarm_id='" + alarm_id + '\'' + ", name='" + name + '\''
                + ", url='" + url + '\'' + ", chart_name='" + chart_name + '\'' + ", metric_id='"
                + metric_id + '\'' + ", attr='" + attr + '\'' + ", alarm_level='" + alarm_level
                + '\'' + ", alarm_rule='" + alarm_rule + '\'' + ", alarmmsg='" + alarmmsg + '\''
                + ", dimension1='" + dimension1 + '\'' + ", dimension2='" + dimension2 + '\''
                + ", dimension3='" + dimension3 + '\'' + ", alarm_num=" + alarm_num
                + ", first_time='" + first_time + '\'' + ", last_time='" + last_time + '\''
                + ", confirm_state=" + confirm_state + ", confirm_time='" + confirm_time + '\''
                + ", confirm_name='" + confirm_name + '\'' + ", clear_time='" + clear_time + '\''
                + ", start_time='" + start_time + '\'' + ", end_time='" + end_time + '\''
                + ", metric_threshold='" + metric_threshold + '\'' + ", alarmtext='" + alarmtext
                + '\'' + '}';
    }

}
