package com.asiainfo.lcims.omc.model.ems;

import java.util.List;

public class EmsDelHostRequest {
    private String uuid;
    private List<EmsDelHost> vmidinfos;
    public List<EmsDelHost> getVmidinfos() {
        return vmidinfos;
    }
    public void setVmidinfos(List<EmsDelHost> vmidinfos) {
        this.vmidinfos = vmidinfos;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
