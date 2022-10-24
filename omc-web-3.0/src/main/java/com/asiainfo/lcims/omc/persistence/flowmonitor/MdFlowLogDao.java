package com.asiainfo.lcims.omc.persistence.flowmonitor;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowLog;

public interface MdFlowLogDao {
    @InsertProvider(method = "insertFlowLog", type = FlowMonitorProvider.class)
    int insert(@Param("info") MdFlowLog info);
}
