package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.operatehistory.OperateHis;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 复杂sql
 *
 * @author yangyc
 *
 */
public class SqlOperateHisProvider {

    private static final Logger LOG = LoggerFactory.make();

    public String getOperateHisList(Map<String, Object> parameters) {
        OperateHis operateHis = (OperateHis) parameters.get("operateHis");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT M.OPERATE_ID , M.OPERATE_NAME , N.DESCRIPTION AS OPERATE_TYPE , M.OPERATE_DESC , " + DbSqlUtil.getDateFormatSql("M.OPERATE_TIME") + " AS OPERATE_TIME ")
                .append("FROM MD_OPERATE_HIS M LEFT JOIN MD_PARAM N ON N.TYPE = 20 AND N.CODE = M.OPERATE_TYPE WHERE 1=1  ");
        if (!StringUtils.isEmpty(operateHis.getStart_time())) {
            strb.append(" AND M.OPERATE_TIME >= '").append(operateHis.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(operateHis.getEnd_time())) {
            strb.append(" AND M.OPERATE_TIME <= '").append(operateHis.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(operateHis.getOperate_name())) {
            strb.append(" AND M.OPERATE_NAME LIKE '%").append(operateHis.getOperate_name()).append("%'");
        }
        if (!StringUtils.isEmpty(operateHis.getOperate_type())) {
            strb.append(" AND M.OPERATE_TYPE = '").append(operateHis.getOperate_type()).append("'");
        }
        if (!Constant.ADMIN_ROLEID.equals(operateHis.getRole_id())){
            strb.append(" AND M.OPERATE_NAME = '").append(operateHis.getCurrent_user()).append("'");
        }
        strb.append(" ORDER BY OPERATE_TIME DESC");

        Page page = (Page) parameters.get("page");
        String sql = strb.toString();
        sql = PagingUtil.getPageSql(sql, page);
        return sql;
    }

    public String getOperateHisInfoCount(Map<String, Object> parameters) {
        OperateHis operateHis = (OperateHis) parameters.get("operateHis");
        StringBuffer strb = new StringBuffer();

        strb.append("SELECT COUNT(*) ")
                .append("FROM MD_OPERATE_HIS M   WHERE 1=1  ");
        if (!StringUtils.isEmpty(operateHis.getStart_time())) {
            strb.append(" AND M.OPERATE_TIME >= '").append(operateHis.getStart_time()).append("'");
        }
        if (!StringUtils.isEmpty(operateHis.getEnd_time())) {
            strb.append(" AND M.OPERATE_TIME <= '").append(operateHis.getEnd_time()).append("'");
        }
        if (!StringUtils.isEmpty(operateHis.getOperate_name())) {
            strb.append(" AND M.OPERATE_NAME LIKE '%").append(operateHis.getOperate_name()).append("%'");
        }
        if (!StringUtils.isEmpty(operateHis.getOperate_type())) {
            strb.append(" AND M.OPERATE_TYPE = '").append(operateHis.getOperate_type()).append("'");
        }
        if (!Constant.ADMIN_ROLEID.equals(operateHis.getRole_id())){
            strb.append(" AND M.OPERATE_NAME = '").append(operateHis.getCurrent_user()).append("'");
        }
        return strb.toString();
    }

}
