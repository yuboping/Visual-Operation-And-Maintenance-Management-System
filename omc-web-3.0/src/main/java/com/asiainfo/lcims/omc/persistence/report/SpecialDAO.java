package com.asiainfo.lcims.omc.persistence.report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.gdcu.GdcuReportFieldInfo;
import com.asiainfo.lcims.omc.model.report.ReportFieldInfo;

public class SpecialDAO {
    private static final Logger logger = LoggerFactory.getLogger(SpecialDAO.class);

    /**
     * 根据sql查询数据，更根据fieldList对应排列顺序生成数组。
     * 
     * @param conn:数据库源
     * @param sql:查询sql
     * @param fieldList:sql展示字段
     * @return
     */
    public static String[][] SelectReportInfoSql(Connection conn, String sql,
            List<ReportFieldInfo> fieldList) {
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stat.executeQuery(sql);
            rs.last();
            int row = rs.getRow();
            int fieldsize = fieldList.size();
            String[][] result = new String[row][fieldsize];
            int i = 0;
            rs.beforeFirst();
            while (rs.next()) {
                for (int j = 0; j < fieldsize; j++) {
                    String o = rs.getString(fieldList.get(j).getSqlField());
                    result[i][j] = "" + o;
                }
                i++;
            }
            return result;
        } catch (SQLException e) {
            logger.error("{" + sql + "}sql execute error:", e);
        } finally {
            close(rs, stat);
        }
        return new String[0][0];
    }

    public static String[][] selectGdcuReportInfoSql(Connection conn, String sql,
            List<GdcuReportFieldInfo> fieldList) {
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stat.executeQuery(sql);
            rs.last();
            int row = rs.getRow();
            int fieldsize = fieldList.size();
            String[][] result = new String[row][fieldsize];
            int i = 0;
            rs.beforeFirst();
            while (rs.next()) {
                for (int j = 0; j < fieldsize; j++) {
                    String o = rs.getString(fieldList.get(j).getSqlfield());
                    result[i][j] = "" + o;
                }
                i++;
            }
            return result;
        } catch (SQLException e) {
            logger.error("{" + sql + "} gdcu sql execute error:", e);
        } finally {
            close(rs, stat);
        }
        return new String[0][0];
    }

    public static int SelectReportCount(Connection conn, String sql) {
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("{" + sql + "}sql execute error:", e);
        } finally {
            close(rs, stat);
        }
        return 0;
    }

    /**
     * 获取查询SQL的查询列名
     * 
     * @param conn
     * @param sql
     * @return
     */
    public static List<String> getSqlMetaData(Connection conn, String sql) {
        Statement stat = null;
        ResultSet rs = null;
        List<String> list = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            list = new ArrayList<String>();
            for (int i = 0; i < count; i++) {
                list.add(rsmd.getColumnLabel(i + 1).toLowerCase());
            }
        } catch (SQLException e) {
            logger.error("{" + sql + "}matchSql error:" + e.getMessage(), e);
        } finally {
            close(rs, stat);
        }
        return list;
    }

    private static void close(ResultSet rs, Statement stat) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("stat close error:" + e.getMessage(), e);
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
//                e.printStackTrace();
                logger.error("stat close error:" + e.getMessage(), e);
            }
        }
    }

    public static String selectReportHeaderContent(Connection conn, String sql) {
        Statement stat = null;
        ResultSet rs = null;
        String result = "";
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString(1);
                return result;
            }
            return result;
        } catch (SQLException e) {
            logger.error("{" + sql + "}sql execute error:", e);
        } finally {
            close(rs, stat);
        }
        return result;
    }
}
