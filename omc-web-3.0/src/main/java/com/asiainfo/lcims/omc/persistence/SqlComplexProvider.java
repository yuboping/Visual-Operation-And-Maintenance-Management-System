package com.asiainfo.lcims.omc.persistence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.configmanage.MdFactory;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

/**
 * 复杂sql语句
 * 
 * @author zhujiansheng
 * @date 2018年7月25日 上午10:35:25
 * @version V1.0
 */
public class SqlComplexProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlComplexProvider.class);

    public String getMdFactory(Map<String, Object> paras) {
        MdFactory mdFactory = (MdFactory) paras.get("mdFactory");
        StringBuffer buffer = new StringBuffer(
                "SELECT ID,FACTORY_NAME,DESCRIPTION FROM MD_FACTORY WHERE 1=1");
        if (!StringUtils.isEmpty(mdFactory.getFactory_name())) {
            String factoryName = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdFactory.getFactory_name()));
            buffer.append(" AND FACTORY_NAME LIKE '%").append(factoryName)
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdFactory.getDescription())) {
            String description = DbSqlUtil
                    .replaceSpecialStr(StringUtil.SqlFilter(mdFactory.getDescription()));
            buffer.append(" AND DESCRIPTION='").append(description).append("'");
        }
        if (!StringUtils.isEmpty(mdFactory.getId())) {
            buffer.append(" AND ID='").append(mdFactory.getId()).append("'");
        }
        LOG.info("getMdFactory sql = {}", buffer.toString());
        return buffer.toString();
    }

    public String updateMdFactory(Map<String, Object> paras) {
        MdFactory mdFactory = (MdFactory) paras.get("mdFactory");
        StringBuffer buffer = new StringBuffer("UPDATE MD_FACTORY SET ID=ID");
        if (!StringUtils.isEmpty(mdFactory.getFactory_name())) {
            buffer.append(",FACTORY_NAME='").append(mdFactory.getFactory_name()).append("'");
        }
        if (!StringUtils.isEmpty(mdFactory.getDescription())) {
            buffer.append(",DESCRIPTION='").append(mdFactory.getDescription()).append("'");
        }
        buffer.append(" WHERE ID='").append(mdFactory.getId()).append("'");
        LOG.info("updateMdFactory sql = {}", buffer.toString());
        return buffer.toString();
    }
}
