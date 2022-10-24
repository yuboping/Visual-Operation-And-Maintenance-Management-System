package com.asiainfo.lcims.omc.model.gdcu;

import java.sql.Timestamp;

/**
 * 批处理记录表：BAT_OPT_REC，记录批量在LM踢用户下线和批量踢用户下线记录
 * 
 * @author zhujiansheng
 * @date 2019年4月10日 下午3:58:07
 * @version V1.0
 */
public class BatOptRec {

    private String serno;

    private String opttype;

    private String nasip;

    private String admin;

    private String ipaddress;

    private Timestamp opttime;

    private String optreason;

    private String returncode;

    private String resultfile;

    public String getSerno() {
        return serno;
    }

    public void setSerno(String serno) {
        this.serno = serno;
    }

    public String getNasip() {
        return nasip;
    }

    public void setNasip(String nasip) {
        this.nasip = nasip;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getOptreason() {
        return optreason;
    }

    public void setOptreason(String optreason) {
        this.optreason = optreason;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
    }

    public Timestamp getOpttime() {
        return this.opttime;
    }

    public void setOpttime(Timestamp timestamp) {
        this.opttime = timestamp;
    }

    public String getResultfile() {
        return resultfile;
    }

    public void setResultfile(String resultfile) {
        this.resultfile = resultfile;
    }

}
