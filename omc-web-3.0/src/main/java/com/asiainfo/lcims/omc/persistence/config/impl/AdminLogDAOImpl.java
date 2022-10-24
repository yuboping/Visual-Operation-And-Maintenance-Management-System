package com.asiainfo.lcims.omc.persistence.config.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.persistence.po.config.MdAdminLog;
import com.asiainfo.lcims.omc.util.DbSqlUtil;

/**
 * AdminLogDAO的sql实现
 * 
 * @author zhujiansheng
 * @date 2019年4月10日 上午11:09:11
 * @version V1.0
 */
public class AdminLogDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdminLogDAOImpl.class);

    public String insertAdminLog(Map<String, Object> parameters) {
        MdAdminLog adminLog = (MdAdminLog) parameters.get("adminLog");
        String sql = "INSERT INTO M_ADMIN_LOG(SERNO,ADMIN,AREANO,ROLENAME,OPERATETIME,IPADDRESS,FUNCID,USERNAME,NOTE)VALUES("
                + "'" + adminLog.getSerno() + "','" + adminLog.getAdmin() + "','"
                + adminLog.getAreano() + "','" + adminLog.getRolename() + "',"
                + DbSqlUtil.getDateMethod() + ",'" + adminLog.getIpaddress() + "','"
                + adminLog.getFuncid() + "','" + adminLog.getUsername() + "','')";
        LOG.debug("AdminLogDAOImpl insertAdminLog sql = {}", sql);
        return sql;
    }

}
