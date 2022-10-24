package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 踢用户下线请求
 * 
 * @author luohuawuyin
 *
 */
public class KickBRASUserReq extends BaseData {
    public static final String command = "KickBRASUser";
    private String serialno = getRandomID();
    private String dmuserip;
    private String dmsession;
    private String dmbras;
    private String dmuser;
    public String getSerialno() {
        return serialno;
    }

    public String getDmuserip() {
        return dmuserip;
    }

    public void setDmuserip(String dmuserip) {
        this.dmuserip = dmuserip;
    }

    public String getDmsession() {
        return dmsession;
    }

    public void setDmsession(String dmsession) {
        this.dmsession = dmsession;
    }

    public String getDmbras() {
        return dmbras;
    }

    public void setDmbras(String dmbras) {
        this.dmbras = dmbras;
    }

    public String getDmuser() {
        return dmuser;
    }

    public void setDmuser(String dmuser) {
        this.dmuser = dmuser;
    }

    public String getCommand() {
        return command;
    }
}
