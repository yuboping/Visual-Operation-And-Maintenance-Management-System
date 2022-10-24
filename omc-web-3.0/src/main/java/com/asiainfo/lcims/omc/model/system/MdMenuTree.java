package com.asiainfo.lcims.omc.model.system;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MdMenuTree {

    private static final Logger LOG = LoggerFactory.getLogger(MdMenuTree.class);


    private String id;

    private String name;

    private String show_name;

    private Integer is_menu;

    private Integer is_grant;

    private Integer is_show;

    private String url;

    private String parent_id;

    private Integer sequence;

    private Integer level;

    private String dynamic;

    private String largeicon;

    private String icon;

    private Boolean active = false;

    private String parent;

    private String text;

    private String checked;

    private Map<String, Boolean> state = new HashMap<String, Boolean>();

    private List<MdMenuTree> children;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
//        if(StringUtils.isEmpty(parent) || "null".equals(parent)){
//            parent = "#";
//            state.put("undetermined",true);
//        }
        this.parent = parent;
    }

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public List<MdMenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<MdMenuTree> children) {
        this.children = children;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
        if("1".equals(checked)){
            state.put("selected",true);
        }
    }

    public Map<String, Boolean> getState() {
        return state;
    }

    public void setStateMap(Map<String, Boolean> stateMap){
        this.state = stateMap;
    }

    @Override
    public String toString() {
        return "MdMenuTree{" +
                "id='" + id + '\'' +
                ", parent='" + parent + '\'' +
                ", text='" + text + '\'' +
                ", state=" + state +
                '}';
    }
}
