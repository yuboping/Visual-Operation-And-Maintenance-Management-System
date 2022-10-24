package com.asiainfo.lcims.omc.persistence.radius;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.db.ConnPool;
import com.asiainfo.lcims.omc.model.radius.BusinessData;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusinessDataDao extends BaseDAO {

    private static final Logger logger = LoggerFactory.make();

    /**
     * 查询折线图数据
     * @param
     * @return
     */
    public static List<BusinessData> getRadiusData(String startDate, String endDate) {
        Connection connection = ConnPool.getConnection();
        ResultSet resultSet = null;
        PreparedStatement prest = null;

        StringBuilder sql = new StringBuilder("SELECT " + DbSqlUtil.getTimeHMSql("LOG_TIME")
                + " AS LOG_TIME ,"  + DbSqlUtil.getDateSql("LOG_TIME") + " AS LOG_TIME_YMD ,")
                .append(" AUTH_COUNT, AUTH_OK, BIND_ERROR, LM_ERROR, LOCK_ERROR, PASSWD_ERROR, USE_NOT_FOUND, OTHER FROM monitor_result WHERE ")
                .append(DbSqlUtil.getDateSql("LOG_TIME"))
                .append(" >= '" + startDate + "' AND ")
                .append(DbSqlUtil.getDateSql("LOG_TIME"))
                .append(" <= '" + endDate + "'");

        List<BusinessData> ret = new ArrayList<>();

        try {
            logger.info(sql.toString());
            prest =connection.prepareStatement(sql.toString());
            resultSet = prest.executeQuery();
            while (resultSet.next()) {
                try {
                    BusinessData businessData = new BusinessData();
                    businessData.setLogTime(resultSet.getString("LOG_TIME"));
                    businessData.setLogTimeYmd(resultSet.getString("LOG_TIME_YMD"));
                    businessData.setAuthCount(resultSet.getString("AUTH_COUNT"));
                    businessData.setAuthOk(resultSet.getString("AUTH_OK"));
                    businessData.setBindError(resultSet.getString("BIND_ERROR"));
                    businessData.setLmError(resultSet.getString("LM_ERROR"));
                    businessData.setLockError(resultSet.getString("LOCK_ERROR"));
                    businessData.setPasswdError(resultSet.getString("PASSWD_ERROR"));
                    businessData.setUserNotFound(resultSet.getString("USE_NOT_FOUND"));
                    businessData.setOther(resultSet.getString("OTHER"));
                    ret.add(businessData);
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
