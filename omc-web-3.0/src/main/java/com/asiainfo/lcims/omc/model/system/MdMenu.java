package com.asiainfo.lcims.omc.model.system;

import java.util.List;

public class MdMenu {
    private String id;

    private String name;

    private String show_name;

    private Integer is_menu;

    private Integer is_grant;

    private Integer is_show;

    private String url;

    private String parent_id;

    private Integer sequence;

    private Integer menu_level;

    private String dynamic;

    private String largeicon;

    private String icon;

    private Boolean active = false;

    private List<MdMenu> children;

    private String business_link = "";

    private Integer alarmcount;

    private String show_title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public Integer getIs_menu() {
        return is_menu;
    }

    public void setIs_menu(Integer is_menu) {
        this.is_menu = is_menu;
    }

    public Integer getIs_grant() {
        return is_grant;
    }

    public void setIs_grant(Integer is_grant) {
        this.is_grant = is_grant;
    }

    public Integer getIs_show() {
        return is_show;
    }

    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getMenu_level() {
        return menu_level;
    }

    public void setMenu_level(Integer menu_level) {
        this.menu_level = menu_level;
    }

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    public String getLargeicon() {
        return largeicon;
    }

    public void setLargeicon(String largeicon) {
        this.largeicon = largeicon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<MdMenu> getChildren() {
        return children;
    }

    public void setChildren(List<MdMenu> children) {
        this.children = children;
    }

    public String getBusiness_link() {
        return business_link;
    }

    public void setBusiness_link(String business_link) {
        this.business_link = business_link;
    }

    public Integer getAlarmcount() {
        return alarmcount;
    }

    public void setAlarmcount(Integer alarmcount) {
        this.alarmcount = alarmcount;
    }

    public String getShow_title() {
        return show_title;
    }

    public void setShow_title(String show_title) {
        this.show_title = show_title;
    }
}