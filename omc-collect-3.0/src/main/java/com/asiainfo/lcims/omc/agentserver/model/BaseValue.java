package com.asiainfo.lcims.omc.agentserver.model;

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

    /**
     * 判断info是否为空,如果为空则返回true,不为空则返回false
     * 
     * @return
     */
    public boolean checkInfoListIsEmpty() {
        if (this.info == null) {
            return true;
        }
        if (this.info.isEmpty()) {
            return true;
        }
        return false;
    }
}
