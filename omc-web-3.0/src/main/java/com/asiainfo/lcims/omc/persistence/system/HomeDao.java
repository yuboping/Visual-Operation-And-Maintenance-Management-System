package com.asiainfo.lcims.omc.persistence.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.serverlist.HomeAlarmBean;
import com.asiainfo.lcims.omc.model.serverlist.ServerlistBean;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface HomeDao {

    @SelectProvider(method = "getRadiusProcess", type = SqlProvider.class)
    List<Map<String, Object>> getRadiusProcess(@Param("radiusProcessKey") String radiusProcessKey,
            @Param("tableName") String tableName, @Param("stime") String stime);

    @SelectProvider(method = "getRadiusAnalytic", type = SqlProvider.class)
    List<Map<String, Object>> getRadiusAnalytic(@Param("metricId") String metricId,
            @Param("tableName") String tableName, @Param("hostIds") String hostIds,
            @Param("hostField") String hostField);

    @SelectProvider(method = "getHostNetConnectable", type = SqlProvider.class)
    List<Map<String, Object>> getHostNetConnectable(
            @Param("sysNetConnectableMetricId") String sysNetConnectableMetricId,
            @Param("tableName") String tableName, @Param("stime") String stime);

    @SelectProvider(method = "getHostState", type = SqlProvider.class)
    List<Map<String, Object>> getHostState(@Param("metricId") String metricId,
            @Param("tableName") String tableName, @Param("stime") String stime);

    @SelectProvider(method = "getHostStateAllHost", type = SqlProvider.class)
    List<Map<String, Object>> getHostStateAllHost(@Param("metricIds") String metricIds,
            @Param("tableName") String tableName, @Param("stime") String stime);
    
    @Select(value = "select c.MVALUE from STATIS_DATA_DAY_${date} c "
    		+ "where c.METRIC_ID='5f46cdda9b80402d9efe9cfedf0c489a' and attr1='summary' "
    		+ "order by c.STIME desc,c.CREATE_TIME desc LIMIT 0,1")
    String queryHomeAuthentication(@Param("date") String date);
    
    
    @SelectProvider(method = "queryServerList", type = SqlProvider.class)
    List<ServerlistBean> queryServerList();
    
    @SelectProvider(method = "queryServerHostList", type = SqlProvider.class)
    List<ServerlistBean> queryServerHostList();
    
    
    @SelectProvider(method = "queryHomeAlarm", type = SqlProvider.class)
    List<HomeAlarmBean> queryHomeAlarm(@Param("urlList") List<String> urlList);
    
    @SelectProvider(method = "homeAlarmQueryById", type = SqlProvider.class) 
    List<HomeAlarmBean> homeAlarmQueryById(@Param("alarmId") String alarmId,@Param("urlList") List<String> urlList);
    
    @SelectProvider(method = "queryAlarmByAlarmlevel", type = SqlProvider.class) 
    List<HomeAlarmBean> queryAlarmByAlarmlevel(@Param("urlList") List<String> urlList);
}
