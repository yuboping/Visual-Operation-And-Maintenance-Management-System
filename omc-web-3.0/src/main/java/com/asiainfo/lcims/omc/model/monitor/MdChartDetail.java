package com.asiainfo.lcims.omc.model.monitor;

/**
 * 图表详情表 对应 MD_CHART_DETAIL 表
 * 
 * @author zhul
 *
 */
public class MdChartDetail {
    private String chart_name;
    private String chart_title;
    private String chart_type;
    private String data_url;
    private String style;
    private Integer is_legend;
    private String color;
    private String extend;

    // 用于显示具体图表的告警信息
    private boolean isAlarm = false;
    private String alarmmsg = "";
    private String chart_interval;
    private String scope;

    public String getChart_name() {
        return chart_name;
    }

    public MdChartDetail setChart_name(String chart_name) {
        this.chart_name = chart_name;
        return this;
    }

    public String getChart_title() {
        return chart_title;
    }

    public MdChartDetail setChart_title(String chart_title) {
        this.chart_title = chart_title;
        return this;
    }

    public String getChart_type() {
        return chart_type;
    }

    public MdChartDetail setChart_type(String chart_type) {
        this.chart_type = chart_type;
        return this;
    }

    public String getData_url() {
        return data_url;
    }

    public MdChartDetail setData_url(String data_url) {
        this.data_url = data_url;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public MdChartDetail setStyle(String style) {
        this.style = style;
        return this;
    }

    public Integer getIs_legend() {
        return is_legend;
    }

    public MdChartDetail setIs_legend(Integer is_legend) {
        this.is_legend = is_legend;
        return this;
    }

    public String getColor() {
        return color;
    }

    public MdChartDetail setColor(String color) {
        this.color = color;
        return this;
    }

    public String getExtend() {
        return extend;
    }

    public MdChartDetail setExtend(String extend) {
        this.extend = extend;
        return this;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

    public String getAlarmmsg() {
        return alarmmsg;
    }

    public void setAlarmmsg(String alarmmsg) {
        this.alarmmsg = alarmmsg;
    }

    public String getChart_interval() {
        return chart_interval;
    }

    public void setChart_interval(String chart_interval) {
        this.chart_interval = chart_interval;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public MdChartDetail(MdChartDetail mdChartDetail) {
        this.chart_name = mdChartDetail.chart_name;
        this.chart_title = mdChartDetail.chart_title;
        this.chart_type = mdChartDetail.chart_type;
        this.data_url = mdChartDetail.data_url;
        this.style = mdChartDetail.style;
        this.is_legend = mdChartDetail.is_legend;
        this.color = mdChartDetail.color;
        this.extend = mdChartDetail.extend;
        this.chart_interval = mdChartDetail.chart_interval;
        this.scope = mdChartDetail.scope;
    }

    public MdChartDetail() {
    }

    @Override
    public String toString() {
        return "{\"chart_name\":\"" + chart_name + "\",\"chart_title\":\"" + chart_title
                + "\",\"chart_type\":\"" + chart_type + "\",\"style\":\"" + style
                + "\",\"data_url\":\"" + data_url + "\",\"is_legend\":\"" + is_legend
                + "\",\"color\":\"" + color + "\",\"extend\":\"" + extend
                + "\",\"chart_interval\":\"" + chart_interval + "\",\"scope\":\"" + scope + "\"}";
    }
}
