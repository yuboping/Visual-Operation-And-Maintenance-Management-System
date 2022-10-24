package com.asiainfo.lcims.omc.model.operatehistory;

public class OperateHis {

    private String operate_id;

    private String operate_name;

    private String operate_time;

    private String operate_type;

    private String operate_desc;

    private String start_time;

    private String end_time;

    private String role_id;

    private String current_user;

    public String getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(String operate_id) {
        this.operate_id = operate_id;
    }

    public String getOperate_name() {
        return operate_name;
    }

    public void setOperate_name(String operate_name) {
        this.operate_name = operate_name;
    }

    public String getOperate_time() {
        return operate_time;
    }

    public void setOperate_time(String operate_time) {
        this.operate_time = operate_time;
    }

    public String getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(String operate_type) {
        this.operate_type = operate_type;
    }

    public String getOperate_desc() {
        return operate_desc;
    }

    public void setOperate_desc(String operate_desc) {
        this.operate_desc = operate_desc;
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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }

    @Override
    public String toString() {
        return "OperateHis{" +
                "operate_id='" + operate_id + '\'' +
                ", operate_name='" + operate_name + '\'' +
                ", operate_time='" + operate_time + '\'' +
                ", operate_type='" + operate_type + '\'' +
                ", operate_desc='" + operate_desc + '\'' +
                '}';
    }
}
