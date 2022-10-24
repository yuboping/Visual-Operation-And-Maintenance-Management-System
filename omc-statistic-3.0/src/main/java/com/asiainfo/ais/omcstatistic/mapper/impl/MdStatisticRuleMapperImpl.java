package com.asiainfo.ais.omcstatistic.mapper.impl;

import com.asiainfo.ais.omcstatistic.common.Constant;
import com.asiainfo.ais.omcstatistic.common.DateUtil;
import com.asiainfo.ais.omcstatistic.pojo.StatisData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MdStatisticRuleMapperImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MdStatisticRuleMapperImpl.class);

    /**
     * 执行动态sql
     *
     * @param parameters
     * @return
     */
    public String getStatisDataList(Map<String, Object> parameters) {
        String sql = (String) parameters.get("sql");
        StringBuffer strb = new StringBuffer();
        strb.append(sql);
        LOG.debug("getStatisDataList sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 执行动态sql
     *
     * @param parameters
     * @return
     */
    public String insertStatisDataList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        StatisData statisData = (StatisData) parameters.get("statisData");
        String tableName = (String) parameters.get("tableName");
        strb.append("INSERT INTO "+ tableName +" (STIME, METRIC_ID, ATTR1, ATTR2, ATTR3, ATTR4, MVALUE, CREATE_TIME) VALUES ");
        strb.append("(to_date('" + statisData.getStime() + "','yyyy-MM-dd hh24:mi:ss' ), '" + statisData.getMetricId() + "','"
                + statisData.getAttr1() + "','" + statisData.getAttr2() + "','"
                + statisData.getAttr3() + "','"
                + statisData.getAttr4() + "','"
                + statisData.getMvalue() + "',to_date('" + statisData.getCreateTime() + "','yyyy-MM-dd hh24:mi:ss' ) )");
        LOG.debug("insertStatisDataList sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 插入二次统计数据
     *
     * @param parameters
     * @return
     */
    public String insertStatisDataListWithMysql(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        StatisData statisData = (StatisData) parameters.get("statisData");
        String tableName = (String) parameters.get("tableName");
        strb.append("INSERT INTO "+ tableName +" (STIME, METRIC_ID, ATTR1, ATTR2, ATTR3, ATTR4, MVALUE, CREATE_TIME) VALUES ");
        strb.append("(date_format('" + statisData.getStime() + "','%Y-%m-%d %H:%i:%s' ), '" + statisData.getMetricId() + "','"
                + statisData.getAttr1() + "','" + statisData.getAttr2() + "','"
                + statisData.getAttr3() + "','"
                + statisData.getAttr4() + "','"
                + statisData.getMvalue() + "',date_format('" + statisData.getCreateTime() + "','%Y-%m-%d %H:%i:%s' ) )");
        LOG.debug("insertStatisDataListWithMysql sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 删除指定日期数据
     *
     * @param parameters
     * @return
     */
    public String deleteStatisDataList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String deleteTime = (String) parameters.get("deleteTime");
        String offset = (String) parameters.get("offset");
        deleteTime = DateUtil.getFormatTime(deleteTime, Constant.YESTERDAY_FORMAT, Constant.MONTH_REPORT, offset);
        String deleteTable =  "STATIS_DATA_MONTH_" + deleteTime.substring(5,7);
        strb.append("DELETE FROM " + deleteTable + " WHERE STIME LIKE '%" + deleteTime + "%'");
        LOG.debug("insertStatisDataListWithMysql sql = {}", strb.toString());
        return strb.toString();
    }

}
