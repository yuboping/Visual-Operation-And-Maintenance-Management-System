package com.asiainfo.lcims.omc.model.configmanage;

public class MdMetricType {
    private String id;

    private String metric_type_name;

    private String description;

    public String getId() {
        return id;
    }

    public String getMetric_type_name() {
        return metric_type_name;
    }

    public void setMetric_type_name(String metric_type_name) {
        this.metric_type_name = metric_type_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

}