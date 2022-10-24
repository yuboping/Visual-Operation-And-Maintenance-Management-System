package com.asiainfo.lcims.omc.persistence.gscm5G.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileDiff;
import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileMonitor;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

public class CollectFileDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CollectFileDAOImpl.class);

    public String getMdCollectFile(Map<String, Object> parameters) {
        MdCollectFileMonitor mdCollectFileMonitor = (MdCollectFileMonitor) parameters
                .get("mdCollectFileMonitor");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT ID,HOST_ID,FIREWALL_LOG_SIZE,THIRDPARTY_LOG_SIZE,HOST_IP,COLLECT_TIME,FIREWALL_LOG_NAME,FIREWALL_LOG_STATE,THIRDPARTY_LOG_NAME,THIRDPARTY_LOG_STATE,CASE FIREWALL_LOG_STATE WHEN '采集完成' THEN '下载' ELSE '--' END AS FIREWALL_LOG_DOWNLOAD,CASE THIRDPARTY_LOG_STATE WHEN '输出完成' THEN '下载' ELSE '--' END AS THIRDPARTY_LOG_DOWNLOAD FROM MD_COLLECT_FILE_MONITOR WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdCollectFileMonitor.getHost_id())) {
            strb.append(" AND HOST_ID='").append(mdCollectFileMonitor.getHost_id()).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileMonitor.getHost_ip())) {
            strb.append(" AND HOST_IP='")
                    .append(StringUtil.SqlFilter(mdCollectFileMonitor.getHost_ip())).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileMonitor.getCollect_time())) {
            strb.append(" AND " + DbSqlUtil.getDateSql("COLLECT_TIME") + "='")
                    .append(mdCollectFileMonitor.getCollect_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileMonitor.getId())) {
            strb.append(" AND M.ID='").append(mdCollectFileMonitor.getId()).append("'");
        }
        LOG.debug("getMdCollectFile sql = {}", strb.toString());
        return strb.toString();
    }

    public String getMdCollectFileDiff(Map<String, Object> parameters) {
        MdCollectFileDiff mdCollectFileDiff = (MdCollectFileDiff) parameters
                .get("mdCollectFileDiff");
        StringBuffer strb = new StringBuffer();
        strb.append(
                "SELECT ID,HOST_ID,HOST_IP," + DbSqlUtil.getDateSql("COLLECT_TIME")
                        + " AS COLLECT_TIME,FIREWALL_LOG_NAME,THIRDPARTY_LOG_NAME,FIREWALL_LOG_NUM,THIRDPARTY_LOG_NUM,DIFF_LOG_NAME FROM MD_COLLECT_FILE_DIFF WHERE 1=1 ");
        if (!StringUtils.isEmpty(mdCollectFileDiff.getHost_id())) {
            strb.append(" AND HOST_ID='").append(mdCollectFileDiff.getHost_id()).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileDiff.getHost_ip())) {
            strb.append(" AND HOST_IP='")
                    .append(StringUtil.SqlFilter(mdCollectFileDiff.getHost_ip())).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileDiff.getCollect_time())) {
            strb.append(" AND " + DbSqlUtil.getDateSql("COLLECT_TIME") + "='")
                    .append(mdCollectFileDiff.getCollect_time()).append("'");
        }
        if (!StringUtils.isEmpty(mdCollectFileDiff.getId())) {
            strb.append(" AND M.ID='").append(mdCollectFileDiff.getId()).append("'");
        }
        LOG.debug("getMdCollectFileDiff sql = {}", strb.toString());
        return strb.toString();
    }
}
