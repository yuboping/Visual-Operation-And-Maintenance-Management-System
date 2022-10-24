package com.asiainfo.lcims.omc.alarm.model;

public class Host {
    private String hostid;
    private String addr;
    private String hostname;
    private String nodeid;
    private String hosttype;
    private String hosttypename;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public String getHosttype() {
        return hosttype;
    }

    public void setHosttype(String hosttype) {
        this.hosttype = hosttype;
    }

    public String getHosttypename() {
        return hosttypename;
    }

    public void setHosttypename(String hosttypename) {
        this.hosttypename = hosttypename;
    }

    @Override
    public String toString() {
        return "Host [hostid=" + hostid + ", addr=" + addr + ", hostname=" + hostname + ", nodeid="
                + nodeid + ", hosttype=" + hosttype + ", hosttypename=" + hosttypename + "]";
    }

}