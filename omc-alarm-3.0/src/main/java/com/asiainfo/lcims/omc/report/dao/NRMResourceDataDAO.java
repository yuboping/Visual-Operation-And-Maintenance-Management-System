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
import com.asiainfo.lcims.omc.report.model.NRMResourceData;

public class NRMResourceDataDAO extends BaseDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NRMResourceDataDAO.class);

    public static List<NRMResourceData> getNRMResourceData() {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<NRMResourceData> list = new ArrayList<>();
        try {
            StringBuilder strb = new StringBuilder(
                    "SELECT VNFINSTANCEID,ID,USERLABEL,LOCATION_NAME,MANAGEDBY,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,VERSION,VENDOR_NAME,HARDWARE_PLATFORM,PVFLAG,MANAGEMENT_IPADDRS,SYSUPTIME,ADMINISTRATIVE_STATE,OPERATIONAL_STATE,PATCH_INFO,NETYPE FROM MD_NRM_OMC");
            prst = connection.prepareStatement(strb.toString());
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                NRMResourceData nrmData = new NRMResourceData();
                nrmData.setVnfInstanceID(resultSet.getString("VNFINSTANCEID"));
                nrmData.setId(resultSet.getString("ID"));
                nrmData.setUserLabel(resultSet.getString("USERLABEL"));
                nrmData.setLocationName(resultSet.getString("LOCATION_NAME"));
                nrmData.setManagedBy(resultSet.getString("MANAGEDBY"));
                nrmData.setSoftwareName(resultSet.getString("SOFTWARE_NAME"));
                nrmData.setSoftwareVersion(resultSet.getString("SOFTWARE_VERSION"));
                nrmData.setSoftwareVendor(resultSet.getString("SOFTWARE_VENDOR"));
                nrmData.setVersion(resultSet.getString("VERSION"));
                nrmData.setVendorName(resultSet.getString("VENDOR_NAME"));
                nrmData.setHardwarePlatform(resultSet.getString("HARDWARE_PLATFORM"));
                nrmData.setPvFlag(resultSet.getString("PVFLAG"));
                nrmData.setManagementIpAddrs(resultSet.getString("MANAGEMENT_IPADDRS"));
                nrmData.setSysUpTime(resultSet.getString("SYSUPTIME"));
                nrmData.setAdministrativeState(resultSet.getString("ADMINISTRATIVE_STATE"));
                nrmData.setOperationalState(resultSet.getString("OPERATIONAL_STATE"));
                nrmData.setPatchInfo(resultSet.getString("PATCH_INFO"));
                nrmData.setNeType(resultSet.getString("NETYPE"));
                list.add(nrmData);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

    public static List<NRMResourceData> getLimitNRMResourceData(long limit, long offset) {
        Connection connection = ConnPool.getConnection();
        PreparedStatement prst = null;
        ResultSet resultSet = null;
        List<NRMResourceData> list = new ArrayList<>();
        try {
            StringBuilder strb = new StringBuilder(
                    "SELECT VNFINSTANCEID,ID,USERLABEL,LOCATION_NAME,MANAGEDBY,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,VERSION,VENDOR_NAME,HARDWARE_PLATFORM,PVFLAG,MANAGEMENT_IPADDRS,SYSUPTIME,ADMINISTRATIVE_STATE,OPERATIONAL_STATE,PATCH_INFO,NETYPE FROM MD_NRM_OMC LIMIT ?,?");
            prst = connection.prepareStatement(strb.toString());
            prst.setLong(1, limit);
            prst.setLong(2, offset);
            resultSet = prst.executeQuery();
            while (resultSet.next()) {
                NRMResourceData nrmData = new NRMResourceData();
                nrmData.setVnfInstanceID(resultSet.getString("VNFINSTANCEID"));
                nrmData.setId(resultSet.getString("ID"));
                nrmData.setUserLabel(resultSet.getString("USERLABEL"));
                nrmData.setLocationName(resultSet.getString("LOCATION_NAME"));
                nrmData.setManagedBy(resultSet.getString("MANAGEDBY"));
                nrmData.setSoftwareName(resultSet.getString("SOFTWARE_NAME"));
                nrmData.setSoftwareVersion(resultSet.getString("SOFTWARE_VERSION"));
                nrmData.setSoftwareVendor(resultSet.getString("SOFTWARE_VENDOR"));
                nrmData.setVersion(resultSet.getString("VERSION"));
                nrmData.setVendorName(resultSet.getString("VENDOR_NAME"));
                nrmData.setHardwarePlatform(resultSet.getString("HARDWARE_PLATFORM"));
                nrmData.setPvFlag(resultSet.getString("PVFLAG"));
                nrmData.setManagementIpAddrs(resultSet.getString("MANAGEMENT_IPADDRS"));
                nrmData.setSysUpTime(resultSet.getString("SYSUPTIME"));
                nrmData.setAdministrativeState(resultSet.getString("ADMINISTRATIVE_STATE"));
                nrmData.setOperationalState(resultSet.getString("OPERATIONAL_STATE"));
                nrmData.setPatchInfo(resultSet.getString("PATCH_INFO"));
                nrmData.setNeType(resultSet.getString("NETYPE"));
                list.add(nrmData);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            close(resultSet, prst, connection);
        }
        return list;
    }

}
