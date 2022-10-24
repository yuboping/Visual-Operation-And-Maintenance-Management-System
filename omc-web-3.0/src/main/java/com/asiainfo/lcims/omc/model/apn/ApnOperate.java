package com.asiainfo.lcims.omc.model.apn;

/**
 * APN增删操作
 * 
 * @author zhujiansheng
 * @version V1.0
 */
public class ApnOperate {

    private String otype;

    private String apn;

    private String updateTime;

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ApnOperate [otype=" + otype + ", apn=" + apn + ", updateTime=" + updateTime + "]";
    }

}
