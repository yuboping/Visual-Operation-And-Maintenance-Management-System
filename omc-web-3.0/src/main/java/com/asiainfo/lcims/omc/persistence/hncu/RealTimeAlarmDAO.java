package com.asiainfo.lcims.omc.persistence.hncu;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfoHn;
import com.asiainfo.lcims.omc.util.page.Page;
import org.apache.ibatis.annotations.*;

import com.asiainfo.lcims.omc.model.hncu.RealTimeAlarmInfo;
import com.asiainfo.lcims.omc.persistence.hncu.impl.RealTimeAlarmDAOImpl;

import java.util.List;
import java.util.Map;

public interface RealTimeAlarmDAO {

    // 新增主机
    @InsertProvider(method = "insertRealTimeAlarm", type = RealTimeAlarmDAOImpl.class)
    int insertRealTimeAlarm(@Param("realTimeAlarmInfo") RealTimeAlarmInfo realTimeAlarmInfo);

    @SelectProvider(method = "getAlarmInfoHnCount", type = RealTimeAlarmDAOImpl.class)
    int getAlarmInfoHnCount(@Param("alarmInfoHn") MdAlarmInfoHn alarmInfoHn);

    @SelectProvider(method = "getAlarmInfoHnList", type = RealTimeAlarmDAOImpl.class)
    List<MdAlarmInfoHn> getAlarmInfoHnList(@Param("alarmInfoHn") MdAlarmInfoHn alarmInfoHn, @Param("page") Page page);

    @Select("SELECT CODE,DESCRIPTION FROM MD_PARAM WHERE TYPE = '150'")
    List<Map<String, Object>> getAlarmTypeList();

    @SelectProvider(method = "getAlarmInfoWithId", type = RealTimeAlarmDAOImpl.class)
    MdAlarmInfoHn getAlarmInfoWithId(@Param("alarmId") String alarmId);

    @UpdateProvider(method = "confirmAlarmInfoById", type = RealTimeAlarmDAOImpl.class)
    int confirmAlarmInfoById(@Param("alarmId") String alarmId);
}
