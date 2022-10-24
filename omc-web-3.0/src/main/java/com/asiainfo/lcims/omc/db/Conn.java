package com.asiainfo.lcims.omc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.asiainfo.lcims.omc.model.report.ReportDataSource;
import com.asiainfo.lcims.omc.util.EnumUtil;

public class Conn {
    private static final Logger logger = LoggerFactory.getLogger(Conn.class);

    public static Connection getConn(boolean autoCommit) {
        WebApplicationContext webApplicationContext = ContextLoader
                .getCurrentWebApplicationContext();
        SqlSessionFactory factory = (SqlSessionFactory) webApplicationContext
                .getBean("sqlSessionFactory");
        SqlSession sessoion = factory.openSession(autoCommit);
        return sessoion.getConnection();
    }

    public static Connection getConn() {
        WebApplicationContext webApplicationContext = ContextLoader
                .getCurrentWebApplicationContext();
        SqlSessionFactory factory = (SqlSessionFactory) webApplicationContext
                .getBean("sqlSessionFactory");
        SqlSession sessoion = factory.openSession();
        return sessoion.getConnection();
    }

    public static Connection getConn(ReportDataSource dataSource) {
        String driver = null;
        if (EnumUtil.DB_MYSQL.equalsIgnoreCase(dataSource.getType())) {
            driver = "com.mysql.jdbc.Driver";
        } else if (EnumUtil.DB_ORACLE.equalsIgnoreCase(dataSource.getType())) {
            driver = "oracle.jdbc.driver.OracleDriver";
        }
        logger.info("DB_info:" + dataSource.toString());
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(),
                    dataSource.getPwd());
        } catch (Exception e) {
            logger.error("方法执行错误",e);
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("方法执行错误",e);
            }
        }
    }
}
