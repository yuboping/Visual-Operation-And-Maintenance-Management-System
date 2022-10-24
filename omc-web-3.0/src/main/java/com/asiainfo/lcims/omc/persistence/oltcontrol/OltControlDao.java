package com.asiainfo.lcims.omc.persistence.oltcontrol;

import com.asiainfo.lcims.omc.model.oltcontrol.OltControl;
import com.asiainfo.lcims.omc.model.oltcontrol.OltControlAlarm;
import com.asiainfo.lcims.omc.model.oltcontrol.OltControlSelectRespVo;
import com.asiainfo.lcims.omc.model.oltcontrol.OltControlVo;
import com.asiainfo.lcims.omc.persistence.SqlOltControlProvider;
import com.asiainfo.lcims.omc.util.page.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface OltControlDao {

    // 查询表格数据
    @SelectProvider(method = "getOltControlAlarmList", type = SqlOltControlProvider.class)
    List<OltControlAlarm> getOltControlAlarmList(@Param("oltControlVo") OltControlVo oltControlVo, @Param("page") Page page);

    // 查询下拉框Olt告警数据
    @SelectProvider(method = "getOltSelectControlAlarmList", type = SqlOltControlProvider.class)
    List<OltControlSelectRespVo> getOltSelectControlAlarmList();

    // 查询表格数据总数量
    @SelectProvider(method = "getOltControlAlarmCount", type = SqlOltControlProvider.class)
    int getOltControlAlarmCount(@Param("oltControlVo") OltControlVo oltControlVo);

    // 查询折线图数据
    @SelectProvider(method = "getLineData", type = SqlOltControlProvider.class)
    List<OltControl> getLineData(@Param("oltControlVo") OltControlVo oltControlVo);

    // 查询折线图数据
    @SelectProvider(method = "getSelectOltSingleData", type = SqlOltControlProvider.class)
    List<OltControlSelectRespVo> getSelectOltSingleData(@Param("todayOrYesterday") String todayOrYesterday);


}
