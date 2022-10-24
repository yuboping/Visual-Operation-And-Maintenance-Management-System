package com.asiainfo.lcims.omc.model.maintool;

public class HostCapability {

    private String host_name;

    private String ip;

    private String cpu;

    private String cpu_use_percent;

    private String memory;

    private String memory_use_percent;

    private String stime;

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCpu_use_percent() {
        return cpu_use_percent;
    }

    public void setCpu_use_percent(String cpu_use_percent) {
        this.cpu_use_percent = cpu_use_percent;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMemory_use_percent() {
        return memory_use_percent;
    }

    public void setMemory_use_percent(String memory_use_percent) {
        this.memory_use_percent = memory_use_percent;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    @Override
    public String toString() {
        return "HostCapability{" +
                "host_name='" + host_name + '\'' +
                ", ip='" + ip + '\'' +
                ", cpu='" + cpu + '\'' +
                ", cpu_use_percent='" + cpu_use_percent + '\'' +
                ", memory='" + memory + '\'' +
                ", memory_use_percent='" + memory_use_percent + '\'' +
                ", stime='" + stime + '\'' +
                '}';
    }
}
