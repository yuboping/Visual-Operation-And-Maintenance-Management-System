package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.asiainfo.lcims.omc.model.configmanage.MdHostFileSystem;
import com.asiainfo.lcims.omc.persistence.SqlProvider;

public interface MdHostFileSystemDAO {
    @Select("SELECT HOST_ID,FILESYSTEM FROM MD_HOST_FILESYSTEM WHERE HOST_ID = #{host_id}")
    List<MdHostFileSystem> getMdHostFileSystemListByHostId(@Param("host_id") String host_id);

    @Update("TRUNCATE TABLE MD_HOST_FILESYSTEM")
    void truncate();

    @InsertProvider(method = "insertFileSystem", type = SqlProvider.class)
    int insertFileSystem(@Param("date") String date, @Param("metricId") String metricId);
}
