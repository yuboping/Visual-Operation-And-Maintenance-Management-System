package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.ais.AisGroupMetricModel;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface AisGroupMetricDAO {

    @SelectProvider(method = "getAisGroupMetricModel", type = SqlProvider.class)
    List<AisGroupMetricModel> getAisGroupMetricModel(
            @Param("aisGroupMetricModel") AisGroupMetricModel aisGroupMetricModel);

    @InsertProvider(method = "insertAisGroupMetricModel", type = SqlProvider.class)
    int insertAisGroupMetricModel(
            @Param("aisGroupMetricModel") AisGroupMetricModel aisGroupMetricModel);

    @Delete("DELETE FROM WD_INS_ITEM_GROUP_METRIC WHERE GROUP_METRIC_ID = #{group_metric_id}")
    public int delete(@Param("group_metric_id") String group_metric_id);

    @UpdateProvider(method = "updateAisGroupMetricModel", type = SqlProvider.class)
    int update(@Param("aisGroupMetricModel") AisGroupMetricModel aisGroupMetricModel);
}
