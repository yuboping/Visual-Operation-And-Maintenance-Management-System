package com.asiainfo.lcims.omc.persistence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.util.DbSqlUtil;

/**
 * 复杂sql
 *
 * @author yangyc
 *
 */
public class SqlMaintoolProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlMaintoolProvider.class);

    public String getHostCapabilityList(Map<String, Object> parameters) {
        String hostname = (String) parameters.get("hostname");
        String tableName = (String) parameters.get("tableName");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT N.HOSTID AS HOST_ID , N.HOSTNAME AS HOST_NAME, N.ADDR AS IP, N.MEMORY , N.CPU , S.CPU_USE_PERCENT , S.MEMORY_USE_PERCENT , " + DbSqlUtil.getTimeSql("S.STIME") + " AS STIME FROM MON_HOST N ")
                .append("LEFT JOIN (SELECT CPU.HOST_ID, CPU.STIME, CPU.CPU_USE_PERCENT, MEMORY.MEMORY_USE_PERCENT FROM (SELECT M.HOST_ID, M.STIME, MAX(M.MVALUE) AS CPU_USE_PERCENT FROM ")
                .append(tableName)
                .append(" M LEFT JOIN md_metric md ON M.METRIC_ID = md.ID WHERE md.METRIC_IDENTITY = 'cpu_use_rate' and M.STIME = ( SELECT MAX(T.STIME) FROM ")
                .append(tableName)
                .append(" T ) GROUP BY M.HOST_ID, M.STIME) CPU LEFT JOIN (SELECT M.HOST_ID, M.STIME, MAX(M.MVALUE) AS MEMORY_USE_PERCENT FROM ")
                .append(tableName)
                .append(" M LEFT JOIN md_metric md ON M.METRIC_ID = md.ID WHERE md.METRIC_IDENTITY = 'memory_use_rate' and M.STIME = ( SELECT MAX(T.STIME) FROM ")
                .append(tableName)
                .append(" T ) GROUP BY M.HOST_ID, M.STIME) MEMORY ON CPU.STIME = ( SELECT MAX(T.STIME) FROM ")
                .append(tableName)
                .append(" T ) WHERE CPU.HOST_ID = MEMORY.HOST_ID AND CPU.STIME = MEMORY.STIME GROUP BY CPU.HOST_ID, CPU.STIME) ")
                .append("S ON S.HOST_ID = N.HOSTID ");
        if (!StringUtils.isEmpty(hostname)) {
            strb.append(" WHERE N.ADDR = '").append(hostname).append("'");
        }
        String sql = strb.toString();
        LOG.debug("getHostCapabilityList sql = {}", sql);
        return sql;
    }

}
