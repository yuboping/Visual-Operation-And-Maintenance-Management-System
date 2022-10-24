package com.asiainfo.lcims.omc.persistence.report;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.report.ReportInfo;

public interface ReportInfoDAO {
    @Select("SELECT * FROM  REPORT_INFO ORDER BY REPORTID DESC")
    List<ReportInfo> getAllReport();

    @Select("SELECT * FROM  REPORT_INFO WHERE REPORTID=#{reportid}")
    ReportInfo getReportByReportId(@Param("reportid") String reportid);

    @Select("SELECT * FROM  REPORT_INFO WHERE MENUTREENAME=#{menuTreeName}")
    ReportInfo getReportByMenuName(@Param("menuTreeName") String menuTreeName);

    @Select("SELECT * FROM REPORT_INFO WHERE REPORTNAME=#{reportname}")
    List<ReportInfo> getReportByReportName(@Param("reportname") String reportname);
}
