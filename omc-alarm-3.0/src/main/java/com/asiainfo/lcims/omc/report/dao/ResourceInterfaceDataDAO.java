package com.asiainfo.lcims.omc.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.omc.alarm.dao.BaseDAO;
import com.asiainfo.lcims.omc.report.model.ResourceInterfaceData;
import com.asiainfo.lcims.util.DbSqlUtil;

public class ResourceInterfaceDataDAO extends BaseDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceInterfaceDataDAO.class);

    public static List<ResourceInterfaceData> getResourceData(String time) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ResourceInterfaceData> list = new ArrayList<ResourceInterfaceData>();
        try {
            StringBuilder strb = new StringBuilder(
                    "SELECT HOST_ADDR,CPU,MEMORY,DISK,NETCARD,UPDATE_TIME FROM MD_RESOURCE_INTERFACE WHERE ");
            strb.append(DbSqlUtil.getTimeDHMSql("UPDATE_TIME") + "='" + time + "' ");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ResourceInterfaceData interfaceData = new ResourceInterfaceData();
                interfaceData.setHost_addr(resultSet.getString("HOST_ADDR"));
                interfaceData.setCpu(resultSet.getString("CPU"));
                interfaceData.setMemory(resultSet.getString("MEMORY"));
                interfaceData.setDisk(resultSet.getString("DISK"));
                interfaceData.setNetcard(resultSet.getString("NETCARD"));
                interfaceData.setUpdate_time(resultSet.getString("UPDATE_TIME"));
                list.add(interfaceData);
            }
        } catch (SQLException e) {
            LOG.error("getResourceData : database query operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<String> getSqlMetaData() {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<String> list = new ArrayList<String>();
        try {
            String sql = "SELECT CPU,MEMORY,DISK,NETCARD FROM MD_RESOURCE_INTERFACE";
            prst = connection.prepareStatement(sql);
            resultSet = prst.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 0; i < count; i++) {
                String columnName = rsmd.getColumnName(i + 1);
                list.add(columnName);
            }
        } catch (SQLException e) {
            LOG.error("sql matchSql error, reason : ", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

}
