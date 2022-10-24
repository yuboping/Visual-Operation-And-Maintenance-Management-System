package com.asiainfo.lcims.omc.model.ems;

import java.util.List;

public class EmsAddHostRequest {
    private String uuid;
    private List<EmsAddHost> hostinfos;
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public List<EmsAddHost> getHostinfos() {
        return hostinfos;
    }
    public void setHostinfos(List<EmsAddHost> hostinfos) {
        this.hostinfos = hostinfos;
    }
    
}
