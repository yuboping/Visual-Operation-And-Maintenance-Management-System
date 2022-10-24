package com.asiainfo.lcims.omc.persistence.system;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.system.MdCollCycle;

public interface MdCollCycleDAO {
    @Select("SELECT CYCLEID,CYCLENAME,CYCLE,RUNDAY,CRON,DESCRIPTION FROM MD_COLL_CYCLE ")
    List<MdCollCycle> getMdCollCycle();
}
