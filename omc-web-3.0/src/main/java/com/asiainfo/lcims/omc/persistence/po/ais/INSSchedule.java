package com.asiainfo.lcims.omc.persistence.po.ais;

import java.util.List;

import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.model.ais.WdInsSheetModel;

public class INSSchedule {

    private String id;
    private String title;
    private String timer;
    private String items;
    private String emails;
    private String phones;

    /**
     * 根据items获取groupids
     */
    private String group_ids;

    private String create_time;
    private String update_time;

    /**
     * 1:year ,2:month,3:week,4:day
     */
    private int schedule_type;

    private String schedule_name;

    private List<WdInsSheetModel> sheetlist;

    private List<AisGroupMetricModel> groupMetrics;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(String group_ids) {
        this.group_ids = group_ids;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getSchedule_type() {
        return schedule_type;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public void setSchedule_type(int schedule_type) {
        this.schedule_type = schedule_type;
    }

    public List<WdInsSheetModel> getSheetlist() {
        return sheetlist;
    }

    public void setSheetlist(List<WdInsSheetModel> sheetlist) {
        this.sheetlist = sheetlist;
    }

    public List<AisGroupMetricModel> getGroupMetrics() {
        return groupMetrics;
    }

    public void setGroupMetrics(List<AisGroupMetricModel> groupMetrics) {
        this.groupMetrics = groupMetrics;
    }
}
