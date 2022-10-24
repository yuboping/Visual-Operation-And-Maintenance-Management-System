package com.asiainfo.lcims.omc.model.radius;

import java.util.List;

public class RadiusChartVo {

    private List<String> xRateData;

    private List<String> yRateData;

    private String dataName;

    private int type;

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

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
