package com.asiainfo.lcims.omc.model.shcm;

public class AuthenSituationChartData {

    private String mark;

    private String value;

    private String authen_type;

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

    public String getAuthen_type() {
        return authen_type;
    }

    public void setAuthen_type(String authen_type) {
        this.authen_type = authen_type;
    }

    @Override
    public String toString() {
        return "AuthenSituationChartData [mark=" + mark + ", value=" + value + ", authen_type="
                + authen_type + "]";
    }

}
