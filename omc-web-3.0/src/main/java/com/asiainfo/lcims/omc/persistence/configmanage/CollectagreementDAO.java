package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.configmanage.MdCollectAgreement;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CollectagreementDAO {

    @SelectProvider(method = "getMdCollectAgreement", type = SqlProvider.class)
    List<MdCollectAgreement> getMdCollectAgreement(@Param("mdCollectAgreement") MdCollectAgreement mdCollectAgreement);

    @Insert("INSERT INTO MD_COLLECT_PROTOCOL (ID,PROTOCOL_IDENTITY,PROTOCOL_NAME,CYCLE_ID,SCRIPT,SCRIPT_PARAM,SCRIPT_RETURN_TYPE,PROTOCOL_TYPE,DESCRIPTION,SERVER_TYPE) " +
            "VALUES(#{mdCollectAgreement.id},#{mdCollectAgreement.protocol_identity},#{mdCollectAgreement.protocol_name},#{mdCollectAgreement.cycle_id},#{mdCollectAgreement.script}," +
            "#{mdCollectAgreement.script_param},#{mdCollectAgreement.script_return_type},#{mdCollectAgreement.protocol_type},#{mdCollectAgreement.description},#{mdCollectAgreement.server_type})")
    int insert(@Param("mdCollectAgreement") MdCollectAgreement mdCollectAgreement);

    @Delete("DELETE FROM MD_COLLECT_PROTOCOL WHERE ID NOT IN (SELECT METRIC_ID FROM MD_HOST_METRIC) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateMdCollectAgreement", type = SqlProvider.class)
    int update(@Param("mdCollectAgreement") MdCollectAgreement mdCollectAgreement);

    @SelectProvider(method = "getMdMetricListByIds", type = SqlProvider.class)
    List<MdMetric> getMdMetricListByIds(@Param("ids") String ids);
}
