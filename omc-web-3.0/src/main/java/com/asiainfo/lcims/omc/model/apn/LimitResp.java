package com.asiainfo.lcims.omc.model.apn;

public class LimitResp {

    private String stype;

    private String otype;

    private String wid;

    private String ret;

    private String desc;

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
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
        return "LimitResp [stype=" + stype + ", otype=" + otype + ", wid=" + wid + ", ret=" + ret
                + ", desc=" + desc + "]";
    }

}
