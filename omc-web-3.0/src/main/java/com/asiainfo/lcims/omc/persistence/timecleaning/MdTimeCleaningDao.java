package com.asiainfo.lcims.omc.persistence.timecleaning;

import com.asiainfo.lcims.omc.model.timecleaning.MdTimeCleaning;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MdTimeCleaningDao {

    @Select("SELECT * FROM MD_TIME_CLEANING")
    List<MdTimeCleaning> selectAllTimeCleaning();

    @DeleteProvider(method = "deleteNormalTableData", type = SqlCleaningProvider.class)
    int deleteNormalTableData(@Param("timeCleaning") MdTimeCleaning mdTimeCleaning);

}
