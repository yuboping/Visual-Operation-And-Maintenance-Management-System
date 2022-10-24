package com.asiainfo.lcims.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.asiainfo.lcims.omc.alarm.business.AlarmService;

/**
 * 获取系统外的数据库连接
 * @author zhul
 *
 */
public class ConnPoolOutSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ConnPoolOutSystem.class);

    private static final DataSource DS = initDb();
    
    private static DataSource initDb(){
        DataSource dataSource = null;
        if(AlarmService.conf.outDbExist()){
            InputStream input = null;
            try {
                input = Thread.currentThread().getContextClassLoader()
                        .getResource("druid_out.properties").openStream();
                Properties prop = new Properties();
                prop.load(input);
                dataSource = DruidDataSourceFactory.createDataSource(prop);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            } finally {
                close(input);
            }
        }
        return dataSource;
    }
    
    private static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private ConnPoolOutSystem() {

    }

    /**
     * session不为空时关闭session
     * 
     * @param session
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    /**
     * connection不为空时提交session
     * 
     * @param connection
     */
    public static void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    /**
     * connection不为空时rollback
     * 
     * @param connection
     */
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取Connection
     * 
     * @return
     */
    public static java.sql.Connection getConnection() {
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static void close(ResultSet rset, Statement stat, Connection conn) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
