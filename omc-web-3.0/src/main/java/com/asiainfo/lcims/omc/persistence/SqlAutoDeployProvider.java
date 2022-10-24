package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.autodeploy.MdDeployLog;
import com.asiainfo.lcims.omc.model.autodeploy.MdHostHardwareInfoLog;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.AutoDeployConstant;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @Author: YuChao
 * @Date: 2019/3/27 15:17
 */
public class SqlAutoDeployProvider {

    private static final Logger LOG = LoggerFactory.make();

    /**
     * 根据IP查询MON_HOST表部署状态
     *
     * @param parameters
     * @return
     */
    public String getMonHostInfoWithIp(Map<String, Object> parameters) {
        MonHost monHost = (MonHost) parameters.get("monHost");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM MON_HOST WHERE ADDR = '").append(monHost.getAddr()).append("'");
        return strb.toString();
    }

    /**
     *
     * @Description: 插入MON_HOST表数据
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String addMonHostInfo(Map<String, Object> parameters) {
        MonHost monHost = (MonHost) parameters
                .get("monHost");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MON_HOST(HOSTID, ADDR, HOSTNAME, NODEID, STATUS, OS, CPU, " +
                        "MEMORY, DISKSPACE, PROVIDER, PRODUCTNAME, HOSTTYPE, " +
                        "SERIALNUMBER, IPV4, IPV6, HOST_ROOM, RACK_NUM, " +
                        "LOCATION, SSH_USER, SSH_PASSWORD, SSH_PORT, DEPLOY_STATUS, DEPLOY_DES) VALUES('")
                .append(monHost.getHostid()).append("','")
                .append(monHost.getAddr()).append("','");
        if (null != monHost.getHostname()) {
            strb.append(monHost.getHostname()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getNodeid()) {
            strb.append(monHost.getNodeid()).append("',");
        }else {
            strb.append("',");
        }
        if (null != monHost.getStatus()) {
            strb.append(monHost.getStatus()).append(",'");
        }else {
            strb.append(AutoDeployConstant.DEFAULT_HOST_STATUS).append(",'");
        }
        if (null != monHost.getOs()) {
            strb.append(monHost.getOs()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getCpu()) {
            strb.append(monHost.getCpu()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getMemory()) {
            strb.append(monHost.getMemory()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getDiskspace()) {
            strb.append(monHost.getDiskspace()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getProvider()) {
            strb.append(monHost.getProvider()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getProductname()) {
            strb.append(monHost.getProductname()).append("',");
        }else {
            strb.append("',");
        }if (null != monHost.getHosttype()) {
            strb.append(monHost.getHosttype()).append(",'");
        }else {
            strb.append(AutoDeployConstant.DEFAULT_HOST_TYPE).append(",'");
        }
        if (null != monHost.getSerialnumber()) {
            strb.append(monHost.getSerialnumber()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getIpv4()) {
            strb.append(monHost.getIpv4()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getIpv6()) {
            strb.append(monHost.getIpv6()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getHost_room()) {
            strb.append(monHost.getHost_room()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getRack_num()) {
            strb.append(monHost.getRack_num()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getLocation()) {
            strb.append(monHost.getLocation()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getSsh_user()) {
            strb.append(monHost.getSsh_user()).append("','");
        }else {
            strb.append("','");
        }
        if (null != monHost.getSsh_password()) {
            strb.append(monHost.getSsh_password()).append("',");
        }else {
            strb.append("',");
        }
        if (null != monHost.getSsh_port()) {
            strb.append(monHost.getSsh_port()).append(",");
        }else {
            strb.append(AutoDeployConstant.DEFAULT_SSH_PORT).append(",");
        }
        if (null != monHost.getDeploy_status()) {
            strb.append(monHost.getDeploy_status()).append(",'");
        }else {
            strb.append(AutoDeployConstant.DEPLOY_STATUS_RUNNING).append(",'");
        }
        if (null != monHost.getDeploy_des()) {
            strb.append(monHost.getDeploy_des()).append("')");
        }else {
            strb.append("')");
        }
        LOG.debug("addMonHostInfo sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Description: 更新MON_HOST表数据
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String updateMonHostInfo(Map<String, Object> parameters) {
        MonHost monHost = (MonHost) parameters
                .get("monHost");
        StringBuilder strb = new StringBuilder(
                "UPDATE MON_HOST SET ");
        strb.append("DEPLOY_STATUS=").append(monHost.getDeploy_status()).append(" ");
        if (null != monHost.getHostname()) {
            strb.append(" ,HOSTNAME = '")
                    .append(monHost.getHostname()).append("' ");
        }
        if (null != monHost.getNodeid()) {
            strb.append(",NODEID='").append(monHost.getNodeid())
                    .append("' ");
        }
        if (null != monHost.getOs()) {
            strb.append(",OS='")
                    .append(monHost.getOs()).append("' ");
        }
        if (null != monHost.getCpu()) {
            strb.append(",CPU='")
                    .append(monHost.getCpu()).append("' ");
        }
        if (null != monHost.getMemory()) {
            strb.append(",MEMORY='")
                    .append(monHost.getMemory()).append("' ");
        }
        if (null != monHost.getDiskspace()) {
            strb.append(",DISKSPACE='")
                    .append(monHost.getDiskspace()).append("' ");
        }
        if (null != monHost.getProvider()) {
            strb.append(",PROVIDER='")
                    .append(monHost.getProvider()).append("' ");
        }
        if (null != monHost.getProductname()) {
            strb.append(",PRODUCTNAME='")
                    .append(monHost.getProductname()).append("' ");
        }
        if (null != monHost.getHosttype()) {
            strb.append(",HOSTTYPE=")
                    .append(monHost.getHosttype()).append(" ");
        }
        if (null != monHost.getSerialnumber()) {
            strb.append(",SERIALNUMBER='")
                    .append(monHost.getSerialnumber()).append("' ");
        }
        if (null != monHost.getIpv4()) {
            strb.append(",IPV4='")
                    .append(monHost.getIpv4()).append("' ");
        }
        if (null != monHost.getIpv6()) {
            strb.append(",IPV6='")
                    .append(monHost.getIpv6()).append("' ");
        }
        if (null != monHost.getHost_room()) {
            strb.append(",HOST_ROOM='")
                    .append(monHost.getHost_room()).append("' ");
        }
        if (null != monHost.getRack_num()) {
            strb.append(",RACK_NUM='")
                    .append(monHost.getRack_num()).append("' ");
        }
        if (null != monHost.getLocation()) {
            strb.append(",LOCATION='").append(monHost.getLocation()).append("' ");
        }
        if (null != monHost.getSsh_user()) {
            strb.append(",SSH_USER='").append(monHost.getSsh_user()).append("' ");
        }
        if (null != monHost.getSsh_password()) {
            strb.append(",SSH_PASSWORD='").append(monHost.getSsh_password()).append("' ");
        }
        if (null != monHost.getSsh_port()) {
            strb.append(",SSH_PORT=").append(monHost.getSsh_port()).append(" ");
        }
        if (null != monHost.getDeploy_des()) {
            strb.append(",DEPLOY_DES='").append(monHost.getDeploy_des()).append("' ");
        }
        strb.append("WHERE ADDR='").append(monHost.getAddr())
                .append("'");
        LOG.debug("updateMonHostInfo sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Description: 插入MD_DEPLOY_LOG表数据
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String addDeployLogInfo(Map<String, Object> parameters) {
        MdDeployLog deployLog = (MdDeployLog) parameters
                .get("deployLog");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MD_DEPLOY_LOG(ID, IP, DEPLOY_STATUS, DEPLOY_DES, DEPLOY_BATCH, DEPLOY_TIME) VALUES('")
                .append(deployLog.getId()).append("','")
                .append(deployLog.getIp()).append("',")
                .append(deployLog.getDeploy_status()).append(",'");
        if (null != deployLog.getDeploy_des()) {
            strb.append(deployLog.getDeploy_des()).append("',");
        }else {
            strb.append("',");
        }
        if (null != deployLog.getDeploy_batch()) {
            strb.append("'").append(deployLog.getDeploy_batch()).append("',");
        }else {
            strb.append("'',");
        }
        strb.append(DbSqlUtil.getDateMethod()).append(")");
        LOG.debug("addDeployLogInfo sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     *
     * @Description: 插入MD_HOST_HARDWARE_INFO_LOG表数据
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String addHostHardwareInfoLogInfo(Map<String, Object> parameters) {
        MdHostHardwareInfoLog hostHardwareInfoLog = (MdHostHardwareInfoLog) parameters
                .get("hostHardwareInfoLog");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO MD_HOST_HARDWARE_INFO_LOG(ID, IP, OS, CPU, MEMORY, DISKSPACE, DEPLOY_TIME) VALUES('")
                .append(hostHardwareInfoLog.getId()).append("','")
                .append(hostHardwareInfoLog.getIp()).append("','");
        if (null != hostHardwareInfoLog.getOs()) {
            strb.append(hostHardwareInfoLog.getOs()).append("','");
        }else {
            strb.append("','");
        }
        if (null != hostHardwareInfoLog.getCpu()) {
            strb.append(hostHardwareInfoLog.getCpu()).append("','");
        }else {
            strb.append("','");
        }
        if (null != hostHardwareInfoLog.getMemory()) {
            strb.append(hostHardwareInfoLog.getMemory()).append("','");
        }else {
            strb.append("','");
        }
        if (null != hostHardwareInfoLog.getDiskspace()) {
            strb.append(hostHardwareInfoLog.getDiskspace()).append("',");
        }else {
            strb.append("',");
        }
        strb.append(DbSqlUtil.getDateMethod()).append(")");
        LOG.debug("addDeployLogInfo sql = {}", strb.toString());
        return strb.toString();
    }
    
    /**
     * 根据业务列表查询指标信息
     * @param parameters
     * @return
     */
    public String queryMetricList(Map<String, Object> parameters) {
        String businesslist = (String) parameters.get("businesslist");
        StringBuilder strb = new StringBuilder("SELECT M.ID,M.CYCLE_ID, M.METRIC_TYPE,M.SCRIPT,M.SCRIPT_PARAM,M.SCRIPT_RETURN_TYPE FROM ");
        strb.append("(SELECT SOURCE_ID,BUSINESS_TAG FROM MD_BUSINESS_REL WHERE SOURCE_TYLE=")
            .append(AutoDeployConstant.SOURCE_TYLE_METRIC)
            .append(") R")
            .append(" LEFT JOIN MD_METRIC M ON R.SOURCE_ID = M.ID");
        strb.append(" WHERE R.BUSINESS_TAG IN (").append(getBusinesslistToSqlIn(businesslist)).append(")");
        return strb.toString();
    }
    
    /**
     * 组装业务列表为sql in 条件查询格式  
     * @param businesslist
     * @return '','',''
     */
    private String getBusinesslistToSqlIn(String businesslist){
        String returnSql = null;
        String [] str = businesslist.split(AutoDeployConstant.AUTO_DEPLOY_SPLIT_STR);
        if(str.length==1){
            returnSql = "'"+businesslist+"'";
        }else{
            StringBuilder strb = new StringBuilder();
            for (String strparam : str) {
                strb.append("'"+strparam+"',");
            }
            returnSql = strb.toString();
            returnSql = returnSql.substring(0, returnSql.length()-1);
        }
        return returnSql;
    }
    
    /**
     * 根据业务列表获取业务信息
     * @param parameters
     * @return
     */
    public String getFindBusinessList(Map<String, Object> parameters) {
        String businesslist = (String) parameters.get("businesslist");
        StringBuilder strb = new StringBuilder("SELECT BUSINESS_TAG,PROCESS_KEY,PROCESS_PARAMS FROM MD_FIND_BUSINESS WHERE BUSINESS_TAG IN");
        strb.append("(").append(getBusinesslistToSqlIn(businesslist)).append(")");
        return strb.toString();
    }
    
    
    /**
     * 根据业务列表查询菜单信息
     * @param parameters
     * @return
     */
    public String queryMenuList(Map<String, Object> parameters) {
        String businesslist = (String) parameters.get("businesslist");
        StringBuilder strb = new StringBuilder("SELECT M.NAME FROM ");
        strb.append("(SELECT SOURCE_ID,BUSINESS_TAG FROM MD_BUSINESS_REL WHERE SOURCE_TYLE=")
            .append(AutoDeployConstant.SOURCE_TYLE_MENU)
            .append(") R")
            .append(" LEFT JOIN MD_MENU_TREE M ON R.SOURCE_ID = M.ID");
        strb.append(" WHERE R.BUSINESS_TAG IN (").append(getBusinesslistToSqlIn(businesslist)).append(")");
        return strb.toString();
    }
    
}
