package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.AisScheduleModel;
import com.asiainfo.lcims.omc.model.ais.WdInsSheetHeaderModel;
import com.asiainfo.lcims.omc.model.ais.WdInsSheetModel;
import com.asiainfo.lcims.omc.persistence.SqlAisProvider;

public interface AisGroupDAO {

    @SelectProvider(method = "getAisGroupList", type = SqlAisProvider.class)
    List<AisGroupModel> getAisGroupList(@Param("aisGroup") AisGroupModel aisGroup);

    @SelectProvider(method = "getAisGroupCount", type = SqlAisProvider.class)
    int getAisGroupCount(@Param("aisGroup") AisGroupModel aisGroup);

    @Select("SELECT * FROM WD_INS_ITEM_GROUP WHERE GROUP_ID = #{group_id}")
    AisGroupModel getAisGroupById(@Param("group_id") String group_id);

    @Select("SELECT COUNT(*) FROM WD_INS_ITEM_GROUP_METRIC WHERE GROUP_ID = #{group_id}")
    int getAisGroupMetricById(@Param("group_id") String group_id);

    @Select("SELECT COUNT(*) FROM WD_INS_SCHEDULE WHERE GROUP_IDS LIKE '%'|| #{group_id} ||'%'")
    int getAisScheduleById(@Param("group_id") String group_id);

    @Select("SELECT * FROM WD_INS_SCHEDULE")
    List<AisScheduleModel> getAisScheduleListById();

    @Insert("INSERT INTO WD_INS_ITEM_GROUP (GROUP_ID, GROUP_NAME, STATUS, DESCRIPTION, ICON) VALUES(#{aisGroup.group_id},#{aisGroup.group_name},#{aisGroup.status},#{aisGroup.description},#{aisGroup.icon})")
    int insert(@Param("aisGroup") AisGroupModel aisGroup);

    @Update("UPDATE WD_INS_ITEM_GROUP SET GROUP_NAME=#{aisGroup.group_name}, DESCRIPTION=#{aisGroup.description}, ICON=#{aisGroup.icon} WHERE GROUP_ID=#{aisGroup.group_id}")
    int update(@Param("aisGroup") AisGroupModel aisGroup);

    @Delete("DELETE FROM WD_INS_ITEM_GROUP WHERE GROUP_ID = #{group_id}")
    int deleteById(@Param("group_id") String group_id);
    
    // 2018-11-26 zl
    @Select("SELECT GROUP_ID,GROUP_NAME,STATUS,DESCRIPTION,ICON FROM WD_INS_ITEM_GROUP")
    List<AisGroupModel> getAllAisGroup();

    @Select("SELECT ID,GROUPID,SHEETNAME,SHEETTYPE,SELECTSQL FROM WD_INS_SHEET WHERE GROUPID = #{groupid}")
    List<WdInsSheetModel> getSheetByGroupId(@Param("groupid") String groupid);

    @Select("SELECT ID,SHEETID,HEADERTYPE,ISUSESQL,SELECTSQL,CONTENT,FIRSTROW,LASTROW,FIRSTCOL,LASTCOL FROM WD_INS_SHEET_HEADER WHERE SHEETID = #{sheetid}")
    List<WdInsSheetHeaderModel> getSheetHeaderBySheetId(@Param("sheetid") String sheetid);
}
