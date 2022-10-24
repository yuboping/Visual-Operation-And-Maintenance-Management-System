package com.asiainfo.lcims.omc.model.configmanage;

import java.util.Date;

public class MdProcess {
    private String process_id;

    private String process_name;

    private String process_key;

    private Date create_time;

    private Date update_time;

    private String description;

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String process_id) {
        this.process_id = process_id;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getProcess_key() {
        return process_key;
    }

    public void setProcess_key(String process_key) {
        this.process_key = process_key;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}