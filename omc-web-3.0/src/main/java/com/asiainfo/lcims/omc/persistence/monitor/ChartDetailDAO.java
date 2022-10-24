package com.asiainfo.lcims.omc.persistence.monitor;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;

public interface ChartDetailDAO {
    
    @Select("SELECT * FROM MD_CHART_DETAIL ")
    List<MdChartDetail> getChartDetailAll(); 
    
    @Select("SELECT * FROM MD_CHART_DETAIL WHERE CHART_NAME=#{chartname}")
    MdChartDetail getChartDetailByName(@Param("chartname") String chartname);
}
