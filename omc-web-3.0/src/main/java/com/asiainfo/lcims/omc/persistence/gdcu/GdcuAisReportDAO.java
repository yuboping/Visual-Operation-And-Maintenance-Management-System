package com.asiainfo.lcims.omc.persistence.gdcu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.asiainfo.lcims.omc.model.gdcu.GdcuReportFieldInfo;
import com.asiainfo.lcims.omc.model.gdcu.GdcuReportHeader;
import com.asiainfo.lcims.omc.model.gdcu.GdcuReportInfo;
import com.asiainfo.lcims.omc.model.report.ReportDataSource;

public interface GdcuAisReportDAO {
    
    @Select("SELECT ID,GROUPID,SHEET_TYPE,SHEET_NAME,ISUSESQL,SELECTSQL,HEADERCONTENT,FIRSTROW,LASTROW,FIRSTCOL,LASTCOL,CREATETIME FROM GDCU_REPORT_HEADER WHERE GROUPID = #{groupid} AND SHEET_TYPE= #{sheetType}")
    List<GdcuReportHeader> getReportHeaderList(@Param("groupid") String groupid,
            @Param("sheetType") String sheetType);

    @Select("SELECT ID,GROUPID,SHEET_TYPE,SHEET_NAME,REPORTREMARKS,SELECTSQL,CREATETIME FROM GDCU_REPORT_INFO WHERE GROUPID = #{groupid} AND SHEET_TYPE= #{sheetType}")
    GdcuReportInfo getReportInfoList(@Param("groupid") String groupid,
            @Param("sheetType") String sheetType);

    @Select("SELECT * FROM REPORT_DATASOURCE where ID=#{id}")
    ReportDataSource getDataSourceById(@Param("id") String id);

    @Select("SELECT * FROM GDCU_REPORT_FIELD_INFO where REPORTID=#{reportid}")
    List<GdcuReportFieldInfo> getReportFieldInfoList(@Param("reportid") String reportid);

}
