package com.asiainfo.lcims.omc.model.apn;

public class MdApnLimitRule {

    private String ruleId;

    private String apnId;

    private String apn; //apn名称

    private String limit_cycle;

    private String auth_value;

    private String log_value;

    private String record_value;

    private String day_value;

    private String update_time;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getApnId() {
        return apnId;
    }

    public void setApnId(String apnId) {
        this.apnId = apnId;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getLimit_cycle() {
        return limit_cycle;
    }

    public void setLimit_cycle(String limit_cycle) {
        this.limit_cycle = limit_cycle;
    }

    public String getAuth_value() {
        return auth_value;
    }

    public void setAuth_value(String auth_value) {
        this.auth_value = auth_value;
    }

    public String getLog_value() {
        return log_value;
    }

    public void setLog_value(String log_value) {
        this.log_value = log_value;
    }

    public String getRecord_value() {
        return record_value;
    }

    public void setRecord_value(String record_value) {
        this.record_value = record_value;
    }

    public String getDay_value() {
        return day_value;
    }

    public void setDay_value(String day_value) {
        this.day_value = day_value;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
