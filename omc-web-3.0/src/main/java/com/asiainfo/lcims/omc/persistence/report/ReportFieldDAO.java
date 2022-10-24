package com.asiainfo.lcims.omc.persistence.report;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.report.ReportFieldInfo;

public interface ReportFieldDAO {
    @Select("SELECT * FROM  REPORT_FIELD_INFO WHERE REPORTID=#{reportid} ORDER BY FIELDID")
    List<ReportFieldInfo> getReportFieldByReportId(@Param("reportid") String reportid);
    
    @Insert("INSERT INTO REPORT_FIELD_INFO (reportid,showname,sqlfield) VALUES(#{fieldInfo.reportid},#{fieldInfo.showname},#{fieldInfo.sqlfield})")
    int insert(@Param("fieldInfo") ReportFieldInfo fieldInfo);
    
    @Delete("DELETE FROM REPORT_FIELD_INFO WHERE reportid=#{reportid} ")
    int deleteByReportId(@Param("reportid") String reportid);
}
