package com.asiainfo.lcims.omc.alarm.model;

/**
 * 图表数据配置表
 * 对应 MD_CHART_DATASET 表
 * @author zhul
 *
 */
public class MdChartDataSet {
    private String chart_name;
    private String table_name;
    private String metric_ids;
    private String metric_names;
    /** 数据范围 
     *  recent：最新一次数据，1day：从凌晨到现在，slottime：时间段（请求参数需带开始及结束时间）
     * */
    private String scope;
    
    private String marktype;
    private String chart_interval;
    private String groupby;
    private String method;
    
    private String left_join;
    private String conditions;
    
    //表格查询字段标识
    private String tablefields;
    
    private String round_num;
    
    public String getChart_name() {
        return chart_name;
    }
    public MdChartDataSet setChart_name(String chart_name) {
        this.chart_name = chart_name;
        return this;
    }
    public String getTable_name() {
        return table_name;
    }
    public MdChartDataSet setTable_name(String table_name) {
        this.table_name = table_name;
        return this;
    }
    public String getMetric_ids() {
        return metric_ids;
    }
    public MdChartDataSet setMetric_ids(String metric_ids) {
        this.metric_ids = metric_ids;
        return this;
    }
    public String getMetric_names() {
        return metric_names;
    }
    public MdChartDataSet setMetric_names(String metric_names) {
        this.metric_names = metric_names;
        return this;
    }
    public String getScope() {
        return scope;
    }
    public MdChartDataSet setScope(String scope) {
        this.scope = scope;
        return this;
    }
    public String getMarktype() {
        return marktype;
    }
    public MdChartDataSet setMarktype(String marktype) {
        this.marktype = marktype;
        return this;
    }
    public String getGroupby() {
        return groupby;
    }
    public MdChartDataSet setGroupby(String groupby) {
        this.groupby = groupby;
        return this;
    }
    public String getMethod() {
        return method;
    }
    public MdChartDataSet setMethod(String method) {
        this.method = method;
        return this;
    }
    public String getLeft_join() {
        return left_join;
    }
    public MdChartDataSet setLeft_join(String left_join) {
        this.left_join = left_join;
        return this;
    }
    public String getConditions() {
        return conditions;
    }
    public MdChartDataSet setConditions(String conditions) {
        this.conditions = conditions;
        return this;
    }
    
    public String getTablefields() {
        return tablefields;
    }

    public MdChartDataSet setTablefields(String tablefields) {
        this.tablefields = tablefields;
        return this;
    }
    public String getChart_interval() {
        return chart_interval;
    }
    
    public MdChartDataSet setChart_interval(String chart_interval) {
        this.chart_interval = chart_interval;
        return this;
    }
    
    public String getRound_num() {
        return round_num;
    }
    public MdChartDataSet setRound_num(String round_num) {
        this.round_num = round_num;
        return this;
    }
    
    public MdChartDataSet clone(){
        return new MdChartDataSet().setChart_name(chart_name).setTable_name(table_name).setMetric_ids(metric_ids)
                .setMetric_names(metric_names).setScope(scope).setMarktype(marktype).setChart_interval(chart_interval)
                .setGroupby(groupby).setMethod(method).setLeft_join(left_join).setConditions(conditions)
                .setTablefields(tablefields).setRound_num(round_num);
    }
}
