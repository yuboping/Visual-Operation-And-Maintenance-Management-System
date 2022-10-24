package com.asiainfo.lcims.omc.persistence.apn;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface LimitStrategyDAO {

    @Select("SELECT DESCRIPTION FROM MD_PARAM WHERE TYPE='30' AND CODE ='limit_strategy'")
    String getWidSeq();

    @Update("UPDATE MD_PARAM SET DESCRIPTION=#{widSeq} WHERE TYPE='30' AND CODE ='limit_strategy'")
    int updateWidSeq(@Param("widSeq") String widSeq);

}
