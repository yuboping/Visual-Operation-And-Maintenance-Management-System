package com.asiainfo.lcims.omc.persistence.shcm;

import com.asiainfo.lcims.omc.model.shcm.OnlineUserStatisticVo;
import com.asiainfo.lcims.omc.model.shcm.QueryResultChartData;
import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.persistence.SqlShcmProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface ShcmReportDao {

    @SelectProvider(method = "getOnlineUserStatisticListWithHour", type = SqlShcmProvider.class)
    List<StatisData> getOnlineUserStatisticListWithHour(@Param("onlineUserStatisticVo") OnlineUserStatisticVo onlineUserStatisticVo);
    
    @SelectProvider(method = "getQueryAuthFailReasonInfos", type = SqlShcmProvider.class)
    List<QueryResultChartData> queryAuthFailReasonInfos(@Param("paramsMap") Map<String, Object> params);
    
}
