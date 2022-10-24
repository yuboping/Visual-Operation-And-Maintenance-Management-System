package com.asiainfo.lcims.omc.persistence.radius;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.db.ConnPool;
import com.asiainfo.lcims.omc.model.radius.Radius;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RadiusDao extends BaseDAO {
    private static final Logger logger = LoggerFactory.make();

    /**
     * 查询折线图数据
     * @param
     * @return
     */
    public static List<Radius> getRadiusData(String startDate, String endDate, String hostName) {
        Connection connection = ConnPool.getConnection();
        ResultSet resultSet = null;
        PreparedStatement prest = null;

        StringBuilder sql = new StringBuilder("SELECT " + DbSqlUtil.getTimeHMSql("LOG_TIME")
                + " AS LOG_TIME ,"  + DbSqlUtil.getDateSql("LOG_TIME") + " AS LOG_TIME_YMD ,")
                .append(" ACCESS_REQUEST, ACCESS_ACCEPT, NOT_FOUND, PASSWORD,")
                .append(" LOCKED, LIMIT_EXCEED, AI_VLAN_ID, NAS_IP_ADDRESS, NO_RESPONSE, OTHER ")
                .append(" FROM auth_rate WHERE ")
                .append(DbSqlUtil.getDateSql("LOG_TIME"))
                .append(" >= '" + startDate + "' AND ")
                .append(DbSqlUtil.getDateSql("LOG_TIME"))
                .append(" <= '" + endDate + "'")
                .append(" AND HOST_NAME = '" + hostName + "'");

        List<Radius> ret = new ArrayList<>();

        try {
            logger.info(sql.toString());
            prest =connection.prepareStatement(sql.toString());
            resultSet = prest.executeQuery();
            while (resultSet.next()) {
                try {
                    Radius radius = new Radius();
                    radius.setLogTime(resultSet.getString("LOG_TIME"));
                    radius.setLogTimeYmd(resultSet.getString("LOG_TIME_YMD"));
                    radius.setAccessRequest(resultSet.getString("ACCESS_REQUEST"));
                    radius.setAccessAccept(resultSet.getString("ACCESS_ACCEPT"));
                    radius.setNotFound(resultSet.getString("NOT_FOUND"));
                    radius.setPassword(resultSet.getString("PASSWORD"));
                    radius.setLocked(resultSet.getString("LOCKED"));
                    radius.setLimitExceed(resultSet.getString("LIMIT_EXCEED"));
                    radius.setAiVlanId(resultSet.getString("AI_VLAN_ID"));
                    radius.setNasIpAddress(resultSet.getString("NAS_IP_ADDRESS"));
                    radius.setNoResponse(resultSet.getString("NO_RESPONSE"));
                    radius.setOther(resultSet.getString("OTHER"));
                    ret.add(radius);
                } catch (Exception e) {
                    logger.error("alarm table mon_threshold has error data", e);
                }
            }
        } catch (SQLException e) {
            logger.error("database operate error.", e);
        } finally {
            close(resultSet,prest,connection);
        }
        return ret;
    }
}
