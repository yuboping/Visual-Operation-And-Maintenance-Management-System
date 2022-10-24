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
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.omc.report.service.GdcuUploadService;
import com.asiainfo.lcims.util.DbSqlUtil;

public class GdcuMetricDataDAO extends BaseDAO{
    
    private static final Logger logger = LoggerFactory.make();
    
    public static List<ReportChartData> getReqTotalOrderArea(String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT area.AREANO AS MARK,SUM(DT.MVALUE) AS MVALUE FROM METRIC_DATA_MULTI_"+TimeControl.getTbsuffixesByTime(time)+" DT ");
            strb.append(" LEFT JOIN bd_nas nas ON DT.ATTR1 = nas.NAS_IP")
                .append(" LEFT JOIN md_area area ON nas.AREA_NO = area.AREANO")
                .append(" WHERE (DT.METRIC_ID = '8cb2409faf07460190b807634e23abf1' OR DT.METRIC_ID='7f4078a39fc94a7eb027c835aa63f753') AND "+DbSqlUtil.getTimeDHMSql("DT.STIME")+"='"+time+"' ")
                .append(" GROUP BY area.AREANO");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("MARK"));
                chartData.setValue(resultSet.getString("MVALUE"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }
    
    public static List<ReportChartData> getDelayAvgOrderArea(String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        String metricId = InitParam.getMetricByIdentity(GdcuUploadService.radius_delay_avg).getId();
        try {
            StringBuilder strb = new StringBuilder("SELECT DT.ATTR1 AS MARK,DT.MVALUE AS MVALUE FROM STATIS_DATA_DAY_"+TimeControl.getTbsuffixesByTime(time)+" DT ");
            strb.append(" WHERE DT.METRIC_ID='"+metricId+"' AND DT.ATTR2='area' AND "+DbSqlUtil.getTimeDHMSql("DT.STIME")+"='"+time+"' ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("MARK"));
                chartData.setValue(resultSet.getString("MVALUE"));
                list.add(chartData);
            }
        } catch (SQLException e) {
            logger.error("getSimpleData :database operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }
    
    public static List<ReportChartData> getOnlieUserOrderArea(String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ReportChartData> list = new ArrayList<ReportChartData>();
        try {
            StringBuilder strb = new StringBuilder("SELECT b.AREA_NO AS MARK,SUM(DT.MVALUE) AS MVALUE FROM METRIC_DATA_MULTI_"+TimeControl.getTbsuffixesByTime(time)+" DT ");
            strb.append(" INNER JOIN bd_nas b ON b.NAS_IP = DT.ATTR1")
                .append(" WHERE DT.METRIC_ID='c88092f1caab44508ea033f5f9c9a0fe' AND "+DbSqlUtil.getTimeDHMSql("DT.STIME")+"='"+time+"' ")
                .append(" GROUP BY b.AREA_NO");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ReportChartData chartData = new ReportChartData();
                chartData.setMark(resultSet.getString("MARK"));
                chartData.setValue(resultSet.getString("MVALUE"));
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
