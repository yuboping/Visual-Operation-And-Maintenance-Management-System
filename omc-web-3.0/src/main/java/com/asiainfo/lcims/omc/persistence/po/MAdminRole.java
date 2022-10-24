package com.asiainfo.lcims.omc.persistence.po;

/**
 * 角色表
 * 
 * @author luohuawuyin
 *
 */
public class MAdminRole {
    private Integer roleid;
    private String name;
    private String menulist;
    private String description;

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenulist() {
        return menulist;
    }

    public void setMenulist(String menulist) {
        this.menulist = menulist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
