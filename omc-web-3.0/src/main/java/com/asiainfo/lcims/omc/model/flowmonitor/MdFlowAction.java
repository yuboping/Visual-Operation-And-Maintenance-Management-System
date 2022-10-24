package com.asiainfo.lcims.omc.model.flowmonitor;

/**
 * MD_FLOW_NODE_ACTION
 * 
 * @author XHT
 *
 */
public class MdFlowAction {
    private String action_id;
    private String action_name;
    private String action_type;
    private String node_id;
    private Integer order_num;
    private Integer sleep_time;
    private String break_flag;// 1:多线程执行时有一个结果为true的时候就中断执行；2:多线程执行时有一个结果为false的时候就中断执行；3:等待所有线程执行完
    private String descript;
    private String create_time;
    private String update_time;

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public Integer getOrder_num() {
        return order_num;
    }

    public void setOrder_num(Integer order_num) {
        this.order_num = order_num;
    }

    public Integer getSleep_time() {
        return sleep_time;
    }

    public void setSleep_time(Integer sleep_time) {
        this.sleep_time = sleep_time;
    }

    public String getBreak_flag() {
        return break_flag;
    }

    public void setBreak_flag(String break_flag) {
        this.break_flag = break_flag;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
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
}
