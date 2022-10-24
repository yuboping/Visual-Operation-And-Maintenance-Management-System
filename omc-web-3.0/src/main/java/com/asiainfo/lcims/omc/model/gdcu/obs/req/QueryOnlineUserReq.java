package com.asiainfo.lcims.omc.model.gdcu.obs.req;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 在线用户查询请求
 *
 */
public class QueryOnlineUserReq extends BaseData {
    public static final String command = "QueryOnlineUser";
    private String serialno = getRandomID();
    private String querytype;
    private String queryvalue;

    public String getSerialno() {
        return serialno;
    }

    public String getQuerytype() {
        return querytype;
    }

    public void setQuerytype(String querytype) {
        this.querytype = querytype;
    }

    public String getQueryvalue() {
        return queryvalue;
    }

    public void setQueryvalue(String queryvalue) {
        this.queryvalue = queryvalue;
    }

    public String getCommand() {
        return command;
    }

}
