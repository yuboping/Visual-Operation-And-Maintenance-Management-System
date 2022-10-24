package com.asiainfo.lcims.omc.persistence.shcm;

import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeData;
import com.asiainfo.lcims.omc.model.shcm.WideFlowRangeVo;
import com.asiainfo.lcims.omc.persistence.SqlWideFlowRangeProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface WideFlowRangeDao {

    @SelectProvider(method = "getWideFlowRangeListWithDay", type = SqlWideFlowRangeProvider.class)
    List<WideFlowRangeVo> getWideFlowRangeListWithDay(@Param("wideFlowRangeData") WideFlowRangeData wideFlowRangeData);

    @SelectProvider(method = "getWideFlowRangeListWithMonth", type = SqlWideFlowRangeProvider.class)
    List<WideFlowRangeVo> getWideFlowRangeListWithMonth(@Param("wideFlowRangeData") WideFlowRangeData wideFlowRangeData);
}
