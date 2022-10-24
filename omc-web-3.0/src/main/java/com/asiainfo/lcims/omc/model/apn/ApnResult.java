package com.asiainfo.lcims.omc.model.apn;

/**
 * APN增删操作的响应结果
 * 
 * @author zhujiansheng
 * @version V1.0
 */
public class ApnResult {

    private String otype;

    private String apn;

    private String ret;

    private String desc;

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

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ApnResult [otype=" + otype + ", apn=" + apn + ", ret=" + ret + ", desc=" + desc
                + "]";
    }

}
