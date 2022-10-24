package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface MdMetricDAO {
    @SelectProvider(method = "getMdMetric", type = SqlProvider.class)
    List<MdMetric> getMdMetric(@Param("mdMetric") MdMetric mdMetric);

    @Insert("INSERT INTO MD_METRIC (ID,METRIC_IDENTITY,METRIC_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,METRIC_TYPE,DESCRIPTION,SERVER_TYPE) VALUES(#{mdMetric.id},#{mdMetric.metric_identity},#{mdMetric.metric_name},#{mdMetric.cycle_id},#{mdMetric.script},#{mdMetric.script_param},#{mdMetric.script_return_type},#{mdMetric.metric_type},#{mdMetric.description},#{mdMetric.server_type})")
    int insert(@Param("mdMetric") MdMetric mdMetric);

    @Delete("DELETE FROM MD_METRIC WHERE ID NOT IN (SELECT METRIC_ID FROM MD_HOST_METRIC) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateMdMetric", type = SqlProvider.class)
    int update(@Param("mdMetric") MdMetric mdMetric);

    @SelectProvider(method = "getMdMetricListByIds", type = SqlProvider.class)
    List<MdMetric> getMdMetricListByIds(@Param("ids") String ids);
}
