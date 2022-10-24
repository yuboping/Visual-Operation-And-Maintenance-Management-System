package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 主机进程关联关系实体类
 */
public class MdHostProcess {

    private String id;
    private String host_id;
    private String process_id;
    private String process_key;
    private String start_script;
    /** 脚本类型 1.启动 2.停止  */
    private String stop_script;
    private String create_time;
    private String update_time;
    private String description;
    private String type_desc;
    private String host_ip;
    private String hostname;
    private String addr;
    private String returntypename;
    private String flag;
    private String process_name;
    private String operate_state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String process_id) {
        this.process_id = process_id;
    }

    public String getProcess_key() {
        return process_key;
    }

    public void setProcess_key(String process_key) {
        this.process_key = process_key;
    }

    public String getStart_script() {
        return start_script;
    }

    public void setStart_script(String start_script) {
        this.start_script = start_script;
    }

    public String getStop_script() {
        return stop_script;
    }

    public void setStop_script(String stop_script) {
        this.stop_script = stop_script;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getHost_ip() {
        return host_ip;
    }

    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getReturntypename() {
        return returntypename;
    }

    public void setReturntypename(String returntypename) {
        this.returntypename = returntypename;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getOperate_state() {
        return operate_state;
    }

    public void setOperate_state(String operate_state) {
        this.operate_state = operate_state;
    }

    @Override
    public String toString() {
        return "MdHostProcess{" +
                "id='" + id + '\'' +
                ", host_id='" + host_id + '\'' +
                ", process_id='" + process_id + '\'' +
                ", process_key='" + process_key + '\'' +
                ", start_script='" + start_script + '\'' +
                ", stop_script='" + stop_script + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", description='" + description + '\'' +
                ", type_desc='" + type_desc + '\'' +
                ", host_ip='" + host_ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", addr='" + addr + '\'' +
                ", returntypename='" + returntypename + '\'' +
                ", flag='" + flag + '\'' +
                ", process_name='" + process_name + '\'' +
                ", operate_state='" + operate_state + '\'' +
                '}';
    }
}
