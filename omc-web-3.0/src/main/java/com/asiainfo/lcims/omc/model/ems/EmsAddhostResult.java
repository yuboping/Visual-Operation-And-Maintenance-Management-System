package com.asiainfo.lcims.omc.model.ems;

public class EmsAddhostResult {
    private String vmid;
    private int result;
    private String desc;
    public EmsAddhostResult(String vmid, int result, String desc) {
        this.vmid = vmid;
        this.result = result;
        this.desc = desc;
    }
    public String getVmid() {
        return vmid;
    }
    public void setVmid(String vmid) {
        this.vmid = vmid;
    }
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
