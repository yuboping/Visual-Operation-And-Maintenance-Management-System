package com.asiainfo.lcims.omc.model.hncu;

/**
 * MD_SYSTEM_LOG表对应的java类
 * 
 * @author zhujiansheng
 * @date 2020年1月15日 下午2:32:52
 * @version V1.0
 */
public class MdSystemLog {

    private String system_id;

    private String event_occur_host;

    private String system_time;

    private String host_name;

    private String process_name;

    private String error_level;

    private String msg_data;

    private String start_time;

    private String end_time;

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getEvent_occur_host() {
        return event_occur_host;
    }

    public void setEvent_occur_host(String event_occur_host) {
        this.event_occur_host = event_occur_host;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getError_level() {
        return error_level;
    }

    public void setError_level(String error_level) {
        this.error_level = error_level;
    }

    public String getMsg_data() {
        return msg_data;
    }

    public void setMsg_data(String msg_data) {
        this.msg_data = msg_data;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "MdSystemLog [system_id=" + system_id + ", event_occur_host=" + event_occur_host
                + ", system_time=" + system_time + ", host_name=" + host_name + ", process_name="
                + process_name + ", error_level=" + error_level + ", msg_data=" + msg_data
                + ", start_time=" + start_time + ", end_time=" + end_time + "]";
    }

}
