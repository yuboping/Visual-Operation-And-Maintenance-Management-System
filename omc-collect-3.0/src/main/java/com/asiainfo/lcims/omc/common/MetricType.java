package com.asiainfo.lcims.omc.common;

public enum MetricType {
    SINGLE_TYPE("1", "单维度"), DOUBLE_TYPE("2", "二维度"), MORE_TYPE("3", "多维度"), MONTH_MORE_TYPE("4","月表多维度");

    private String type;
    private String name;

    MetricType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static MetricType state(String type) {
        MetricType[] values = MetricType.values();
        for (MetricType metricType : values) {
            if (metricType.getType().equals(type)) {
                return metricType;
            }
        }
        return null;
    }
}
