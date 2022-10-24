package com.asiainfo.lcims.omc.model.autodeploy;

/**
 * 主机指标配置接收参数类
 * @author zhul
 *
 */
public class ParamHostMetric {
    private String ip;
    private String osType;
    private String businesslist;
    private String pwdDir;
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getOsType() {
        return osType;
    }
    public void setOsType(String osType) {
        this.osType = osType;
    }
    public String getBusinesslist() {
        return businesslist;
    }
    public void setBusinesslist(String businesslist) {
        this.businesslist = businesslist;
    }
    public String getPwdDir() {
        return pwdDir;
    }
    public void setPwdDir(String pwdDir) {
        this.pwdDir = pwdDir;
    }

    @Override
    public String toString() {
        return "ParamHostMetric{" +
                "ip='" + ip + '\'' +
                ", osType='" + osType + '\'' +
                ", businesslist='" + businesslist + '\'' +
                ", pwdDir='" + pwdDir + '\'' +
                '}';
    }
}
