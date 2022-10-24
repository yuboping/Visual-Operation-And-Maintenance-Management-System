package com.asiainfo.lcims.omc.model.monitor;

public class ChartData {

    private String mark;
    private String value;

    public ChartData() {
    }

    public ChartData(String mark, String value) {
        this.mark = mark;
        this.value = value;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ChartData [mark=" + mark + ", value=" + value + "]";
    }

}
