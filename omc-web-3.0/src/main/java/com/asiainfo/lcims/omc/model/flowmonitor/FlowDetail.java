package com.asiainfo.lcims.omc.model.flowmonitor;

/**
 * 业务全流程监控执行细节
 * 
 * @author luohuawuyin
 *
 */
public class FlowDetail {
    private String task_id;
    private String node_id;
    private String node_name;
    private String isend;
    private String result_flag;
    private String result_descript;
    private String update_time;

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

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
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

    public String getIsend() {
        return isend;
    }

    public void setIsend(String isend) {
        this.isend = isend;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
