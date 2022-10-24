package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.util.page.Page;

public interface MdProcessDAO {
    @SelectProvider(method = "getMdProcess", type = SqlProvider.class)
    List<MdProcess> getMdProcess(@Param("mdProcess") MdProcess mdProcess);

    @SelectProvider(method = "getMdProcessTotalCount", type = SqlProvider.class)
    int getMdProcessTotalCount(@Param("mdProcess") MdProcess mdProcess);

    @SelectProvider(method = "getMdProcessPage", type = SqlProvider.class)
    List<MdProcess> getMdProcessPage(@Param("mdProcess") MdProcess mdProcess,
            @Param("page") Page page);

    @Insert("INSERT INTO MD_PROCESS (PROCESS_ID,PROCESS_NAME,PROCESS_KEY,CREATE_TIME,UPDATE_TIME,DESCRIPTION) VALUES(#{mdProcess.process_id},#{mdProcess.process_name},#{mdProcess.process_key},#{mdProcess.create_time},#{mdProcess.update_time},#{mdProcess.description})")
    int insert(@Param("mdProcess") MdProcess mdProcess);

    @Delete("DELETE FROM MD_PROCESS WHERE PROCESS_ID NOT IN (SELECT PROCESS_ID FROM MD_HOST_PROCESS) AND PROCESS_ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateMdProcess", type = SqlProvider.class)
    int update(@Param("mdProcess") MdProcess mdProcess);

    @Select("SELECT PROCESS_ID, PROCESS_NAME , PROCESS_KEY FROM MD_PROCESS")
    List<MdProcess> getAllMdProcess();
}
