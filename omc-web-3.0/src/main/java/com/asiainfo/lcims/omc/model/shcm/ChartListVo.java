package com.asiainfo.lcims.omc.model.shcm;

import java.util.List;

public class ChartListVo {

    private List<WideChartVo> wideChartVoList;

    private String name;

    public List<WideChartVo> getWideChartVoList() {
        return wideChartVoList;
    }

    public void setWideChartVoList(List<WideChartVo> wideChartVoList) {
        this.wideChartVoList = wideChartVoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
