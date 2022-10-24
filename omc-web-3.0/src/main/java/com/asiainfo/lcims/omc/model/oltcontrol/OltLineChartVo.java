package com.asiainfo.lcims.omc.model.oltcontrol;

import java.util.List;

public class OltLineChartVo {

    private List<String> xRateData;

    private List<String> yRateData;

    private String oltIp;

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

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }
}
