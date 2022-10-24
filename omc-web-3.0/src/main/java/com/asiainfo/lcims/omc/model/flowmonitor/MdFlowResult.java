package com.asiainfo.lcims.omc.model.flowmonitor;

import java.util.Date;

/**
 * MD_FLOW_RESULT
 * 
 * @author XHT
 *
 */
public class MdFlowResult {
    private String id;
    private String serial_num;
    private String task_id;
    private String node_id;
    private String result_flag;
    private String isend;
    private String result_descript;
    private Date create_time;
    private Date update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getResult_flag() {
        return result_flag;
    }

    public void setResult_flag(String result_flag) {
        this.result_flag = result_flag;
    }

    public String getResult_descript() {
        return result_descript;
    }

    public void setResult_descript(String result_descript) {
        this.result_descript = result_descript;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getIsend() {
        return isend;
    }

    public void setIsend(String isend) {
        this.isend = isend;
    }
}
