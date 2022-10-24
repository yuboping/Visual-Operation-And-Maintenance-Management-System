package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdMetricType;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.util.page.Page;

public interface MdMetricTypeDAO {
    @SelectProvider(method = "getMdMetricType", type = SqlProvider.class)
    List<MdMetricType> getMdMetricType(@Param("mdMetricType") MdMetricType mdMetricType);

    @SelectProvider(method = "getMdMetricTypeTotalCount", type = SqlProvider.class)
    int getMdMetricTypeTotalCount(@Param("mdMetricType") MdMetricType mdMetricType);

    @SelectProvider(method = "getMdMetricTypePage", type = SqlProvider.class)
    List<MdMetricType> getMdMetricTypePage(@Param("mdMetricType") MdMetricType mdMetricType,
            @Param("page") Page page);

    @Insert("INSERT INTO MD_METRIC_TYPE (ID,METRIC_TYPE_NAME,DESCRIPTION) VALUES(#{mdMetricType.id},#{mdMetricType.metric_type_name},#{mdMetricType.description})")
    int insert(@Param("mdMetricType") MdMetricType mdMetricType);

    @Delete("DELETE FROM MD_METRIC_TYPE WHERE ID NOT IN (SELECT METRIC_TYPE FROM MD_METRIC) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateMdMetricType", type = SqlProvider.class)
    int update(@Param("mdMetricType") MdMetricType mdMetricType);
}
