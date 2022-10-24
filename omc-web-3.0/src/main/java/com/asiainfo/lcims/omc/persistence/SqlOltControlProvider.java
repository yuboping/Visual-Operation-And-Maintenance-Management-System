package com.asiainfo.lcims.omc.persistence;

import com.alibaba.druid.util.StringUtils;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.oltcontrol.OltControlVo;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Map;

public class SqlOltControlProvider {

    private static final Logger LOG = LoggerFactory.make();

    /** 表名 */
    private static final String TABLE_NAME = "METRIC_DATA_SINGLE_";

    /**
     * 获取表格数据
     * @param parameters
     * @return
     */
    public String getOltControlAlarmList(Map<String, Object> parameters) {
        OltControlVo oltControlVo = (OltControlVo) parameters.get("oltControlVo");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT c.ATTR1 , d.ALARMNAME AS ALARM_LEVEL , c.ALARM_MSG , ")
                .append(DbSqlUtil.getTimeSql("c.ALARM_TIME"))
                .append(" AS ALARM_TIME FROM MD_OTHER_ALARM_INFO c LEFT JOIN MD_ALARM_LEVEL d ON c.ALARM_LEVEL=d.ALARMLEVEL WHERE c.ALARM_NUM > 0 ");

        if (!StringUtils.isEmpty(oltControlVo.getOltIp())) {
            strb.append(" AND c.ATTR1 = '").append(oltControlVo.getOltIp()).append("'");
        }
        if (!StringUtils.isEmpty(oltControlVo.getOltIps())) {
            strb.append(" AND c.ATTR1 IN (").append(getOltIpArrayString(oltControlVo.getOltIps())).append(")");
        }
        if (!StringUtils.isEmpty(oltControlVo.getSearch_time())){
            strb.append(" AND " + DbSqlUtil.getDateSql("c.ALARM_TIME") + "='" + oltControlVo.getSearch_time() + "'");
        }

        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        return sql;
    }

    /**
     * 获取表格数据总数量
     * @param parameters
     * @return
     */
    public String getOltControlAlarmCount(Map<String, Object> parameters) {
        OltControlVo oltControlVo = (OltControlVo) parameters.get("oltControlVo");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT COUNT(*) ")
                .append("FROM MD_OTHER_ALARM_INFO c WHERE c.ALARM_NUM > 0 ");
        if (!StringUtils.isEmpty(oltControlVo.getOltIp())) {
            strb.append(" AND c.ATTR1 = '").append(oltControlVo.getOltIp()).append("'");
        }
        if (!StringUtils.isEmpty(oltControlVo.getOltIps())) {
            strb.append(" AND c.ATTR1 IN (").append(getOltIpArrayString(oltControlVo.getOltIps())).append(")");
        }
        if (!StringUtils.isEmpty(oltControlVo.getSearch_time())){
            strb.append(" AND " + DbSqlUtil.getDateSql("c.ALARM_TIME") + "='" + oltControlVo.getSearch_time() + "'");
        }
        return strb.toString();
    }

    /**
     * 获取折线图
     * @param parameters
     * @return
     */
    public String getLineData(Map<String, Object> parameters) {
        OltControlVo oltControlVo = (OltControlVo) parameters.get("oltControlVo");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT b.MVALUE, b.ITEM, ").append(DbSqlUtil.getTimeSql("b.STIME")).append(" AS STIME FROM ")
            .append(TABLE_NAME
                    + DateUtil.getFormatTimeByFormat(
                        oltControlVo.getSearch_time(),
                        Constant.TODAY_CONSTANT,
                        Constant.TABLE_NAME_FORMAT)
                    + " b LEFT JOIN MD_METRIC c on b.METRIC_ID = c.ID ")
                .append("WHERE c.METRIC_IDENTITY = '"
                        + CommonInit.BUSCONF.getOltMetricIdentity() + "'" );

        if (!StringUtils.isEmpty(oltControlVo.getOltIp())){
            strb.append(" AND b.ITEM = '" + oltControlVo.getOltIp() + "'");
        }
        if (!StringUtils.isEmpty(oltControlVo.getOltIps())) {
            strb.append(" AND b.ITEM IN (").append(getOltIpArrayString(oltControlVo.getOltIps())).append(")");
        }
        if (!StringUtils.isEmpty(oltControlVo.getSearch_time())){
            strb.append(" AND " + DbSqlUtil.getDateSql("b.STIME") + "='" + oltControlVo.getSearch_time() + "'");
        }
        strb.append("ORDER BY b.STIME");

        return strb.toString();
    }

    /**
     * 获取下拉框数据
     * @param parameters
     * @return
     */
    public String getSelectOltSingleData(Map<String, Object> parameters) {
        String todayOrYesterday = (String) parameters.get("todayOrYesterday");
        StringBuffer strb = new StringBuffer();
        String dateOfNow = DateUtil.parseStr(new Date(), Constant.CREATE_FORMAT);
        strb.append("SELECT b.ITEM AS oltIp ").append("FROM ")
                .append(TABLE_NAME
                        + DateUtil.getFormatTimeByFormat(
                        dateOfNow,
                        todayOrYesterday,
                        Constant.TABLE_NAME_FORMAT)
                        + " b LEFT JOIN MD_METRIC c on b.METRIC_ID = c.ID ")
                .append("WHERE c.METRIC_IDENTITY = '"
                        + CommonInit.BUSCONF.getOltMetricIdentity() + "' GROUP BY b.ITEM " );
        Page page = new Page(0);
        page.setPageNumber(1);
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        return sql;
    }

    /**
     * 获取下拉框数据
     * @param parameters
     * @return
     */
    public String getOltSelectControlAlarmList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        strb.append(" SELECT ATTR1 AS oltIp FROM MD_OTHER_ALARM_INFO WHERE ALARM_NUM > 0 GROUP BY ATTR1 " );
        Page page = new Page(0);
        page.setPageNumber(1);
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        return sql;
    }

    private static String getOltIpArrayString(String oltIps) {
        String[] oltIpArray = oltIps.split(",");
        String oltIpString = "";
        for (String oltIp : oltIpArray){
            oltIp = "'" + oltIp + "',";
            oltIpString += oltIp;
        }
        oltIpString = oltIpString.substring(0,oltIpString.length()-1);
        return oltIpString;
    }

}
