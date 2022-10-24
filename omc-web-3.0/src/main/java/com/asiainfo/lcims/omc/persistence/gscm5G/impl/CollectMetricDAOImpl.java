package com.asiainfo.lcims.omc.persistence.gscm5G.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class CollectMetricDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CollectMetricDAOImpl.class);

    public String getMdFirewallListByHost(Map<String, Object> parameters) {
        MonHost host = (MonHost) parameters.get("host");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT").append(" COALESCE(HM.HOST_ID,'").append(host.getHostid())
                .append("') AS HOST_ID,").append("COALESCE(H.ADDR,'").append(host.getAddr())
                .append("') AS ADDR,").append("COALESCE(H.HOSTNAME,'").append(host.getHostname())
                .append("') AS HOSTNAME,")
                .append("M.ID AS METRIC_ID,M.FIREWALL_NAME AS METRIC_NAME,")
                .append("COALESCE(HM.CYCLE_ID,'') AS CYCLE_ID,")
                .append("COALESCE(HM.SCRIPT,'') AS SCRIPT,")
                .append("COALESCE(HM.SCRIPT_PARAM,'') AS SCRIPT_PARAM,")
                .append("COALESCE(HM.SCRIPT_RETURN_TYPE,'') AS SCRIPT_RETURN_TYPE,")
                .append("COALESCE(HM.HOST_ID,'-1') AS FLAG")
                .append(" FROM MD_FIREWALL M LEFT JOIN MD_HOST_FIREWALL HM ON HM.FIREWALL_ID = M.ID AND HM.HOST_ID='")
                .append(host.getHostid()).append("'")
                .append(" LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID AND H.HOSTID='")
                .append(host.getHostid()).append("' ORDER BY FLAG");
        LOG.debug("getMdFirewallListByHost sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdThirdpartyListByHost(Map<String, Object> parameters) {
        MonHost host = (MonHost) parameters.get("host");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT").append(" COALESCE(HM.HOST_ID,'").append(host.getHostid())
                .append("') AS HOST_ID,").append("COALESCE(H.ADDR,'").append(host.getAddr())
                .append("') AS ADDR,").append("COALESCE(H.HOSTNAME,'").append(host.getHostname())
                .append("') AS HOSTNAME,")
                .append("M.ID AS METRIC_ID,M.THIRDPARTY_NAME AS METRIC_NAME,")
                .append("COALESCE(HM.CYCLE_ID,'') AS CYCLE_ID,")
                .append("COALESCE(HM.SCRIPT,'') AS SCRIPT,")
                .append("COALESCE(HM.SCRIPT_PARAM,'') AS SCRIPT_PARAM,")
                .append("COALESCE(HM.SCRIPT_RETURN_TYPE,'') AS SCRIPT_RETURN_TYPE,")
                .append("COALESCE(HM.HOST_ID,'-1') AS FLAG")
                .append(" FROM MD_THIRDPARTY M LEFT JOIN MD_HOST_THIRDPARTY HM ON HM.THIRDPARTY_ID = M.ID AND HM.HOST_ID='")
                .append(host.getHostid()).append("'")
                .append(" LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID AND H.HOSTID='")
                .append(host.getHostid()).append("' ORDER BY FLAG");
        LOG.debug("getMdThirdpartyListByHost sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdProtocolListByHost(Map<String, Object> parameters) {
        MonHost host = (MonHost) parameters.get("host");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT").append(" COALESCE(HM.HOST_ID,'").append(host.getHostid())
                .append("') AS HOST_ID,").append("COALESCE(H.ADDR,'").append(host.getAddr())
                .append("') AS ADDR,").append("COALESCE(H.HOSTNAME,'").append(host.getHostname())
                .append("') AS HOSTNAME,")
                .append("M.ID AS METRIC_ID,M.PROTOCOL_NAME AS METRIC_NAME,")
                .append("COALESCE(HM.CYCLE_ID,M.CYCLE_ID) AS CYCLE_ID,")
                .append("COALESCE(HM.SCRIPT,M.SCRIPT) AS SCRIPT,")
                .append("COALESCE(HM.SCRIPT_PARAM,M.SCRIPT_PARAM) AS SCRIPT_PARAM,")
                .append("COALESCE(HM.SCRIPT_RETURN_TYPE,M.SCRIPT_RETURN_TYPE) AS SCRIPT_RETURN_TYPE,")
                .append("COALESCE(HM.HOST_ID,'-1') AS FLAG")
                .append(" FROM MD_COLLECT_PROTOCOL M LEFT JOIN MD_HOST_PROTOCOL HM ON HM.PROTOCOL_ID = M.ID AND HM.HOST_ID='")
                .append(host.getHostid()).append("'")
                .append(" LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID AND H.HOSTID='")
                .append(host.getHostid()).append("' ORDER BY FLAG");
        LOG.debug("getMdProtocolListByHost sql = {}", strb.toString());
        return strb.toString();
    }

    public String insertHostFirewall(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append(
                "INSERT INTO MD_HOST_FIREWALL(ID,HOST_ID,FIREWALL_ID,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,STATE,UPDATE_TIME)VALUES");
        strb.append("('" + hostMetric.getId() + "','" + hostMetric.getHost_id() + "','"
                + hostMetric.getMetric_id() + "'," + hostMetric.getCycle_id() + ",'"
                + hostMetric.getScript() + "','"
                + StringUtil.SqlFilter(hostMetric.getScript_param()) + "',"
                + hostMetric.getScript_return_type() + ",0" + "," + DbSqlUtil.getDateMethod()
                + ")");
        LOG.debug("insertHostFirewall sql = {}", strb.toString());
        return strb.toString();
    }

    public String insertHostThirdparty(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append(
                "INSERT INTO MD_HOST_THIRDPARTY(ID,HOST_ID,THIRDPARTY_ID,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,STATE,UPDATE_TIME)VALUES");
        strb.append("('" + hostMetric.getId() + "','" + hostMetric.getHost_id() + "','"
                + hostMetric.getMetric_id() + "'," + hostMetric.getCycle_id() + ",'"
                + hostMetric.getScript() + "','"
                + StringUtil.SqlFilter(hostMetric.getScript_param()) + "',"
                + hostMetric.getScript_return_type() + ",0" + "," + DbSqlUtil.getDateMethod()
                + ")");
        LOG.debug("insertHostThirdparty sql = {}", strb.toString());
        return strb.toString();
    }

    public String insertHostProtocol(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append(
                "INSERT INTO MD_HOST_PROTOCOL(ID,HOST_ID,PROTOCOL_ID,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,STATE,UPDATE_TIME)VALUES");
        strb.append("('" + hostMetric.getId() + "','" + hostMetric.getHost_id() + "','"
                + hostMetric.getMetric_id() + "'," + hostMetric.getCycle_id() + ",'"
                + hostMetric.getScript() + "','"
                + StringUtil.SqlFilter(hostMetric.getScript_param()) + "',"
                + hostMetric.getScript_return_type() + ",0" + "," + DbSqlUtil.getDateMethod()
                + ")");
        LOG.debug("insertHostProtocol sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostFirewallInfoCount(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(" COUNT(1) ")
                .append(" FROM MD_HOST_FIREWALL HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_FIREWALL M ON HM.FIREWALL_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        LOG.debug("getHostFirewallInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostFirewallInfo(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(
                "HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.FIREWALL_ID AS METRIC_ID,M.FIREWALL_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,")
                .append("C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,HM.SCRIPT_PARAM,")
                .append("HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,HM.STATE,P2.DESCRIPTION AS statename")
                .append(" FROM MD_HOST_FIREWALL HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_FIREWALL M ON HM.FIREWALL_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        strb.append(" order by HM.ID");
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getHostFirewallInfo sql = {}", sql);
        return sql;
    }

    public String getHostThirdpartyInfoCount(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(" COUNT(1) ")
                .append(" FROM MD_HOST_THIRDPARTY HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_THIRDPARTY M ON HM.THIRDPARTY_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        LOG.debug("getHostThirdpartyInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostThirdpartyInfo(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(
                "HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.THIRDPARTY_ID AS METRIC_ID,M.THIRDPARTY_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,")
                .append("C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,HM.SCRIPT_PARAM,")
                .append("HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,HM.STATE,P2.DESCRIPTION AS statename")
                .append(" FROM MD_HOST_THIRDPARTY HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_THIRDPARTY M ON HM.THIRDPARTY_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        strb.append(" order by HM.ID");
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getHostThirdpartyInfo sql = {}", sql);
        return sql;
    }

    public String getHostProtocolInfoCount(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(" COUNT(1) ")
                .append(" FROM MD_HOST_PROTOCOL HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_COLLECT_PROTOCOL M ON HM.PROTOCOL_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        LOG.debug("getHostProtocolInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostProtocolInfo(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        strb.append("SELECT ").append(
                "HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.PROTOCOL_ID AS METRIC_ID,M.PROTOCOL_NAME AS METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,")
                .append("C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,HM.SCRIPT_PARAM,")
                .append("HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,HM.STATE,P2.DESCRIPTION AS statename")
                .append(" FROM MD_HOST_PROTOCOL HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_COLLECT_PROTOCOL M ON HM.PROTOCOL_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        strb.append(" order by HM.ID");
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getHostProtocolInfo sql = {}", sql);
        return sql;
    }

    public String getHostFirewallInfoByIds(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        strb.append(
                "SELECT ID,HOST_ID,FIREWALL_ID AS METRIC_ID,SCRIPT,SCRIPT_PARAM,STATE FROM MD_HOST_FIREWALL WHERE ID ");
        if (hostMetricidArray.length == 1) {
            strb.append(" = '" + hostMetricidArray[0] + "'");
        } else {
            strb.append("IN (");
            for (int i = 0; i < hostMetricidArray.length; i++) {
                String id = hostMetricidArray[i];
                strb.append("'" + id + "'");
                if (i < hostMetricidArray.length - 1) {
                    strb.append(",");
                }
            }
            strb.append(")");
        }
        LOG.debug("getHostFirewallInfoByIds  sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostThirdpartyInfoByIds(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        strb.append(
                "SELECT ID,HOST_ID,THIRDPARTY_ID AS METRIC_ID,SCRIPT,SCRIPT_PARAM,STATE FROM MD_HOST_THIRDPARTY WHERE ID ");
        if (hostMetricidArray.length == 1) {
            strb.append(" = '" + hostMetricidArray[0] + "'");
        } else {
            strb.append("IN (");
            for (int i = 0; i < hostMetricidArray.length; i++) {
                String id = hostMetricidArray[i];
                strb.append("'" + id + "'");
                if (i < hostMetricidArray.length - 1) {
                    strb.append(",");
                }
            }
            strb.append(")");
        }
        LOG.debug("getHostThirdpartyInfoByIds  sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostProtocolInfoByIds(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        strb.append(
                "SELECT ID,HOST_ID,PROTOCOL_ID AS METRIC_ID,SCRIPT,SCRIPT_PARAM,STATE FROM MD_HOST_PROTOCOL WHERE ID ");
        if (hostMetricidArray.length == 1) {
            strb.append(" = '" + hostMetricidArray[0] + "'");
        } else {
            strb.append("IN (");
            for (int i = 0; i < hostMetricidArray.length; i++) {
                String id = hostMetricidArray[i];
                strb.append("'" + id + "'");
                if (i < hostMetricidArray.length - 1) {
                    strb.append(",");
                }
            }
            strb.append(")");
        }
        LOG.debug("getHostProtocolInfoByIds  sql = {}", strb.toString());
        return strb.toString();
    }

    public String modifyHostFirewall(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append("UPDATE MD_HOST_FIREWALL SET CYCLE_ID=" + hostMetric.getCycle_id())
                .append(",STATE=" + hostMetric.getState() + "," + "SCRIPT='"
                        + hostMetric.getScript() + "'");
        if (!StringUtils.isEmpty(hostMetric.getScript_param())) {
            strb.append(
                    ",SCRIPT_PARAM='" + StringUtil.SqlFilter(hostMetric.getScript_param()) + "'");
        } else {
            strb.append(",SCRIPT_PARAM=null");
        }
        strb.append(",SCRIPT_RETURN_TYPE=" + hostMetric.getScript_return_type());
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE ID='" + hostMetric.getId() + "'");
        LOG.debug("modifyHostFirewall sql = {}", strb.toString());
        return strb.toString();
    }

    public String modifyHostThirdparty(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append("UPDATE MD_HOST_THIRDPARTY SET CYCLE_ID=" + hostMetric.getCycle_id())
                .append(",STATE=" + hostMetric.getState() + "," + "SCRIPT='"
                        + hostMetric.getScript() + "'");
        if (!StringUtils.isEmpty(hostMetric.getScript_param())) {
            strb.append(
                    ",SCRIPT_PARAM='" + StringUtil.SqlFilter(hostMetric.getScript_param()) + "'");
        } else {
            strb.append(",SCRIPT_PARAM=null");
        }
        strb.append(",SCRIPT_RETURN_TYPE=" + hostMetric.getScript_return_type());
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE ID='" + hostMetric.getId() + "'");
        LOG.debug("modifyHostThirdparty sql = {}", strb.toString());
        return strb.toString();
    }

    public String modifyHostProtocol(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append("UPDATE MD_HOST_PROTOCOL SET CYCLE_ID=" + hostMetric.getCycle_id())
                .append(",STATE=" + hostMetric.getState() + "," + "SCRIPT='"
                        + hostMetric.getScript() + "'");
        if (!StringUtils.isEmpty(hostMetric.getScript_param())) {
            strb.append(
                    ",SCRIPT_PARAM='" + StringUtil.SqlFilter(hostMetric.getScript_param()) + "'");
        } else {
            strb.append(",SCRIPT_PARAM=null");
        }
        strb.append(",SCRIPT_RETURN_TYPE=" + hostMetric.getScript_return_type());
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE ID='" + hostMetric.getId() + "'");
        LOG.debug("modifyHostProtocol sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 更新状态
     *
     * @param parameters
     * @return
     */
    public String updateHostFirewallState(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] idArray = (String[]) parameters.get("idArray");
        Integer state = (Integer) parameters.get("state");
        strb.append("UPDATE MD_HOST_FIREWALL SET STATE=" + state + ",UPDATE_TIME="
                + DbSqlUtil.getDateMethod() + " WHERE ID IN (");
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            strb.append("'" + id + "'");
            if (i < idArray.length - 1) {
                strb.append(",");
            }
        }
        strb.append(")");
        if (state.intValue() == ConstantUtill.HOST_METRIC_STATE_DELETE.intValue()) {
            // 解绑操作
            strb.append(" AND STATE !=" + state);
        }
        LOG.debug("updateHostMetricState  sql = {}", strb.toString());
        return strb.toString();
    }

    public String updateHostThirdpartyState(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] idArray = (String[]) parameters.get("idArray");
        Integer state = (Integer) parameters.get("state");
        strb.append("UPDATE MD_HOST_THIRDPARTY SET STATE=" + state + ",UPDATE_TIME="
                + DbSqlUtil.getDateMethod() + " WHERE ID IN (");
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            strb.append("'" + id + "'");
            if (i < idArray.length - 1) {
                strb.append(",");
            }
        }
        strb.append(")");
        if (state.intValue() == ConstantUtill.HOST_METRIC_STATE_DELETE.intValue()) {
            // 解绑操作
            strb.append(" AND STATE !=" + state);
        }
        LOG.debug("updateHostMetricState  sql = {}", strb.toString());
        return strb.toString();
    }

    public String updateHostProtocolState(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] idArray = (String[]) parameters.get("idArray");
        Integer state = (Integer) parameters.get("state");
        strb.append("UPDATE MD_HOST_PROTOCOL SET STATE=" + state + ",UPDATE_TIME="
                + DbSqlUtil.getDateMethod() + " WHERE ID IN (");
        for (int i = 0; i < idArray.length; i++) {
            String id = idArray[i];
            strb.append("'" + id + "'");
            if (i < idArray.length - 1) {
                strb.append(",");
            }
        }
        strb.append(")");
        if (state.intValue() == ConstantUtill.HOST_METRIC_STATE_DELETE.intValue()) {
            // 解绑操作
            strb.append(" AND STATE !=" + state);
        }
        LOG.debug("updateHostMetricState  sql = {}", strb.toString());
        return strb.toString();
    }
}
