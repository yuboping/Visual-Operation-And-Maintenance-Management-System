package com.asiainfo.lcims.omc.persistence.ais;

import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.persistence.SqlAisProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AisScheduleDAO {

    @SelectProvider(method = "getAisScheduleList", type = SqlAisProvider.class)
    List<AisScheduleModel> getAisScheduleList(@Param("aisSchedule") AisScheduleModel aisSchedule);

    @SelectProvider(method = "getAisGroupNameList", type = SqlAisProvider.class)
    List<AisGroupModel> getAisGroupNameList(@Param("aisSchedule") AisScheduleModel aisSchedule);

    @Select("SELECT * FROM WD_INS_SCHEDULE WHERE ID = #{schedule_id}")
    AisScheduleModel getAisScheduleById(@Param("schedule_id") String schedule_id);

    @Insert("INSERT INTO WD_INS_SCHEDULE(ID,TITLE,TIMER,GROUP_IDS,EMAILS,PHONES,SCHEDULE_TYPE,CREATE_TIME,UPDATE_TIME) VALUES(#{schedule.id}, #{schedule.title},#{schedule.timer},#{schedule.group_ids},#{schedule.emails},#{schedule.phones},#{schedule.schedule_type},#{schedule.create_time},#{schedule.update_time})")
    int insert(@Param("aisScheduleModel") AisScheduleModel aisScheduleModel);

    @Update("UPDATE WD_INS_SCHEDULE SET TITLE = #{schedule.title}, TIMER=#{schedule.timer},GROUP_IDS=#{schedule.items},EMAILS=#{schedule.emails},PHONES=#{schedule.phones},SCHEDULE_TYPE=#{schedule.schedule_type},UPDATE_TIME=#{schedule.update_time}  WHERE ID=#{schedule.id}")
    int update(@Param("aisScheduleModel") AisScheduleModel aisScheduleModel);

    @Delete("DELETE FROM WD_INS_SCHEDULE WHERE ID = #{schedule_id}")
    int deleteById(@Param("schedule_id") String schedule_id);

}
