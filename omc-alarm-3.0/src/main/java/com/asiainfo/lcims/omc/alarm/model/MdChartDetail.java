package com.asiainfo.lcims.omc.alarm.model;
/**
 * 图表详情表
 * 对应 MD_CHART_DETAIL 表
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
    
    public MdChartDetail clone(){
        return new MdChartDetail().setChart_name(chart_name).setChart_title(chart_title).setChart_type(chart_type)
                .setData_url(data_url).setStyle(style).setIs_legend(is_legend).setColor(color).setExtend(extend);
    }
    
    
    @Override
    public String toString() {
        return "{\"chart_name\":\"" + chart_name + "\",\"chart_title\":\"" + chart_title
                + "\",\"chart_type\":\"" + chart_type + "\",\"style\":\"" + style + "\",\"data_url\":\""
                + data_url + "\",\"is_legend\":\""+is_legend
                + "\",\"color\":\""+color
                + "\",\"extend\":\""+extend
                + "\"}";
    }
}
