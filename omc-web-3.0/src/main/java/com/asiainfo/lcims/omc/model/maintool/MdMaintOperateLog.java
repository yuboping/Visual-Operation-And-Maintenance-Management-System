package com.asiainfo.lcims.omc.model.maintool;

import java.sql.Timestamp;

public class MdMaintOperateLog {
    
    private String id;
    private String uuid;
    private String host_ip; 
    private int operate_state;
    private String operate_state_name;
    private int operate_type;
    private String operate_user;
    private String createTime;
    private Timestamp create_time;
    public String getId() {
        return id;
    }
    public MdMaintOperateLog setId(String id) {
        this.id = id;
        return this;
    }
    public String getUuid() {
        return uuid;
    }
    public MdMaintOperateLog setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
    public String getHost_ip() {
        return host_ip;
    }
    public MdMaintOperateLog setHost_ip(String host_ip) {
        this.host_ip = host_ip;
        return this;
    }
    public int getOperate_state() {
        return operate_state;
    }
    public MdMaintOperateLog setOperate_state(int operate_state) {
        this.operate_state = operate_state;
        return this;
    }
    public String getCreateTime() {
        return createTime;
    }
    public MdMaintOperateLog setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }
    public Timestamp getCreate_time() {
        return create_time;
    }
    public MdMaintOperateLog setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
        return this;
    }
    public String getOperate_user() {
        return operate_user;
    }
    public MdMaintOperateLog setOperate_user(String operate_user) {
        this.operate_user = operate_user;
        return this;
    }
    public int getOperate_type() {
        return operate_type;
    }
    public MdMaintOperateLog setOperate_type(int operate_type) {
        this.operate_type = operate_type;
        return this;
    }
    public String getOperate_state_name() {
        return operate_state_name;
    }
    public void setOperate_state_name(String operate_state_name) {
        this.operate_state_name = operate_state_name;
    }
}
