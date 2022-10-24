package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 在线用户统计---查询用户在线总数请求
 * 
 * @author luohuawuyin
 *
 */
public class CheckOnlineUserReq extends BaseData {
    public static final String command = "CheckOnlineUser";
    private String serialno = getRandomID();

    public String getSerialno() {
        return serialno;
    }

    public String getCommand() {
        return command;
    }

}
