package com.asiainfo.lcims.omc.model.autodeploy;

import java.util.Date;

/**
 * @Author: YuChao
 * @Date: 2019/3/28 14:35
 */
public class MdDeployLog {

    private String id;
    private String ip;
    private Integer deploy_status;
    private String deploy_des;
    private Date deploy_time;
    private String deploy_batch;

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

    public Integer getDeploy_status() {
        return deploy_status;
    }

    public void setDeploy_status(Integer deploy_status) {
        this.deploy_status = deploy_status;
    }

    public String getDeploy_des() {
        return deploy_des;
    }

    public void setDeploy_des(String deploy_des) {
        this.deploy_des = deploy_des;
    }

    public Date getDeploy_time() {
        return deploy_time;
    }

    public void setDeploy_time(Date deploy_time) {
        this.deploy_time = deploy_time;
    }

    public String getDeploy_batch() {
        return deploy_batch;
    }

    public void setDeploy_batch(String deploy_batch) {
        this.deploy_batch = deploy_batch;
    }

    @Override
    public String toString() {
        return "MdDeployLog{" + "id='" + id + '\'' + ", ip='" + ip + '\'' + ", deploy_status="
                + deploy_status + ", deploy_des='" + deploy_des + '\'' + ", deploy_time="
                + deploy_time + ", deploy_batch=" + deploy_batch + '}';
    }
}
