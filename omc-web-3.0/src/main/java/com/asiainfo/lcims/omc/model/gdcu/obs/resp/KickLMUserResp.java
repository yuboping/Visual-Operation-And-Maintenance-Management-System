package com.asiainfo.lcims.omc.model.gdcu.obs.resp;

import com.asiainfo.lcims.omc.model.gdcu.BaseData;

/**
 * 删除用户在线信息
 * 
 * @author luohuawuyin
 *
 */
public class KickLMUserResp extends BaseData {
    private String serialno;
    private String returncode;
    private String errordescription;

    public KickLMUserResp() {

    }
    public KickLMUserResp(String serialno, String returncode, String errordescription) {
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

}
