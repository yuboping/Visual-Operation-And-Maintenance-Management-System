package com.asiainfo.lcims.omc.model;

/**
 * 管理员管理时页面显示使用
 * 
 * @author Administrator
 *
 */
public class KeyValueModel {

    private String key;

    private String value;

    private boolean checkflag;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCheckflag() {
        return checkflag;
    }

    public void setCheckflag(boolean checkflag) {
        this.checkflag = checkflag;
    }
}
