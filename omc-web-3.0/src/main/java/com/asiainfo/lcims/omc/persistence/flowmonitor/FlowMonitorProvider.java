package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.Map;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowResult;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

public class FlowMonitorProvider {
    public String insertFlowLog(Map<String, Object> parameters) {
        MdFlowLog info = (MdFlowLog) parameters.get("info");

        StringBuilder strb = new StringBuilder();
        strb.append("INSERT INTO MD_FLOW_LOG (ID,SERIAL_NUM,RESULT_TYPE,TASK_ID,NODE_ID,ACTION_ID")
                .append(",DETAIL_ID,RESULT_FLAG,RESULT_DATA,RESULT_DESCRIPT,CREATE_TIME) VALUES('")
                .append(IDGenerateUtil.getUuid()).append("','").append(info.getSerial_num())
                .append("','").append(info.getResult_type()).append("','").append(info.getTask_id())
                .append("','").append(info.getNode_id()).append("','").append(info.getAction_id())
                .append("','").append(info.getDetail_id()).append("','")
                .append(info.getResult_flag()).append("','").append(info.getResult_data())
                .append("','").append(info.getResult_descript()).append("',")
                .append(DbSqlUtil.getDateMethod()).append(")");
        return strb.toString();
    }

    public String insertFlowResult(Map<String, Object> parameters) {
        MdFlowResult info = (MdFlowResult) parameters.get("info");
        StringBuilder strb = new StringBuilder();
        strb.append("INSERT INTO MD_FLOW_RESULT (ID,SERIAL_NUM,TASK_ID,NODE_ID,")
                .append("RESULT_FLAG,RESULT_DESCRIPT,ISEND,CREATE_TIME) VALUES('")
                .append(IDGenerateUtil.getUuid()).append("','").append(info.getSerial_num())
                .append("','").append(info.getTask_id()).append("','").append(info.getNode_id())
                .append("','").append(info.getResult_flag()).append("','")
                .append(info.getResult_descript()).append("',");
        if (info.getIsend() == null) {
            strb.append("null,");
        } else {
            strb.append("'").append(info.getIsend()).append("',");
        }
        strb.append(DbSqlUtil.getDateMethod()).append(")");
        return strb.toString();
    }

    public String updateFlowResult(Map<String, Object> parameters) {
        MdFlowResult info = (MdFlowResult) parameters.get("info");
        StringBuilder strb = new StringBuilder();
        strb.append("UPDATE MD_FLOW_RESULT SET RESULT_FLAG='").append(info.getResult_flag())
                .append("',RESULT_DESCRIPT='").append(info.getResult_descript())
                .append("',UPDATE_TIME=").append(DbSqlUtil.getDateMethod());
        if (info.getIsend() != null) {
            strb.append(",ISEND='").append(info.getIsend()).append("'");
        }
        strb.append(" WHERE SERIAL_NUM='").append(info.getSerial_num()).append("' AND NODE_ID='")
                .append(info.getNode_id()).append("'");
        return strb.toString();
    }
}
