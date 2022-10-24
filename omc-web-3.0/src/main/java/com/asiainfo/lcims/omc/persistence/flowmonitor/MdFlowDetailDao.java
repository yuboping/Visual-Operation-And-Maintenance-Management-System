package com.asiainfo.lcims.omc.persistence.flowmonitor;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.flowmonitor.MdFlowDetail;

public interface MdFlowDetailDao {
    @Select("SELECT * FROM MD_FLOW_DETAIL WHERE DETAIL_ID = #{detailId}")
    MdFlowDetail selectByDetailId(@Param("detailId") String detailId);

    @Select("SELECT * FROM MD_FLOW_DETAIL WHERE ACTION_ID = #{actionId}")
    List<MdFlowDetail> selectByActionId(@Param("actionId") String actionId);
}