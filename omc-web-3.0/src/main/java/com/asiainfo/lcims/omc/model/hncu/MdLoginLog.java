package com.asiainfo.lcims.omc.model.hncu;

/**
 * MD_LOGIN_LOG表对应的java类
 * 
 * @author zhujiansheng
 * @date 2020年2月11日 下午5:34:51
 * @version V1.0
 */
public class MdLoginLog {

    private String login_id;

    private String event_occur_host;

    private String admin_account;

    private String login_ip;

    private String login_time;

    private String logout_time;

    private String online_time;

    private String login_flag;

    private String start_time;

    private String end_time;

    private String start_out_time;

    private String end_out_time;

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getEvent_occur_host() {
        return event_occur_host;
    }

    public void setEvent_occur_host(String event_occur_host) {
        this.event_occur_host = event_occur_host;
    }

    public String getAdmin_account() {
        return admin_account;
    }

    public void setAdmin_account(String admin_account) {
        this.admin_account = admin_account;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getOnline_time() {
        return online_time;
    }

    public void setOnline_time(String online_time) {
        this.online_time = online_time;
    }

    public String getLogin_flag() {
        return login_flag;
    }

    public void setLogin_flag(String login_flag) {
        this.login_flag = login_flag;
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

    public String getStart_out_time() {
        return start_out_time;
    }

    public void setStart_out_time(String start_out_time) {
        this.start_out_time = start_out_time;
    }

    public String getEnd_out_time() {
        return end_out_time;
    }

    public void setEnd_out_time(String end_out_time) {
        this.end_out_time = end_out_time;
    }

    @Override
    public String toString() {
        return "MdLoginLog [login_id=" + login_id + ", event_occur_host=" + event_occur_host
                + ", admin_account=" + admin_account + ", login_ip=" + login_ip + ", login_time="
                + login_time + ", logout_time=" + logout_time + ", online_time=" + online_time
                + ", login_flag=" + login_flag + ", start_time=" + start_time + ", end_time="
                + end_time + ", start_out_time=" + start_out_time + ", end_out_time=" + end_out_time
                + "]";
    }

}
