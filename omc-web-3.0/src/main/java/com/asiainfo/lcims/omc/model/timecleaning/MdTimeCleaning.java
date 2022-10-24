package com.asiainfo.lcims.omc.model.timecleaning;

public class MdTimeCleaning {

    private String id;

    private String clean_table_name;

    private String clean_column_name;

    private Integer type;

    private Integer clean_interval;

    private String delete_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClean_table_name() {
        return clean_table_name;
    }

    public void setClean_table_name(String clean_table_name) {
        this.clean_table_name = clean_table_name;
    }

    public String getClean_column_name() {
        return clean_column_name;
    }

    public void setClean_column_name(String clean_column_name) {
        this.clean_column_name = clean_column_name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getClean_interval() {
        return clean_interval;
    }

    public void setClean_interval(Integer clean_interval) {
        this.clean_interval = clean_interval;
    }


    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    @Override
    public String toString() {
        return "MdTimeCleaning{" +
                "id='" + id + '\'' +
                ", clean_table_name='" + clean_table_name + '\'' +
                ", clean_column_name='" + clean_column_name + '\'' +
                ", type=" + type +
                ", clean_interval=" + clean_interval +
                '}';
    }

}
