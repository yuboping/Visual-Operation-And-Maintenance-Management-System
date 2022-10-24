package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdAlarmRule;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface MdAlarmRuleDAO {
    @SelectProvider(method = "getMdAlarmRule", type = SqlProvider.class)
    List<MdAlarmRule> getMdAlarmRule(@Param("mdAlarmRule") MdAlarmRule mdAlarmRule);

    @Insert("INSERT INTO MD_ALARM_RULE ( RULE_ID,NAME,URL,DIMENSION_TYPE,CHART_NAME,METRIC_ID,ATTR,ALARM_LEVEL,ALARM_RULE,MODES,ALARMMSG,DIMENSION1,DIMENSION2,DIMENSION3,CREATE_TIME,UPDATE_TIME ) VALUES(#{mdAlarmRule.rule_id},#{mdAlarmRule.name},#{mdAlarmRule.url},#{mdAlarmRule.dimension_type},#{mdAlarmRule.chart_name},#{mdAlarmRule.metric_id},#{mdAlarmRule.attr},#{mdAlarmRule.alarm_level},#{mdAlarmRule.alarm_rule},#{mdAlarmRule.modes},#{mdAlarmRule.alarmmsg},#{mdAlarmRule.dimension1},#{mdAlarmRule.dimension2},#{mdAlarmRule.dimension3},#{mdAlarmRule.create_time},#{mdAlarmRule.update_time})")
    int insert(@Param("mdAlarmRule") MdAlarmRule mdAlarmRule);

    @InsertProvider(method = "insertMdAlarmRule", type = SqlProvider.class)
    int insertMdAlarmRule(@Param("mdAlarmRule") MdAlarmRule mdAlarmRule);

    @Delete("DELETE FROM MD_ALARM_RULE WHERE RULE_ID = #{rule_id}")
    public int delete(@Param("rule_id") String rule_id);

    @UpdateProvider(method = "updateMdAlarmRule", type = SqlProvider.class)
    int update(@Param("mdAlarmRule") MdAlarmRule mdAlarmRule);

    @Select("SELECT MODES FROM MD_ALARM_RULE")
    List<String> getAllModes();

    @Select("SELECT MODES FROM MD_ALARM_RULE")
    List<MdAlarmRule> getMdAlarmRuleByDimensionType(
            @Param("dimensionTypeArray") String dimensionTypeArray);

    @Delete("DELETE FROM MD_ALARM_RULE WHERE DIMENSION1 = #{dimension} OR DIMENSION1 = #{dimension}")
    public int deleteByDimension(@Param("dimension") String dimension);

    @Delete("DELETE FROM MD_ALARM_RULE WHERE METRIC_ID = #{metric_id}")
    public int deleteByMetricId(@Param("metric_id") String metric_id);
}
