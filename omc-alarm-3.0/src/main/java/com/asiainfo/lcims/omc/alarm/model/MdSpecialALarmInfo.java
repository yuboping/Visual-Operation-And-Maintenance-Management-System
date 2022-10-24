package com.asiainfo.lcims.omc.alarm.model;

import java.sql.Timestamp;

/**
 * 
 * @author zhul
 * MD_OTHER_ALARM_INFO
 */
public class MdSpecialALarmInfo {
    private String rule_id;
    private String alarm_id;
    private String metric_id;
    /** 告警序列号 */
    private Integer alarm_seq;
    private Integer alarm_seq_new;
    private String alarm_rule;
    private Integer alarm_num;
    private String alarm_msg;
    private String msg_desc;
    private String alarm_level;
    /** 告警值 */
    private String alarm_val;
    /** 告警生成的文件名 */
    private String alarm_filename;
    /** 告警生成的文件路径 */
    private String file_path;
    private Timestamp alarm_time;
    private Timestamp clear_time;
    private String attr;
    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private boolean exist;
    public String getRule_id() {
        return rule_id;
    }
    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }
    public String getAlarm_id() {
        return alarm_id;
    }
    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
    public String getMetric_id() {
        return metric_id;
    }
    public void setMetric_id(String metric_id) {
        this.metric_id = metric_id;
    }
    public Integer getAlarm_seq() {
        return alarm_seq;
    }
    public void setAlarm_seq(Integer alarm_seq) {
        this.alarm_seq = alarm_seq;
    }
    public String getAlarm_rule() {
        return alarm_rule;
    }
    public void setAlarm_rule(String alarm_rule) {
        this.alarm_rule = alarm_rule;
    }
    public Integer getAlarm_num() {
        return alarm_num;
    }
    public void setAlarm_num(Integer alarm_num) {
        this.alarm_num = alarm_num;
    }
    public String getAlarm_msg() {
        return alarm_msg;
    }
    public void setAlarm_msg(String alarm_msg) {
        this.alarm_msg = alarm_msg;
    }
    public String getAlarm_val() {
        return alarm_val;
    }
    public void setAlarm_val(String alarm_val) {
        this.alarm_val = alarm_val;
    }
    public String getAlarm_filename() {
        return alarm_filename;
    }
    public void setAlarm_filename(String alarm_filename) {
        this.alarm_filename = alarm_filename;
    }
    public String getFile_path() {
        return file_path;
    }
    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
    public String getAttr1() {
        return attr1;
    }
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }
    public String getAttr2() {
        return attr2;
    }
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }
    public String getAttr3() {
        return attr3;
    }
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }
    public String getAttr4() {
        return attr4;
    }
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }
    public String getAlarm_level() {
        return alarm_level;
    }
    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }
    public boolean isExist() {
        return exist;
    }
    public void setExist(boolean exist) {
        this.exist = exist;
    }
    public Timestamp getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(Timestamp alarm_time) {
        this.alarm_time = alarm_time;
    }
    public Timestamp getClear_time() {
        return clear_time;
    }
    public void setClear_time(Timestamp clear_time) {
        this.clear_time = clear_time;
    }
    public Integer getAlarm_seq_new() {
        return alarm_seq_new;
    }
    public void setAlarm_seq_new(Integer alarm_seq_new) {
        this.alarm_seq_new = alarm_seq_new;
    }
    public String getMsg_desc() {
        return msg_desc;
    }
    public void setMsg_desc(String msg_desc) {
        this.msg_desc = msg_desc;
    }
    public String getAttr() {
        return attr;
    }
    public void setAttr(String attr) {
        this.attr = attr;
    }
}
