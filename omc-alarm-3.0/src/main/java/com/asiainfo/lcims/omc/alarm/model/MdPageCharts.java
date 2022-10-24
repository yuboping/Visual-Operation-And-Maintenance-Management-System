package com.asiainfo.lcims.omc.alarm.model;

/**
 * 图表页面展示配置表
 * 对应 MD_PAGE_CHARTS 表
 * @author zhul
 *
 */
public class MdPageCharts {
    private String url;
    private String chart_name;
    private int sequence;
    
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
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
