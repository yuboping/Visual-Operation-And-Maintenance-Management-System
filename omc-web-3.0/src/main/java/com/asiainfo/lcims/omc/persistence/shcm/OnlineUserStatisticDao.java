package com.asiainfo.lcims.omc.persistence.shcm;

import com.asiainfo.lcims.omc.model.shcm.OnlineUserStatisticVo;
import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.persistence.SqlShcmProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface OnlineUserStatisticDao {

    @SelectProvider(method = "getOnlineUserStatisticListWithHour", type = SqlShcmProvider.class)
    List<StatisData> getOnlineUserStatisticListWithHour(@Param("onlineUserStatisticVo") OnlineUserStatisticVo onlineUserStatisticVo);

    @SelectProvider(method = "getOnlineUserStatisticListWithDay", type = SqlShcmProvider.class)
    List<StatisData> getOnlineUserStatisticListWithDay(@Param("onlineUserStatisticVo") OnlineUserStatisticVo onlineUserStatisticVo);

    @SelectProvider(method = "getOnlineUserStatisticListWithMonth", type = SqlShcmProvider.class)
    List<StatisData> getOnlineUserStatisticListWithMonth(@Param("onlineUserStatisticVo") OnlineUserStatisticVo onlineUserStatisticVo);
}
