package com.asiainfo.ais.omcstatistic.pojo;

import javax.persistence.*;

@Table(name = "MD_COLL_CYCLE")
public class MdCollCycle {
    @Id
    @Column(name = "CYCLEID")
    private Integer cycleid;

    @Column(name = "CYCLENAME")
    private String cyclename;

    @Column(name = "CYCLE")
    private Integer cycle;

    /**
     * 定时时间
     */
    @Column(name = "CRON")
    private String cron;

    /**
     * 例如采集周期是按月，用于指定是每月几日采集
     */
    @Column(name = "RUNDAY")
    private Integer runday;

    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * @return CYCLEID
     */
    public Integer getCycleid() {
        return cycleid;
    }

    /**
     * @param cycleid
     */
    public void setCycleid(Integer cycleid) {
        this.cycleid = cycleid;
    }

    /**
     * @return CYCLENAME
     */
    public String getCyclename() {
        return cyclename;
    }

    /**
     * @param cyclename
     */
    public void setCyclename(String cyclename) {
        this.cyclename = cyclename;
    }

    /**
     * @return CYCLE
     */
    public Integer getCycle() {
        return cycle;
    }

    /**
     * @param cycle
     */
    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    /**
     * 获取定时时间
     *
     * @return CRON - 定时时间
     */
    public String getCron() {
        return cron;
    }

    /**
     * 设置定时时间
     *
     * @param cron 定时时间
     */
    public void setCron(String cron) {
        this.cron = cron;
    }

    /**
     * 获取例如采集周期是按月，用于指定是每月几日采集
     *
     * @return RUNDAY - 例如采集周期是按月，用于指定是每月几日采集
     */
    public Integer getRunday() {
        return runday;
    }

    /**
     * 设置例如采集周期是按月，用于指定是每月几日采集
     *
     * @param runday 例如采集周期是按月，用于指定是每月几日采集
     */
    public void setRunday(Integer runday) {
        this.runday = runday;
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}