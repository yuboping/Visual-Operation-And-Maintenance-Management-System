package com.asiainfo.lcims.omc.model.flowmonitor;

/**
 * 业务全流程监控概览
 * 
 * @author luohuawuyin
 *
 */
public class FlowOverview {
    private String task_id;
    private String task_name;
    private String create_time;
    private String result_flag;
    private String serial_num;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getResult_flag() {
        return result_flag;
    }

    public void setResult_flag(String result_flag) {
        this.result_flag = result_flag;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }

}
