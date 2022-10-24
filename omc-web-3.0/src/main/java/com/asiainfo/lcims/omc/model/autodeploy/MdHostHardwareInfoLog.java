package com.asiainfo.lcims.omc.model.autodeploy;

import java.util.Date;

/**
 * @Author: YuChao
 * @Date: 2019/3/28 16:56
 */
public class MdHostHardwareInfoLog {

    private String id;
    private String ip;
    private String os;
    private String cpu;
    private String memory;
    private String diskspace;
    private Date deploy_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
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

    public String getDiskspace() {
        return diskspace;
    }

    public void setDiskspace(String diskspace) {
        this.diskspace = diskspace;
    }

    public Date getDeploy_time() {
        return deploy_time;
    }

    public void setDeploy_time(Date deploy_time) {
        this.deploy_time = deploy_time;
    }

    @Override
    public String toString() {
        return "MdHostHardwareInfoLog{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", os='" + os + '\'' +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", diskspace='" + diskspace + '\'' +
                ", deploy_time=" + deploy_time +
                '}';
    }
}
