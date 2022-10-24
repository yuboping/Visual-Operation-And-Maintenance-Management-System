package com.asiainfo.lcims.omc.model.system;

import java.util.List;

import com.asiainfo.lcims.omc.model.KeyValueModel;

public class MAdmin {

    private String admin;
    private String password;
    private int passwordtype;
    private String nodelist;
    private String areanolist;
    private String modulelist;
    private String areano;
    private int status;
    private String roleid;
    private int groupno;
    private int powerlevel;
    private String name;
    private String contactphone;
    private String rolename;
    private String nodenamelist;
    private String areanamelist;
    private String modulenamelist;
    private String corpname;
    private String coaddr;
    private String email;
    private String description;
    private List<KeyValueModel> rolelistforcheck;
    private String oldPwd;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPasswordtype() {
        return passwordtype;
    }

    public void setPasswordtype(int passwordtype) {
        this.passwordtype = passwordtype;
    }

    public String getNodelist() {
        return nodelist;
    }

    public void setNodelist(String nodelist) {
        this.nodelist = nodelist;
    }

    public String getAreanolist() {
        return areanolist;
    }

    public void setAreanolist(String areanolist) {
        this.areanolist = areanolist;
    }

    public String getModulelist() {
        return modulelist;
    }

    public void setModulelist(String modulelist) {
        this.modulelist = modulelist;
    }

    public String getAreano() {
        return areano;
    }

    public void setAreano(String areano) {
        this.areano = areano;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public int getGroupno() {
        return groupno;
    }

    public void setGroupno(int groupno) {
        this.groupno = groupno;
    }

    public int getPowerlevel() {
        return powerlevel;
    }

    public void setPowerlevel(int powerlevel) {
        this.powerlevel = powerlevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getNodenamelist() {
        return nodenamelist;
    }

    public void setNodenamelist(String nodenamelist) {
        this.nodenamelist = nodenamelist;
    }

    public String getAreanamelist() {
        return areanamelist;
    }

    public void setAreanamelist(String areanamelist) {
        this.areanamelist = areanamelist;
    }

    public String getModulenamelist() {
        return modulenamelist;
    }

    public void setModulenamelist(String modulenamelist) {
        this.modulenamelist = modulenamelist;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getCoaddr() {
        return coaddr;
    }

    public void setCoaddr(String coaddr) {
        this.coaddr = coaddr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<KeyValueModel> getRolelistforcheck() {
        return rolelistforcheck;
    }

    public void setRolelistforcheck(List<KeyValueModel> rolelistforcheck) {
        this.rolelistforcheck = rolelistforcheck;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }
}
