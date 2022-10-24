package com.asiainfo.lcims.omc.model.autodeploy;

/**
 * 业务主机接收参数类
 * @author zhul
 *
 */
public class ParamBusinessHost {
    private String ip;
    private String businesslist;
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getBusinesslist() {
        return businesslist;
    }
    public void setBusinesslist(String businesslist) {
        this.businesslist = businesslist;
    }

    @Override
    public String toString() {
        return "ParamBusinessHost{" +
                "ip='" + ip + '\'' +
                ", businesslist='" + businesslist + '\'' +
                '}';
    }
}
