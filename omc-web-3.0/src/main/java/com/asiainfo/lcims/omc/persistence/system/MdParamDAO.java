package com.asiainfo.lcims.omc.persistence.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.MdParam;

public interface MdParamDAO {
    @Select("SELECT * FROM MD_PARAM WHERE TYPE=#{type}")
    List<MdParam> getParamByType(@Param("type")String type);
    
    @Select("SELECT * FROM MD_PARAM WHERE DESCRIPTION=#{description}")
	MdParam getParam(@Param("description")String description);
    
    @Select("SELECT * FROM MD_PARAM")
	List<MdParam> getParamAllInfo();
}
