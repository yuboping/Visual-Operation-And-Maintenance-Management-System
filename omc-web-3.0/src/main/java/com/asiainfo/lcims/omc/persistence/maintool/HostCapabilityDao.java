package com.asiainfo.lcims.omc.persistence.maintool;

import com.asiainfo.lcims.omc.model.maintool.HostCapability;
import com.asiainfo.lcims.omc.persistence.SqlMaintoolProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface HostCapabilityDao {

    @SelectProvider(method = "getHostCapabilityList", type = SqlMaintoolProvider.class)
    List<HostCapability> getHostCapabilityList(@Param("hostname") String hostname, @Param("tableName") String tableName);
}

