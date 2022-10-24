package com.asiainfo.lcims.omc.model.system;

/**
 * 角色权限表
 */
public class MdRolePermissions {

    private String roleid;
    private String type;
    private String permissionid;
    private String menu_type;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(String permissinonid) {
        this.permissionid = permissinonid;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    @Override
    public String toString() {
        return "MdRolePermissions{" +
                "roleid='" + roleid + '\'' +
                ", type='" + type + '\'' +
                ", permissionid='" + permissionid + '\'' +
                '}';
    }
}
