package com.asiainfo.lcims.omc.model.ais;

import java.util.Date;

import com.asiainfo.lcims.omc.util.ToolsUtils;

public class AisGroupMetricModel {

    private String group_id;

    private String group_metric_id;

    private String name;

    private String url;

    private Integer dimension_type;

    private String chart_name;

    private String metric_id;

    private String attr;

    private String dimension1;

    private String dimension2;

    private String dimension3;

    private Date create_time;

    private Date update_time;

    private String businesslinkurl;

    private String module;

    private String monitortarget1;

    private String monitortarget3;

    private String metric_name;

    private String module_name;

    private String dimension1_name;

    private String dimension2_name;

    private String dimension3_name;

    private String chart_title;

    private String dimension_type_name;

    private String group_name;
    
    private String alarm_rule;
    
    private String alarm_mvalue;
    
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_metric_id() {
        return group_metric_id;
    }

    public void setGroup_metric_id(String group_metric_id) {
        this.group_metric_id = group_metric_id;
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

    public String getBusinesslinkurl() {
        return businesslinkurl;
    }

    public void setBusinesslinkurl(String businesslinkurl) {
        this.businesslinkurl = businesslinkurl;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMonitortarget1() {
        return monitortarget1;
    }

    public void setMonitortarget1(String monitortarget1) {
        this.monitortarget1 = monitortarget1;
    }

    public String getMonitortarget3() {
        return monitortarget3;
    }

    public void setMonitortarget3(String monitortarget3) {
        this.monitortarget3 = monitortarget3;
    }

    public String getMetric_name() {
        return metric_name;
    }

    public void setMetric_name(String metric_name) {
        this.metric_name = metric_name;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
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

    public String getChart_title() {
        return chart_title;
    }

    public void setChart_title(String chart_title) {
        this.chart_title = chart_title;
    }

    public String getDimension_type_name() {
        return dimension_type_name;
    }

    public void setDimension_type_name(String dimension_type_name) {
        this.dimension_type_name = dimension_type_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAlarm_rule() {
        if(ToolsUtils.StringIsNull(alarm_rule))
            return "";
        return alarm_rule;
    }

    public void setAlarm_rule(String alarm_rule) {
        this.alarm_rule = alarm_rule;
    }

    public String getAlarm_mvalue() {
        if(ToolsUtils.StringIsNull(alarm_mvalue))
            return "";
        return alarm_mvalue;
    }

    public void setAlarm_mvalue(String alarm_mvalue) {
        this.alarm_mvalue = alarm_mvalue;
    }
}
