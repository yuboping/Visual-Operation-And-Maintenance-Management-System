package com.asiainfo.lcims.omc.socket.model;

import java.util.List;

public class BaseValue<T extends BaseData> {
    private String mark;
    private String optype;
    private List<T> info;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public List<T> getInfo() {
        return info;
    }

    public void setInfo(List<T> info) {
        this.info = info;
    }

}
