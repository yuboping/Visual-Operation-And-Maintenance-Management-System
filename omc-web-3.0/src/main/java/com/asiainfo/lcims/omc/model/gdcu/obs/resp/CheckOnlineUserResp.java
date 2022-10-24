package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 在线用户统计---查询用户在线总数响应
 * 
 * @author luohuawuyin
 *
 */
public class CheckOnlineUserResp extends BaseData {
    private String serialno;
    private String returncode;
    private String errordescription;
    private String num;

    public CheckOnlineUserResp() {

    }
    public CheckOnlineUserResp(String serialno, String returncode, String errordescription) {
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
    	if (log.isDebugEnabled()) {
            log.info("num=" + num);
        }
    	try {
            num = num.substring(num.indexOf("=")+1);
            num = num.trim();
        } catch (Exception e) {
            log.error("check online user resp setNum error", e);
        }
        this.num = num;
    }

}
