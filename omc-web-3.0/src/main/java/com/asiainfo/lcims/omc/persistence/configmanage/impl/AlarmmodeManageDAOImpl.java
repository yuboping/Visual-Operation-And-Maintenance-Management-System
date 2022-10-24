package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

/**
 * 实现MD_ALARM_MODE中复杂的sql语句
 * 
 * @author zhujiansheng
 * @date 2018年8月30日 下午4:00:54
 * @version V1.0
 */
public class AlarmmodeManageDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmmodeManageDAOImpl.class);

    public String getMdAlarmMode(Map<String, Object> paras) {
        MdAlarmMode mdAlarmMode = (MdAlarmMode) paras.get("mdAlarmMode");
        StringBuffer buffer = new StringBuffer(
                "SELECT t1.MODEID,t1.MODENAME,t1.MODEATTR,t1.MODETYPE,t2.DESCRIPTION FROM MD_ALARM_MODE t1,MD_PARAM t2 WHERE t2.TYPE=4 AND t1.MODETYPE=t2.CODE");
        if (!StringUtils.isEmpty(mdAlarmMode.getModename())) {
            String modename = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdAlarmMode.getModename()));
            buffer.append(" AND t1.MODENAME LIKE '%").append(modename).append("%'");
        }
        if (!StringUtils.isEmpty(mdAlarmMode.getModetype())) {
            String modetype = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdAlarmMode.getModetype()));
            buffer.append(" AND t1.MODETYPE='").append(modetype).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmMode.getModeid())) {
            buffer.append(" AND t1.MODEID='").append(mdAlarmMode.getModeid()).append("'");
        }
        LOG.debug("getMdAlarmMode sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String updateMdAlarmMode(Map<String, Object> paras) {
        MdAlarmMode mdAlarmMode = (MdAlarmMode) paras.get("mdAlarmMode");
        StringBuffer buffer = new StringBuffer("UPDATE MD_ALARM_MODE SET MODEID=MODEID");
        if (!StringUtils.isEmpty(mdAlarmMode.getModename())) {
            buffer.append(",MODENAME='").append(mdAlarmMode.getModename()).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmMode.getModeattr())) {
            buffer.append(",MODEATTR='").append(mdAlarmMode.getModeattr()).append("'");
        }
        if (!StringUtils.isEmpty(mdAlarmMode.getModetype())) {
            buffer.append(",MODETYPE='").append(mdAlarmMode.getModetype()).append("'");
        }
        buffer.append(" WHERE MODEID='").append(mdAlarmMode.getModeid()).append("'");
        LOG.debug("updateMdAlarmMode sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String deleteMdAlarmMode(Map<String, Object> paras) {
        String modes = (String) paras.get("modes");
        String alarmmodeId = (String) paras.get("alarmmodeId");
        if ("''".equals(modes)) {
            StringBuffer buffer = new StringBuffer("DELETE FROM MD_ALARM_MODE WHERE MODEID=");
            buffer.append("'").append(alarmmodeId).append("'");
            LOG.debug("deleteMdAlarmMode sql = {}", buffer.toString());
            return buffer.toString();
        } else {
            StringBuffer buffer = new StringBuffer(
                    "DELETE FROM MD_ALARM_MODE WHERE MODEID NOT IN (");
            buffer.append(modes).append(")");
            buffer.append(" AND MODEID='");
            buffer.append(alarmmodeId).append("'");
            LOG.debug("deleteMdAlarmMode sql = {}", buffer.toString());
            return buffer.toString();
        }
    }
}
