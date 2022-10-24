package com.asiainfo.lcims.omc.persistence.monitor;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.monitor.MdChartDataSet;


public interface ChartDatsetDAO {
    
    @Select("SELECT * FROM MD_CHART_DATASET WHERE CHART_NAME=#{chartname}")
    MdChartDataSet getChartDataSetByName(@Param("chartname") String chartname);
    
    @Select("SELECT * FROM MD_CHART_DATASET")
    List<MdChartDataSet> getChartDataSetAll();
}
