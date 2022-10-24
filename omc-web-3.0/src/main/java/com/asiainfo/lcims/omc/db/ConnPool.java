package com.asiainfo.lcims.omc.db;

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
import com.asiainfo.lcims.omc.util.ReadFile;

/**
 * 获取数据库连接
 * 
 * @author qinwoli
 * 
 */
public class ConnPool {
    private static final Logger LOG = LoggerFactory.getLogger(ConnPool.class);
    private static final DataSource DS;
    /** 默认的配置文件名 */
    private static final String DEFAULT_CONFIG_NAME = ReadFile
            .getFileExistFolder("druid.properties") + "/druid.properties";
    static {
        DataSource dataSource;
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(DEFAULT_CONFIG_NAME);
            Properties prop = new Properties();
            prop.load(input);
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            dataSource.setLoginTimeout(60);
        } catch (Exception e) {
            LOG.error("", e);
            dataSource = null;
        } finally {
            close(input);
        }
        DS = dataSource;
    }

    private static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
    }

    private ConnPool() {

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
                LOG.error("", e);
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
                LOG.error("", e);
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
                LOG.error("", e);
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
            LOG.error("", e);
        }
        return null;
    }

    public static void close(ResultSet rset, Statement stat, Connection conn) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
    }

    public static void close(Statement stat, Connection conn) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
    }
    
    public static void close(Statement stat) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
    }
    
    public static void close(ResultSet rset, Statement stat) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                LOG.error("", e);
            }
        }
    }
}
