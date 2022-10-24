package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.configmanage.MdAlarmMode;
import com.asiainfo.lcims.omc.persistence.configmanage.impl.AlarmmodeManageDAOImpl;

/**
 * 告警方式管理DAO层
 * 
 * @author zhujiansheng
 * @date 2018年8月30日 上午11:21:12
 * @version V1.0
 */
public interface AlarmmodeManageDAO {

    @Select("SELECT TYPE,CODE,DESCRIPTION FROM MD_PARAM WHERE TYPE=#{type}")
    List<MdParam> getParamByType(@Param("type") String type);

    @SelectProvider(method = "getMdAlarmMode", type = AlarmmodeManageDAOImpl.class)
    List<MdAlarmMode> getMdAlarmMode(@Param("mdAlarmMode") MdAlarmMode mdAlarmMode);

    @Insert("INSERT INTO MD_ALARM_MODE(MODEID,MODENAME,MODEATTR,MODETYPE) VALUES(#{mdAlarmMode.modeid},#{mdAlarmMode.modename},#{mdAlarmMode.modeattr},#{mdAlarmMode.modetype})")
    int insert(@Param("mdAlarmMode") MdAlarmMode mdAlarmMode);

    @UpdateProvider(method = "updateMdAlarmMode", type = AlarmmodeManageDAOImpl.class)
    int update(@Param("mdAlarmMode") MdAlarmMode mdAlarmMode);

    @DeleteProvider(method = "deleteMdAlarmMode", type = AlarmmodeManageDAOImpl.class)
    int delete(@Param("modes") String modes, @Param("alarmmodeId") String alarmmodeId);

    @Select("SELECT MODEID,MODENAME,MODEATTR,MODETYPE FROM MD_ALARM_MODE WHERE MODEID=#{alarmmodeId}")
    MdAlarmMode getModeName(@Param("alarmmodeId") String alarmmodeId);

    @Select("SELECT mam.MODETYPE,mp.DESCRIPTION FROM MD_ALARM_MODE mam LEFT JOIN MD_PARAM mp ON (mp.TYPE = #{type} AND mp.CODE = mam.MODETYPE) GROUP BY mam.MODETYPE,mp.DESCRIPTION")
    List<Map<String, Object>> getModesByType(@Param("type") String type);

    @Select("SELECT * FROM MD_ALARM_MODE")
    List<MdAlarmMode> getAll();
}
