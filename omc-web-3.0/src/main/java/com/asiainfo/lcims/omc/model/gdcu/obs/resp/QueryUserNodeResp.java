package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 用户信息（属地）查询接口
 *
 */
public class QueryUserNodeResp extends BaseData {
    private String serialno;
    private String returncode;
    private String errordescription;
    private String node;

    public QueryUserNodeResp() {

    }
    public QueryUserNodeResp(String serialno, String returncode, String errordescription) {
        this.serialno = serialno;
        this.returncode = returncode;
        this.errordescription = errordescription;
    }
    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getErrordescription() {
        return errordescription;
    }

    public void setErrordescription(String errordescription) {
        this.errordescription = errordescription;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

}
