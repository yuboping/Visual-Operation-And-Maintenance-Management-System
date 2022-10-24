package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.configmanage.*;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.EnumUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 复杂sql
 *
 * @author yangyc
 *
 */
public class SqlSpecialProvider {

    private static final Logger LOG = LoggerFactory.make();

    public String getProcessOperateByOperateId(Map<String, Object> parameters) {
        String operateId = (String) parameters.get("operateId");
        StringBuffer strb = new StringBuffer();
        if (EnumUtil.DB_ORACLE.equals(CommonInit.BUSCONF.getDbName())) {
            strb.append("SELECT * FROM ( ");
        }
        strb.append("SELECT M.ID, M.OPERATE_ID, M.HOST_IP, M.PROCESS_NAME, M.PROCESS_SCRIPT, ")
                .append("M.OPERATE_STATE, M.SCRIPT_TYPE, M.OPERATE_RESULT, " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME, ")
                .append("M.UPDATE_TIME FROM PROCESS_OPERATE M WHERE M.OPERATE_ID = ")
                .append("'" + operateId + "'").append(" ORDER BY UPDATE_TIME DESC ");
        if (EnumUtil.DB_MYSQL.equals(CommonInit.BUSCONF.getDbName())) {
            strb.append(" LIMIT 1 ");
        } else if (EnumUtil.DB_ORACLE.equals(CommonInit.BUSCONF.getDbName())) {
            strb.append(" ) F WHERE ROWNUM = 1");
        }
        LOG.info("getProcessOperateByOperateId sql = {}", strb.toString());
        return strb.toString();
    }

    public String getProcessOperateById(Map<String, Object> parameters) {
        String id = (String) parameters.get("id");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.ID, M.OPERATE_ID, M.HOST_IP, M.PROCESS_NAME, M.PROCESS_SCRIPT, ")
                .append("M.OPERATE_STATE, M.SCRIPT_TYPE, M.OPERATE_RESULT, " + DbSqlUtil.getTimeSql("M.CREATE_TIME") + " AS CREATE_TIME, ")
                .append("M.UPDATE_TIME FROM PROCESS_OPERATE M WHERE M.ID = ")
                .append("'" + id + "'");
        return strb.toString();
    }

    /**
     * 查询进程主机关联信息
     *
     * @param parameters
     * @return
     */
    public String getHostProcessList(Map<String, Object> parameters) {
        MdHostProcess mdHostProcess = (MdHostProcess) parameters.get("hostProcessParam");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT HM.PROCESS_NAME, M.ID, M.HOST_ID, M.PROCESS_ID, M.PROCESS_KEY, M.START_SCRIPT, M.STOP_SCRIPT, M.DESCRIPTION, M.CREATE_TIME, M.UPDATE_TIME, H.ADDR as HOST_IP , H.HOSTNAME as HOSTNAME, ");
        if (EnumUtil.DB_MYSQL.equals(CommonInit.BUSCONF.getDbName())) {
            strb.append(
                    " (SELECT Q.DESCRIPTION FROM PROCESS_OPERATE S LEFT JOIN MD_PARAM Q ON Q.CODE = S.OPERATE_STATE AND Q.TYPE = 13 WHERE S.OPERATE_ID = M.ID ORDER BY S.UPDATE_TIME DESC LIMIT 1) as OPERATE_STATE ");
        } else if (EnumUtil.DB_ORACLE.equals(CommonInit.BUSCONF.getDbName())) {
            strb.append(
                    " (SELECT Q.DESCRIPTION FROM (SELECT N.DESCRIPTION FROM PROCESS_OPERATE S LEFT JOIN MD_PARAM N ON N.CODE = S.OPERATE_STATE AND N.TYPE = 13 WHERE S.OPERATE_ID = M.ID ORDER BY S.UPDATE_TIME DESC) Q WHERE ROWNUM =1) as OPERATE_STATE ");
        }
        if (!StringUtils.isEmpty(mdHostProcess.getStart_script())) {
            strb.append(" , (SELECT 1 FROM MD_HOST_PROCESS B WHERE B.ID = M.ID AND B.ID IN ( ")
                    .append(mdHostProcess.getStart_script()).append(" )) AS FLAG   ");
        }
        strb.append(" FROM MD_HOST_PROCESS M ")
                .append(" LEFT JOIN MON_HOST H ON M.HOST_ID = H.HOSTID ")
                .append(" LEFT JOIN MD_PROCESS HM ON HM.PROCESS_ID = M.PROCESS_ID ")
                .append(" WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdHostProcess.getHost_id())) {
            strb.append(" AND M.HOST_ID = '").append(mdHostProcess.getHost_id()).append("'");
        }
        if (!StringUtils.isEmpty(mdHostProcess.getProcess_id())) {
            strb.append(" AND M.PROCESS_ID = '").append(mdHostProcess.getProcess_id()).append("'");
        }
        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        LOG.info("getHostProcess sql = {}", sql);
        return sql;
    }

    /**
     * 查询数据总数
     *
     * @param parameters
     * @return
     */
    public String getHostProcessInfoCount(Map<String, Object> parameters) {
        MdHostProcess mdHostProcess = (MdHostProcess) parameters.get("hostProcessParam");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(1) ").append(" FROM MD_HOST_PROCESS M ")
                .append(" LEFT JOIN MON_HOST H ON M.HOST_ID = H.HOSTID ")
                .append(" LEFT JOIN MD_PROCESS HM ON HM.PROCESS_ID = M.PROCESS_ID ")
                .append(" WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdHostProcess.getHost_id())) {
            strb.append(" AND M.HOST_ID = '").append(mdHostProcess.getHost_id()).append("'");
        }
        if (!StringUtils.isEmpty(mdHostProcess.getProcess_id())) {
            strb.append(" AND M.PROCESS_ID = '").append(mdHostProcess.getProcess_id()).append("'");
        }
        LOG.info("getHostProcessInfoCount sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 更新状态
     *
     * @param parameters
     * @return
     */
    public String modifyHostProcess(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        MdHostProcess hostProcess = (MdHostProcess) parameters.get("hostProcess");
        strb.append("UPDATE MD_HOST_PROCESS SET PROCESS_KEY='" + hostProcess.getProcess_key() + "',"
                + "START_SCRIPT='" + hostProcess.getStart_script() + "'");
        strb.append(",DESCRIPTION='" + hostProcess.getDescription() + "'");
        strb.append(",STOP_SCRIPT='" + hostProcess.getStop_script() + "'");
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE ID='" + hostProcess.getId() + "'");
        LOG.info("modifyHostProcess sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 调用后台接口失败则修改执行操作表状态，将状态修改为失败
     *
     * @param parameters
     * @return
     */
    public String modifyProcessOperate(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        ProcessOperate processOperate = (ProcessOperate) parameters.get("processOperate");
        strb.append("UPDATE PROCESS_OPERATE SET OPERATE_STATE=" + processOperate.getOperate_state()
                + "'");
        strb.append(",OPERATE_RESULT=" + processOperate.getOperate_result());
        strb.append(",UPDATE_TIME=" + DbSqlUtil.getDateMethod());
        strb.append(" WHERE ID='" + processOperate.getId() + "'");
        LOG.info("modifyProcessOperate sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdProcessListByProcess(Map<String, Object> parameters) {
        MdProcess mdProcess = (MdProcess) parameters.get("mdProcess");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT H.HOSTID AS HOST_ID,H.HOSTNAME,H.ADDR,")
                .append("COALESCE(M.PROCESS_NAME,'").append(mdProcess.getProcess_name())
                .append("') AS PROCESS_NAME,").append("COALESCE(M.PROCESS_KEY,'")
                .append(mdProcess.getProcess_key()).append("') AS PROCESS_KEY,")
                .append("COALESCE(HM.PROCESS_ID,'").append(mdProcess.getProcess_id())
                .append("') AS PROCESS_ID,").append("COALESCE(HM.START_SCRIPT,'') AS START_SCRIPT,")
                .append("COALESCE(HM.STOP_SCRIPT,'') AS STOP_SCRIPT,")
                .append("COALESCE(HM.DESCRIPTION,'') AS DESCRIPTION,")
                .append("COALESCE(HM.PROCESS_ID,'-1') AS FLAG")
                .append(" FROM MON_HOST H LEFT JOIN MD_HOST_PROCESS HM ON HM.HOST_ID = H.HOSTID AND HM.PROCESS_ID='")
                .append(mdProcess.getProcess_id()).append("'")
                .append(" LEFT JOIN MD_PROCESS M ON HM.PROCESS_ID = M.PROCESS_ID AND M.PROCESS_ID='")
                .append(mdProcess.getProcess_id()).append("'").append(" ORDER BY FLAG ASC");
        LOG.info("getMdProcessListByProcess sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdProcessListByHost(Map<String, Object> parameters) {
        MonHost host = (MonHost) parameters.get("host");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT").append(" COALESCE(HM.HOST_ID,'").append(host.getHostid())
                .append("') AS HOST_ID,").append("COALESCE(H.ADDR,'").append(host.getAddr())
                .append("') AS ADDR,").append("COALESCE(H.HOSTNAME,'").append(host.getHostname())
                .append("') AS HOSTNAME,").append("COALESCE(HM.START_SCRIPT,'') AS START_SCRIPT,")
                .append("COALESCE(HM.STOP_SCRIPT,'') AS STOP_SCRIPT,")
                .append("COALESCE(HM.DESCRIPTION,'') AS DESCRIPTION,")
                .append("M.PROCESS_ID AS PROCESS_ID,M.PROCESS_NAME as PROCESS_NAME,")
                .append("M.PROCESS_KEY AS PROCESS_KEY,").append("COALESCE(HM.HOST_ID,'-1') AS FLAG")
                .append(" FROM MD_PROCESS M LEFT JOIN MD_HOST_PROCESS HM ON HM.PROCESS_ID = M.PROCESS_ID AND HM.HOST_ID='")
                .append(host.getHostid()).append("'")
                .append(" LEFT JOIN MON_HOST H ON HM.HOST_ID = H.HOSTID AND H.HOSTID='")
                .append(host.getHostid()).append("'").append(" ORDER BY FLAG ASC");
        LOG.info("getMdProcessListByHost sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 批量插入主机进程关联关系配置数据
     *
     * @param parameters
     * @return
     */
    public String insertHostProcessList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        @SuppressWarnings("unchecked")
        List<MdHostProcess> list = (List<MdHostProcess>) parameters.get("list");
        strb.append(
                "INSERT INTO MD_HOST_PROCESS(ID,HOST_ID,PROCESS_ID,PROCESS_KEY,START_SCRIPT,STOP_SCRIPT,DESCRIPTION,CREATE_TIME,UPDATE_TIME)VALUES");
        for (int i = 0; i < list.size(); i++) {
            MdHostProcess mdHostProcess = list.get(i);
            strb.append("('" + mdHostProcess.getId() + "','" + mdHostProcess.getHost_id() + "','"
                    + mdHostProcess.getProcess_id() + "','" + mdHostProcess.getProcess_key() + "','"
                    + mdHostProcess.getStart_script() + "','" + mdHostProcess.getStop_script()
                    + "','" + mdHostProcess.getDescription() + "'," + DbSqlUtil.getDateMethod()
                    + "," + DbSqlUtil.getDateMethod() + ")");
            if (i < list.size() - 1) {
                strb.append(",");
            }
        }
        LOG.info("insertHostProcessList sql = {}", strb.toString());
        return strb.toString();
    }

    /**
     * 根据id数组查询 ip列表
     *
     * @param parameters
     * @return
     */
    public String getHostProcessIpList(Map<String, Object> parameters) {
        StringBuffer strb = new StringBuffer();
        String[] hostProcessArray = (String[]) parameters.get("hostProcessArray");
        strb.append("SELECT H.ADDR FROM MD_HOST_PROCESS HM "
                + "LEFT JOIN MON_HOST H ON HM.HOST_ID=H.HOSTID WHERE HM.ID IN (");
        for (int i = 0; i < hostProcessArray.length; i++) {
            String id = hostProcessArray[i];
            strb.append("'" + id + "'");
            if (i < hostProcessArray.length - 1) {
                strb.append(",");
            }
        }
        strb.append(") GROUP BY H.ADDR");
        LOG.info("getHostProcessIpList  sql = {}", strb.toString());
        return strb.toString();
    }

}
