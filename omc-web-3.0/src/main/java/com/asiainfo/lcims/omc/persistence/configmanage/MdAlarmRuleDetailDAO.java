package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRuleDetail;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface MdAlarmRuleDetailDAO {
    @Insert("INSERT INTO MD_ALARM_RULE_DETAIL ( ALARM_ID,RULE_ID,NAME,URL,DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,ALARM_RULE,MODES,ALARMMSG,DIMENSION1,DIMENSION2,DIMENSION3,CREATE_TIME,UPDATE_TIME ) VALUES(#{mdAlarmRuleDetail.alarm_id},#{mdAlarmRuleDetail.rule_id},#{mdAlarmRuleDetail.name},#{mdAlarmRuleDetail.url},#{mdAlarmRuleDetail.dimension_type},#{mdAlarmRuleDetail.chart_name},#{mdAlarmRuleDetail.metric_id},#{mdAlarmRuleDetail.attr},#{mdAlarmRuleDetail.alarm_level},#{mdAlarmRuleDetail.alarm_rule},#{mdAlarmRuleDetail.modes},#{mdAlarmRuleDetail.alarmmsg},#{mdAlarmRuleDetail.dimension1},#{mdAlarmRuleDetail.dimension2},#{mdAlarmRuleDetail.dimension3},#{mdAlarmRuleDetail.create_time},#{mdAlarmRuleDetail.update_time})")
    int insert(@Param("mdAlarmRuleDetail") MdAlarmRuleDetail mdAlarmRuleDetail);

    @InsertProvider(method = "insertMdAlarmRuleDetail", type = SqlProvider.class)
    int insertMdAlarmRuleDetail(@Param("mdAlarmRuleDetail") MdAlarmRuleDetail mdAlarmRuleDetail);

    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE RULE_ID = #{rule_id}")
    public int delete(@Param("rule_id") String rule_id);

    @UpdateProvider(method = "updateMdAlarmRuleDetail", type = SqlProvider.class)
    int update(@Param("mdAlarmRuleDetail") MdAlarmRuleDetail mdAlarmRuleDetail);

    @Select("SELECT * FROM MD_ALARM_RULE_DETAIL WHERE URL = #{url}")
    List<MdAlarmRuleDetail> getMdAlarmRuleDetailByUrl(@Param("url") String url);

    @Select("SELECT * FROM MD_ALARM_RULE_DETAIL WHERE URL = #{url} AND RULE_ID = #{rule_id}")
    List<MdAlarmRuleDetail> getMdAlarmRuleDetailByUrlAndRuleId(@Param("url") String url,
            @Param("rule_id") String rule_id);

    @UpdateProvider(method = "updateMdAlarmRuleDetailByAlarmId", type = SqlProvider.class)
    int updateByAlarmId(@Param("mdAlarmRuleDetail") MdAlarmRuleDetail mdAlarmRuleDetail);

    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE ALARM_ID = #{alarm_id}")
    public int deleteByAlarmId(@Param("alarm_id") String alarm_id);

    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE DIMENSION1 = #{dimension} OR DIMENSION2 = #{dimension}")
    public int deleteByDimension(@Param("dimension") String dimension);

    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE METRIC_ID = #{metric_id}")
    public int deleteByMetricId(@Param("metric_id") String metric_id);

    @Select("SELECT * FROM MD_ALARM_RULE_DETAIL WHERE RULE_ID = #{rule_id}")
    List<MdAlarmRuleDetail> getMdAlarmRuleDetailByRuleId(@Param("rule_id") String rule_id);
    
    /**
     * 根据dimensions批量删除 EMS 模块调用
     * @param dimensions
     * @return
     */
    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE DIMENSION1 IN (${dimensions}) OR DIMENSION2 IN (${dimensions})")
    public int deleteByDimensions(@Param("dimensions") String dimensions);

    /**
     * 
     * @Title: updateApnAlarmRuleDetail @Description:
     * TODO(更新APN告警规则明细) @param @param mdAlarmRuleDetail @param @return
     * 参数 @return int 返回类型 @throws
     */
    @Update("UPDATE MD_ALARM_RULE_DETAIL SET ALARM_RULE=#{mdAlarmRuleDetail.alarm_rule} WHERE URL=#{mdAlarmRuleDetail.url} AND METRIC_ID=#{mdAlarmRuleDetail.metric_id}")
    int updateApnAlarmRuleDetail(@Param("mdAlarmRuleDetail") MdAlarmRuleDetail mdAlarmRuleDetail);

    @Delete("DELETE FROM MD_ALARM_RULE_DETAIL WHERE URL=#{alarmRuleDetail.url} AND METRIC_ID=#{alarmRuleDetail.metric_id}")
    int deleteApnAlarmDetailRule(@Param("alarmRuleDetail") MdAlarmRuleDetail alarmRuleDetail);
}
