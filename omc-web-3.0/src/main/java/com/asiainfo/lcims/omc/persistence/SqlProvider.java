package com.asiainfo.lcims.omc.persistence;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.asiainfo.lcims.omc.model.configmanage.*;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.boot.JettyServerConf;
import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

/**
 * 复杂sql
 *
 * @author yuboping
 *
 */
public class SqlProvider {

    private static final Logger LOG = LoggerFactory.make();
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    private static String databaseType = CommonInit.BUSCONF.getDbName();
    public static final JettyServerConf conf = new JettyServerConf();

    /**
     * 查询指标信息
     *
     * @param parameters
     * @return
     */
    public String getMdMetric(Map<String, Object> parameters) {
        MdMetric mdMetric = (MdMetric) parameters.get("mdMetric");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT M.ID,M.METRIC_IDENTITY,M.METRIC_NAME,M.CYCLE_ID,M.SCRIPT,M.SCRIPT_PARAM,M.SCRIPT_RETURN_TYPE,M.METRIC_TYPE,"
                + "M.DESCRIPTION,C.CYCLENAME AS CYCLENAME,MT.METRIC_TYPE_NAME AS MDPARAMDESCRIPTION,P.DESCRIPTION AS METRIC_TYPE_NAME,P1.DESCRIPTION  AS SERVER_TYPE "
                + "FROM MD_METRIC M LEFT JOIN MD_COLL_CYCLE C ON (M.CYCLE_ID=C.CYCLEID) LEFT JOIN MD_METRIC_TYPE MT ON (M.METRIC_TYPE=MT.ID) "
                + "LEFT JOIN MD_PARAM P ON (M.SCRIPT_RETURN_TYPE=P.CODE AND P.TYPE=7) LEFT JOIN MD_PARAM P1 ON (M.SERVER_TYPE = P1.CODE AND P1.TYPE = 106)  WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdMetric.getMetric_name())) {
            strb.append(" AND M.METRIC_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdMetric.getMetric_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdMetric.getMetric_type())) {
            strb.append(" AND M.METRIC_TYPE='").append(mdMetric.getMetric_type()).append("'");
        }
        if (!StringUtils.isEmpty(mdMetric.getMetric_identity())) {
            strb.append(" AND M.METRIC_IDENTITY='")
                    .append(StringUtil.SqlFilter(mdMetric.getMetric_identity())).append("'");
        }
        if (!StringUtils.isEmpty(mdMetric.getId())) {
            strb.append(" AND M.ID='").append(mdMetric.getId()).append("'");
        }
        LOG.debug("getMdMetric sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询指标信息(采集机协议插件管理)
     *
     * @param parameters
     * @return
     */
    public String getMdCollectAgreement(Map<String, Object> parameters) {
        MdCollectAgreement mdCollectAgreement = (MdCollectAgreement) parameters.get("mdCollectAgreement");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT M.ID,M.PROTOCOL_IDENTITY,M.PROTOCOL_NAME,M.CYCLE_ID,M.SCRIPT,M.SCRIPT_PARAM,M.SCRIPT_RETURN_TYPE,M.PROTOCOL_TYPE,"
                        + "M.DESCRIPTION,C.CYCLENAME AS CYCLENAME,MT.METRIC_TYPE_NAME AS MDPARAMDESCRIPTION,P.DESCRIPTION AS METRIC_TYPE_NAME,P1.DESCRIPTION  AS SERVER_TYPE "
                        + "FROM MD_COLLECT_PROTOCOL M LEFT JOIN MD_COLL_CYCLE C ON (M.CYCLE_ID=C.CYCLEID) LEFT JOIN MD_METRIC_TYPE MT ON (M.PROTOCOL_TYPE=MT.ID) "
                        + "LEFT JOIN MD_PARAM P ON (M.SCRIPT_RETURN_TYPE=P.CODE AND P.TYPE=7) LEFT JOIN MD_PARAM P1 ON (M.SERVER_TYPE = P1.CODE AND P1.TYPE = 106)  WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdCollectAgreement.getProtocol_name())) {
            strb.append(" AND M.PROTOCOL_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdCollectAgreement.getProtocol_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdCollectAgreement.getProtocol_type())) {
            strb.append(" AND M.PROTOCOL_TYPE='").append(mdCollectAgreement.getProtocol_type()).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectAgreement.getProtocol_identity())) {
            strb.append(" AND M.PROTOCOL_IDENTITY='")
                    .append(StringUtil.SqlFilter(mdCollectAgreement.getProtocol_identity())).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectAgreement.getId())) {
            strb.append(" AND M.ID='").append(mdCollectAgreement.getId()).append("'");
        }
        LOG.debug("getMdCollectAgreement sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdMetricListByIds @Description: TODO(获取指标ids) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdMetricListByIds(Map<String, Object> parameters) {
        String ids = (String) parameters.get("ids");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,METRIC_TYPE,DESCRIPTION FROM MD_METRIC WHERE ID IN (");
        strb.append(ids).append(")");
        LOG.debug("getMdMetricListByIds sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getChartNamePageChart @Description: TODO(获取图表名称) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getChartNamePageChart(Map<String, Object> parameters) {
        String URL = (String) parameters.get("URL");
        String metric_id = (String) parameters.get("metric_id");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT mpc.URL, mpc.CHART_NAME, mcd.METRIC_IDS FROM MD_PAGE_CHARTS mpc LEFT JOIN MD_CHART_DATASET mcd ON mcd.CHART_NAME = mpc.CHART_NAME WHERE mpc.URL = '");
        strb.append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(URL)))
                .append("' AND mcd.METRIC_IDS  LIKE '%");
        strb.append(DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(metric_id))).append("%'");
        LOG.debug("getChartNamePageChart sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: updateMdMetric @Description: TODO(更新指标) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdMetric(Map<String, Object> parameters) {
        MdMetric mdMetric = (MdMetric) parameters.get("mdMetric");
        StringBuilder strb = new StringBuilder("UPDATE MD_METRIC SET ID = ID ");
        if (null != mdMetric.getMetric_identity()) {
            strb.append(",METRIC_IDENTITY='")
                    .append(StringUtil.SqlFilter(mdMetric.getMetric_identity())).append("' ");
        }
        if (null != mdMetric.getMetric_name()) {
            strb.append(",METRIC_NAME='").append(StringUtil.SqlFilter(mdMetric.getMetric_name()))
                    .append("' ");
        }
        if (null != mdMetric.getCycle_id()) {
            strb.append(",CYCLE_ID='").append(mdMetric.getCycle_id()).append("' ");
        }
        if (null != mdMetric.getScript()) {
            strb.append(",SCRIPT='").append(StringUtil.SqlFilter(mdMetric.getScript()))
                    .append("' ");
        }
        if (null != mdMetric.getScript_param()) {
            strb.append(",SCRIPT_PARAM='").append(StringUtil.SqlFilter(mdMetric.getScript_param()))
                    .append("' ");
        }
        if (null != mdMetric.getScript_return_type()) {
            strb.append(",SCRIPT_RETURN_TYPE='").append(mdMetric.getScript_return_type())
                    .append("' ");
        }
        if (null != mdMetric.getServer_type()) {
            strb.append(",SERVER_TYPE='").append(mdMetric.getServer_type())
                    .append("' ");
        }
        if (null != mdMetric.getMetric_type()) {
            strb.append(",METRIC_TYPE='").append(mdMetric.getMetric_type()).append("' ");
        }
        if (null != mdMetric.getDescription()) {
            strb.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdMetric.getDescription()))
                    .append("' ");
        }
        strb.append("WHERE ID='").append(mdMetric.getId()).append("'");
        LOG.debug("updateMdMetric sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *采集机协议插件管理
     * @Title: updateMdCollectAgreement @Description: TODO(更新指标) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdCollectAgreement(Map<String, Object> parameters) {
        MdCollectAgreement mdCollectAgreement = (MdCollectAgreement) parameters.get("mdCollectAgreement");
        StringBuilder strb = new StringBuilder("UPDATE MD_COLLECT_PROTOCOL SET ID = ID ");
        if (null != mdCollectAgreement.getProtocol_identity()) {
            strb.append(",PROTOCOL_IDENTITY='")
                    .append(StringUtil.SqlFilter(mdCollectAgreement.getProtocol_identity())).append("' ");
        }
        if (null != mdCollectAgreement.getProtocol_name()) {
            strb.append(",PROTOCOL_NAME='").append(StringUtil.SqlFilter(mdCollectAgreement.getProtocol_name()))
                    .append("' ");
        }
        if (null != mdCollectAgreement.getCycle_id()) {
            strb.append(",CYCLE_ID='").append(mdCollectAgreement.getCycle_id()).append("' ");
        }
        if (null != mdCollectAgreement.getScript()) {
            strb.append(",SCRIPT='").append(StringUtil.SqlFilter(mdCollectAgreement.getScript()))
                    .append("' ");
        }
        if (null != mdCollectAgreement.getScript_param()) {
            strb.append(",SCRIPT_PARAM='").append(StringUtil.SqlFilter(mdCollectAgreement.getScript_param()))
                    .append("' ");
        }
        if (null != mdCollectAgreement.getScript_return_type()) {
            strb.append(",SCRIPT_RETURN_TYPE='").append(mdCollectAgreement.getScript_return_type())
                    .append("' ");
        }
        if (null != mdCollectAgreement.getServer_type()) {
            strb.append(",SERVER_TYPE='").append(mdCollectAgreement.getServer_type())
                    .append("' ");
        }
        if (null != mdCollectAgreement.getProtocol_type()) {
            strb.append(",PROTOCOL_TYPE='").append(mdCollectAgreement.getProtocol_type()).append("' ");
        }
        if (null != mdCollectAgreement.getDescription()) {
            strb.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdCollectAgreement.getDescription()))
                    .append("' ");
        }
        strb.append("WHERE ID='").append(mdCollectAgreement.getId()).append("'");
        LOG.debug("updateMdCollectAgreement sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: getMdMetricType @Description: TODO(获取指标类型) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdMetricType(Map<String, Object> parameters) {
        MdMetricType mdMetricType = (MdMetricType) parameters.get("mdMetricType");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT ID,METRIC_TYPE_NAME,DESCRIPTION FROM MD_METRIC_TYPE WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdMetricType.getMetric_type_name())) {
            strb.append(" AND METRIC_TYPE_NAME LIKE '%").append(DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdMetricType.getMetric_type_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdMetricType.getDescription())) {
            strb.append(" AND DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdMetricType.getDescription())).append("'");
        }
        if (!StringUtils.isEmpty(mdMetricType.getId())) {
            strb.append(" AND ID='").append(mdMetricType.getId()).append("'");
        }
        LOG.debug("getMdMetricType sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdMetricTypeTotalCount @Description:
     *         TODO(获取指标类型) @param @param parameters @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String getMdMetricTypeTotalCount(Map<String, Object> parameters) {
        MdMetricType mdMetricType = (MdMetricType) parameters.get("mdMetricType");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(*) FROM MD_METRIC_TYPE WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdMetricType.getMetric_type_name())) {
            strb.append(" AND METRIC_TYPE_NAME LIKE '%").append(DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdMetricType.getMetric_type_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdMetricType.getDescription())) {
            strb.append(" AND DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdMetricType.getDescription())).append("'");
        }
        if (!StringUtils.isEmpty(mdMetricType.getId())) {
            strb.append(" AND ID='").append(mdMetricType.getId()).append("'");
        }
        LOG.debug("getMdMetricTypeTotalCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdMetricType @Description: TODO(获取指标类型) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdMetricTypePage(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        String sql = getMdMetricType(parameters);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getMdMetricTypePage sql = {}", sql);
        return sql;
    }

    /**
     *
     * @Title: updateMdMetricType @Description: TODO(更新指标类型) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdMetricType(Map<String, Object> parameters) {
        MdMetricType mdMetricType = (MdMetricType) parameters.get("mdMetricType");
        StringBuilder strb = new StringBuilder("UPDATE MD_METRIC_TYPE SET ID = ID ");
        if (null != mdMetricType.getMetric_type_name()) {
            strb.append(",METRIC_TYPE_NAME='")
                    .append(StringUtil.SqlFilter(mdMetricType.getMetric_type_name())).append("' ");
        }
        if (null != mdMetricType.getDescription()) {
            strb.append(",DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdMetricType.getDescription())).append("' ");
        }
        strb.append("WHERE ID='").append(mdMetricType.getId()).append("'");
        LOG.debug("updateMdMetricType sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: getMdNode @Description: TODO(获取节点) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdNode(Map<String, Object> parameters) {
        MdNode mdNode = (MdNode) parameters.get("mdNode");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT ID,NODE_NAME,DESCRIPTION FROM MD_NODE WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdNode.getNode_name())) {
            strb.append(" AND NODE_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdNode.getNode_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdNode.getDescription())) {
            strb.append(" AND DESCRIPTION='").append(StringUtil.SqlFilter(mdNode.getDescription()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdNode.getId())) {
            strb.append(" AND ID='").append(mdNode.getId()).append("'");
        }
        LOG.debug("getMdNode sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: updateMdNode @Description: TODO(更新节点) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdNode(Map<String, Object> parameters) {
        MdNode mdNode = (MdNode) parameters.get("mdNode");
        StringBuilder strb = new StringBuilder("UPDATE MD_NODE SET ID = ID ");
        if (null != mdNode.getNode_name()) {
            strb.append(",NODE_NAME='").append(StringUtil.SqlFilter(mdNode.getNode_name()))
                    .append("' ");
        }
        if (null != mdNode.getDescription()) {
            strb.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdNode.getDescription()))
                    .append("' ");
        }
        strb.append("WHERE ID='").append(mdNode.getId()).append("'");
        LOG.debug("updateMdNode sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: getMdProcess @Description: TODO(获取进程) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdProcess(Map<String, Object> parameters) {
        MdProcess mdProcess = (MdProcess) parameters.get("mdProcess");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT PROCESS_ID,PROCESS_NAME,PROCESS_KEY,CREATE_TIME,UPDATE_TIME,DESCRIPTION FROM MD_PROCESS WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdProcess.getProcess_name())) {
            strb.append(" AND PROCESS_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdProcess.getProcess_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdProcess.getProcess_key())) {
            strb.append(" AND PROCESS_KEY LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdProcess.getProcess_key())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdProcess.getCreate_time())) {
            strb.append(" AND CREATE_TIME='").append(mdProcess.getCreate_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getUpdate_time())) {
            strb.append(" AND UPDATE_TIME='").append(mdProcess.getUpdate_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getDescription())) {
            strb.append(" AND DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdProcess.getDescription())).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getProcess_id())) {
            strb.append(" AND PROCESS_ID='").append(mdProcess.getProcess_id()).append("'");
        }
        LOG.debug("getMdProcess sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdProcessTotalCount @Description: TODO(获取进程) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdProcessTotalCount(Map<String, Object> parameters) {
        MdProcess mdProcess = (MdProcess) parameters.get("mdProcess");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(*) FROM MD_PROCESS WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdProcess.getProcess_name())) {
            strb.append(" AND PROCESS_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdProcess.getProcess_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdProcess.getProcess_key())) {
            strb.append(" AND PROCESS_KEY LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdProcess.getProcess_key())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdProcess.getCreate_time())) {
            strb.append(" AND CREATE_TIME='").append(mdProcess.getCreate_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getUpdate_time())) {
            strb.append(" AND UPDATE_TIME='").append(mdProcess.getUpdate_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getDescription())) {
            strb.append(" AND DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdProcess.getDescription())).append("'");
        }
        if (!StringUtils.isEmpty(mdProcess.getProcess_id())) {
            strb.append(" AND PROCESS_ID='").append(mdProcess.getProcess_id()).append("'");
        }
        LOG.debug("getMdProcessTotalCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdProcess @Description: TODO(获取进程) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdProcessPage(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        String sql = getMdProcess(parameters);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getMdProcessPage sql = {}", sql);
        return sql;
    }

    /**
     *
     * @Title: updateMdProcess @Description: TODO(更新进程) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdProcess(Map<String, Object> parameters) {
        MdProcess mdProcess = (MdProcess) parameters.get("mdProcess");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder strb = new StringBuilder("UPDATE MD_PROCESS SET PROCESS_ID = PROCESS_ID ");
        if (null != mdProcess.getProcess_name()) {
            strb.append(",PROCESS_NAME='").append(StringUtil.SqlFilter(mdProcess.getProcess_name()))
                    .append("' ");
        }
        if (null != mdProcess.getProcess_key()) {
            strb.append(",PROCESS_KEY='").append(StringUtil.SqlFilter(mdProcess.getProcess_key()))
                    .append("' ");
        }
        if (null != mdProcess.getCreate_time()) {
            strb.append(",CREATE_TIME='").append(mdProcess.getCreate_time()).append("' ");
        }
        if (null != mdProcess.getUpdate_time()) {
            if (MYSQL.equals(databaseType)) {
                strb.append(",UPDATE_TIME='").append(sdf.format(mdProcess.getUpdate_time()))
                        .append("' ");
            } else if (ORACLE.equals(databaseType)) {
                strb.append(",UPDATE_TIME= to_date('")
                        .append(sdf.format(mdProcess.getUpdate_time()))
                        .append("' ,'yyyy-mm-dd hh24:mi:ss')");
            }
        }
        if (null != mdProcess.getDescription()) {
            strb.append(",DESCRIPTION='").append(StringUtil.SqlFilter(mdProcess.getDescription()))
                    .append("' ");
        }
        strb.append("WHERE PROCESS_ID='").append(mdProcess.getProcess_id()).append("'");
        LOG.debug("updateMdProcess sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Title: getMdBusinessHost @Description: TODO(获取业务关联主机配置) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdBusinessHost(Map<String, Object> parameters) {
        MdBusinessHost mdBusinessHost = (MdBusinessHost) parameters.get("mdBusinessHost");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT bh.ID,bh.NAME,bh.HOSTID,h.ADDR AS HOSTIP FROM MD_BUSINESS_HOST bh LEFT JOIN MON_HOST h ON (h.HOSTID= bh.HOSTID) WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdBusinessHost.getName())) {
            strb.append(" AND bh.NAME='").append(StringUtil.SqlFilter(mdBusinessHost.getName()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdBusinessHost.getHostid())) {
            strb.append(" AND bh.HOSTID='").append(StringUtil.SqlFilter(mdBusinessHost.getHostid()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdBusinessHost.getHostip())) {
            strb.append(" AND h.ADDR='").append(StringUtil.SqlFilter(mdBusinessHost.getHostip()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdBusinessHost.getId())) {
            strb.append(" AND bh.ID='").append(mdBusinessHost.getId()).append("'");
        }
        strb.append(" ORDER BY h.NODEID,h.HOSTTYPE,h.HOSTNAME");
        LOG.debug("getMdBusinessHost sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdBusinessHostTotalCount @Description:
     *         TODO(获取业务关联主机配置) @param @param parameters @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getMdBusinessHostTotalCount(Map<String, Object> parameters) {
        MdBusinessHost mdBusinessHost = (MdBusinessHost) parameters.get("mdBusinessHost");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(*) FROM MD_BUSINESS_HOST WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdBusinessHost.getName())) {
            strb.append(" AND NAME='").append(StringUtil.SqlFilter(mdBusinessHost.getName()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdBusinessHost.getHostid())) {
            strb.append(" AND HOSTID='").append(StringUtil.SqlFilter(mdBusinessHost.getHostid()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdBusinessHost.getId())) {
            strb.append(" AND ID='").append(mdBusinessHost.getId()).append("'");
        }
        LOG.debug("getMdBusinessHostTotalCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMdBusinessHost @Description: TODO(获取业务关联主机配置) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getMdBusinessHostPage(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        String sql = getMdBusinessHost(parameters);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getMdBusinessHostPage sql = {}", sql);
        return sql;
    }

    /**
     *
     * @Title: updateMdBusinessHost @Description: TODO(更新业务关联主机配置) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdBusinessHost(Map<String, Object> parameters) {
        MdBusinessHost mdBusinessHost = (MdBusinessHost) parameters.get("mdBusinessHost");
        StringBuilder strb = new StringBuilder("UPDATE MD_BUSINESS_HOST SET ID = ID ");
        if (null != mdBusinessHost.getName()) {
            strb.append(",NAME='").append(StringUtil.SqlFilter(mdBusinessHost.getName()))
                    .append("' ");
        }
        if (null != mdBusinessHost.getHostid()) {
            strb.append(",HOSTID='").append(StringUtil.SqlFilter(mdBusinessHost.getHostid()))
                    .append("' ");
        }
        strb.append("WHERE ID='").append(mdBusinessHost.getId()).append("'");
        LOG.debug("updateMdBusinessHost sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询告警规则信息
     *
     * @param parameters
     * @return
     */
    public String getMdAlarmRule(Map<String, Object> parameters) {
        MdAlarmRule mdAlarmRule = (MdAlarmRule) parameters.get("mdAlarmRule");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT mar.RULE_ID, mar.NAME, mar.URL, mar.DIMENSION_TYPE, mar.CHART_NAME, mar.METRIC_ID, mar.ATTR, mar.ALARM_LEVEL, mar.ALARM_RULE, mar.MODES, mar.ALARMMSG, mar.DIMENSION1, mar.DIMENSION2, mar.DIMENSION3, mar.CREATE_TIME, mar.UPDATE_TIME, mm.METRIC_NAME, mcd.CHART_TITLE, mar.REPORT_RULE, mar.ALARM_TYPE, P.DESCRIPTION AS ALARM_TYPE_NAME FROM MD_ALARM_RULE mar LEFT JOIN MD_METRIC mm ON (mar.METRIC_ID = mm.ID) LEFT JOIN MD_CHART_DETAIL mcd ON (mar.CHART_NAME = mcd.CHART_NAME) LEFT JOIN MD_PARAM P ON ( mar.ALARM_TYPE = P.CODE AND P.TYPE = 40 ) WHERE 1 = 1 ");
        if (!StringUtils.isEmpty(mdAlarmRule.getName())) {
            strb.append(" AND mar.NAME='").append(mdAlarmRule.getName()).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getUrl())) {
            strb.append(" AND mar.URL='").append(StringUtil.SqlFilter(mdAlarmRule.getUrl()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getDimension_type())) {
            strb.append(" AND mar.DIMENSION_TYPE='").append(mdAlarmRule.getDimension_type())
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getChart_name())) {
            strb.append(" AND mar.CHART_NAME='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getChart_name())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getMetric_id())) {
            strb.append(" AND mar.METRIC_ID='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getMetric_id())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getAttr())) {
            strb.append(" AND mar.ATTR='").append(StringUtil.SqlFilter(mdAlarmRule.getAttr()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getAlarm_level())) {
            strb.append(" AND mar.ALARM_LEVEL='").append(mdAlarmRule.getAlarm_level()).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getAlarm_rule())) {
            strb.append(" AND mar.ALARM_RULE='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getAlarm_rule())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getModes())) {
            strb.append(" AND mar.MODES='").append(StringUtil.SqlFilter(mdAlarmRule.getModes()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getAlarmmsg())) {
            strb.append(" AND mar.ALARMMSG='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getAlarmmsg())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getDimension1())) {
            strb.append(" AND mar.DIMENSION1='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getDimension1())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getDimension2())) {
            strb.append(" AND mar.DIMENSION2='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getDimension2())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getDimension3())) {
            strb.append(" AND mar.DIMENSION3='")
                    .append(StringUtil.SqlFilter(mdAlarmRule.getDimension3())).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmRule.getRule_id())) {
            strb.append(" AND mar.RULE_ID='").append(mdAlarmRule.getRule_id()).append("'");
        }
        LOG.debug("getMdAlarmRule sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: insertMdAlarmRule @Description: TODO(插入告警规则) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String insertMdAlarmRule(Map<String, Object> parameters) {
        MdAlarmRule mdAlarmRule = (MdAlarmRule) parameters.get("mdAlarmRule");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MD_ALARM_RULE ( RULE_ID,NAME,URL,DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,ALARM_RULE,MODES,ALARMMSG,DIMENSION1,DIMENSION2,DIMENSION3,CREATE_TIME,UPDATE_TIME,REPORT_RULE,ALARM_TYPE ) VALUES('")
                .append(mdAlarmRule.getRule_id()).append("','").append(mdAlarmRule.getName())
                .append("','").append(mdAlarmRule.getUrl()).append("','")
                .append(mdAlarmRule.getDimension_type()).append("','")
                .append(mdAlarmRule.getChart_name()).append("','")
                .append(mdAlarmRule.getMetric_id()).append("','")
                .append(StringUtil.SqlFilter(mdAlarmRule.getAttr())).append("','")
                .append(mdAlarmRule.getAlarm_level()).append("','")
                .append(mdAlarmRule.getAlarm_rule()).append("','").append(mdAlarmRule.getModes())
                .append("','").append(mdAlarmRule.getAlarmmsg()).append("','")
                .append(mdAlarmRule.getDimension1()).append("','")
                .append(mdAlarmRule.getDimension2()).append("','")
                .append(mdAlarmRule.getDimension3()).append("',").append(DbSqlUtil.getDateMethod())
                .append(",").append(DbSqlUtil.getDateMethod()).append(",'")
                .append(mdAlarmRule.getReport_rule()).append("','")
                .append(mdAlarmRule.getAlarm_type()).append("')");
        return strb.toString();
    }

    /**
     *
     * @Title: updateMdAlarmRule @Description: TODO(更新告警规则) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdAlarmRule(Map<String, Object> parameters) {
        MdAlarmRule mdAlarmRule = (MdAlarmRule) parameters.get("mdAlarmRule");
        StringBuilder strb = new StringBuilder("UPDATE MD_ALARM_RULE SET RULE_ID = RULE_ID ");
        if (null != mdAlarmRule.getName()) {
            strb.append(",NAME='").append(StringUtil.SqlFilter(mdAlarmRule.getName())).append("' ");
        }
        if (null != mdAlarmRule.getUrl()) {
            strb.append(",URL='").append(StringUtil.SqlFilter(mdAlarmRule.getUrl())).append("' ");
        }
        if (null != mdAlarmRule.getDimension_type()) {
            strb.append(",DIMENSION_TYPE='").append(mdAlarmRule.getDimension_type()).append("' ");
        }
        if (null != mdAlarmRule.getChart_name()) {
            strb.append(",CHART_NAME='").append(StringUtil.SqlFilter(mdAlarmRule.getChart_name()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getMetric_id()) {
            strb.append(",METRIC_ID='").append(StringUtil.SqlFilter(mdAlarmRule.getMetric_id()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getAttr()) {
            strb.append(",ATTR='").append(mdAlarmRule.getAttr()).append("' ");
        }
        if (null != mdAlarmRule.getAlarm_level()) {
            strb.append(",ALARM_LEVEL='").append(mdAlarmRule.getAlarm_level()).append("' ");
        }
        if (null != mdAlarmRule.getAlarm_rule()) {
            strb.append(",ALARM_RULE='").append(StringUtil.SqlFilter(mdAlarmRule.getAlarm_rule()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getModes()) {
            strb.append(",MODES='").append(StringUtil.SqlFilter(mdAlarmRule.getModes()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getAlarmmsg()) {
            strb.append(",ALARMMSG='").append(StringUtil.SqlFilter(mdAlarmRule.getAlarmmsg()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getDimension1()) {
            strb.append(",DIMENSION1='").append(StringUtil.SqlFilter(mdAlarmRule.getDimension1()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getDimension2()) {
            strb.append(",DIMENSION2='").append(StringUtil.SqlFilter(mdAlarmRule.getDimension2()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getDimension3()) {
            strb.append(",DIMENSION3='").append(StringUtil.SqlFilter(mdAlarmRule.getDimension3()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getReport_rule()) {
            strb.append(",REPORT_RULE='").append(StringUtil.SqlFilter(mdAlarmRule.getReport_rule()))
                    .append("' ");
        }
        if (null != mdAlarmRule.getAlarm_type()) {
            strb.append(",ALARM_TYPE='").append(StringUtil.SqlFilter(mdAlarmRule.getAlarm_type()))
                    .append("' ");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != mdAlarmRule.getUpdate_time()) {
            if (MYSQL.equals(databaseType)) {
                strb.append(",UPDATE_TIME='").append(sdf.format(mdAlarmRule.getUpdate_time()))
                        .append("' ");
            } else if (ORACLE.equals(databaseType)) {
                strb.append(",UPDATE_TIME= to_date('")
                        .append(sdf.format(mdAlarmRule.getUpdate_time()))
                        .append("' ,'yyyy-mm-dd hh24:mi:ss')");
            }
        }
        strb.append("WHERE RULE_ID='").append(mdAlarmRule.getRule_id()).append("'");
        LOG.debug("updateMdAlarmRule sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: insertMdAlarmRuleDetail @Description:
     *         TODO(插入告警规则明细) @param @param parameters @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String insertMdAlarmRuleDetail(Map<String, Object> parameters) {
        MdAlarmRuleDetail mdAlarmRuleDetail = (MdAlarmRuleDetail) parameters
                .get("mdAlarmRuleDetail");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MD_ALARM_RULE_DETAIL ( ALARM_ID,RULE_ID,NAME,URL,DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,ALARM_RULE,MODES,ALARMMSG,DIMENSION1,DIMENSION2,DIMENSION3,CREATE_TIME,UPDATE_TIME,REPORT_RULE,ALARM_TYPE ) VALUES('")
                .append(mdAlarmRuleDetail.getAlarm_id()).append("','")
                .append(mdAlarmRuleDetail.getRule_id()).append("','")
                .append(mdAlarmRuleDetail.getName()).append("','")
                .append(mdAlarmRuleDetail.getUrl()).append("','")
                .append(mdAlarmRuleDetail.getDimension_type()).append("','")
                .append(mdAlarmRuleDetail.getChart_name()).append("','")
                .append(mdAlarmRuleDetail.getMetric_id()).append("','")
                .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAttr())).append("','")
                .append(mdAlarmRuleDetail.getAlarm_level()).append("','")
                .append(mdAlarmRuleDetail.getAlarm_rule()).append("','")
                .append(mdAlarmRuleDetail.getModes()).append("','")
                .append(mdAlarmRuleDetail.getAlarmmsg()).append("','")
                .append(mdAlarmRuleDetail.getDimension1()).append("','")
                .append(mdAlarmRuleDetail.getDimension2()).append("','")
                .append(mdAlarmRuleDetail.getDimension3()).append("',")
                .append(DbSqlUtil.getDateMethod()).append(",").append(DbSqlUtil.getDateMethod())
                .append(",'").append(mdAlarmRuleDetail.getReport_rule()).append("','")
                .append(mdAlarmRuleDetail.getAlarm_type()).append("')");
        LOG.debug("insertMdAlarmRuleDetail sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: updateMdAlarmRuleDetail @Description: TODO(告警信息细表) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String updateMdAlarmRuleDetail(Map<String, Object> parameters) {
        MdAlarmRuleDetail mdAlarmRuleDetail = (MdAlarmRuleDetail) parameters
                .get("mdAlarmRuleDetail");
        StringBuilder strb = new StringBuilder(
                "UPDATE MD_ALARM_RULE_DETAIL SET ALARM_ID = ALARM_ID ");
        if (null != mdAlarmRuleDetail.getName()) {
            strb.append(",NAME='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getName()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getUrl()) {
            strb.append(",URL='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getUrl()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension_type()) {
            strb.append(",DIMENSION_TYPE='").append(mdAlarmRuleDetail.getDimension_type())
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getChart_name()) {
            strb.append(",CHART_NAME='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getChart_name())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getMetric_id()) {
            strb.append(",METRIC_ID='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getMetric_id())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAttr()) {
            strb.append(",ATTR='").append(mdAlarmRuleDetail.getAttr()).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarm_level()) {
            strb.append(",ALARM_LEVEL='").append(mdAlarmRuleDetail.getAlarm_level()).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarm_rule()) {
            strb.append(",ALARM_RULE='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAlarm_rule())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getModes()) {
            strb.append(",MODES='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getModes()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarmmsg()) {
            strb.append(",ALARMMSG='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAlarmmsg()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension1()) {
            strb.append(",DIMENSION1='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension1())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension2()) {
            strb.append(",DIMENSION2='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension2())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension3()) {
            strb.append(",DIMENSION3='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension3())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getReport_rule()) {
            strb.append(",REPORT_RULE='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getReport_rule())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarm_type()) {
            strb.append(",ALARM_TYPE='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAlarm_type())).append("' ");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != mdAlarmRuleDetail.getUpdate_time()) {
            if (MYSQL.equals(databaseType)) {
                strb.append(",UPDATE_TIME='").append(sdf.format(mdAlarmRuleDetail.getUpdate_time()))
                        .append("' ");
            } else if (ORACLE.equals(databaseType)) {
                strb.append(",UPDATE_TIME= to_date('")
                        .append(sdf.format(mdAlarmRuleDetail.getUpdate_time()))
                        .append("' ,'yyyy-mm-dd hh24:mi:ss')");
            }
        }
        strb.append("WHERE RULE_ID='").append(mdAlarmRuleDetail.getRule_id()).append("'");
        LOG.debug("updateMdAlarmRuleDetail sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: updateMdAlarmRuleDetailByAlarmId @Description:
     *         TODO(告警信息细表) @param @param parameters @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String updateMdAlarmRuleDetailByAlarmId(Map<String, Object> parameters) {
        MdAlarmRuleDetail mdAlarmRuleDetail = (MdAlarmRuleDetail) parameters
                .get("mdAlarmRuleDetail");
        StringBuilder strb = new StringBuilder(
                "UPDATE MD_ALARM_RULE_DETAIL SET ALARM_ID = ALARM_ID ");
        if (null != mdAlarmRuleDetail.getRule_id()) {
            strb.append(",RULE_ID='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getRule_id()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getName()) {
            strb.append(",NAME='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getName()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getUrl()) {
            strb.append(",URL='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getUrl()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension_type()) {
            strb.append(",DIMENSION_TYPE='").append(mdAlarmRuleDetail.getDimension_type())
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getChart_name()) {
            strb.append(",CHART_NAME='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getChart_name())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getMetric_id()) {
            strb.append(",METRIC_ID='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getMetric_id())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAttr()) {
            strb.append(",ATTR='").append(mdAlarmRuleDetail.getAttr()).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarm_level()) {
            strb.append(",ALARM_LEVEL='").append(mdAlarmRuleDetail.getAlarm_level()).append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarm_rule()) {
            strb.append(",ALARM_RULE='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAlarm_rule())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getModes()) {
            strb.append(",MODES='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getModes()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getAlarmmsg()) {
            strb.append(",ALARMMSG='").append(StringUtil.SqlFilter(mdAlarmRuleDetail.getAlarmmsg()))
                    .append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension1()) {
            strb.append(",DIMENSION1='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension1())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension2()) {
            strb.append(",DIMENSION2='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension2())).append("' ");
        }
        if (null != mdAlarmRuleDetail.getDimension3()) {
            strb.append(",DIMENSION3='")
                    .append(StringUtil.SqlFilter(mdAlarmRuleDetail.getDimension3())).append("' ");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != mdAlarmRuleDetail.getUpdate_time()) {
            if (MYSQL.equals(databaseType)) {
                strb.append(",UPDATE_TIME='").append(sdf.format(mdAlarmRuleDetail.getUpdate_time()))
                        .append("' ");
            } else if (ORACLE.equals(databaseType)) {
                strb.append(",UPDATE_TIME= to_date('")
                        .append(sdf.format(mdAlarmRuleDetail.getUpdate_time()))
                        .append("' ,'yyyy-mm-dd hh24:mi:ss')");
            }
        }
        strb.append("WHERE ALARM_ID='").append(mdAlarmRuleDetail.getAlarm_id()).append("'");
        LOG.debug("updateMdAlarmRuleDetailByAlarmId sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: insertFileSystem @Description: TODO(插入文件系统表) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String insertFileSystem(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String date = (String) parameters.get("date");
        String metricId = (String) parameters.get("metricId");
        strb.append(
                "INSERT INTO MD_HOST_FILESYSTEM (HOST_ID, FILESYSTEM) SELECT HOST_ID, ATTR1 AS FILESYSTEM FROM METRIC_DATA_MULTI_")
                .append(date).append(" WHERE METRIC_ID = '").append(metricId)
                .append("' GROUP BY HOST_ID,METRIC_ID,ATTR1");
        LOG.debug("insertFileSystem sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询数量
     * 
     * @param parameters
     * @return
     */
    public String getHostMetricInfoCount(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        String metricid = (String) parameters.get("metricid");
        strb.append("SELECT ").append(" COUNT(1) ")
                .append(" FROM MD_HOST_METRIC HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_METRIC M ON HM.METRIC_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        if (!StringUtils.isEmpty(metricid)) {
            strb.append(" AND HM.METRIC_ID='").append(metricid).append("'");
        }
        LOG.debug("getHostMetricInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 指标主机查询数据
     *
     * @param parameters
     * @return
     */
    public String getHostMetricInfo(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String hostid = (String) parameters.get("hostid");
        String metricid = (String) parameters.get("metricid");
        strb.append("SELECT ").append(
                "HM.ID,HM.HOST_ID,H.HOSTNAME,H.ADDR,HM.METRIC_ID,M.METRIC_NAME,HM.CYCLE_ID,C.CYCLENAME,C.CRON,")
                .append("C.DESCRIPTION AS CYCLE_DESCRIPTION,HM.SCRIPT,HM.SCRIPT_PARAM,")
                .append("HM.SCRIPT_RETURN_TYPE,P1.DESCRIPTION AS RETURNTYPENAME,HM.STATE,P2.DESCRIPTION AS statename")
                .append(" FROM MD_HOST_METRIC HM LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID")
                .append(" LEFT JOIN MD_METRIC M ON HM.METRIC_ID = M.ID")
                .append(" LEFT JOIN MD_COLL_CYCLE C ON HM.CYCLE_ID = C.CYCLEID")
                .append(" LEFT JOIN MD_PARAM P1 ON HM.SCRIPT_RETURN_TYPE = P1.CODE AND P1.TYPE=7")
                .append(" LEFT JOIN MD_PARAM P2 ON HM.STATE = P2.CODE AND P2.TYPE=8")
                .append(" WHERE 1=1");
        if (!StringUtils.isEmpty(hostid)) {
            strb.append(" AND HM.HOST_ID='").append(hostid).append("'");
        }
        if (!StringUtils.isEmpty(metricid)) {
            strb.append(" AND HM.METRIC_ID='").append(metricid).append("'");
        }
        strb.append(" order by HM.ID");
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.debug("getHostMetricInfo sql = {}", sql);
        return sql;
    }

    public String getMdMetricListByMetric(Map<String, Object> parameters) {
        MdMetric mdMetric = (MdMetric) parameters.get("metric");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT H.HOSTID AS HOST_ID,H.HOSTNAME,H.ADDR,")
                .append("COALESCE(M.METRIC_NAME");
        if (!StringUtils.isEmpty(mdMetric.getMetric_name())) {
            strb.append(" ,'").append(StringUtil.SqlFilter(mdMetric.getMetric_name())).append("'");
        }
        strb.append(") AS METRIC_NAME,").append("COALESCE(HM.METRIC_ID,'").append(mdMetric.getId())
                .append("') AS METRIC_ID,").append("COALESCE(HM.CYCLE_ID,M.CYCLE_ID,")
                .append(mdMetric.getCycle_id()).append(") AS CYCLE_ID,")
                .append("COALESCE(HM.SCRIPT,M.SCRIPT");
        if (!StringUtils.isEmpty(mdMetric.getScript())) {
            strb.append(" ,'").append(StringUtil.SqlFilter(mdMetric.getScript())).append("'");
        }
        strb.append(") AS SCRIPT,COALESCE(HM.SCRIPT_PARAM,M.SCRIPT_PARAM");
        if (!StringUtils.isEmpty(mdMetric.getScript_param())) {
            strb.append(" ,'").append(StringUtil.SqlFilter(mdMetric.getScript_param())).append("'");
        }
        strb.append(") AS SCRIPT_PARAM, COALESCE(HM.SCRIPT_RETURN_TYPE,M.SCRIPT_RETURN_TYPE,")
                .append(mdMetric.getScript_return_type()).append(") AS SCRIPT_RETURN_TYPE,")
                .append("COALESCE(HM.METRIC_ID,'-1') AS FLAG")
                .append(" FROM MON_HOST H LEFT JOIN MD_HOST_METRIC HM ON HM.HOST_ID = H.HOSTID AND HM.METRIC_ID='")
                .append(mdMetric.getId()).append("'")
                .append(" LEFT JOIN MD_METRIC M ON HM.METRIC_ID = M.ID AND M.ID='")
                .append(mdMetric.getId()).append("' ORDER BY FLAG");
        LOG.debug("getMdMetricListByMetric sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdMetricListByHost(Map<String, Object> parameters) {
        MonHost host = (MonHost) parameters.get("host");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT").append(" COALESCE(HM.HOST_ID,'").append(host.getHostid())
                .append("') AS HOST_ID,").append("COALESCE(H.ADDR,'").append(host.getAddr())
                .append("') AS ADDR,").append("COALESCE(H.HOSTNAME,'").append(host.getHostname())
                .append("') AS HOSTNAME,").append("M.ID AS METRIC_ID,M.METRIC_NAME,")
                .append("COALESCE(HM.CYCLE_ID,M.CYCLE_ID) AS CYCLE_ID,")
                .append("COALESCE(HM.SCRIPT,M.SCRIPT) AS SCRIPT,")
                .append("COALESCE(HM.SCRIPT_PARAM,M.SCRIPT_PARAM) AS SCRIPT_PARAM,")
                .append("COALESCE(HM.SCRIPT_RETURN_TYPE,M.SCRIPT_RETURN_TYPE) AS SCRIPT_RETURN_TYPE,")
                .append("COALESCE(HM.HOST_ID,'-1') AS FLAG")
                .append(" FROM MD_METRIC M LEFT JOIN MD_HOST_METRIC HM ON HM.METRIC_ID = M.ID AND HM.HOST_ID='")
                .append(host.getHostid()).append("'")
                .append(" LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID AND H.HOSTID='")
                .append(host.getHostid()).append("' ORDER BY FLAG");
        LOG.debug("getMdMetricListByHost sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 批量插入主机指标配置数据
     *
     * @param parameters
     * @return
     */
    public String insertHostMetricList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        @SuppressWarnings("unchecked")
        List<MdHostMetric> list = (List<MdHostMetric>) parameters.get("list");
        strb.append(
                "INSERT INTO MD_HOST_METRIC(ID,HOST_ID,METRIC_ID,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,STATE,UPDATE_TIME)VALUES");
        for (int i = 0; i < list.size(); i++) {
            MdHostMetric hostMetric = list.get(i);
            strb.append("('" + hostMetric.getId() + "','" + hostMetric.getHost_id() + "','"
                    + hostMetric.getMetric_id() + "'," + hostMetric.getCycle_id() + ",'"
                    + hostMetric.getScript() + "','"
                    + StringUtil.SqlFilter(hostMetric.getScript_param()) + "',"
                    + hostMetric.getScript_return_type() + ",0" + "," + DbSqlUtil.getDateMethod()
                    + ")");
            if (i < list.size() - 1) {
                strb.append(",");
            }
        }
        LOG.debug("insertHostMetricList sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 更新状态
     *
     * @param parameters
     * @return
     */
    public String updateHostMetricState(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        Integer state = (Integer) parameters.get("state");
        strb.append("UPDATE MD_HOST_METRIC SET STATE=" + state + ",UPDATE_TIME="
                + DbSqlUtil.getDateMethod() + " WHERE ID IN (");
        for (int i = 0; i < hostMetricidArray.length; i++) {
            String id = hostMetricidArray[i];
            strb.append("'" + id + "'");
            if (i < hostMetricidArray.length - 1) {
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

    /**
     * 根据id数组查询 ip列表
     *
     * @param parameters
     * @return
     */
    public String getHostMetricIpList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        strb.append("SELECT H.ADDR FROM MD_HOST_METRIC HM "
                + "LEFT JOIN MON_HOST H ON HM.HOST_ID=H.HOSTID WHERE HM.ID IN (");
        for (int i = 0; i < hostMetricidArray.length; i++) {
            String id = hostMetricidArray[i];
            strb.append("'" + id + "'");
            if (i < hostMetricidArray.length - 1) {
                strb.append(",");
            }
        }
        strb.append(") GROUP BY H.ADDR");
        LOG.debug("getHostMetricIpList  sql = {}", strb.toString());
        return strb.toString();
    }

    public String getHostMetricInfoByIds(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostMetricidArray = (String[]) parameters.get("hostMetricidArray");
        strb.append(
                "SELECT ID,HOST_ID,METRIC_ID,SCRIPT,SCRIPT_PARAM,STATE FROM MD_HOST_METRIC WHERE ID ");
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
        LOG.debug("getHostMetricIpList  sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 更新状态
     *
     * @param parameters
     * @return
     */
    public String modifyHostMetric(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append("UPDATE MD_HOST_METRIC SET CYCLE_ID=" + hostMetric.getCycle_id())
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
        LOG.debug("modifyHostMetric sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询主机信息
     *
     * @param parameters
     * @return
     */
    public String getHost(Map<String, Object> parameters) {
        String hostname = (String) parameters.get("hostname");
        String addr = (String) parameters.get("addr");
        String hosttype = (String) parameters.get("hosttype");
        String nodeid = (String) parameters.get("nodeid");
        StringBuffer strb = new StringBuffer();
        if (conf.getAutoDeploySwitch()) {
            strb.append(
                    "SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME ,D.DESCRIPTION AS DEPLOY_STATUS_NAME FROM MON_HOST A LEFT JOIN MD_NODE B ON ")
                    .append("A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 LEFT JOIN MD_PARAM D ON A.DEPLOY_STATUS = D.CODE AND D.TYPE = 30 WHERE 1=1 ");
        } else {
            strb.append(
                    "SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME FROM MON_HOST A LEFT JOIN MD_NODE B ON ")
                    .append("A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 WHERE 1=1 ");
        }
        if (!StringUtils.isEmpty(hostname)) {
            String hostnameStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(hostname));
            strb.append(" AND A.HOSTNAME LIKE '%").append(hostnameStr).append("%'");
        }
        if (!StringUtils.isEmpty(addr)) {
            strb.append(" AND A.ADDR='").append(addr).append("'");
        }
        if (!StringUtils.isEmpty(hosttype)) {
            String hosttypeStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(hosttype));
            strb.append(" AND A.HOSTTYPE='").append(hosttypeStr).append("'");
        }
        if (!StringUtils.isEmpty(nodeid)) {
            String nodeidStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(nodeid));
            strb.append(" AND A.NODEID='").append(nodeidStr).append("'");
        }
        strb.append(" ORDER BY A.HOSTID DESC");
        LOG.debug("getHost sql = {}", strb);
        return strb.toString();
    }

    /**
     * 查询主机信息 采集机管理（去掉主机类型查询条件）
     *
     * @param parameters
     * @return
     */
    public String getCollectorHost(Map<String, Object> parameters) {
        String hostname = (String) parameters.get("hostname");
        String addr = (String) parameters.get("addr");
        String nodeid = (String) parameters.get("nodeid");
        StringBuffer strb = new StringBuffer();
        if (conf.getAutoDeploySwitch()) {
            strb.append(
                    "SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME ,D.DESCRIPTION AS DEPLOY_STATUS_NAME FROM MON_HOST A LEFT JOIN MD_NODE B ON ")
                    .append("A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 LEFT JOIN MD_PARAM D ON A.DEPLOY_STATUS = D.CODE AND D.TYPE = 30 WHERE C.DESCRIPTION = '采集机主机' ");
        } else {
            strb.append(
                    "SELECT A.*,B.NODE_NAME AS NODEIDNAME,C.DESCRIPTION AS HOSTTYPENAME FROM MON_HOST A LEFT JOIN MD_NODE B ON ")
                    .append("A.NODEID= B.ID  LEFT JOIN MD_PARAM C ON A.HOSTTYPE = C.CODE AND C.TYPE = 3 WHERE C.DESCRIPTION = '采集机主机' ");
        }
        if (!StringUtils.isEmpty(hostname)) {
            String hostnameStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(hostname));
            strb.append(" AND A.HOSTNAME LIKE '%").append(hostnameStr).append("%'");
        }
        if (!StringUtils.isEmpty(addr)) {
            strb.append(" AND A.ADDR='").append(addr).append("'");
        }
        if (!StringUtils.isEmpty(nodeid)) {
            String nodeidStr = DbSqlUtil.replaceSpecialStr(StringUtil.SqlFilter(nodeid));
            strb.append(" AND A.NODEID='").append(nodeidStr).append("'");
        }
        strb.append(" ORDER BY A.HOSTID DESC");
        LOG.debug("getHost sql = {}", strb);
        return strb.toString();
    }

    /**
     * 根据查询条件查询属地
     * 
     * @param parameters
     * @return
     */
    public String getArea(Map<String, Object> parameters) {
        String areano = (String) parameters.get("areano");
        String name = (String) parameters.get("name");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM MD_AREA WHERE AREANO <>'00' ");
        if (!StringUtils.isEmpty(areano)) {
            strb.append(" AND AREANO='").append(areano).append("'");
        }
        if (!StringUtils.isEmpty(name)) {
            strb.append(" AND NAME LIKE '%").append(name).append("%'");
        }
        return strb.toString();
    }

    /**
     * 批量插入主机指标配置数据
     *
     * @param parameters
     * @return
     */
    public String insertHostMetric(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostMetric hostMetric = (MdHostMetric) parameters.get("hostMetric");
        strb.append(
                "INSERT INTO MD_HOST_METRIC(ID,HOST_ID,METRIC_ID,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,STATE,UPDATE_TIME)VALUES");
        strb.append("('" + hostMetric.getId() + "','" + hostMetric.getHost_id() + "','"
                + hostMetric.getMetric_id() + "'," + hostMetric.getCycle_id() + ",'"
                + hostMetric.getScript() + "','"
                + StringUtil.SqlFilter(hostMetric.getScript_param()) + "',"
                + hostMetric.getScript_return_type() + ",0" + "," + DbSqlUtil.getDateMethod()
                + ")");
        LOG.debug("insertHostMetric sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getDayReport @Description: TODO(获取日报表数据) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getDayReport(Map<String, Object> parameters) {
        String cpuRateMetricId = (String) parameters.get("cpuRateMetricId");
        String memoryMetricId = (String) parameters.get("memoryMetricId");
        String tableName = (String) parameters.get("tableName");
        String addr = (String) parameters.get("addr");
        StringBuffer strb = new StringBuffer();
        StringBuffer strbT = new StringBuffer();
        String stime = DbSqlUtil.getTimeHSql("STIME");
        String cpumvalue = DbSqlUtil.getRoundingSql("CPUMVALUE", 2);
        String memorymvalue = DbSqlUtil.getRoundingSql("MEMORYMVALUE", 2);
        strbT.append(
                "SELECT STIME,SUM(CPUMVALUE) AS CPUMVALUE,SUM(MEMORYMVALUE) AS MEMORYMVALUE FROM ( SELECT * FROM (SELECT AVG(MVALUE) AS CPUMVALUE,NULL AS MEMORYMVALUE,STIME FROM ")
                .append(tableName)
                .append(" mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE mh.ADDR = '")
                .append(addr).append("' AND METRIC_ID = '").append(cpuRateMetricId)
                .append("' GROUP BY STIME) T1 UNION SELECT * FROM (SELECT NULL AS CPUMVALUE, AVG(MVALUE) AS MEMORYMVALUE,STIME  FROM ")
                .append(tableName)
                .append(" mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE mh.ADDR = '")
                .append(addr).append("' AND METRIC_ID = '").append(memoryMetricId)
                .append("' GROUP BY STIME) T2 ) AS T3  GROUP BY STIME ");
        strb.append("SELECT STIME,").append(cpumvalue).append(" AS CPUMVALUE,").append(memorymvalue)
                .append(" AS MEMORYMVALUE FROM (SELECT STIME,SUM(CPUMVALUE) AS CPUMVALUE,SUM(MEMORYMVALUE) AS MEMORYMVALUE FROM ( SELECT * FROM (SELECT AVG(MVALUE) AS CPUMVALUE,NULL AS MEMORYMVALUE,")
                .append(stime).append(" AS STIME  FROM ").append(tableName)
                .append(" mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE mh.ADDR = '")
                .append(addr).append("' AND METRIC_ID = '").append(cpuRateMetricId)
                .append("' GROUP BY ").append(stime)
                .append(") T1 UNION SELECT * FROM (SELECT NULL AS CPUMVALUE, AVG(MVALUE) AS MEMORYMVALUE, ")
                .append(stime).append(" AS STIME  FROM ").append(tableName)
                .append(" mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE mh.ADDR = '")
                .append(addr).append("' AND METRIC_ID = '").append(memoryMetricId)
                .append("' GROUP BY ").append(stime).append(") T2 ) AS T3  GROUP BY STIME ")
                .append(" UNION SELECT '平均' AS STIME, (CASE WHEN AVG(CPUMVALUE) IS NULL THEN 0 ELSE AVG(CPUMVALUE) END) AS CPUMVALUE,(CASE WHEN AVG(MEMORYMVALUE)  IS NULL THEN 0 ELSE AVG(MEMORYMVALUE) END) AS MEMORYMVALUE FROM ( ")
                .append(strbT)
                .append(" ) AS T4 UNION SELECT '最小' AS STIME, (CASE WHEN MIN(CPUMVALUE) IS NULL THEN 0 ELSE MIN(CPUMVALUE) END) AS CPUMVALUE,(CASE WHEN MIN(MEMORYMVALUE)  IS NULL THEN 0 ELSE MIN(MEMORYMVALUE) END) AS MEMORYMVALUE FROM ( ")
                .append(strbT)
                .append(" ) AS T4 UNION SELECT '最大' AS STIME, (CASE WHEN MAX(CPUMVALUE) IS NULL THEN 0 ELSE MAX(CPUMVALUE) END) AS CPUMVALUE,(CASE WHEN MAX(MEMORYMVALUE) IS NULL THEN 0 ELSE MAX(MEMORYMVALUE) END) AS MEMORYMVALUE FROM ( ")
                .append(strbT).append(" ) AS T4) AS T5 ");
        LOG.debug("getDayReport sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getMonthReport @Description: TODO(获取周、月报表数据) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getWMReport(Map<String, Object> parameters) {
        String cpuRateMetricId = (String) parameters.get("cpuRateMetricId");
        String memoryMetricId = (String) parameters.get("memoryMetricId");
        @SuppressWarnings("unchecked")
        List<String> tableNameList = (List<String>) parameters.get("tableNameList");
        String addr = (String) parameters.get("addr");
        StringBuffer strb = new StringBuffer();
        String avgcpumvalue = DbSqlUtil.getRoundingSql("AVG(CPUMVALUE)", 2);
        String mincpumvalue = DbSqlUtil.getRoundingSql("MIN(CPUMVALUE)", 2);
        String maxcpumvalue = DbSqlUtil.getRoundingSql("MAX(CPUMVALUE)", 2);
        String avgmemorymvalue = DbSqlUtil.getRoundingSql("AVG(MEMORYMVALUE)", 2);
        String minmemorymvalue = DbSqlUtil.getRoundingSql("MIN(MEMORYMVALUE)", 2);
        String maxmemorymvalue = DbSqlUtil.getRoundingSql("MAX(MEMORYMVALUE)", 2);
        strb.append("SELECT HOSTNAME,ADDR,").append(avgcpumvalue).append(" AS AVGCPUMVALUE,")
                .append(mincpumvalue).append(" AS MINCPUMVALUE,").append(maxcpumvalue)
                .append(" AS MAXCPUMVALUE,").append(avgmemorymvalue).append(" AS AVGMEMORYMVALUE,")
                .append(minmemorymvalue).append(" AS MINMEMORYMVALUE,").append(maxmemorymvalue)
                .append(" AS MAXMEMORYMVALUE  FROM (")
                .append("SELECT STIME,SUM(CPUMVALUE) AS CPUMVALUE,SUM(MEMORYMVALUE) AS MEMORYMVALUE,ADDR,HOSTNAME FROM (SELECT * FROM (SELECT SUM(MVALUE) AS CPUMVALUE,NULL AS MEMORYMVALUE,STIME,mh.ADDR AS ADDR,mh.HOSTNAME AS HOSTNAME FROM ( ");
        for (int i = 0; i < tableNameList.size(); i++) {
            strb.append("SELECT * FROM ").append(tableNameList.get(i));
            if (i < (tableNameList.size() - 1)) {
                strb.append(" UNION ");
            }
        }
        strb.append(" ) mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE METRIC_ID = '")
                .append(cpuRateMetricId);
        if (!StringUtils.isEmpty(addr)) {
            strb.append("' AND mh.ADDR = '").append(addr);
        }
        strb.append(
                "' GROUP BY STIME,mh.ADDR,mh.HOSTNAME) T1 UNION SELECT * FROM (SELECT NULL AS CPUMVALUE, SUM(MVALUE) AS MEMORYMVALUE,STIME,mh.ADDR AS ADDR,mh.HOSTNAME AS HOSTNAME FROM ( ");
        for (int i = 0; i < tableNameList.size(); i++) {
            strb.append("SELECT * FROM ").append(tableNameList.get(i));
            if (i < (tableNameList.size() - 1)) {
                strb.append(" UNION ");
            }

        }
        strb.append(" ) mds LEFT JOIN MON_HOST mh ON (mh.HOSTID = mds.HOST_ID) WHERE METRIC_ID = '")
                .append(memoryMetricId);
        if (!StringUtils.isEmpty(addr)) {
            strb.append("' AND mh.ADDR = '").append(addr);
        }
        strb.append(
                "' GROUP BY STIME,mh.ADDR,mh.HOSTNAME) T2 ) AS T3  GROUP BY STIME,ADDR,HOSTNAME ")
                .append(") AS T4 GROUP BY ADDR,HOSTNAME");
        LOG.debug("getWMReport sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getRadiusProcess @Description:
     *         TODO(获取RadiusProcess连通性) @param @param parameters @param @return
     *         参数 @return String 返回类型 @throws
     */
    public String getRadiusProcess(Map<String, Object> parameters) {
        String radiusProcessKey = (String) parameters.get("radiusProcessKey");
        String tableName = (String) parameters.get("tableName");
        String stime = (String) parameters.get("stime");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT s.HOST_ID,s.METRIC_ID,s.ITEM,h.ADDR,h.HOSTNAME FROM ").append(tableName)
                .append(" s LEFT JOIN MON_HOST h ON (s.HOST_ID = h.HOSTID) WHERE s.ITEM IN (")
                .append(StringUtil.SqlQuotation(radiusProcessKey)).append(") AND s.STIME = '")
                .append(stime).append("'");
        LOG.debug("getRadiusProcess sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getRadiusAnalytic @Description: TODO(获取Radius解析量数据) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getRadiusAnalytic(Map<String, Object> parameters) {
        String hostIds = (String) parameters.get("hostIds");
        String tableName = (String) parameters.get("tableName");
        String metricId = (String) parameters.get("metricId");
        String hostField = (String) parameters.get("hostField");
        String stime = DbSqlUtil.getTimeHMSql("STIME");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT SUM(MVALUE) AS value, ").append(stime).append(" AS mark FROM ")
                .append(tableName).append(" WHERE METRIC_ID = '")
                .append(StringUtil.SqlFilter(metricId)).append("' AND ").append(hostField)
                .append(" IN (").append(hostIds).append(") GROUP BY STIME");
        LOG.debug("getRadiusAnalytic sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getHostNetConnectable @Description: TODO(主机连通性) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getHostNetConnectable(Map<String, Object> parameters) {
        String sysNetConnectableMetricId = (String) parameters.get("sysNetConnectableMetricId");
        String tableName = (String) parameters.get("tableName");
        String stime = (String) parameters.get("stime");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT m.MVALUE,m.ATTR1,h.HOSTNAME FROM ").append(tableName)
                .append(" m LEFT JOIN MON_HOST h ON (m.ATTR1 = h.ADDR) WHERE m.METRIC_ID = '")
                .append(sysNetConnectableMetricId).append("' AND m.STIME = '").append(stime)
                .append("'");
        LOG.debug("getHostNetConnectable sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getHostState @Description: TODO(主机状态) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getHostState(Map<String, Object> parameters) {
        String metricId = (String) parameters.get("metricId");
        String tableName = (String) parameters.get("tableName");
        String stime = (String) parameters.get("stime");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT s.HOST_ID,s.METRIC_ID,s.MVALUE,h.HOSTNAME,h.ADDR FROM ")
                .append(tableName)
                .append(" s LEFT JOIN MON_HOST h ON (s.HOST_ID = h.HOSTID) WHERE s.METRIC_ID = '")
                .append(metricId).append("' AND s.STIME = '").append(stime).append("'");
        LOG.debug("getHostState sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: getHostStateAllHost @Description: TODO(查询主机状态的所有主机) @param @param
     *         parameters @param @return 参数 @return String 返回类型 @throws
     */
    public String getHostStateAllHost(Map<String, Object> parameters) {
        String metricIds = (String) parameters.get("metricIds");
        String tableName = (String) parameters.get("tableName");
        String stime = (String) parameters.get("stime");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT s.HOST_ID,h.HOSTNAME FROM ").append(tableName)
                .append(" s LEFT JOIN MON_HOST h ON (s.HOST_ID = h.HOSTID) WHERE s.METRIC_ID IN (")
                .append(StringUtil.SqlQuotation(metricIds)).append(") AND m.STIME = '")
                .append(stime).append("' GROUP BY s.HOST_ID,h.HOSTNAME");
        LOG.debug("getHostStateAllHost sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 查询巡检组指标信息
     *
     * @param parameters
     * @return
     */
    public String getAisGroupMetricModel(Map<String, Object> parameters) {
        AisGroupMetricModel aisGroupMetricModel = (AisGroupMetricModel) parameters
                .get("aisGroupMetricModel");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT wiigm.GROUP_METRIC_ID,wiigm.GROUP_ID,wiigm.NAME,wiigm.URL,wiigm.DIMENSION_TYPE,wiigm.CHART_NAME,wiigm.METRIC_ID,wiigm.ATTR,wiigm.DIMENSION1,wiigm.DIMENSION2,wiigm.DIMENSION3,wiigm.CREATE_TIME,wiigm.UPDATE_TIME,mm.METRIC_NAME,mcd.CHART_TITLE ,wiig.GROUP_NAME FROM WD_INS_ITEM_GROUP_METRIC wiigm LEFT JOIN MD_METRIC mm ON (wiigm.METRIC_ID = mm.ID) LEFT JOIN MD_CHART_DETAIL mcd ON (wiigm.CHART_NAME = mcd.CHART_NAME) LEFT JOIN WD_INS_ITEM_GROUP wiig ON (wiigm.GROUP_ID = wiig.GROUP_ID) WHERE 1 = 1 ");
        if (!StringUtils.isEmpty(aisGroupMetricModel.getGroup_id())) {
            strb.append(" AND wiigm.GROUP_ID='").append(aisGroupMetricModel.getGroup_id())
                    .append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getName())) {
            strb.append(" AND wiigm.NAME='").append(aisGroupMetricModel.getName()).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getUrl())) {
            strb.append(" AND wiigm.URL='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getUrl())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getDimension_type())) {
            strb.append(" AND wiigm.DIMENSION_TYPE='")
                    .append(aisGroupMetricModel.getDimension_type()).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getChart_name())) {
            strb.append(" AND wiigm.CHART_NAME='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getChart_name())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getMetric_id())) {
            strb.append(" AND wiigm.METRIC_ID='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getMetric_id())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getAttr())) {
            strb.append(" AND wiigm.ATTR='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getAttr())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getDimension1())) {
            strb.append(" AND wiigm.DIMENSION1='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension1())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getDimension2())) {
            strb.append(" AND wiigm.DIMENSION2='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension2())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getDimension3())) {
            strb.append(" AND wiigm.DIMENSION3='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension3())).append("'");
        }
        if (!StringUtils.isEmpty(aisGroupMetricModel.getGroup_metric_id())) {
            strb.append(" AND wiigm.GROUP_METRIC_ID='")
                    .append(aisGroupMetricModel.getGroup_metric_id()).append("'");
        }
        LOG.debug("getAisGroupMetricModel sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 
     * @Title: insertAisGroupMetricModel @Description:
     *         TODO(插入巡检组指标) @param @param parameters @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String insertAisGroupMetricModel(Map<String, Object> parameters) {
        AisGroupMetricModel aisGroupMetricModel = (AisGroupMetricModel) parameters
                .get("aisGroupMetricModel");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO WD_INS_ITEM_GROUP_METRIC ( GROUP_ID,GROUP_METRIC_ID,NAME,URL,DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,DIMENSION1,DIMENSION2,DIMENSION3,CREATE_TIME,UPDATE_TIME ) VALUES('")
                .append(aisGroupMetricModel.getGroup_id()).append("','")
                .append(aisGroupMetricModel.getGroup_metric_id()).append("','")
                .append(aisGroupMetricModel.getName()).append("','")
                .append(aisGroupMetricModel.getUrl()).append("','")
                .append(aisGroupMetricModel.getDimension_type()).append("','")
                .append(aisGroupMetricModel.getChart_name()).append("','")
                .append(aisGroupMetricModel.getMetric_id()).append("','")
                .append(StringUtil.SqlFilter(aisGroupMetricModel.getAttr())).append("','")
                .append(aisGroupMetricModel.getDimension1()).append("','")
                .append(aisGroupMetricModel.getDimension2()).append("','")
                .append(aisGroupMetricModel.getDimension3()).append("',")
                .append(DbSqlUtil.getDateMethod()).append(",").append(DbSqlUtil.getDateMethod())
                .append(")");
        return strb.toString();
    }

    /**
     * 
     * @Title: updateAisGroupMetricModel @Description:
     *         TODO(更新巡检组指标) @param @param parameters @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String updateAisGroupMetricModel(Map<String, Object> parameters) {
        AisGroupMetricModel aisGroupMetricModel = (AisGroupMetricModel) parameters
                .get("aisGroupMetricModel");
        StringBuilder strb = new StringBuilder(
                "UPDATE WD_INS_ITEM_GROUP_METRIC SET GROUP_METRIC_ID = GROUP_METRIC_ID ");
        if (null != aisGroupMetricModel.getGroup_id()) {
            strb.append(",GROUP_ID='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getGroup_id())).append("' ");
        }
        if (null != aisGroupMetricModel.getName()) {
            strb.append(",NAME='").append(StringUtil.SqlFilter(aisGroupMetricModel.getName()))
                    .append("' ");
        }
        if (null != aisGroupMetricModel.getUrl()) {
            strb.append(",URL='").append(StringUtil.SqlFilter(aisGroupMetricModel.getUrl()))
                    .append("' ");
        }
        if (null != aisGroupMetricModel.getDimension_type()) {
            strb.append(",DIMENSION_TYPE='").append(aisGroupMetricModel.getDimension_type())
                    .append("' ");
        }
        if (null != aisGroupMetricModel.getChart_name()) {
            strb.append(",CHART_NAME='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getChart_name())).append("' ");
        }
        if (null != aisGroupMetricModel.getMetric_id()) {
            strb.append(",METRIC_ID='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getMetric_id())).append("' ");
        }
        if (null != aisGroupMetricModel.getAttr()) {
            strb.append(",ATTR='").append(aisGroupMetricModel.getAttr()).append("' ");
        }
        if (null != aisGroupMetricModel.getDimension1()) {
            strb.append(",DIMENSION1='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension1())).append("' ");
        }
        if (null != aisGroupMetricModel.getDimension2()) {
            strb.append(",DIMENSION2='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension2())).append("' ");
        }
        if (null != aisGroupMetricModel.getDimension3()) {
            strb.append(",DIMENSION3='")
                    .append(StringUtil.SqlFilter(aisGroupMetricModel.getDimension3())).append("' ");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != aisGroupMetricModel.getUpdate_time()) {
            if (MYSQL.equals(databaseType)) {
                strb.append(",UPDATE_TIME='")
                        .append(sdf.format(aisGroupMetricModel.getUpdate_time())).append("' ");
            } else if (ORACLE.equals(databaseType)) {
                strb.append(",UPDATE_TIME= to_date('")
                        .append(sdf.format(aisGroupMetricModel.getUpdate_time()))
                        .append("' ,'yyyy-mm-dd hh24:mi:ss')");
            }
        }
        strb.append("WHERE GROUP_METRIC_ID='").append(aisGroupMetricModel.getGroup_metric_id())
                .append("'");
        LOG.debug("updateAisGroupMetricModel sql = {}", strb.toString());
        return strb.toString();
    }
    
    public String queryServerList() {
    	StringBuilder strb = new StringBuilder(
                "SELECT MAX( N.NODE_NAME ) AS NODENAME,N.ID AS NODEID,P.DESCRIPTION AS SERVERTYPE, ");
    	if (MYSQL.equals(databaseType)) {
    		strb.append("MAX(P.`CODE`) AS SERVERNAME ");
        } else if (ORACLE.equals(databaseType)) {
        	strb.append("MAX(P.CODE) AS SERVERNAME ");
        }
    	strb.append(" FROM MD_SERVIVE_LIST B LEFT JOIN MON_HOST H ON B.HOSTID=H.HOSTID LEFT JOIN");
    	strb.append(" MD_NODE N ON H.NODEID=N.ID LEFT JOIN MD_PARAM P ON ");
    	if (MYSQL.equals(databaseType)) {
    		strb.append(" B.SERVER_TYPE=P.`CODE`");
        } else if (ORACLE.equals(databaseType)) {
        	strb.append(" B.SERVER_TYPE=P.CODE ");
        }
    	strb.append(" AND P.TYPE='106' WHERE B.IS_SHOW=1 AND H.ADDR IS NOT NULL GROUP BY N.ID,P.DESCRIPTION");
    	
    	LOG.info("主机列表节点sql:queryServerList sql = {}", strb.toString());
    	
    	return strb.toString();
    }
    
    public String queryServerHostList() {
    	StringBuilder strb = new StringBuilder(
                "SELECT N.ID AS NODEID,P.DESCRIPTION AS SERVERTYPE,H.ADDR,H.HOSTNAME,H.HOSTID, ");
    	if (MYSQL.equals(databaseType)) {
    		strb.append("P.`CODE` AS SERVERNAME ");
        } else if (ORACLE.equals(databaseType)) {
        	strb.append("P.CODE AS SERVERNAME ");
        }
    	strb.append(" FROM MD_SERVIVE_LIST B LEFT JOIN MON_HOST H ON B.HOSTID = H.HOSTID LEFT JOIN MD_NODE N ON H.NODEID = N.ID ");
    	strb.append(" LEFT JOIN MD_PARAM P ON  ");
    	if (MYSQL.equals(databaseType)) {
    		strb.append(" B.SERVER_TYPE=P.`CODE`");
        } else if (ORACLE.equals(databaseType)) {
        	strb.append(" B.SERVER_TYPE=P.CODE ");
        }
    	strb.append(" AND P.TYPE = '106' WHERE B.IS_SHOW = 1 AND H.ADDR IS NOT NULL ");
    	LOG.info("主机服务器列表sql:queryServerList sql = {}", strb.toString());
    	
    	return strb.toString();
    }
    @SuppressWarnings("unchecked")
    public String  queryHomeAlarm(Map<String, Object> parameters) {
    	List<String> urlList=(List<String>) parameters.get("urlList");
    	StringBuilder strb = new StringBuilder(
                "SELECT N.ALARM_ID AS ALARMID,N.URL,N.DIMENSION_TYPE as DIMENSIONTYPE,N.ALARM_LEVEL as ALARMLEVEL,N.ALARMMSG,N.DIMENSION1, ");
    	strb.append("N.DIMENSION2,N.DIMENSION1_NAME as DIMENSION1NAME,N.DIMENSION2_NAME as DIMENSION2NAME,M.SERVER_TYPE as SERVERTYPE ");
    	strb.append(" FROM MD_ALARM_INFO N LEFT JOIN MD_METRIC M ON N.METRIC_ID=M.ID");
    	strb.append(" WHERE N.ALARM_NUM > 0  AND N.DELETE_STATE = 0 ");
    	String url=getSqlStrByList(urlList, 1000, "N.URL");
    	if(url!=null) {
    		strb.append(" AND "+url);
    	}
    	LOG.info("主机列表告警sql:queryServerList sql = {}", strb.toString());
    	return strb.toString();
    }
    @SuppressWarnings("unchecked")
    public String  homeAlarmQueryById(Map<String, Object> parameters) {
    	String alarmId=(String) parameters.get("alarmId");
    	List<String> urlList=(List<String>) parameters.get("urlList");
    	String[] alarmIds=alarmId.split("#");
		StringBuilder strb = new StringBuilder(
                "SELECT N.ALARM_ID AS ALARMID,N.URL,N.DIMENSION_TYPE as DIMENSIONTYPE,N.ALARM_LEVEL as ALARMLEVEL,N.ALARMMSG,N.DIMENSION1, ");
    	strb.append("N.DIMENSION2,N.DIMENSION1_NAME as DIMENSION1NAME,N.DIMENSION2_NAME as DIMENSION2NAME,M.SERVER_TYPE as SERVERTYPE ");
    	strb.append(" FROM MD_ALARM_INFO N LEFT JOIN MD_METRIC M ON N.METRIC_ID=M.ID");
    	strb.append(" WHERE N.ALARM_NUM > 0  AND N.DELETE_STATE = 0 ");
    	String url=getSqlStrByList(urlList, 1000, "N.URL");
    	if(url!=null) {
    		strb.append(" AND "+url);
    	}
    	if(alarmId!=null && alarmId.length()>0) {
    		strb.append(" AND N.ALARM_ID IN (");
    		for (int i = 0; i < alarmIds.length; i++) {
				if(i!=alarmIds.length-1) {
					strb.append("'"+alarmIds[i]+"',");
				}else {
					strb.append("'"+alarmIds[i]+"'");
				}
			}
    		strb.append(")");
    	}
    	
    	LOG.info("主机列表告警sql:homeAlarmQueryById sql = {}", strb.toString());
    	return strb.toString();
    }
    
    @SuppressWarnings("unchecked")
    public String queryAlarmByAlarmlevel(Map<String, Object> parameters) {
        List<String> urlList = (List<String>) parameters.get("urlList");
        StringBuilder strb = new StringBuilder(
                "SELECT N.URL,N.ALARM_LEVEL ALARMLEVEL,N.ALARMMSG,CASE WHEN N.DIMENSION2_NAME IS NULL THEN N.DIMENSION1_NAME ELSE N.DIMENSION2_NAME END AS dimension1Name,N.LAST_TIME AS lastTime ");
        strb.append("FROM MD_ALARM_INFO N WHERE N.ALARM_NUM > 0  AND N.DELETE_STATE = 0  ");
        String url = getSqlStrByList(urlList, 1000, "N.URL");
        if (url != null) {
            strb.append(" AND " + url);
        }
        strb.append(" ORDER BY N.ALARM_LEVEL DESC");
        LOG.info("主机列表告警queryAlarmByAlarmlevel sql = {}", strb.toString());
        return strb.toString();
    }
    
    public String getSqlStrByList(List<String> sqhList, int splitNum, String columnName) {
        if(splitNum>1000) //因为数据库的列表sql限制，不能超过1000.
            return null;
        StringBuffer sql = new StringBuffer();
        if (sqhList != null) {
            sql.append(" ( ").append(columnName).append (" IN ( ");
            for (int i = 0; i < sqhList.size(); i++) {
                sql.append("'").append(sqhList.get(i) + "',");
                if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" ) OR ").append(columnName).append (" IN (");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ) ");
        }
        return sql.toString();
    }
}
