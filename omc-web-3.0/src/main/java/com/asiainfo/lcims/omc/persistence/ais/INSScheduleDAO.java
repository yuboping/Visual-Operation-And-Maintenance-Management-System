package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import com.asiainfo.lcims.omc.persistence.SqlAisProvider;
import org.apache.ibatis.annotations.*;

import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;

public interface INSScheduleDAO {

    @Select("SELECT ID,TITLE,TIMER,GROUP_IDS,EMAILS,PHONES,SCHEDULE_TYPE FROM WD_INS_SCHEDULE ORDER BY ID DESC")
    List<INSSchedule> getAllSchedule();

//    @Insert("INSERT INTO WD_INS_SCHEDULE(ID,TITLE,TIMER,GROUP_IDS,EMAILS,PHONES,SCHEDULE_TYPE,CREATE_TIME,UPDATE_TIME) VALUES(#{schedule.id}, #{schedule.title},#{schedule.timer},#{schedule.group_ids},#{schedule.emails},#{schedule.phones},#{schedule.schedule_type},#{schedule.create_time},#{schedule.update_time})")
//    int addSchedule(@Param("schedule") INSSchedule schedule);

    @InsertProvider(method = "addSchedule", type = SqlAisProvider.class)
    int addSchedule(@Param("schedule") INSSchedule schedule);

//    @Update("UPDATE WD_INS_SCHEDULE SET TITLE = #{schedule.title}, TIMER=#{schedule.timer},GROUP_IDS=#{schedule.group_ids},EMAILS=#{schedule.emails},PHONES=#{schedule.phones},SCHEDULE_TYPE=#{schedule.schedule_type},UPDATE_TIME=#{schedule.update_time}  WHERE ID=#{schedule.id}")
    @UpdateProvider(method = "updateSchedule", type = SqlAisProvider.class)
    int updateSchedule(@Param("schedule") INSSchedule schedule);

    @Delete("DELETE FROM WD_INS_SCHEDULE WHERE ID=#{id}")
    int deleteSchedule(@Param("id") String id);

    @Select("SELECT ID,TITLE,TIMER,GROUP_IDS,EMAILS,PHONES,SCHEDULE_TYPE FROM WD_INS_SCHEDULE WHERE ID=#{id}")
    INSSchedule getScheduleDetail(@Param("id") String id);

    @Select("SELECT COUNT(*) FROM WD_INS_SCHEDULE WHERE TITLE=#{title}")
    int getScheduleCount(@Param("title") String title);
}
