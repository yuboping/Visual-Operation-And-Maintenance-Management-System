package com.asiainfo.lcims.omc.persistence.hncu.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.hncu.MdLoginLog;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;

public class LoginLogDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogDAOImpl.class);

    public String getLoginLogCount(Map<String, Object> parameters) {
        MdLoginLog loginLog = (MdLoginLog) parameters.get("loginLog");
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(*) FROM MD_LOGIN_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(loginLog.getStart_time())) {
            buffer.append(" AND M.LOGIN_TIME >= '").append(loginLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEnd_time())) {
            buffer.append(" AND M.LOGOUT_TIME <= '").append(loginLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getAdmin_account())) {
            buffer.append(" AND M.ADMIN_ACCOUNT LIKE '%").append(loginLog.getAdmin_account())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(loginLog.getLogin_ip())) {
            buffer.append(" AND M.LOGIN_IP = '").append(loginLog.getLogin_ip()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(loginLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        LOG.info("getLoginLogCount sql = {}", sql);
        return sql;
    }

    public String getLoginLogList(Map<String, Object> parameters) {
        Page page = (Page) parameters.get("page");
        MdLoginLog loginLog = (MdLoginLog) parameters.get("loginLog");
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                "SELECT M.EVENT_OCCUR_HOST,M.ADMIN_ACCOUNT,M.LOGIN_IP,M.ONLINE_TIME,M.LOGIN_FLAG,")
                .append(DbSqlUtil.getDateFormatSql("M.LOGIN_TIME")).append(" AS LOGIN_TIME,")
                .append(DbSqlUtil.getDateFormatSql("M.LOGOUT_TIME")).append(" AS LOGOUT_TIME")
                .append(" FROM MD_LOGIN_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(loginLog.getStart_time())) {
            buffer.append(" AND M.LOGIN_TIME >= '").append(loginLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEnd_time())) {
            buffer.append(" AND M.LOGOUT_TIME <= '").append(loginLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getAdmin_account())) {
            buffer.append(" AND M.ADMIN_ACCOUNT LIKE '%").append(loginLog.getAdmin_account())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(loginLog.getLogin_ip())) {
            buffer.append(" AND M.LOGIN_IP = '").append(loginLog.getLogin_ip()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(loginLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        sql = PagingUtil.getPageSql(sql, page);
        LOG.info("query login log list sql = {}", sql);
        return sql;
    }

    public String exportLoginLogList(Map<String, Object> parameters) {
        MdLoginLog loginLog = (MdLoginLog) parameters.get("loginLog");
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                "SELECT M.EVENT_OCCUR_HOST,M.ADMIN_ACCOUNT,M.LOGIN_IP,M.ONLINE_TIME,M.LOGIN_FLAG,")
                .append(DbSqlUtil.getDateFormatSql("M.LOGIN_TIME")).append(" AS LOGIN_TIME,")
                .append(DbSqlUtil.getDateFormatSql("M.LOGOUT_TIME")).append(" AS LOGOUT_TIME")
                .append(" FROM MD_LOGIN_LOG M WHERE 1=1 ");
        if (!StringUtils.isEmpty(loginLog.getStart_time())) {
            buffer.append(" AND M.LOGIN_TIME >= '").append(loginLog.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEnd_time())) {
            buffer.append(" AND M.LOGOUT_TIME <= '").append(loginLog.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getAdmin_account())) {
            buffer.append(" AND M.ADMIN_ACCOUNT LIKE '%").append(loginLog.getAdmin_account())
                    .append("%'");
        }
        if (!StringUtils.isEmpty(loginLog.getLogin_ip())) {
            buffer.append(" AND M.LOGIN_IP = '").append(loginLog.getLogin_ip()).append("'");
        }
        if (!StringUtils.isEmpty(loginLog.getEvent_occur_host())) {
            buffer.append(" AND M.EVENT_OCCUR_HOST = '").append(loginLog.getEvent_occur_host())
                    .append("'");
        }
        String sql = String.valueOf(buffer);
        LOG.info("export login log list sql = {}", sql);
        return sql;
    }
}
