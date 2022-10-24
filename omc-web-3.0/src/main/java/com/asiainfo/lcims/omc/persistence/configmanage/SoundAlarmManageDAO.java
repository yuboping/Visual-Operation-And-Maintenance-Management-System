package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdSoundAlarm;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.SoundAlarmManageDAOImpl;

public interface SoundAlarmManageDAO {

    @SelectProvider(method = "getMdSoundAlarm", type = SoundAlarmManageDAOImpl.class)
    List<MdSoundAlarm> getMdSoundAlarm(@Param("mdSoundAlarm") MdSoundAlarm mdSoundAlarm);

    @Insert("INSERT INTO MD_SOUND_ALARM(ID,SOUND_NAME,SOUND_DURATION,SOUND_MUSIC,DESCRIPTION) VALUES(#{mdSoundAlarm.id},#{mdSoundAlarm.sound_name},#{mdSoundAlarm.sound_duration},#{mdSoundAlarm.sound_music},#{mdSoundAlarm.description})")
    int insert(@Param("mdSoundAlarm") MdSoundAlarm mdSoundAlarm);

    @UpdateProvider(method = "updateMdSoundAlarm", type = SoundAlarmManageDAOImpl.class)
    int update(@Param("mdSoundAlarm") MdSoundAlarm mdSoundAlarm);

    @Select("SELECT ID,SOUND_NAME,SOUND_DURATION,SOUND_MUSIC,DESCRIPTION FROM MD_SOUND_ALARM")
    List<MdSoundAlarm> getMdSoundAlarmList();

    @Select("SELECT ID,SOUND_NAME,SOUND_DURATION,SOUND_MUSIC,DESCRIPTION FROM MD_SOUND_ALARM WHERE ID = #{soundAlarmId}")
    MdSoundAlarm getOneSoundAlarm(@Param("soundAlarmId") String soundAlarmId);

    @Delete("DELETE FROM MD_SOUND_ALARM WHERE ID IN (#{soundAlarmId})")
    int delete(@Param("soundAlarmId") String soundAlarmId);
}
