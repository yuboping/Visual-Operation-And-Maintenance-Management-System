package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 在线用户统计---根据basip查询用户在线数请求
 * 
 * @author luohuawuyin
 *
 */
public class CheckBasUserReq extends BaseData {
    public static final String command = "CheckBasUser";
    private String serialno = getRandomID();

    public String getSerialno() {
        return serialno;
    }

    public String getCommand() {
        return command;
    }

}
