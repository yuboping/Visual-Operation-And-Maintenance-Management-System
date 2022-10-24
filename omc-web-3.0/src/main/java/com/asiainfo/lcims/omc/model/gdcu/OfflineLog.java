package com.asiainfo.lcims.omc.model.gdcu;

import java.sql.Timestamp;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;

/**
 * 
 * @author zhujiansheng
 * @date 2019年4月10日 上午10:36:10
 * @version V1.0
 */
public class OfflineLog {

    private String nasip;

    private String opttime;

    private String optreason;

    private String opttype;

    private String ipaddress;

    private String admin;

    private String adminname;

    private String areaname;

    public String getNasip() {
        return nasip;
    }

    public void setNasip(String nasip) {
        this.nasip = nasip;
    }

    public String getOpttime() {
        return opttime;
    }

    public void setOpttime(Timestamp opttime) {
        this.opttime = TimeTools.time2String(opttime);
    }

    public String getOptreason() {
        return optreason;
    }

    public void setOptreason(String optreason) {
        this.optreason = optreason;
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public void setOpttime(String opttime) {
        this.opttime = opttime;
    }

}
