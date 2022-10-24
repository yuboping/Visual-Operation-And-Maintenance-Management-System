package com.asiainfo.lcims.omc.persistence.configmanage.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.configmanage.MdSoundAlarm;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.StringUtil;

public class SoundAlarmManageDAOImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SoundAlarmManageDAOImpl.class);

    public String getMdSoundAlarm(Map<String, Object> parameters) {
        MdSoundAlarm mdSoundAlarm = (MdSoundAlarm) parameters.get("mdSoundAlarm");
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                "SELECT SOUND.ID,SOUND.SOUND_NAME,SOUND.SOUND_MUSIC,SOUND.DESCRIPTION,PARAM.DESCRIPTION AS SOUND_DURATION FROM MD_SOUND_ALARM SOUND LEFT JOIN MD_PARAM PARAM ON PARAM.CODE=SOUND.SOUND_DURATION WHERE PARAM.TYPE=21 ");
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_name())) {
            buffer.append(" AND SOUND.SOUND_NAME LIKE '%")
                    .append(DbSqlUtil
                            .replaceSpecialStr(StringUtil.SqlFilter(mdSoundAlarm.getSound_name())))
                    .append("%'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_duration())) {
            buffer.append(" AND SOUND.SOUND_DURATION='")
                    .append(StringUtil.SqlFilter(mdSoundAlarm.getSound_duration()))
                    .append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_music())) {
            buffer.append(" AND SOUND.SOUND_MUSIC='")
                    .append(StringUtil.SqlFilter(mdSoundAlarm.getSound_music())).append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getDescription())) {
            buffer.append(" AND SOUND.DESCRIPTION='")
                    .append(StringUtil.SqlFilter(mdSoundAlarm.getDescription())).append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getId())) {
            buffer.append(" AND ID='").append(mdSoundAlarm.getId()).append("'");
        }
        String sql = buffer.toString();
        LOG.debug("getMdSoundAlarm sql = {}", sql);
        return sql;
    }

    public String updateMdSoundAlarm(Map<String, Object> parameters) {
        MdSoundAlarm mdSoundAlarm = (MdSoundAlarm) parameters.get("mdSoundAlarm");
        StringBuffer buffer = new StringBuffer("UPDATE MD_SOUND_ALARM SET ID=ID");
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_name())) {
            buffer.append(",SOUND_NAME='").append(mdSoundAlarm.getSound_name()).append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_duration())) {
            buffer.append(",SOUND_DURATION='").append(mdSoundAlarm.getSound_duration()).append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getSound_music())) {
            buffer.append(",SOUND_MUSIC='").append(mdSoundAlarm.getSound_music()).append("'");
        }
        if (!StringUtils.isEmpty(mdSoundAlarm.getDescription())) {
            buffer.append(",DESCRIPTION='").append(mdSoundAlarm.getDescription()).append("'");
        }
        buffer.append(" WHERE ID='").append(mdSoundAlarm.getId()).append("'");
        String sql = buffer.toString();
        LOG.info("updateMdSoundAlarm sql = {}", sql);
        return sql;
    }
}
