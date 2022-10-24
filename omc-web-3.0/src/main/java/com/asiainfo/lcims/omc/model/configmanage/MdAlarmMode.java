package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 表MD_ALARM_MODE对应的JAVA类
 * 
 * @author zhujiansheng
 * @date 2018年8月30日 下午3:52:50
 * @version V1.0
 */
public class MdAlarmMode {

    private String modeid;

    private String modename;

    private String modeattr;

    private String modetype;

    // 用于页面展示
    private String description;

    public String getModeid() {
        return modeid;
    }

    public void setModeid(String modeid) {
        this.modeid = modeid;
    }

    public String getModename() {
        return modename;
    }

    public void setModename(String modename) {
        this.modename = modename;
    }

    public String getModeattr() {
        return modeattr;
    }

    public void setModeattr(String modeattr) {
        this.modeattr = modeattr;
    }

    public String getModetype() {
        return modetype;
    }

    public void setModetype(String modetype) {
        this.modetype = modetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MdAlarmMode [modeid=" + modeid + ", modename=" + modename + ", modeattr=" + modeattr
                + ", modetype=" + modetype + ", description=" + description + "]";
    }

}
