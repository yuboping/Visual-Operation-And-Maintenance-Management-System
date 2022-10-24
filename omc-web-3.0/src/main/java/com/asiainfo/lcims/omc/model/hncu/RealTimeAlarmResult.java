package com.asiainfo.lcims.omc.model.hncu;

/**
 * 实时告警接口应答
 * 
 * @author zhujiansheng
 * @date 2020年12月23日 下午3:22:08
 * @version V1.0
 */
public class RealTimeAlarmResult {

    private String ret;

    private String desc;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RealTimeAlarmResult [ret=" + ret + ", desc=" + desc + "]";
    }

}
