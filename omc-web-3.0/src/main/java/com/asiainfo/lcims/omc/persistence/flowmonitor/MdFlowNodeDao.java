package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowNode;

public interface MdFlowNodeDao {
    @Select("SELECT * FROM MD_FLOW_NODE WHERE NODE_ID = #{nodeId}")
    MdFlowNode selectByNodeId(@Param("nodeId") String nodeId);

    @Select("SELECT * FROM MD_FLOW_NODE WHERE TASK_ID = #{taskId} ORDER BY ORDER_NUM")
    List<MdFlowNode> selectByTaskId(@Param("taskId") String taskId);
}
