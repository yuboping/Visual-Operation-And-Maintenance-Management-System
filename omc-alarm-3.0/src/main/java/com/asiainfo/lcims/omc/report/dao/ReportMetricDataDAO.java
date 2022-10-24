package com.asiainfo.lcims.omc.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.business.TimeControl;
import com.asiainfo.lcims.omc.alarm.dao.BaseDAO;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.util.DbSqlUtil;

public class ReportMetricDataDAO extends BaseDAO{
    
    private static final Logger logger = LoggerFactory.make();
    
    public static List<ReportChartData> getSimpleData(String metricid, String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID,METRIC_ID,MVALUE,ITEM FROM METRIC_DATA_SINGLE_"+TimeControl.getTbsuffixesByTime(time));
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getTimeDHMSql("STIME")+"='"+time+"' ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setValue(resultSet.getString("MVALUE"));
                chartData.setItem(resultSet.getString("ITEM"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ReportChartData> getSpecialData (String metricid, String time) {
    	Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;  //20190930
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        String monuthDay=time.substring(4, 6)+"_"+time.substring(6, 8);
        String ftiem=time.substring(0,4)+"-"+time.substring(4, 6)+"-"+time.substring(6, 8);
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID,METRIC_ID,MVALUE,ITEM FROM METRIC_DATA_SINGLE_"+monuthDay);
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getDateSql("STIME")+"='"+ftiem+"' ");
            logger.info(metricid+":query sql is :"+strb.toString());
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setValue(resultSet.getString("MVALUE"));
                chartData.setItem(resultSet.getString("ITEM"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ReportChartData> getReadWrtnSpeedData(String metricid, String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID, METRIC_ID, ATTR2, max(case when attr1 = 'kB_wrtn/s' then MVALUE else 0 end) as ATTR3 , max(case when attr1 = 'kB_read/s' then MVALUE else 0 end) as ATTR4 FROM METRIC_DATA_MULTI_"+TimeControl.getTbsuffixesByTime(time));
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getTimeDHMSql("STIME")+"='"+time+"' GROUP BY HOST_ID, METRIC_ID , ATTR2 ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setAttr(resultSet.getString("ATTR2"));    //磁盘名称
                chartData.setItem(resultSet.getString("ATTR3"));
                chartData.setAttr1(resultSet.getString("ATTR4"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ReportChartData> getMemoryUseRateData(String metricid, String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID,METRIC_ID,MVALUE,ITEM FROM METRIC_DATA_SINGLE_"+TimeControl.getTbsuffixesByTime(time));
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getTimeDHMSql("STIME")+"='"+time+"' ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setValue(resultSet.getString("MVALUE"));
                chartData.setItem(resultSet.getString("ITEM"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ReportChartData> getFilesysUseRateData(String metricid, String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID,METRIC_ID,MVALUE,ATTR1 FROM METRIC_DATA_MULTI_"+TimeControl.getTbsuffixesByTime(time));
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getTimeDHMSql("STIME")+"='"+time+"' AND ATTR2='useRate'");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setValue(resultSet.getString("MVALUE"));
                chartData.setAttr(resultSet.getString("ATTR1"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ReportChartData> getCpuUseRateData(String metricid, String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT HOST_ID,METRIC_ID,MVALUE,ITEM FROM METRIC_DATA_SINGLE_"+TimeControl.getTbsuffixesByTime(time));
            strb.append(" WHERE METRIC_ID='"+metricid+"' AND "+DbSqlUtil.getTimeDHMSql("STIME")+"='"+time+"' ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("HOST_ID"));
                chartData.setMetricid(resultSet.getString("METRIC_ID"));
                chartData.setValue(resultSet.getString("MVALUE"));
                chartData.setItem(resultSet.getString("ITEM"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }
}
