package com.asiainfo.lcims.omc.agentserver.model;

import java.util.List;

/**
 * 脚本执行结果.
 * <p>
 * 如果脚本返回结果中的metricid不为空,则入库的metricid为脚本中传的metricid.
 * <p>
 * 如果脚本返回的metricid为空,则根据此指标信息中的metricid赋值.
 * 
 * @author XHT
 *
 */
public class MetricResult {
    private String metricid;
    private List<MetricResultData> data;

    public String getMetricid() {
        return metricid;
    }

    public void setMetricid(String metricid) {
        this.metricid = metricid;
    }

    public List<MetricResultData> getData() {
        return data;
    }

    public void setData(List<MetricResultData> data) {
        this.data = data;
    }

}
