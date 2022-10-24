package com.asiainfo.lcims.omc.persistence.alarm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmHisInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmLevel;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmStatisInfo;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.persistence.SqlAlarmProvider;
import com.asiainfo.lcims.omc.persistence.alarm.impl.MdAlarmInfoDAOImpl;
import com.asiainfo.lcims.omc.util.page.Page;

public interface MdAlarmInfoDao {

    @SelectProvider(method = "getAlarmInfoList", type = SqlAlarmProvider.class)
    List<MdAlarmInfo> getAlarmInfoList(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList, @Param("page") Page page);
//    List<MdAlarmInfo> getAlarmInfoList(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList, @Param("page") Page page);

    @SelectProvider(method = "getAlarmInfoCount", type = SqlAlarmProvider.class)
    int getAlarmInfoCount(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList);
//    int getAlarmInfoCount(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList);

    @SelectProvider(method = "getAlarmInfoListWithIndex", type = SqlAlarmProvider.class)
    List<MdAlarmInfo> getAlarmInfoListWithIndex(@Param("urlList") List urlList);
//    List<MdAlarmInfo> getAlarmInfoListWithIndex(@Param("urlList") String urlList);

    @Select("SELECT * FROM MD_METRIC")
    List<MdMetric> getAllMetricList();

    @Select("SELECT * FROM MD_ALARM_INFO WHERE ALARM_ID = #{alarmId} ")
    MdAlarmInfo getAlarmListWithId(@Param("alarmId") String alarmId);

    @SelectProvider(method = "getAlarmInfoWithId", type = SqlAlarmProvider.class)
    MdAlarmInfo getAlarmInfoWithId(@Param("alarmId") String alarmId);

    @SelectProvider(method = "getAlarmHisInfoById", type = MdAlarmInfoDAOImpl.class)
    List<MdAlarmHisInfo> getAlarmHisInfoById(@Param("id") String id);

    @SelectProvider(method = "getAlarmInfoById", type = MdAlarmInfoDAOImpl.class)
    List<MdAlarmInfo> getAlarmInfoById(@Param("alarmId") String alarmId);

    @Select("SELECT * FROM MD_ALARM_LEVEL")
    List<MdAlarmLevel> getAllAlarmLevelList();

    @UpdateProvider(method = "confirmAlarmInfoById", type = SqlAlarmProvider.class)
    int confirmAlarmInfoById(@Param("username")String username, @Param("alarmId") String alarmId, @Param("clearflag") String clearflag);

    @UpdateProvider(method = "confirmAlarmHisInfoById", type = SqlAlarmProvider.class)
    int confirmAlarmHisInfoById(@Param("username") String username, @Param("alarmId") String alarmId, @Param("alarmFirstTime") String alarmFirstTime);

    @SelectProvider(method = "getAlarmHisList", type = SqlAlarmProvider.class)
    List<MdAlarmHisInfo> getAlarmHisList(@Param("alarm_id") String alarm_id);

    @Select("SELECT * FROM MD_ALARM_INFO WHERE URL = #{url} AND DELETE_STATE = 0")
    List<MdAlarmInfo> getAlarmNum(@Param("url") String url);

    @Select("SELECT * FROM MD_ALARM_INFO WHERE ALARM_NUM > 0 AND DELETE_STATE = 0")
    List<MdAlarmInfo> getAlarmInfos();
    
    @Select("SELECT * FROM MD_ALARM_INFO WHERE ALARM_NUM > 0 AND DELETE_STATE = 0")
    List<MdAlarmInfo> getAlarmInfosforAis();

    @SelectProvider(method = "getAlarmHisInfoCount", type = SqlAlarmProvider.class)
    int getAlarmHisInfoCount(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList);
//    int getAlarmHisInfoCount(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList);

    @SelectProvider(method = "getAlarmHisInfoList", type = SqlAlarmProvider.class)
    List<MdAlarmHisInfo> getAlarmHisInfoList(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList, @Param("page") Page page);
//    List<MdAlarmHisInfo> getAlarmHisInfoList(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList, @Param("page") Page page);

    @SelectProvider(method = "getAlarmHisInfoWithLevelIndex", type = SqlAlarmProvider.class)
    List<MdAlarmHisInfo> getAlarmHisInfoWithLevelIndex(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList);
//    List<MdAlarmHisInfo> getAlarmHisInfoWithLevelIndex(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList);

    @SelectProvider(method = "getAlarmStatisInfoListWithDay", type = SqlAlarmProvider.class)
    List<MdAlarmStatisInfo> getAlarmStatisInfoListWithDay(@Param("alarmInfo") MdAlarmInfo alarmInfo);

    @SelectProvider(method = "getAlarmStatisInfoListWithWeek", type = SqlAlarmProvider.class)
    List<MdAlarmStatisInfo> getAlarmStatisInfoListWithWeek(@Param("alarmInfo") MdAlarmInfo alarmInfo);

    @SelectProvider(method = "getAlarmStatisInfoListWithMonth", type = SqlAlarmProvider.class)
    List<MdAlarmStatisInfo> getAlarmStatisInfoListWithMonth(@Param("alarmInfo") MdAlarmInfo alarmInfo);

    @Select("SELECT * FROM MD_ALARM_INFO WHERE METRIC_ID = #{metricId} AND ATTR = #{attr} AND (DIMENSION1 = #{hostId} OR DIMENSION2 = #{hostId}) AND ALARM_NUM > 0")
    List<MdAlarmInfo> getAlarmListByRadius(@Param("metricId") String metricId, @Param("attr") String attr, @Param("hostId") String hostId);

    @SelectProvider(method = "getAlarmInfoWithIndexGraph", type = SqlAlarmProvider.class)
    List<ChartData> getAlarmInfoWithIndexGraph(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList);
//    List<ChartData> getAlarmInfoWithIndexGraph(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList);

    @SelectProvider(method = "getAlarmInfoListWithIndexConfirm", type = SqlAlarmProvider.class)
    List<MdAlarmInfo> getAlarmInfoListWithIndexConfirm(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") List urlList);
//    List<MdAlarmInfo> getAlarmInfoListWithIndexConfirm(@Param("alarmInfo") MdAlarmInfo alarmInfo, @Param("urlList") String urlList);

    @Select("SELECT * FROM MD_ALARM_INFO WHERE METRIC_ID = #{metricId} AND (DIMENSION1 = #{hostId} OR DIMENSION2 = #{hostId}) AND ALARM_NUM > 0")
    List<MdAlarmInfo> getAlarmListByHostState(@Param("metricId") String metricId,
            @Param("hostId") String hostId);
    
    @UpdateProvider(method = "deleteAlarmInfoById", type = SqlAlarmProvider.class)
    int deleteAlarmInfoById(@Param("alarmId") String alarmId);

    @Select("SELECT CODE,DESCRIPTION FROM MD_PARAM WHERE TYPE = '40'")
    List<Map<String, Object>> getAlarmTypeList();
}
