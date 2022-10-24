package com.asiainfo.lcims.omc.model.flowmonitor;

import java.util.Date;

/**
 * MD_FLOW_LOG
 * 
 * @author XHT
 *
 */
public class MdFlowLog {
    private String id;
    private String serial_num;
    private String result_type;
    private String task_id;
    private String node_id;
    private String action_id;
    private String detail_id;
    private String result_flag;
    private String result_data;
    private String result_descript;
    private Date create_time;

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

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
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

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getResult_flag() {
        return result_flag;
    }

    public void setResult_flag(String result_flag) {
        this.result_flag = result_flag;
    }

    public String getResult_data() {
        return result_data;
    }

    public void setResult_data(String result_data) {
        this.result_data = result_data;
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
}
