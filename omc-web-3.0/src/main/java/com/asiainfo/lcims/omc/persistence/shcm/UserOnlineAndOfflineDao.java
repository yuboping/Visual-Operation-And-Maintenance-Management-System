package com.asiainfo.lcims.omc.persistence.shcm;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.persistence.SqlShcmUserOnlineOffLineProvider;

public interface UserOnlineAndOfflineDao {


    

    @SelectProvider(method = "getQueryUserOnlineAndOffline", type = SqlShcmUserOnlineOffLineProvider.class)
    List<StatisData> queryUserOnlineAndOffline(@Param("dateSet") Set<String> dateList,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("queryType") int queryType);

    
}
