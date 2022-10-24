package com.asiainfo.lcims.omc.model.shcm;

public class AuthFailResonVo {
    
    /** 统计时间 */
    private String cycleTime;
    /** 宽带认证请求数 */
    private String adsl_request;
    /** 宽带认证成功数 */
    private String adsl_success;
    /** 未注册帐号 */
    private String adsl_failcode1;
    /** 密码错误 */
    private String adsl_failcode2;
    /** 用户加锁 */
    private String adsl_failcode3;
    /** 限时上网 */
    private String adsl_failcode4;
    /** vlan错误 */
    private String adsl_failcode5;
    /** 唯一性拒绝 */
    private String adsl_failcode6;
    
    public AuthFailResonVo() {
        
    }
    
    public AuthFailResonVo(String cycleTime) {
        this.cycleTime = cycleTime;
        this.adsl_request = "0";
        this.adsl_success = "0";
        this.adsl_failcode1 = "0";
        this.adsl_failcode2 = "0";
        this.adsl_failcode3 = "0";
        this.adsl_failcode4 = "0";
        this.adsl_failcode5 = "0";
        this.adsl_failcode6 = "0";
    }
    
    public String getCycleTime() {
        return cycleTime;
    }
    public AuthFailResonVo setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }
    public String getAdsl_request() {
        return adsl_request;
    }
    public AuthFailResonVo setAdsl_request(String adsl_request) {
        this.adsl_request = adsl_request;
        return this;
    }
    public String getAdsl_success() {
        return adsl_success;
    }
    public AuthFailResonVo setAdsl_success(String adsl_success) {
        this.adsl_success = adsl_success;
        return this;
    }
    public String getAdsl_failcode1() {
        return adsl_failcode1;
    }
    public AuthFailResonVo setAdsl_failcode1(String adsl_failcode1) {
        this.adsl_failcode1 = adsl_failcode1;
        return this;
    }
    public String getAdsl_failcode2() {
        return adsl_failcode2;
    }
    public AuthFailResonVo setAdsl_failcode2(String adsl_failcode2) {
        this.adsl_failcode2 = adsl_failcode2;
        return this;
    }
    public String getAdsl_failcode3() {
        return adsl_failcode3;
    }
    public AuthFailResonVo setAdsl_failcode3(String adsl_failcode3) {
        this.adsl_failcode3 = adsl_failcode3;
        return this;
    }
    public String getAdsl_failcode4() {
        return adsl_failcode4;
    }
    public AuthFailResonVo setAdsl_failcode4(String adsl_failcode4) {
        this.adsl_failcode4 = adsl_failcode4;
        return this;
    }
    public String getAdsl_failcode5() {
        return adsl_failcode5;
    }
    public AuthFailResonVo setAdsl_failcode5(String adsl_failcode5) {
        this.adsl_failcode5 = adsl_failcode5;
        return this;
    }
    public String getAdsl_failcode6() {
        return adsl_failcode6;
    }
    public AuthFailResonVo setAdsl_failcode6(String adsl_failcode6) {
        this.adsl_failcode6 = adsl_failcode6;
        return this;
    }
}
