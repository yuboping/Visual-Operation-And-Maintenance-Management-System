package com.asiainfo.lcims.omc.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.omc.alarm.dao.BaseDAO;
import com.asiainfo.lcims.omc.report.model.ResourceOmcData;

public class ResourceOmcDataDAO extends BaseDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceOmcDataDAO.class);

    public static List<ResourceOmcData> getResourceData() {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ResourceOmcData> list = new ArrayList<>();
        try {
            StringBuilder strb = new StringBuilder(
                    "SELECT NATIVE_NAME,VENDOR,EQUIPMEMT_DOMAIN FROM MD_RESOURCE_OMC");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ResourceOmcData omcData = new ResourceOmcData();
                omcData.setNativeName(resultSet.getString("NATIVE_NAME"));
                omcData.setVendor(resultSet.getString("VENDOR"));
                omcData.setEquipmemtDomain(resultSet.getString("EQUIPMEMT_DOMAIN"));
                list.add(omcData);
            }
        } catch (SQLException e) {
            LOG.error("getResourceOmcData : database query operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<ResourceOmcData> getLimitResourceData(long limit, long offset) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<ResourceOmcData> list = new ArrayList<>();
        try {
            StringBuilder strb = new StringBuilder(
                    "SELECT NATIVE_NAME,VENDOR,EQUIPMEMT_DOMAIN FROM MD_RESOURCE_OMC LIMIT ?,?");
            prst = connection.prepareStatement(strb.toString());
            prst.setLong(1, limit);
            prst.setLong(2, offset);
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                ResourceOmcData omcData = new ResourceOmcData();
                omcData.setNativeName(resultSet.getString("NATIVE_NAME"));
                omcData.setVendor(resultSet.getString("VENDOR"));
                omcData.setEquipmemtDomain(resultSet.getString("EQUIPMEMT_DOMAIN"));
                list.add(omcData);
            }
        } catch (SQLException e) {
            LOG.error("getResourceOmcData : database query operate error.", e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

}
