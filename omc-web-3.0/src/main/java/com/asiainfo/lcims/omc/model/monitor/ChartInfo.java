package com.asiainfo.lcims.omc.model.monitor;

import java.util.List;

public class ChartInfo {
    private String legend;
    private String group;
    private String title;
    private List<?> data;
    
    public String getLegend() {
        return legend;
    }
    public void setLegend(String legend) {
        this.legend = legend;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public List<?> getData() {
        return data;
    }
    public void setData(List<?> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
