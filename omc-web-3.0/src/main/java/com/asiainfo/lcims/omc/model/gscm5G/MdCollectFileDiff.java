package com.asiainfo.lcims.omc.model.gscm5G;

public class MdCollectFileDiff {
    private String id;

    private String host_id;

    private String host_ip;

    private String collect_time;

    private String firewall_log_name;

    private String thirdparty_log_name;

    private Integer firewall_log_num;

    private Integer thirdparty_log_num;

    private String diff_log_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getHost_ip() {
        return host_ip;
    }

    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }

    public String getFirewall_log_name() {
        return firewall_log_name;
    }

    public void setFirewall_log_name(String firewall_log_name) {
        this.firewall_log_name = firewall_log_name;
    }

    public String getThirdparty_log_name() {
        return thirdparty_log_name;
    }

    public void setThirdparty_log_name(String thirdparty_log_name) {
        this.thirdparty_log_name = thirdparty_log_name;
    }

    public Integer getFirewall_log_num() {
        return firewall_log_num;
    }

    public void setFirewall_log_num(Integer firewall_log_num) {
        this.firewall_log_num = firewall_log_num;
    }

    public Integer getThirdparty_log_num() {
        return thirdparty_log_num;
    }

    public void setThirdparty_log_num(Integer thirdparty_log_num) {
        this.thirdparty_log_num = thirdparty_log_num;
    }

    public String getDiff_log_name() {
        return diff_log_name;
    }

    public void setDiff_log_name(String diff_log_name) {
        this.diff_log_name = diff_log_name;
    }
}