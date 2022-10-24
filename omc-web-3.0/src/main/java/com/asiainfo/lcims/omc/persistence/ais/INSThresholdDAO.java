package com.asiainfo.lcims.omc.persistence.ais;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.persistence.po.ais.INSThreshold;

public interface INSThresholdDAO {
    @Select("SELECT * FROM MON_INS_THRESHOLD")
    List<INSThreshold> getAllThreshold();
}
