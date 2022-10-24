package com.asiainfo.lcims.omc.report.model;

public class ResourceInterfaceData {

    private String host_addr;
    private String cpu;
    private String memory;
    private String disk;
    private String netcard;
    private String update_time;

    public String getHost_addr() {
        return host_addr;
    }

    public void setHost_addr(String host_addr) {
        this.host_addr = host_addr;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getNetcard() {
        return netcard;
    }

    public void setNetcard(String netcard) {
        this.netcard = netcard;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "ResourceInterfaceData [host_addr=" + host_addr + ", cpu=" + cpu + ", memory="
                + memory + ", disk=" + disk + ", netcard=" + netcard + ", update_time="
                + update_time + "]";
    }

}
