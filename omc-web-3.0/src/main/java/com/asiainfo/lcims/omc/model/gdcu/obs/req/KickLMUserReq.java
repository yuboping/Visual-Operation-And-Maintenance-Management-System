package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 删除用户在线信息请求
 * 
 * @author luohuawuyin
 *
 */
public class KickLMUserReq extends BaseData {
    public static final String command = "KickLMUser";
    private String serialno = getRandomID();
    private String kicktype;
    private String kickname;

    public String getSerialno() {
        return serialno;
    }

    public String getCommand() {
        return command;
    }

    public String getKicktype() {
        return kicktype;
    }

    public void setKicktype(String kicktype) {
        this.kicktype = kicktype;
    }

    public String getKickname() {
        return kickname;
    }

    public void setKickname(String kickname) {
        this.kickname = kickname;
    }

}
