package com.asiainfo.lcims.omc.persistence.hncu.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.hncu.MdSystemLog;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class SystemLogDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogDAOImpl.class);

    public String getSystemLogCount(Map<String, Object> parameters) {
        MdSystemLog systemLog = (MdSystemLog) parameters.get("systemLog");
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(*) FROM MD_SYSTEM_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(systemLog.getStart_time())) {
            buffer.append(" AND M.SYSTEM_TIME >= '").append(systemLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEnd_time())) {
            buffer.append(" AND M.SYSTEM_TIME <= '").append(systemLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getProcess_name())) {
            buffer.append(" AND M.PROCESS_NAME LIKE '%").append(systemLog.getProcess_name())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(systemLog.getError_level())) {
            buffer.append(" AND M.ERROR_LEVEL = '").append(systemLog.getError_level())
                    .append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(systemLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        LOG.info("getSystemLogCount sql = {}", sql);
        return sql;
    }

    public String getSystemLogList(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        MdSystemLog systemLog = (MdSystemLog) parameters.get("systemLog");
        StringBuffer buffer = new StringBuffer();

        buffer.append(
                "SELECT M.EVENT_OCCUR_HOST,M.HOST_NAME,M.PROCESS_NAME,M.ERROR_LEVEL,M.MSG_DATA,")
                .append(DbSqlUtil.getDateFormatSql("M.SYSTEM_TIME")).append(" AS SYSTEM_TIME")
                .append(" FROM MD_SYSTEM_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(systemLog.getStart_time())) {
            buffer.append(" AND M.SYSTEM_TIME >= '").append(systemLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEnd_time())) {
            buffer.append(" AND M.SYSTEM_TIME <= '").append(systemLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getProcess_name())) {
            buffer.append(" AND M.PROCESS_NAME LIKE '%").append(systemLog.getProcess_name())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(systemLog.getError_level())) {
            buffer.append(" AND M.ERROR_LEVEL = '").append(systemLog.getError_level()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(systemLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.info("query system log list sql = {}", sql);
        return sql;
    }

    public String exportSystemLogList(Map<String, Object> parameters) {
        MdSystemLog systemLog = (MdSystemLog) parameters.get("systemLog");
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                "SELECT M.EVENT_OCCUR_HOST,M.HOST_NAME,M.PROCESS_NAME,M.ERROR_LEVEL,M.MSG_DATA,")
                .append(DbSqlUtil.getDateFormatSql("M.SYSTEM_TIME")).append(" AS SYSTEM_TIME")
                .append(" FROM MD_SYSTEM_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(systemLog.getStart_time())) {
            buffer.append(" AND M.SYSTEM_TIME >= '").append(systemLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEnd_time())) {
            buffer.append(" AND M.SYSTEM_TIME <= '").append(systemLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getProcess_name())) {
            buffer.append(" AND M.PROCESS_NAME LIKE '%").append(systemLog.getProcess_name())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(systemLog.getError_level())) {
            buffer.append(" AND M.ERROR_LEVEL = '").append(systemLog.getError_level()).append("'");
        }
        if (!StringUtils.isEmpty(systemLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(systemLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        LOG.info("export system log list sql = {}", sql);
        return sql;
    }

}
