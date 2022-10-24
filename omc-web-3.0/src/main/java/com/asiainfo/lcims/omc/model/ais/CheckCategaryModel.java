package com.asiainfo.lcims.omc.model.ais;

public class CheckCategaryModel {
    private String groupid;
    private String categaryname;
    private String categarydesc;
    private String iconclass;// 大图标样式
    private boolean isdisable = false;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getCategaryname() {
        return categaryname;
    }

    public void setCategaryname(String categaryname) {
        this.categaryname = categaryname;
    }

    public String getCategarydesc() {
        return categarydesc;
    }

    public void setCategarydesc(String categarydesc) {
        this.categarydesc = categarydesc;
    }

    public String getIconclass() {
        return iconclass;
    }

    public void setIconclass(String iconclass) {
        this.iconclass = iconclass;
    }

    public boolean isIsdisable() {
        return isdisable;
    }

    public void setIsdisable(boolean isdisable) {
        this.isdisable = isdisable;
    }

}
