package com.asiainfo.lcims.omc.agentserver.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * Druid获取数据库连接
 * 
 */
public class DruidConnPool {
    private static final Logger log = LoggerFactory.getLogger(DruidConnPool.class);
    private static DruidConnPool databasePool = null;
    private static DruidDataSource dds = null;

    static {
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("druid.properties");
            Properties prop = new Properties();
            prop.load(input);
            dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            log.error("数据库连接池初始化异常", e);
        } finally {
            close(input);
        }
    }

    private DruidConnPool() {

    }

    protected static synchronized DruidConnPool getInstance() {
        if (null == databasePool) {
            databasePool = new DruidConnPool();
        }
        return databasePool;
    }

    protected DruidPooledConnection getConnection() throws SQLException {
        return dds.getConnection();
    }

    private static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                log.error("close input error:", e);
            }
        }
    }

    /**
     * session不为空时关闭session
     * 
     * @param session
     */
    protected static void close(DruidPooledConnection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("colse session error:", e);
            }
        }
    }

    /**
     * connection不为空时提交session
     * 
     * @param connection
     */
    protected static void commit(DruidPooledConnection connection) {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit session error:", e);
            }
        }
    }

    /**
     * connection不为空时rollback
     * 
     * @param connection
     */
    protected static void rollback(DruidPooledConnection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                log.error("connection rollback error:", e);
            }
        }
    }

    protected static void close(ResultSet rset, Statement stat, DruidPooledConnection conn) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException e) {
                log.error("colse rset error:", e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                log.error("colse stat error:", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("colse conn error:", e);
            }
        }
    }

    protected static void close(PreparedStatement stat, DruidPooledConnection conn) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                log.error("colse stat error:", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close conn error:", e);
            }
        }
    }

    protected static void close(Statement stat, DruidPooledConnection conn) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                log.error("close stat error:", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close conn error:", e);
            }
        }
    }

    protected static void close(Statement stat) {
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                log.error("close stat error:", e);
            }
        }
    }

    protected static void close(ResultSet rset, Statement stat) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException e) {
                log.error("close rset error:", e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                log.error("close stat error:", e);
            }
        }
    }
}
