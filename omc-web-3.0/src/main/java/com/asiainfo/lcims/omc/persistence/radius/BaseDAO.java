package com.asiainfo.lcims.omc.persistence.radius;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;

public abstract class BaseDAO {
    private static final Logger logger = LoggerFactory.make();

    protected static void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("close ResultSet  error.", e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("close PreparedStatement error.", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close Connection error.", e);
            }
        }
    }

    protected static void close(Statement st, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("close Statement error.", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close Connection error.", e);
            }
        }

    }

    protected static void logsql(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
    }
}
