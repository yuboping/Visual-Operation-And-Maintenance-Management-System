package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.model.configmanage.MdHostProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.EnumUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.asiainfo.lcims.omc.util.page.PagingUtil;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 复杂sql
 *
 * @author yangyc
 *
 */
public class SqlAisProvider {

    private static final Logger LOG = LoggerFactory.make();

    /**
     * 根据查询条件查询巡检组列表
     *
     * @param parameters
     * @return
     */
    public String getAisGroupList(Map<String, Object> parameters) {
        AisGroupModel aisGroup = (AisGroupModel) parameters.get("aisGroup");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT M.GROUP_ID, M.GROUP_NAME, M.STATUS, M.DESCRIPTION , N.DESCRIPTION AS ICON_NAME, M.ICON  FROM WD_INS_ITEM_GROUP M LEFT JOIN MD_PARAM N ON N.CODE = M.ICON WHERE 1=1 ");
        if (!StringUtils.isEmpty(aisGroup.getGroup_id())) {
            strb.append(" AND M.GROUP_ID='").append(aisGroup.getGroup_id()).append("'");
        }
        if (!StringUtils.isEmpty(aisGroup.getGroup_name())) {
            strb.append(" AND M.GROUP_NAME LIKE '%").append(aisGroup.getGroup_name()).append("%'");
        }
        return strb.toString();
    }

    /**
     * 根据查询条件查询巡检组数量
     *
     * @param parameters
     * @return
     */
    public String getAisGroupCount(Map<String, Object> parameters) {
        AisGroupModel aisGroup = (AisGroupModel) parameters.get("aisGroup");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT COUNT(*) FROM WD_INS_ITEM_GROUP M WHERE 1=1 ");
        if (!StringUtils.isEmpty(aisGroup.getGroup_id())) {
            strb.append(" AND M.GROUP_ID='").append(aisGroup.getGroup_id()).append("'");
        }
        if (!StringUtils.isEmpty(aisGroup.getGroup_name())) {
            strb.append(" AND M.GROUP_NAME ='").append(aisGroup.getGroup_name()).append("'");
        }
        return strb.toString();
    }

    /**
     * 根据查询条件查询巡检组列表
     *
     * @param parameters
     * @return
     */
    public String getAisGroupNameList(Map<String, Object> parameters) {
        AisScheduleModel aisSchedule = (AisScheduleModel) parameters.get("aisSchedule");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM WD_INS_ITEM_GROUP WHERE GROUP_ID IN (").append(aisSchedule.getGroup_ids()).append(")");

        return strb.toString();
    }

    /**
     * 根据查询条件查询巡检组列表
     *
     * @param parameters
     * @return
     */
    public String getAisScheduleList(Map<String, Object> parameters) {
        AisScheduleModel aisSchedule = (AisScheduleModel) parameters.get("aisSchedule");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM WD_INS_SCHEDULE WHERE 1=1 ");
        if (!StringUtils.isEmpty(aisSchedule.getId())) {
            strb.append(" AND ID='").append(aisSchedule.getId()).append("'");
        }
        if (!StringUtils.isEmpty(aisSchedule.getTitle())) {
            strb.append(" AND TITLE LIKE '%").append(aisSchedule.getTitle()).append("%'");
        }
        return strb.toString();
    }
    
    
    public String getRportCountByTime(Map<String, Object> parameters) {
        String searchkey = (String) parameters.get("searchkey");
        String begintime = (String) parameters.get("begintime");
        String endtime = (String) parameters.get("endtime");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(1) FROM WD_INS_REPORT WHERE 1=1 ");
        if (DbSqlUtil.isMySql()) {
            strb.append("AND CREATE_TIME>= STR_TO_DATE('").append(begintime)
                    .append("','%Y-%m-%d %H:%i:%s') AND CREATE_TIME<=STR_TO_DATE('").append(endtime)
                    .append("','%Y-%m-%d %H:%i:%s')");
        } else if (DbSqlUtil.isOracle()) {
            strb.append("AND CREATE_TIME>= TO_DATE('").append(begintime)
                    .append("','yyyy-MM-dd hh24:mi:ss') AND CREATE_TIME<=TO_DATE('").append(endtime)
                    .append("','yyyy-MM-dd hh24:mi:ss')");
        } else {
            strb.append("AND CREATE_TIME>= STR_TO_DATE('").append(begintime)
                    .append("','%Y-%m-%d %H:%i:%s') AND CREATE_TIME<=STR_TO_DATE('").append(endtime)
                    .append("','%Y-%m-%d %H:%i:%s')");
        }
        if (!StringUtils.isEmpty(searchkey)) {
            strb.append(" AND TITLE LIKE '%").append(searchkey).append("%'");
        }
        return strb.toString();
    }
    
    public String getReportBytime(Map<String, Object> parameters) {
        String searchkey = (String) parameters.get("searchkey");
        String begintime = (String) parameters.get("begintime");
        String endtime = (String) parameters.get("endtime");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT ID,TITLE,CREATE_TIME,REPORTLINK FROM WD_INS_REPORT WHERE 1=1 ");
        if (DbSqlUtil.isMySql()) {
            strb.append("AND CREATE_TIME>= STR_TO_DATE('").append(begintime)
                    .append("','%Y-%m-%d %H:%i:%s') AND CREATE_TIME<=STR_TO_DATE('").append(endtime)
                    .append("','%Y-%m-%d %H:%i:%s')");
        } else if (DbSqlUtil.isOracle()) {
            strb.append("AND CREATE_TIME>= TO_DATE('").append(begintime)
                    .append("','yyyy-MM-dd hh24:mi:ss') AND CREATE_TIME<=TO_DATE('").append(endtime)
                    .append("','yyyy-MM-dd hh24:mi:ss')");
        } else {
            strb.append("AND CREATE_TIME>= STR_TO_DATE('").append(begintime)
                    .append("','%Y-%m-%d %H:%i:%s') AND CREATE_TIME<=STR_TO_DATE('").append(endtime)
                    .append("','%Y-%m-%d %H:%i:%s')");
        }
        if (!StringUtils.isEmpty(searchkey)) {
            strb.append(" AND TITLE LIKE '%").append(searchkey).append("%'");
        }
        strb.append(" ORDER BY CREATE_TIME DESC");
        return strb.toString();
    }

    /**
     *
     * @Title: addSchedule
     * @Description: TODO(插入巡检计划)
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String addSchedule(Map<String, Object> parameters) {
        INSSchedule schedule = (INSSchedule) parameters
                .get("schedule");
        StringBuilder strb = new StringBuilder();
        strb.append(
                "INSERT INTO WD_INS_SCHEDULE(ID,TITLE,TIMER,GROUP_IDS,EMAILS,PHONES,SCHEDULE_TYPE,CREATE_TIME,UPDATE_TIME) VALUES('")
                .append(schedule.getId()).append("','")
                .append(schedule.getTitle()).append("','")
                .append(schedule.getTimer()).append("','");
        if (null != schedule.getGroup_ids()) {
            strb.append(schedule.getGroup_ids()).append("','");
        }else {
            strb.append("','");
        }
        if (null != schedule.getEmails()) {
            strb.append(schedule.getEmails()).append("','");
        }else {
            strb.append("','");
        }
        if (null != schedule.getPhones()) {
            strb.append(schedule.getPhones()).append("',");
        }else {
            strb.append("',");
        }
        strb.append(schedule.getSchedule_type()).append(",")
                .append(DbSqlUtil.getDateMethod()).append(",")
                .append(DbSqlUtil.getDateMethod())
                .append(")");
        return strb.toString();
    }

    /**
     *
     * @Title: updateSchedule
     * @Description: TODO(更新巡检计划)
     * @param @param parameters
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public String updateSchedule(Map<String, Object> parameters) {
        INSSchedule schedule = (INSSchedule) parameters
                .get("schedule");
        StringBuilder strb = new StringBuilder(
                "UPDATE WD_INS_SCHEDULE SET ");
        if (null != schedule.getTitle()) {
            strb.append(" TITLE = '")
                    .append(schedule.getTitle()).append("' ");
        }
        if (null != schedule.getTimer()) {
            strb.append(",TIMER='").append(schedule.getTimer())
                    .append("' ");
        }
        if (null != schedule.getGroup_ids()) {
            strb.append(",GROUP_IDS='")
                    .append(schedule.getGroup_ids()).append("' ");
        }
        if (null != schedule.getEmails()) {
            strb.append(",EMAILS='")
                    .append(schedule.getEmails()).append("' ");
        }
        if (null != schedule.getPhones()) {
            strb.append(",PHONES='").append(schedule.getPhones()).append("' ");
        }
        if (0 != schedule.getSchedule_type()) {
            strb.append(",SCHEDULE_TYPE=")
                    .append(schedule.getSchedule_type()).append(" ");
        }
        strb.append(",UPDATE_TIME=")
                .append(DbSqlUtil.getDateMethod()).append(" ");

        strb.append("WHERE ID='").append(schedule.getId())
                .append("'");
        LOG.debug("updateSchedule sql = {}", strb.toString());
        return strb.toString();
    }
}
