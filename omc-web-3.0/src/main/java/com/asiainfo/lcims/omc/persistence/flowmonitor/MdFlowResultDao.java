package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.flowmonitor.FlowDetail;
import com.asiainfo.lcims.omc.model.flowmonitor.FlowOverview;
import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowResult;

public interface MdFlowResultDao {
    @Select("SELECT * FROM MD_FLOW_RESULT WHERE SERIAL_NUM= #{serialNum}")
    List<MdFlowResult> selectBySerialNum(@Param("serialNum") String serialNum);

    @InsertProvider(method = "insertFlowResult", type = FlowMonitorProvider.class)
    int insert(@Param("info") MdFlowResult info);

    @UpdateProvider(method = "updateFlowResult", type = FlowMonitorProvider.class)
    int updateResult(@Param("info") MdFlowResult info);

    @Select("SELECT T1.TASK_ID,T1.TASK_NAME,DATE_FORMAT(TT.CREATE_TIME, '%Y-%m-%d %H:%i:%s') CREATE_TIME,TT.RESULT_FLAG,TT.SERIAL_NUM "
            + "FROM MD_FLOW_TASK T1 LEFT JOIN( SELECT T2.* FROM MD_FLOW_RESULT T2,( "
            + "    SELECT TASK_ID,MAX(CREATE_TIME) CREATE_TIME FROM MD_FLOW_RESULT WHERE ISEND='1' GROUP BY TASK_ID "
            + "  ) T3 WHERE T2.ISEND='1' AND T2.TASK_ID=T3.TASK_ID AND T2.CREATE_TIME=T3.CREATE_TIME "
            + ") TT ON T1.TASK_ID=TT.TASK_ID")
    List<FlowOverview> getAllFlowResult();

    @Select("SELECT T1.TASK_ID,T1.NODE_ID,T1.NODE_NAME,T2.ISEND,T2.RESULT_FLAG,T2.RESULT_DESCRIPT,"
            + "DATE_FORMAT(T2.UPDATE_TIME, '%Y-%m-%d %H:%i:%s') UPDATE_TIME FROM "
            + "(SELECT * FROM MD_FLOW_NODE WHERE TASK_ID=#{task_id} ) T1 LEFT JOIN MD_FLOW_RESULT T2 "
            + "ON T1.NODE_ID=T2.NODE_ID AND T1.TASK_ID=T2.TASK_ID AND T2.SERIAL_NUM=#{serial_num} "
            + "ORDER BY T1.ORDER_NUM")
    List<FlowDetail> getFlowDetail(@Param("task_id") String task_id,
            @Param("serial_num") String serial_num);
}
