package com.asiainfo.lcims.omc.model.shcm;

public class AuthenSituationVo {

    /** 统计时间 */
    private String cycleTime;

    /** 认证成功数 */
    private String authen_success;

    /** 失败认证数 */
    private String authen_fail;

    /** 总认证数 */
    private String authen_total;

    public AuthenSituationVo() {
    }

    public AuthenSituationVo(String cycleTime) {
        this.cycleTime = cycleTime;
        this.authen_success = "0";
        this.authen_fail = "0";
        this.authen_total = "0";
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public AuthenSituationVo setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public String getAuthen_total() {
        return authen_total;
    }

    public AuthenSituationVo setAuthen_total(String authen_total) {
        this.authen_total = authen_total;
        return this;
    }

    public String getAuthen_success() {
        return authen_success;
    }

    public AuthenSituationVo setAuthen_success(String authen_success) {
        this.authen_success = authen_success;
        return this;
    }

    public String getAuthen_fail() {
        return authen_fail;
    }

    public AuthenSituationVo setAuthen_fail(String authen_fail) {
        this.authen_fail = authen_fail;
        return this;
    }

}
