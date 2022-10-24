package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowAction;

public interface MdFlowActionDao {
    @Select("SELECT * FROM MD_FLOW_ACTION WHERE ACTION_ID=#{actionId}")
    MdFlowAction selectByActionId(@Param("actionId") String actionId);

    @Select("SELECT * FROM MD_FLOW_ACTION WHERE NODE_ID=#{nodeId} ORDER BY ORDER_NUM")
    List<MdFlowAction> selectByNodeId(@Param("nodeId") String nodeId);
}
