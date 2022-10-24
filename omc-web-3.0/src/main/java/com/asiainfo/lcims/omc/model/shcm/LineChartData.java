package com.asiainfo.lcims.omc.model.shcm;

import java.util.List;

/**
 * @Author: YuChao
 * @Date: 2020/1/7 12:06
 */
public class LineChartData {

    private List<String> xRateData;

    private List<String> yRateData;

    private String title;

    public List<String> getxRateData() {
        return xRateData;
    }

    public void setxRateData(List<String> xRateData) {
        this.xRateData = xRateData;
    }

    public List<String> getyRateData() {
        return yRateData;
    }

    public void setyRateData(List<String> yRateData) {
        this.yRateData = yRateData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
