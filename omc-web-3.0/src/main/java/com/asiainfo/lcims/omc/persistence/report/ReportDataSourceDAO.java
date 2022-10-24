package com.asiainfo.lcims.omc.persistence.report;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.report.ReportDataSource;

public interface ReportDataSourceDAO {
    @Select("SELECT * FROM REPORT_DATASOURCE ORDER BY ID")
    List<ReportDataSource> getAllReportDataSource();
    
    @Select("SELECT * FROM REPORT_DATASOURCE where ID=#{id}")
    ReportDataSource getDataSourceById(@Param("id") String id);
}