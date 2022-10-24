package com.asiainfo.lcims.omc.alarm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.db.ConnPool;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.AlarmMode;
import com.asiainfo.lcims.omc.alarm.model.Area;
import com.asiainfo.lcims.omc.alarm.model.BdNas;
import com.asiainfo.lcims.omc.alarm.model.DataConver;
import com.asiainfo.lcims.omc.alarm.model.Host;
import com.asiainfo.lcims.omc.alarm.model.MdAlarmLevel;
import com.asiainfo.lcims.omc.alarm.model.MdApnRecord;
import com.asiainfo.lcims.omc.alarm.model.MdChartDataSet;
import com.asiainfo.lcims.omc.alarm.model.MdChartDetail;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycle;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.model.MdParam;
import com.asiainfo.lcims.omc.alarm.model.Node;

public class CommonDAO extends BaseDAO {
    private static final Logger log = LoggerFactory.make();

    public static List<Host> getHosts() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<Host> hosts = new ArrayList<Host>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT h.HOSTID,h.ADDR,h.HOSTNAME,h.NODEID,h.HOSTTYPE,p.DESCRIPTION FROM MON_HOST h LEFT JOIN MD_PARAM p ON (p.CODE = h.HOSTTYPE) WHERE p.TYPE = '3'";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                Host info = new Host();
                info.setHostid(rset.getString("HOSTID"));
                info.setAddr(rset.getString("ADDR"));
                info.setHostname(rset.getString("HOSTNAME"));
                info.setNodeid(rset.getString("NODEID"));
                info.setHosttype(rset.getString("HOSTTYPE"));
                info.setHosttypename(rset.getString("DESCRIPTION"));
                hosts.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return hosts;
    }

    public static List<BdNas> getBdNas() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<BdNas> list = new ArrayList<BdNas>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT ID,NAS_NAME,NAS_IP,AREA_NO FROM BD_NAS";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                BdNas info = new BdNas();
                info.setId(rset.getString("ID"));
                info.setNas_ip(rset.getString("NAS_IP"));
                info.setNas_name(rset.getString("NAS_NAME"));
                info.setArea_no(rset.getString("AREA_NO"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<Area> getAreas() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<Area> list = new ArrayList<Area>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT AREANO,NAME FROM MD_AREA";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                Area info = new Area();
                info.setAreano(rset.getString("AREANO"));
                info.setName(rset.getString("NAME"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<AlarmMode> getModes() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<AlarmMode> list = new ArrayList<AlarmMode>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT MODEID,MODENAME,MODEATTR,MODETYPE FROM MD_ALARM_MODE";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                AlarmMode info = new AlarmMode();
                info.setModeid(rset.getString("MODEID"));
                info.setModename(rset.getString("MODENAME"));
                info.setModetype(rset.getInt("MODETYPE"));
                info.setModeattr(rset.getString("MODEATTR"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<MdParam> getParams() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdParam> list = new ArrayList<MdParam>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT TYPE,CODE,DESCRIPTION FROM MD_PARAM";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdParam info = new MdParam();
                info.setType(rset.getInt("TYPE"));
                info.setCode(rset.getString("CODE"));
                info.setDescription(rset.getString("DESCRIPTION"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<Node> getNodes() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<Node> nodes = new ArrayList<Node>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT ID,NODE_NAME FROM MD_NODE";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                Node info = new Node();
                info.setId(rset.getString("ID"));
                info.setNode_name(rset.getString("NODE_NAME"));
                nodes.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return nodes;
    }

    public static List<DataConver> getDataConvers() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<DataConver> list = new ArrayList<DataConver>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT CONVERFROM,CONVERTO FROM DATA_CONVER";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                DataConver info = new DataConver();
                info.setConverfrom(rset.getString("CONVERFROM"));
                info.setConverto(rset.getString("CONVERTO"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    /**
     * 告警级别
     * 
     * @return
     */
    public static List<MdAlarmLevel> getAlarmLevels() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdAlarmLevel> list = new ArrayList<MdAlarmLevel>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT ALARMLEVEL,ALARMNAME FROM MD_ALARM_LEVEL";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdAlarmLevel info = new MdAlarmLevel();
                info.setAlarmlevel(rset.getString("ALARMLEVEL"));
                info.setAlarmname(rset.getString("ALARMNAME"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    /**
     * 采集周期
     * 
     * @return
     */
    public static List<MdCollCycle> getCollCycles() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdCollCycle> list = new ArrayList<MdCollCycle>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT CYCLEID,CYCLENAME,CYCLE,CRON FROM MD_COLL_CYCLE";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdCollCycle info = new MdCollCycle();
                info.setCycleid(rset.getInt("CYCLEID"));
                info.setCyclename(rset.getString("CYCLENAME"));
                info.setCycle(rset.getInt("CYCLE"));
                info.setCron(rset.getString("CRON"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    /**
     * 图表数据配置表
     * 
     * @return
     */
    public static List<MdChartDataSet> getChartDataSets() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdChartDataSet> list = new ArrayList<MdChartDataSet>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT CHART_NAME,TABLE_NAME,METRIC_IDS,SCOPE,MARKTYPE,GROUPBY,METHOD,"
                    + "LEFT_JOIN,CONDITIONS,TABLEFIELDS FROM MD_CHART_DATASET";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdChartDataSet info = new MdChartDataSet();
                info.setChart_name(rset.getString("CHART_NAME"));
                info.setTable_name(rset.getString("TABLE_NAME"));
                info.setMetric_ids(rset.getString("METRIC_IDS"));
                info.setScope(rset.getString("SCOPE"));
                info.setMarktype(rset.getString("MARKTYPE"));
                info.setGroupby(rset.getString("GROUPBY"));
                info.setMethod(rset.getString("METHOD"));
                info.setLeft_join(rset.getString("LEFT_JOIN"));
                info.setConditions(rset.getString("CONDITIONS"));
                info.setTablefields(rset.getString("TABLEFIELDS"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    /**
     * 指标信息表
     * 
     * @return
     */
    public static List<MdMetric> getMetrics() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdMetric> list = new ArrayList<MdMetric>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID FROM MD_METRIC";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdMetric info = new MdMetric();
                info.setId(rset.getString("ID"));
                info.setMetric_identity(rset.getString("METRIC_IDENTITY"));
                info.setMetric_name(rset.getString("METRIC_NAME"));
                info.setCycle_id(rset.getInt("CYCLE_ID"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<MdChartDetail> getChartDetails() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdChartDetail> list = new ArrayList<MdChartDetail>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT CHART_NAME,CHART_TITLE,CHART_TYPE,STYLE,COLOR,EXTEND FROM MD_CHART_DETAIL";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdChartDetail info = new MdChartDetail();
                info.setChart_name(rset.getString("CHART_NAME"));
                info.setChart_title(rset.getString("CHART_TITLE"));
                info.setChart_type(rset.getString("CHART_TYPE"));
                info.setExtend(rset.getString("EXTEND"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

    public static List<MdApnRecord> getApn() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rset = null;
        List<MdApnRecord> list = new ArrayList<MdApnRecord>();
        try {
            conn = ConnPool.getConnection();
            stat = conn.createStatement();
            String sql = "SELECT APNID,APN,UPDATE_TIME FROM MD_APN_RECORD";
            rset = stat.executeQuery(sql);
            while (rset.next()) {
                MdApnRecord info = new MdApnRecord();
                info.setApnid(rset.getString("APNID"));
                info.setApn(rset.getString("APN"));
                info.setUpdate_time(rset.getString("UPDATE_TIME"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败", e);
        } finally {
            ConnPool.close(rset, stat, conn);
        }
        return list;
    }

}
