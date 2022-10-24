package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 用户信息（属地）查询接口
 *
 */
public class QueryUserNodeReq extends BaseData {
    public static final String command = "QueryUserNode";
    private String serialno = getRandomID();
    private String account;
    private String opernodeid;
    
    public String getSerialno() {
        return serialno;
    }

    public String getCommand() {
        return command;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOpernodeid() {
        return opernodeid;
    }

    public void setOpernodeid(String opernodeid) {
        this.opernodeid = opernodeid;
    }

}
