package com.asiainfo.lcims.omc.model.radius;

import java.util.List;

public class ChartListVo {

   private List<RadiusChartVo> radiusChartVoList;

   private String name;

    public List<RadiusChartVo> getRadiusChartVoList() {
        return radiusChartVoList;
    }

    public void setRadiusChartVoList(List<RadiusChartVo> radiusChartVoList) {
        this.radiusChartVoList = radiusChartVoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
