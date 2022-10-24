package com.asiainfo.lcims.omc.model.gdcu;

import java.sql.Timestamp;

import com.asiainfo.lcims.lcbmi.utils.TimeTools;

/**
 * OptRecord类
 * 
 * @author zhujiansheng
 * @date 2019年4月10日 下午2:45:47
 * @version V1.0
 */
public class OptRecord {

    private String nasip;

    private String opttime;

    private String optreason;

    private String returncode;

    private String serno;

    private String resultfile;

    public String getNasip() {
        return nasip;
    }

    public void setNasip(String nasip) {
        this.nasip = nasip;
    }

    public String getOpttime() {
        return opttime;
    }

    public void setOpttime(Timestamp opttime) {
        this.opttime = TimeTools.time2String(opttime);
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

    public String getSerno() {
        return serno;
    }

    public void setSerno(String serno) {
        this.serno = serno;
    }

    public String getResultfile() {
        return resultfile;
    }

    public void setResultfile(String resultfile) {
        this.resultfile = resultfile;
    }

    @Override
    public String toString() {
        return "OptRecord [nasip=" + nasip + ", opttime=" + opttime + ", optreason=" + optreason
                + ", returncode=" + returncode + ", serno=" + serno + ", resultfile=" + resultfile
                + "]";
    }

}
