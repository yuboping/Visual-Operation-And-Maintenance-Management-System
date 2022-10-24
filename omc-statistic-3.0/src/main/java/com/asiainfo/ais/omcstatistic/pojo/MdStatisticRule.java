package com.asiainfo.ais.omcstatistic.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "MD_STATISTIC_RULE")
public class MdStatisticRule {
    @Id
    @Column(name = "RULE_ID")
    private String ruleId;

    @Column(name = "RULE_NAME")
    private String ruleName;

    @Column(name = "RULE_INTERVAL")
    private String ruleInterval;

    @Column(name = "RET_TABLE")
    private String retTable;

    @Column(name = "EXPRESSION")
    private String expression;

    @Column(name = "RULE_TYPE")
    private String ruleType;

    @Column(name = "METRIC_ID")
    private String metricId;

    @Column(name = "ATTR1")
    private String attr1;

    @Column(name = "ATTR2")
    private String attr2;

    @Column(name = "ATTR3")
    private String attr3;

    @Column(name = "ATTR4")
    private String attr4;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OFFSET")
    private String offset;

    @Transient
    private String stime;

    @Transient
    private String cycleTime;

    /**
     * @return RULE_ID
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return RULE_NAME
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param ruleName
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * @return RULE_INTERVAL
     */
    public String getRuleInterval() {
        return ruleInterval;
    }

    /**
     * @param ruleInterval
     */
    public void setRuleInterval(String ruleInterval) {
        this.ruleInterval = ruleInterval;
    }

    /**
     * @return RET_TABLE
     */
    public String getRetTable() {
        return retTable;
    }

    /**
     * @param retTable
     */
    public void setRetTable(String retTable) {
        this.retTable = retTable;
    }

    /**
     * @return EXPRESSION
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @param expression
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return RULE_TYPE
     */
    public String getRuleType() {
        return ruleType;
    }

    /**
     * @param ruleType
     */
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * @return METRIC_ID
     */
    public String getMetricId() {
        return metricId;
    }

    /**
     * @param metricId
     */
    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    /**
     * @return ATTR1
     */
    public String getAttr1() {
        return attr1;
    }

    /**
     * @param attr1
     */
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    /**
     * @return ATTR2
     */
    public String getAttr2() {
        return attr2;
    }

    /**
     * @param attr2
     */
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    /**
     * @return ATTR3
     */
    public String getAttr3() {
        return attr3;
    }

    /**
     * @param attr3
     */
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    /**
     * @return ATTR4
     */
    public String getAttr4() {
        return attr4;
    }

    /**
     * @param attr4
     */
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
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

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "MdStatisticRule{" +
                "ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", ruleInterval='" + ruleInterval + '\'' +
                ", retTable='" + retTable + '\'' +
                ", expression='" + expression + '\'' +
                ", ruleType='" + ruleType + '\'' +
                ", metricId='" + metricId + '\'' +
                ", attr1='" + attr1 + '\'' +
                ", attr2='" + attr2 + '\'' +
                ", attr3='" + attr3 + '\'' +
                ", attr4='" + attr4 + '\'' +
                ", createTime=" + createTime +
                ", stime='" + stime + '\'' +
                '}';
    }
}