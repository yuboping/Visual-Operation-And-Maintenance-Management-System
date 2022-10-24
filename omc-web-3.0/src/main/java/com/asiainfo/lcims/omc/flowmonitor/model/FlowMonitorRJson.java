package com.asiainfo.lcims.omc.flowmonitor.model;

/**
 * 脚本执行返回的结果
 * 
 * @author XHT
 *
 */
public class FlowMonitorRJson {
    private String result_flag;// 脚本执行成功或者失败标识,必填字段
    private String result_data;// 脚本执行的结果数据,可用于下一步骤计算使用,可不填.
    private String cache_data;// 缓存 result_data的数据
    private String isEnd;//

    public FlowMonitorRJson() {
    }

    public FlowMonitorRJson(String result_flag) {
        this.result_data = result_flag;
        this.result_flag = result_flag;
        this.cache_data = result_flag;
    }

    /**
     * 
     * @param result_flag;
     *            <p>
     *            FlowMonitorParam.RESULT_FLAG_SUCCESS；FlowMonitorParam.RESULT_FLAG_FAILED
     * @param result_data
     */
    public FlowMonitorRJson(String result_flag, String result_data) {
        this.result_data = result_data;
        this.result_flag = result_flag;
    }

    public FlowMonitorRJson(String result_flag, String result_data, String cache_data) {
        this.result_data = result_data;
        this.result_flag = result_flag;
        this.cache_data = cache_data;
    }

    public String getResult_flag() {
        return result_flag;
    }

    public void setResult_flag(String result_flag) {
        this.result_flag = result_flag;
    }

    public String getResult_data() {
        return result_data;
    }

    public void setResult_data(String result_data) {
        this.result_data = result_data;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getCache_data() {
        return cache_data;
    }

    public void setCache_data(String cache_data) {
        this.cache_data = cache_data;
    }
}
