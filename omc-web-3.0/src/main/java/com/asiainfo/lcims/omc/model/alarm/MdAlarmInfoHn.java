package com.asiainfo.lcims.omc.model.alarm;

/**
 * 告警信息表MD_REAL_ALARM_INFO实体类
 */
public class MdAlarmInfoHn {
    private String uuid;    //告警流水号
    private String alarm_msg;   //告警信息
    private String alarm_type;  //告警类别
    private String create_time; //产生告警时间
    private Integer host_name;  //产生告警的网元名称
    private String alarm_id;    //告警的唯一标识
    private String alarm_level; //告警级别
    private String clear_status;    //告警清除状态
    private String clear_time;  //告警清除时间
    private String up_date; //获取本次上报距离首次上报天数时间
    private String UPDATE_TIME; //创建时间

    //查询条件 起始时间
    private String start_time;
    private String end_time;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAlarm_msg() {
        return alarm_msg;
    }

    public void setAlarm_msg(String alarm_msg) {
        this.alarm_msg = alarm_msg;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getHost_name() {
        return host_name;
    }

    public void setHost_name(Integer host_name) {
        this.host_name = host_name;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getAlarm_level() {
        return alarm_level;
    }

    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }

    public String getClear_status() {
        return clear_status;
    }

    public void setClear_status(String clear_status) {
        this.clear_status = clear_status;
    }

    public String getClear_time() {
        return clear_time;
    }

    public void setClear_time(String clear_time) {
        this.clear_time = clear_time;
    }

    public String getUp_date() {
        return up_date;
    }

    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
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
}
