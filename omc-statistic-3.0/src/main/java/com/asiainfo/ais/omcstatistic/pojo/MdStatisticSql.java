package com.asiainfo.ais.omcstatistic.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "MD_STATISTIC_SQL")
public class MdStatisticSql {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "STATISTIC_SQL")
    private String statisticSql;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return STATISTIC_SQL
     */
    public String getStatisticSql() {
        return statisticSql;
    }

    /**
     * @param statisticSql
     */
    public void setStatisticSql(String statisticSql) {
        this.statisticSql = statisticSql;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MdStatisticSql{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}