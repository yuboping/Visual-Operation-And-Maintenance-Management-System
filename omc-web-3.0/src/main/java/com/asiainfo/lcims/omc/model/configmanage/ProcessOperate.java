package com.asiainfo.lcims.omc.model.configmanage;

public class ProcessOperate {

    private String id;
    private String operate_id;
    private String host_ip;
    private String process_name;
    private String process_script;
    private String operate_desc;
    private Integer operate_state;
    private String operate_result;
    private String create_time;
    private String update_time;
    private Integer script_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(String operate_id) {
        this.operate_id = operate_id;
    }

    public String getHost_ip() {
        return host_ip;
    }

    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getProcess_script() {
        return process_script;
    }

    public void setProcess_script(String process_script) {
        this.process_script = process_script;
    }

    public String getOperate_desc() {
        return operate_desc;
    }

    public void setOperate_desc(String operate_desc) {
        this.operate_desc = operate_desc;
    }

    public Integer getOperate_state() {
        return operate_state;
    }

    public void setOperate_state(Integer operate_state) {
        this.operate_state = operate_state;
    }

    public String getOperate_result() {
        return operate_result;
    }

    public void setOperate_result(String operate_result) {
        this.operate_result = operate_result;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getScript_type() {
        return script_type;
    }

    public void setScript_type(Integer script_type) {
        this.script_type = script_type;
    }

    @Override
    public String toString() {
        return "ProcessOperate{" +
                "id='" + id + '\'' +
                ", operate_id='" + operate_id + '\'' +
                ", host_ip='" + host_ip + '\'' +
                ", process_name='" + process_name + '\'' +
                ", process_script='" + process_script + '\'' +
                ", operate_desc='" + operate_desc + '\'' +
                ", operate_state=" + operate_state +
                ", operate_result='" + operate_result + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
