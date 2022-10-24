package com.asiainfo.lcims.omc.model.ems;

public class EmsDelHostResponse {
    private String uuid;
    private int result;
    private String desc;
    
    public EmsDelHostResponse(String uuid, int result, String desc) {
        this.uuid = uuid;
        this.result = result;
        this.desc = desc;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
