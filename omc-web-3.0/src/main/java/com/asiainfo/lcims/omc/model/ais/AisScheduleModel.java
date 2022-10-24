package com.asiainfo.lcims.omc.model.ais;

public class AisScheduleModel {
    private String id;
    private String title;
    private String timer;
    private String group_ids;
    private String emails;
    private String phones;
    private String schedule_type;
    private String create_time;
    private String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(String group_ids) {
        this.group_ids = group_ids;
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

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    @Override
    public String toString() {
        return "AisScheduleModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", timer='" + timer + '\'' +
                ", group_ids='" + group_ids + '\'' +
                ", emails='" + emails + '\'' +
                ", phones='" + phones + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
